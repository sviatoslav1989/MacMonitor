/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.devices;


import com.slavick.macmonitor.model.devices.entries.*;
import com.slavick.macmonitor.model.devices.exceptions.*;
import com.slavick.macmonitor.model.devices.orm.credentials.SnmpCredential;
import com.slavick.macmonitor.model.networkentities.IpAddress;
import com.slavick.macmonitor.model.networkentities.MacAddress;
import com.slavick.macmonitor.model.networkentities.NetworkInterface;
import com.slavick.macmonitor.model.networkentities.Vlan;
import com.slavick.macmonitor.model.networkentities.comparators.VlanComparator;
import com.slavick.macmonitor.model.snmp.SnmpService;
import com.slavick.macmonitor.model.snmp.expceptions.SnmpWalkException;
import com.slavick.macmonitor.model.snmp.snmpenums.PortTrunkStatus;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpOids;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by apanasyonok on 02.11.2016.
 */
public class CiscoSnmpDevice extends Device {

    protected MacTable macTable;
    protected Set<Vlan> vlanSet;
    protected FDBTable fdbTable;
    protected BridgePortTable bridgePortTable;
    protected IfXEntryTable ifXEntryTable;
    protected ArpTable arpTable;
    protected PortTrunkStatusTable portTrunkStatusTable;
    protected SnmpCredential snmpCredential;

    public CiscoSnmpDevice(SnmpCredential snmpCredential){
        super(snmpCredential);
        this.snmpCredential=snmpCredential;
    }


    protected PortTrunkStatusTable fetchPortTrunkStatusTable() throws PortTrunkStatusTableCreationException {
        PortTrunkStatusTable portTrunkStatusTable = new PortTrunkStatusTable();

        Map<OID, Variable> map = null;

        try {

            map = SnmpService.doSnmpWalk(snmpCredential, new OID(SnmpOids.VLAN_TRUNK_PORT_DYNAMIC_STATUS.getOidString()));
        } catch (SnmpWalkException e) {

            throw new PortTrunkStatusTableCreationException(e);
        }

        for (Map.Entry<OID, Variable> entry : map.entrySet()
                ) {
            OID oid = entry.getKey();
            Variable variable = entry.getValue();

            int ifIndex = oid.last();
            PortTrunkStatus status = PortTrunkStatus.getPortStatusByValue(variable.toInt());

            portTrunkStatusTable.add(new PortTrunkStatusTableEntry(ifIndex,status));
        }


        return portTrunkStatusTable;
    }

    protected ArpTable fetchArpTable() throws ArpTableCreationException {
        ArpTable ArpTable = new ArpTable(this);

        Map<OID, Variable> map = null;

        try {


            map = SnmpService.doSnmpWalk(snmpCredential, new OID(SnmpOids.AT_PHYS_ADDRESS.getOidString()));
        } catch (SnmpWalkException e) {
            throw new ArpTableCreationException(e);
        }

        for (Map.Entry<OID, Variable> entry : map.entrySet()
                ) {

            OID oid = entry.getKey();
            Variable variable = entry.getValue();
            MacAddress macAddress = new MacAddress(variable.toString().replaceAll(":","").toLowerCase());

            //get ip address from oid(last four numbers of oid are ip address)
            StringBuilder ip =new StringBuilder();
            for(int i = 0; i < 4;i++){
                ip.insert(0,oid.removeLast()+" ");
            }

            IpAddress ipAddress = null;
            try {
                ipAddress = new IpAddress(ip.toString().trim().replaceAll(" ","."));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                continue;
            }

            //get vlan
            oid.removeLast();
            int id = oid.last();
            //add only needed vlan, exlude 0,1002-1005,and Vlan more than 4094
            if (id >= 1 && id <= 4094) {
                if (id < 1002 || id > 1005) {
                    Vlan vlan = new Vlan(id);
                    ArpTable.add(new ArpEntry(vlan,ipAddress,macAddress));
                }
            }
        }


        return ArpTable;

    }

    protected IfXEntryTable fetchIfXEntryTable() throws IfXEntryTableCreationException {
        IfXEntryTable ifXEntryTable = new IfXEntryTable();

        Map<OID, Variable> map = null;
        try {


            map = SnmpService.doSnmpWalk(snmpCredential, new OID(SnmpOids.IF_X_ENTRY.getOidString()));

        } catch (SnmpWalkException e) {
            throw new IfXEntryTableCreationException(e);
        }

        for (Map.Entry<OID, Variable> entry : map.entrySet()
                ) {

            OID oid = entry.getKey();
            Variable variable = entry.getValue();

            ifXEntryTable.add(new IfXEntry(oid.last(),variable.toString()));
        }

        return ifXEntryTable;
    }

    protected BridgePortTable fetchBridgePortTable() throws BridgePortTableCreationException {
        BridgePortTable bridgePortTable = new BridgePortTable();

        for (Vlan vlan : vlanSet
                ) {
            Map<OID, Variable> map = null;
            try {

                map = SnmpService.doSnmpWalk(snmpCredential, new OID(SnmpOids.DOT1D_BASE_PORT_IF_INDEX.getOidString()),vlan);
            } catch (SnmpWalkException e) {
               throw new BridgePortTableCreationException(e);
            }


            for (Map.Entry<OID, Variable> entry : map.entrySet()
                    ) {

                OID oid = entry.getKey();
                Variable variable = entry.getValue();

                bridgePortTable.add(new BridgePortEntry(oid.last(),variable.toInt()));

            }
        }

        return bridgePortTable;
    }

    protected FDBTable fetchFDBTable() throws FDBTableCreationException {

        FDBTable fdbTable = new FDBTable();

        for (Vlan vlan : vlanSet
                ) {
            Map<OID, Variable> map = null;
            try {

                map = SnmpService.doSnmpWalk(snmpCredential, new OID(SnmpOids.DOT1D_TP_FDB_Port.getOidString()),vlan);
            } catch (SnmpWalkException e) {
                throw new FDBTableCreationException(e);
            }

            for (Map.Entry<OID, Variable> entry : map.entrySet()
                    ) {

                //macOidString convert example (240.146.28.231.92.87 ->f0921ce75c57 )
                OID oid = entry.getKey();
                Variable variable = entry.getValue();
                StringBuilder mac = new StringBuilder();
                for (int i = 0; i < 6; i++) {
                    int number = oid.removeLast();
                    mac.insert(0, String.format("%02X", number).toLowerCase());
                }

                fdbTable.add(new FDBTableEntry(vlan, new MacAddress(mac.toString()), variable.toInt()));
            }

        }

        return fdbTable;
    }

    protected Set<Vlan> fetchVlanSet() throws VlanSetCreationException {

        Set<Vlan> vlanSet = new TreeSet<>(new VlanComparator());

        Map<OID, Variable> map = null;
        try {

            map = SnmpService.doSnmpWalk(snmpCredential, new OID(SnmpOids.VTP_VLAN_STATE.getOidString()));
        } catch (SnmpWalkException e) {
            throw new VlanSetCreationException();
        }

        for (OID oid : map.keySet()
                ) {
            //last number of oid is a vlan id(1.3.6.1.4.1.9.9.46.1.3.1.1.2.1.33), 33 is a vlan id
            int id = oid.last();
            //add only needed vlans, exlude 0,1002-1005,and Vlan more than 4094
            if (id >= 1 && id <= 4094) {
                if (id < 1002 || id > 1005)
                    vlanSet.add(new Vlan(oid.last()));
            }

        }

        return vlanSet;
    }


    private void formMacTable(){
        MacTable macTable = new MacTable(this);

        for (FDBTableEntry fdbTableEntry: fdbTable

                ) {

            Vlan vlan = fdbTableEntry.getVlan();
            MacAddress macAddress= fdbTableEntry.getMacAddress();

            int bridgePortIndex = fdbTableEntry.getBridgePortIndex();
            try {
                int ifIndex = bridgePortTable.findIfIndexByBridgePortIndex(bridgePortIndex);
                String ifName = ifXEntryTable.findIfNameByIfIndex(ifIndex);
                PortTrunkStatus status = portTrunkStatusTable.findPortTrunkStatusByIfIndex(ifIndex);

                MacEntry macEntry = new MacEntry(vlan,macAddress,new NetworkInterface(ifName), status);

                macTable.add(macEntry);

            } catch (NoSuchBridgePortIndexException |NoSuchIfIndexException noSuchBridgePortIndexException) {
                noSuchBridgePortIndexException.printStackTrace();
                continue;
            }



        }
        this.macTable = macTable;
    }

    @Override
    public ArpTable getArpTable() {
        arpTable = null;

        //get  arp table
        try {
            arpTable = fetchArpTable();
        } catch (ArpTableCreationException e) {
            e.printStackTrace();
        }
        return arpTable;

    }

    @Override
    public MacTable getMacTable() {
        macTable = null;
        try {
            //get all existing vlans on switch
            vlanSet = fetchVlanSet();

            //get links beetwen bridgePortId and ifIndex
            bridgePortTable = fetchBridgePortTable();

            //get links beetwen bridgePortId and ifName
            ifXEntryTable = fetchIfXEntryTable();

            //get port status(trunk or access)
            portTrunkStatusTable = fetchPortTrunkStatusTable();
            //get mac table for each vlan
            fdbTable = fetchFDBTable();

            formMacTable();
            //System.out.println(macTable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return macTable;
    }
}
