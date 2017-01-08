/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.devices.entries;

import com.slavick.macmonitor.model.devices.Device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by apanasyonok on 02.11.2016.
 */
public class MacTable {

    Device device;

    private List<MacEntry> macEntries = new ArrayList<>();

    public List<MacEntry> getMacEntries() {
        return macEntries;
    }


    public void setMacEntries(List<MacEntry> macEntries) {
        this.macEntries = macEntries;
    }

    public void add(MacEntry macEntry){
        macEntries.add(macEntry);
    }

    public void remove(MacEntry macEntry){
        macEntries.remove(macEntry);
    }

    public void clear(){
        macEntries.clear();
    }

    public void addAll(Collection<MacEntry> macTableEntries){
        macEntries.addAll(macTableEntries);
    }

   public MacTable() {
    }

    public MacTable(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "MacTable{" +
                "macEntries=" + macEntries +
                '}';
    }
}
