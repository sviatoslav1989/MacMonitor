/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.devices.entries;

/**
 * Created by apanasyonok on 09.11.2016.
 */
public class BridgePortEntry{
    private int bridgePortIndex;
    private int ifIndex;

    public int getBridgePortIndex() {
        return bridgePortIndex;
    }

    public int getIfIndex() {
        return ifIndex;
    }

    public BridgePortEntry(int bridgePortIndex, int ifIndex) {
        this.bridgePortIndex = bridgePortIndex;
        this.ifIndex = ifIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BridgePortEntry that = (BridgePortEntry) o;

        if (bridgePortIndex != that.bridgePortIndex) return false;
        return ifIndex == that.ifIndex;

    }

    @Override
    public int hashCode() {
        int result = bridgePortIndex;
        result = 31 * result + ifIndex;
        return result;
    }

    @Override
    public String toString() {
        return "BridgePortEntry{" +
                "bridgePortIndex=" + bridgePortIndex +
                ", ifIndex=" + ifIndex +
                '}'+'\n';
    }
}
