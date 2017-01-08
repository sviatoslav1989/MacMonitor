/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.service.poll.result;

import com.slavick.macmonitor.model.devices.Device;
import com.slavick.macmonitor.model.devices.entries.ArpTable;
import com.slavick.macmonitor.model.devices.entries.MacTable;
import com.slavick.macmonitor.model.devices.orm.credentials.Credential;
import com.slavick.macmonitor.model.poll.orm.TimeEntry;
import com.slavick.macmonitor.service.device.CredentialServiceImpl;
import com.slavick.macmonitor.service.device.TimeEntryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by apanasyonok on 28.11.2016.
 */


@Service("pollServiceSingleThreaded")
public class PollServiceSingleThreaded implements PollService {

    static Logger log = Logger.getLogger(PollServiceSingleThreaded.class.getName());

    @Autowired
    CredentialServiceImpl credentialService;

    @Autowired
    TimeEntryServiceImpl timeEntryService;

    @Autowired
    ResultTableService resultTableService;

    private final ReentrantLock lock = new ReentrantLock();


    @Override
    public void run() {
        pollDevices();
    }

    @Override
    synchronized public void pollDevices() {
        log.log(Level.INFO, "Poll devices start");

        boolean isAcquired = lock.tryLock();

        if (isAcquired == true) {

            try {
                log.log(Level.INFO, "Lock was ucquired");
                List<Credential> credentials = credentialService.findAllCredentials();
                List<Device> devices = new ArrayList<>();

                TimeEntry timeEntry = new TimeEntry(new Date());
                timeEntryService.saveTimeEntry(timeEntry);

                for (Credential credential : credentials) {
                    devices.add(Device.create(credential));
                }

                List<ArpTable> arpTables = new ArrayList<>();
                List<MacTable> macTables = new ArrayList<>();


                for (Device device : devices
                        ) {

                    arpTables.add(device.getArpTable());
                    macTables.add(device.getMacTable());
                }

                resultTableService.fillResultTable(macTables, arpTables, timeEntry);
            } finally {
                lock.unlock();
            }

        } else {
            log.info("Lock is blocked by another thread");
        }
        log.info("Poll devices end");

    }
}
