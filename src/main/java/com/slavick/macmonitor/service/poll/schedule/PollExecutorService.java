/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.service.poll.schedule;

import com.slavick.macmonitor.model.poll.orm.ScheduleEntry;
import com.slavick.macmonitor.service.poll.result.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by apanasyonok on 20.12.2016.
 */


@Service
public class PollExecutorService {

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    ScheduleEntryServiceImpl scheduleEntryService;

    @Autowired
    @Qualifier("pollServiceMultiThreaded")
    PollService pollService;

    private Map<ScheduleEntry,ScheduledFuture> map = new HashMap<>();

    @Scheduled(fixedRate = Long.MAX_VALUE)
    public  void onProgramStarts(){
        List<ScheduleEntry> entries = scheduleEntryService.getAll();
        for (ScheduleEntry entry: entries
                ) {
            addTime(entry);
        }
    }

    public  void removeTime(ScheduleEntry entry){

        synchronized (this) {
            ScheduledFuture future = map.remove(entry);
            if (future != null) {
                future.cancel(false);
            }
        }
    }



    public  void  addTime(ScheduleEntry entry){
        Calendar currentCalendar = new GregorianCalendar();

        Date scheduleDate = entry.getDate();
        Calendar scheduleCalendar = new GregorianCalendar();
        scheduleCalendar.setTime(scheduleDate);
        scheduleCalendar.set(Calendar.ERA,currentCalendar.get(Calendar.ERA));
        scheduleCalendar.set(Calendar.YEAR,currentCalendar.get(Calendar.YEAR));
        scheduleCalendar.set(Calendar.DAY_OF_YEAR,currentCalendar.get(Calendar.DAY_OF_YEAR));

        if(scheduleCalendar.before(currentCalendar)){
            scheduleCalendar.add(Calendar.DAY_OF_YEAR,1);
        }

        synchronized (this) {
            ScheduledFuture future = taskScheduler.scheduleAtFixedRate(pollService, scheduleCalendar.getTime(), 24 * 60 * 60 * 1000);
            map.put(entry, future);
        }
    }
}
