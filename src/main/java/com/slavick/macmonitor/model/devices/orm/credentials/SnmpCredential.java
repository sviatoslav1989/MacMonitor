/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.devices.orm.credentials;


import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3AuthProtocol;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3PrivProtocol;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3SecurityLevel;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpVersion;

import javax.persistence.*;

/**
 * Created by apanasyonok on 09.11.2016.
 */

@Entity
@Table(name="snmp_credential")
@PrimaryKeyJoinColumn(name="cred_id")
@Access( AccessType.FIELD )
public final class SnmpCredential extends Credential  {


    @Enumerated
    @Column(name = "snmp_version",nullable = false)
    private SnmpVersion snmpVersion;

    @Column(name = "community")
    private String snmpCommunity;

    @Enumerated
    @Column(name = "snmp_v3_security_level")
    private SnmpV3SecurityLevel snmpV3SecurityLevel;

    @Column(name = "snmp_v3_security_name")
    private String snmpV3SecurityName;

    @Enumerated
    @Column(name = "snmp_v3_auth_protocol")
    private SnmpV3AuthProtocol snmpV3AuthProtocol;

    @Enumerated
    @Column(name = "snmp_v3_priv_protocol")
    private SnmpV3PrivProtocol snmpV3PrivProtocol;

    @Column(name = "auth_key")
    private String authKey;

    @Column(name = "priv_key")
    private String privKey;


    public SnmpVersion getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(SnmpVersion snmpVersion) {
        this.snmpVersion = snmpVersion;
    }

    public String getSnmpCommunity() {
        return snmpCommunity;
    }

    public void setSnmpCommunity(String snmpCommunity) {
        this.snmpCommunity = snmpCommunity;
    }

    public SnmpV3SecurityLevel getSnmpV3SecurityLevel() {
        return snmpV3SecurityLevel;
    }

    public void setSnmpV3SecurityLevel(SnmpV3SecurityLevel snmpV3SecurityLevel) {
        this.snmpV3SecurityLevel = snmpV3SecurityLevel;
    }

    public String getSnmpV3SecurityName() {
        return snmpV3SecurityName;
    }

    public void setSnmpV3SecurityName(String snmpV3SecurityName) {
        this.snmpV3SecurityName = snmpV3SecurityName;
    }

    public SnmpV3AuthProtocol getSnmpV3AuthProtocol() {
        return snmpV3AuthProtocol;
    }

    public void setSnmpV3AuthProtocol(SnmpV3AuthProtocol snmpV3AuthProtocol) {
        this.snmpV3AuthProtocol = snmpV3AuthProtocol;
    }

    public SnmpV3PrivProtocol getSnmpV3PrivProtocol() {
        return snmpV3PrivProtocol;
    }

    public void setSnmpV3PrivProtocol(SnmpV3PrivProtocol snmpV3PrivProtocol) {
        this.snmpV3PrivProtocol = snmpV3PrivProtocol;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getPrivKey() {
        return privKey;
    }

    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }


    //for spring mvc device list
    @Override public String getType(){
        return "CiscoSnmp" + snmpVersion.toString();
    }
}
