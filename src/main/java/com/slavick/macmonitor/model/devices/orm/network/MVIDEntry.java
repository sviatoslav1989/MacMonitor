/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.devices.orm.network;

import com.slavick.macmonitor.model.devices.dns.DnsEntry;
import com.slavick.macmonitor.model.networkentities.IpAddress;
import com.slavick.macmonitor.model.networkentities.MacAddress;
import com.slavick.macmonitor.model.networkentities.Vlan;

import javax.persistence.*;

/**
 * Created by apanasyonok on 05.12.2016.
 */

// mac, vlan, ip address, dns entry
@Entity(name = "mvid")
@Access(AccessType.FIELD)
public class MVIDEntry {

    @Id
    @GeneratedValue
    @Column(name = "mvid_id")
    private long mvidId;

    @Embedded
    MacAddress macAddress;

    @Embedded
    Vlan vlan;

    @Embedded
    IpAddress ipAddress;

    @Embedded
    DnsEntry dnsEntry;

    public long getMvidId() {
        return mvidId;
    }

    public void setMvidId(long mvidId) {
        this.mvidId = mvidId;
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(MacAddress macAddress) {
        this.macAddress = macAddress;
    }

    public Vlan getVlan() {
        return vlan;
    }

    public void setVlan(Vlan vlan) {
        this.vlan = vlan;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public DnsEntry getDnsEntry() {
        return dnsEntry;
    }

    public void setDnsEntry(DnsEntry dnsEntry) {
        this.dnsEntry = dnsEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MVIDEntry)) return false;

        MVIDEntry mvidEntry = (MVIDEntry) o;

        if (!macAddress.equals(mvidEntry.macAddress)) return false;
        if (vlan != null ? !vlan.equals(mvidEntry.vlan) : mvidEntry.vlan != null) return false;
        if (ipAddress != null ? !ipAddress.equals(mvidEntry.ipAddress) : mvidEntry.ipAddress != null) return false;
        return dnsEntry != null ? dnsEntry.equals(mvidEntry.dnsEntry) : mvidEntry.dnsEntry == null;

    }

    @Override
    public int hashCode() {
        return macAddress.hashCode();
    }
}
