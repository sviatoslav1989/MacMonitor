/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.service.file;

import com.slavick.macmonitor.model.networkentities.IpAddress;
import com.slavick.macmonitor.model.networkentities.NetworkInterface;

import java.net.UnknownHostException;

/**
 * Created by apanasyonok on 14.12.2016.
 */


public class CsvCrossSocketEntry {

    IpAddress ipAddress;
    NetworkInterface networkInterface;
    String cross;
    String socket;

    public CsvCrossSocketEntry() {
    }

    public CsvCrossSocketEntry(String[] strings) throws UnknownHostException {
        if (strings != null) {

            if (strings.length >= 3) {
                ipAddress = new IpAddress(strings[0]);
                networkInterface = new NetworkInterface(strings[1]);

                if (strings[2].equals("") | strings[2].toLowerCase().equals("null")) {
                    cross = null;
                } else {
                    cross = strings[2];
                }

                if (strings.length >= 4) {
                    if (strings[3].equals("") | strings[3].toLowerCase().equals("null")) {
                        socket = null;
                    } else {
                        socket = strings[3];
                    }
                }
            }


        }
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    public String getCross() {
        return cross;
    }

    public void setCross(String cross) {
        this.cross = cross;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CsvCrossSocketEntry)) return false;

        CsvCrossSocketEntry that = (CsvCrossSocketEntry) o;

        if (!ipAddress.equals(that.ipAddress)) return false;
        return networkInterface.equals(that.networkInterface);

    }

    @Override
    public int hashCode() {
        int result = ipAddress.hashCode();
        result = 31 * result + networkInterface.hashCode();
        return result;
    }
}
