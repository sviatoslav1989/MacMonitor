/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.controller;

import com.slavick.macmonitor.model.poll.orm.ScheduleEntry;
import com.slavick.macmonitor.service.poll.schedule.PollExecutorService;
import com.slavick.macmonitor.service.poll.schedule.ScheduleEntryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by apanasyonok on 16.12.2016.
 */

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleEntryServiceImpl scheduleEntryService;

    @Autowired
    PollExecutorService pollExecutorService;

    @RequestMapping(value =  {"/"}, method = RequestMethod.GET)
    public String redirect(){
        return "redirect:list";
    }

    @RequestMapping(value =  "", method = RequestMethod.GET)
    public String redirect1(){
        return "redirect:schedule/list";
    }

    @RequestMapping(value =  "/list", method = RequestMethod.GET)
    public String list(ModelMap model) {
        List<ScheduleEntry> scheduleEntries = scheduleEntryService.getAll();
        model.put("scheduleEntries",scheduleEntries);
        return "schedule/list";
    }

    @RequestMapping(value =  "/add", method = RequestMethod.GET)
    public String add(ModelMap model){
        model.put("hour",new Integer(0));
        model.put("minute",new Integer(0));
        return "schedule/add";
    }

    @RequestMapping(value =  "/add", method = RequestMethod.POST)
    public String addSubmit(@ModelAttribute("hour") Integer hour, @ModelAttribute("minute") Integer minute){

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        ScheduleEntry scheduleEntry = new ScheduleEntry();
        scheduleEntry.setDate(calendar.getTime());
        scheduleEntryService.persist(scheduleEntry);
        return "redirect:list";
    }

    @RequestMapping(value =  "/delete-{id}", method = RequestMethod.GET)
    public String delete(@PathVariable Long id){
        ScheduleEntry scheduleEntry = scheduleEntryService.getById(id);

        if(scheduleEntry!=null) {
            scheduleEntryService.delete(scheduleEntry);
        }

        return "redirect:list";
    }
}
