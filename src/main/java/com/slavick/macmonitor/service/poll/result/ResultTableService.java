/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.service.poll.result;

import com.slavick.macmonitor.model.devices.dns.DnsEntry;
import com.slavick.macmonitor.model.devices.entries.ArpEntry;
import com.slavick.macmonitor.model.devices.entries.ArpTable;
import com.slavick.macmonitor.model.devices.entries.MacEntry;
import com.slavick.macmonitor.model.devices.entries.MacTable;
import com.slavick.macmonitor.model.devices.orm.crossocket.CrossEntry;
import com.slavick.macmonitor.model.devices.orm.network.MVIDEntry;
import com.slavick.macmonitor.model.devices.orm.network.ResultEntry;
import com.slavick.macmonitor.model.networkentities.IpAddress;
import com.slavick.macmonitor.model.networkentities.MacAddress;
import com.slavick.macmonitor.model.poll.orm.TimeEntry;
import com.slavick.macmonitor.model.snmp.snmpenums.PortTrunkStatus;
import com.slavick.macmonitor.service.device.CrossEntryServiceImpl;
import com.slavick.macmonitor.service.device.MvidEntryServiceImpl;
import com.slavick.macmonitor.service.device.ResultEntryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apanasyonok on 30.12.2016.
 */
@Service
public class ResultTableService {
    @Autowired
    ResultEntryServiceImpl resultEntryService;

    @Autowired
    MvidEntryServiceImpl mvidService;

    @Autowired
    CrossEntryServiceImpl crossService;

    public void fillResultTable(List<MacTable> macTables, List<ArpTable> arpTables, TimeEntry timeEntry){

        Map<IpAddress, DnsEntry> ipDnsMap = new HashMap<>();
        Map<MacAddress, ArpEntry> macArpMap = new HashMap<>();

        //make ip dns map from arp table, and make macArpTAble
        for (ArpTable arpTable : arpTables
                ) {

            for (ArpEntry arpEntry : arpTable
                    ) {
                //TODO dns support// ipDnsMap.put(arpEntry.getIpAddress(),new DnsEntry(arpEntry.getIpAddress()));
                macArpMap.put(arpEntry.getMacAddress(), arpEntry);
            }


        }

        for (MacTable macTable : macTables
                ) {


            for (MacEntry macEntry : macTable.getMacEntries()
                    ) {

                if (macEntry.getPortTrunkStatus() == PortTrunkStatus.NOT_TRUNK) {

                    MVIDEntry mvidEntry = new MVIDEntry();
                    mvidEntry.setMacAddress(macEntry.getMacAddress());
                    mvidEntry.setVlan(macEntry.getVlan());

                    if (macArpMap.containsKey(macEntry.getMacAddress())) {
                        IpAddress ipAddress = macArpMap.get(macEntry.getMacAddress()).getIpAddress();
                        mvidEntry.setIpAddress(ipAddress);

                        // mvidEntry.setDnsEntry(ipDnsMap.get(ipAddress));

                    }


                    Long mvidId = mvidService.getIdByValue(mvidEntry);
                    ResultEntry resultEntry = new ResultEntry();

                    if (mvidId == null) {
                        mvidService.save(mvidEntry);

                    } else {
                        mvidEntry = mvidService.getById(mvidId);
                    }
                    resultEntry.setMvidEntry(mvidEntry);
                    if(mvidEntry==null){
                        System.out.println(1);
                    }

                    CrossEntry crossEntry = new CrossEntry();
                    crossEntry.setCredential(macTable.getDevice().getCredential());
                    crossEntry.setIface(macEntry.getIface());

                    CrossEntry tempCrossEntry = crossService.findByCredentialAndNetworkInterface(crossEntry.getCredential(),crossEntry.getIface());
                    Long crossId = null;

                    if(tempCrossEntry!=null){
                        crossId = tempCrossEntry.getId();
                    }

                    if (crossId == null) {
                        crossService.save(crossEntry);
                    } else {
                        crossEntry = crossService.getById(crossId);
                    }

                    resultEntry.setCrossEntry(crossEntry);


                    resultEntry.setCredential(macTable.getDevice().getCredential());
                    resultEntry.setTimeEntry(timeEntry);

                    Long resId = resultEntryService.findIdByValueIgnoreTime(resultEntry);
                    if(resId==null){

                        resultEntryService.save(resultEntry);
                    }
                    else {
                        resultEntry.setId(resId);
                        resultEntryService.update(resultEntry);
                    }


                }


            }

        }

    }
}
