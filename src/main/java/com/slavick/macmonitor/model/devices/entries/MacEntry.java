/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.devices.entries;

import com.slavick.macmonitor.model.networkentities.MacAddress;
import com.slavick.macmonitor.model.networkentities.NetworkInterface;
import com.slavick.macmonitor.model.networkentities.Vlan;
import com.slavick.macmonitor.model.snmp.snmpenums.PortTrunkStatus;


/**
 * Created by apanasyonok on 04.11.2016.
 */

public class MacEntry {


    private Vlan vlan;

    private MacAddress macAddress;


    private NetworkInterface iface;


    private PortTrunkStatus portTrunkStatus;

    public NetworkInterface getIface() {
        return iface;
    }

    public void setIface(NetworkInterface iface) {
        this.iface = iface;
    }

    protected MacEntry(){}

    public MacEntry(Vlan vlan, MacAddress macAddress, NetworkInterface iface, PortTrunkStatus portTrunkStatus) {

        this.vlan = vlan;
        this.macAddress = macAddress;
        this.iface = iface;
        this.portTrunkStatus = portTrunkStatus;
    }


    public Vlan getVlan() {
        return vlan;
    }

    public void setVlan(Vlan vlan) {
        this.vlan = vlan;
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(MacAddress macAddress) {
        this.macAddress = macAddress;
    }

    public PortTrunkStatus getPortTrunkStatus() {
        return portTrunkStatus;
    }

    public void setPortTrunkStatus(PortTrunkStatus portTrunkStatus) {
        this.portTrunkStatus = portTrunkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MacEntry)) return false;

        MacEntry entry = (MacEntry) o;

        if (vlan != null ? !vlan.equals(entry.vlan) : entry.vlan != null) return false;
        if (macAddress != null ? !macAddress.equals(entry.macAddress) : entry.macAddress != null) return false;
        if (iface != null ? !iface.equals(entry.iface) : entry.iface != null) return false;
        return portTrunkStatus == entry.portTrunkStatus;

    }

    @Override
    public int hashCode() {
        int result = vlan != null ? vlan.hashCode() : 0;
        result = 31 * result + (macAddress != null ? macAddress.hashCode() : 0);
        result = 31 * result + (iface != null ? iface.hashCode() : 0);
        result = 31 * result + (portTrunkStatus != null ? portTrunkStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MacEntry{" +
                "vlan=" + vlan +
                ", macAddress=" + macAddress +
                ", iface=" + iface +
                ", portTrunkStatus=" + portTrunkStatus +
                '}';
    }
}
