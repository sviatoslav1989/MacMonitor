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
import com.slavick.macmonitor.model.networkentities.Vlan;

import javax.persistence.Embedded;

/**
 * Created by apanasyonok on 09.11.2016.
 */

public class FDBTableEntry {
    @Embedded
    private MacAddress macAddress;

    private int bridgePortIndex;

    public Vlan getVlan() {
        return vlan;
    }

    @Embedded
    private Vlan vlan;

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public int getBridgePortIndex() {
        return bridgePortIndex;
    }

    public FDBTableEntry(Vlan vlan,MacAddress macAddress, int bridgePortIndex) {
        this.macAddress = macAddress;
        this.bridgePortIndex = bridgePortIndex;
        this.vlan = vlan;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FDBTableEntry)) return false;

        FDBTableEntry that = (FDBTableEntry) o;

        if (bridgePortIndex != that.bridgePortIndex) return false;
        if (!macAddress.equals(that.macAddress)) return false;
        return vlan.equals(that.vlan);

    }

    @Override
    public int hashCode() {
        int result = macAddress.hashCode();
        result = 31 * result + bridgePortIndex;
        result = 31 * result + vlan.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FDBTableEntry{" +
                "macAddress=" + macAddress +
                ", bridgePortIndex=" + bridgePortIndex +
                ", vlan=" + vlan +
                "}\n";
    }
}
