/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.snmp;

import com.slavick.macmonitor.model.devices.orm.credentials.SnmpCredential;
import com.slavick.macmonitor.model.networkentities.Vlan;
import com.slavick.macmonitor.model.snmp.expceptions.SnmpWalkException;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3AuthProtocol;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3PrivProtocol;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3SecurityLevel;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpVersion;
import org.snmp4j.*;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apanasyonok on 03.11.2016.
 */

public class SnmpService {

    //public static int  pollThreadCount = 10;
    public static final int RETRIES_COUNT = 2;
    public static final int TIMEOUT = 2000;

    public static Map<OID, Variable> doSnmpWalk(SnmpCredential credential, OID oid, Vlan vlan) throws SnmpWalkException {
        if (credential.getSnmpVersion() == SnmpVersion.V3)
            return doSnmpWalkV3(credential, oid, vlan);
        else if (credential.getSnmpVersion() == SnmpVersion.V1 || credential.getSnmpVersion() == SnmpVersion.V2) {
            return doSnmpWalkV1V2(credential, oid, vlan);
        }
        throw new SnmpWalkException("Bad snmp version number");
    }

    public static Map<OID, Variable> doSnmpWalk(SnmpCredential credential, OID oid) throws SnmpWalkException {
        if (credential.getSnmpVersion() == SnmpVersion.V3)
            return doSnmpWalkV3(credential, oid, null);
        else if (credential.getSnmpVersion() == SnmpVersion.V1 || credential.getSnmpVersion() == SnmpVersion.V2) {
            return doSnmpWalkV1V2(credential, oid, null);
        }
        throw new SnmpWalkException("Bad snmp version number");
    }


    protected static PrivacyGeneric getPriv(SnmpV3PrivProtocol privProtocol) {

        PrivacyGeneric priv = null;

        switch (privProtocol) {
            case AES128:
                priv = new PrivAES128();
                break;
            case DES:
                priv = new PrivDES();
                break;
            //following algorithms doesn't work, or i can't configure a switch right.
            /*case AES192:
                priv = new PrivAES192();
                break;
            case AES256:
                priv = new PrivAES256();
                break;

            case DES3:
                priv = new Priv3DES();
                break;*/
        }
        return priv;
    }

    protected static SecurityLevel getSecurityLevel(SnmpV3SecurityLevel level) {
        SecurityLevel securityLevel = null;

        switch (level) {
            case NO_AUTH_NO_PRIV:
                securityLevel = SecurityLevel.noAuthNoPriv;
                break;
            case AUTH_NO_PRIV:
                securityLevel = SecurityLevel.authNoPriv;
                break;
            case AUTH_PRIV:
                securityLevel = SecurityLevel.authPriv;
                break;
        }

        return securityLevel;
    }


    protected static AuthGeneric getAuth(SnmpV3AuthProtocol authProtocol) {
        AuthGeneric auth = null;

        if (authProtocol == SnmpV3AuthProtocol.MD5) {
            auth = new AuthMD5();
        } else if (authProtocol == SnmpV3AuthProtocol.SHA) {
            auth = new AuthSHA();
        }
        return auth;
    }

    protected static Map<OID, Variable> getMapFromTreeEvents(List<TreeEvent> events) {

        Map<OID, Variable> map = new HashMap<>();

        if (events == null || events.size() == 0) {
            return map;
        }

        for (TreeEvent event : events) {
            if (event.isError()) {

            } else {
                VariableBinding[] varBindings = event.getVariableBindings();
                if (varBindings == null || varBindings.length == 0) {
                    //System.out.println("VarBinding: No result returned.");
                }
                for (VariableBinding varBinding : varBindings) {

                    map.put(varBinding.getOid(), varBinding.getVariable());
                }
            }
        }
        return map;

    }

    protected static Map<OID, Variable> doSnmpWalkV3(SnmpCredential credential, OID oid, Vlan vlan) throws SnmpWalkException {

        Map<OID, Variable> map = new HashMap<>();
        Snmp snmp = null;
        TransportMapping transport = null;

        try {
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            Address targetAddress = GenericAddress.parse("udp:" + credential.getIpAddress().getString() +
                    "/" + credential.getPort().getString());
            SecurityModels.getInstance().addSecurityModel(usm);


            transport = new DefaultUdpTransportMapping();
            transport.listen();
            snmp = new Snmp(transport);

            UserTarget target = new UserTarget();
            target.setAddress(targetAddress);
            target.setVersion(SnmpConstants.version3); //SnmpConstants.version3
            target.setRetries(RETRIES_COUNT);
            target.setTimeout(TIMEOUT);
            target.setAuthoritativeEngineID(new byte[0]);
            target.setSecurityName(new OctetString(credential.getSnmpV3SecurityName()));

            UsmUser usmUser = null;

            SnmpV3SecurityLevel securityLevel = credential.getSnmpV3SecurityLevel();

            if (securityLevel == SnmpV3SecurityLevel.NO_AUTH_NO_PRIV) {

                target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
                usmUser = new UsmUser(
                        new OctetString(credential.getSnmpV3SecurityName()), null, null, null, null
                );

            } else if (securityLevel == SnmpV3SecurityLevel.AUTH_NO_PRIV) {

                target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);


                usmUser = new UsmUser(
                        new OctetString(credential.getSnmpV3SecurityName()),
                        getAuth(credential.getSnmpV3AuthProtocol()).getID(),
                        new OctetString(credential.getAuthKey()),
                        null, null
                );


            } else if (securityLevel == SnmpV3SecurityLevel.AUTH_PRIV) {

                target.setSecurityLevel(SecurityLevel.AUTH_PRIV);

                usmUser = new UsmUser(
                        new OctetString(credential.getSnmpV3SecurityName()),
                        getAuth(credential.getSnmpV3AuthProtocol()).getID(),
                        new OctetString(credential.getAuthKey()),
                        getPriv(credential.getSnmpV3PrivProtocol()).getID(),
                        new OctetString(credential.getPrivKey())
                );


            }


            if (usmUser == null) return map;

            snmp.getUSM().addUser(usmUser);

            ScopedPDU scopedPDU = new ScopedPDU();
            scopedPDU.setNonRepeaters(0);

            String context = null;

            if (vlan == null) {
                context = "";
            } else {
                context = "vlan" + "-" + vlan.toString();

            }

            scopedPDU.setContextName(new OctetString(context));

            TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory(PDU.GET, scopedPDU.getContextEngineID(), new OctetString(context)));
            List<TreeEvent> events = treeUtils.getSubtree(target, oid);

            return getMapFromTreeEvents(events);
        } catch (IOException e) {
           throw new SnmpWalkException(e);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    protected static Map<OID, Variable> doSnmpWalkV1V2(SnmpCredential credential, OID oid, Vlan vlan) throws SnmpWalkException {

        TransportMapping transport = null;
        Snmp snmp = null;

        try {
            Address targetAddress = GenericAddress.parse("udp:" + credential.getIpAddress().getString() +
                    "/" + credential.getPort().getString());
            CommunityTarget target = new CommunityTarget();
            String snmpCommunity = credential.getSnmpCommunity() + (vlan == null ? "" : "@" + vlan.toString());
            target.setCommunity(new OctetString(snmpCommunity));
            target.setAddress(targetAddress);
            target.setRetries(RETRIES_COUNT);
            target.setTimeout(TIMEOUT);
            target.setVersion(SnmpConstants.version1);

            transport = new DefaultUdpTransportMapping();
            transport.listen();
            snmp = new Snmp(transport);

            TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
            List<TreeEvent> events = treeUtils.getSubtree(target, oid);

            return getMapFromTreeEvents(events);
        } catch (IOException e) {
            throw new SnmpWalkException(e);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
