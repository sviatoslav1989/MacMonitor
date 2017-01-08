/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.controller;

import com.slavick.macmonitor.model.poll.orm.TimeEntry;
import com.slavick.macmonitor.service.device.TimeEntryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by apanasyonok on 15.12.2016.
 */

@Controller
@RequestMapping("/timestamp")
public class TimeStampController {
    @Autowired
    TimeEntryServiceImpl timeEntryService;

    @RequestMapping(value =  {"/"}, method = RequestMethod.GET)
    public String redirect(){
        return "redirect:list";
    }

    @RequestMapping(value =  "", method = RequestMethod.GET)
    public String redirect1(){
        return "redirect:result/list";
    }

    @RequestMapping(value =  "/list", method = RequestMethod.GET)
    public String listResult(ModelMap model) {
        List<TimeEntry> timeEntries = timeEntryService.getAll();
        model.put("timeEntries",timeEntries);
        return "timestamp/list";
    }

    @RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
    public String delete(@PathVariable Long id, ModelMap model) {
        TimeEntry timeEntry = timeEntryService.findWithLazyDataById(id);
        timeEntryService.deleteTimeEntry(timeEntry);
        return "redirect:list";
    }
}
