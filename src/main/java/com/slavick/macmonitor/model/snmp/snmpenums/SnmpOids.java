/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.snmp.snmpenums;

/**
 * Created by apanasyonok on 02.11.2016.
 */
public enum  SnmpOids {
    VTP_VLAN_STATE("1.3.6.1.4.1.9.9.46.1.3.1.1.2"),
    DOT1D_TP_FDB_Port("1.3.6.1.2.1.17.4.3.1.2"),
    DOT1D_BASE_PORT_IF_INDEX("1.3.6.1.2.1.17.1.4.1.2"),
    IF_X_ENTRY("1.3.6.1.2.1.31.1.1.1.1"),
    AT_PHYS_ADDRESS("1.3.6.1.2.1.3.1.1.2"),
    VLAN_TRUNK_PORT_DYNAMIC_STATUS("1.3.6.1.4.1.9.9.46.1.6.1.1.14");

    private String oidString;

    SnmpOids(String oidString) {
        this.oidString = oidString;
    }

    public String getOidString() {
        return oidString;
    }
}

