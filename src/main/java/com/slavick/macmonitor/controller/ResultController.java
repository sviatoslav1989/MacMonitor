/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.controller;

import com.slavick.macmonitor.dao.device.sortenum.ResultEntrySortOrderEnum;
import com.slavick.macmonitor.model.devices.orm.network.ResultEntry;
import com.slavick.macmonitor.service.device.ResultEntryServiceImpl;
import com.slavick.macmonitor.service.file.CsvService;
import com.slavick.macmonitor.service.poll.result.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * Created by apanasyonok on 12.12.2016.
 */

@Controller
@RequestMapping("/result")
public class ResultController {

    @Autowired
    ResultEntryServiceImpl resultEntryService;

    @Autowired
    @Qualifier("pollServiceMultiThreaded")
    PollService pollService;

    @Autowired
    CsvService csvService;

    @RequestMapping(value =  {"/"}, method = RequestMethod.GET)
    public String redirect(){
        return "redirect:list";
    }

    @RequestMapping(value =  "", method = RequestMethod.GET)
    public String redirect1(){
        return "redirect:result/list";
    }

    @RequestMapping(value =  "/list", method = RequestMethod.GET)
    public String listResult(ModelMap model, @RequestParam(value = "order",required = false) ResultEntrySortOrderEnum order) {
        List<ResultEntry> resultEntries=null;
        if(order==null){
            resultEntries = resultEntryService.getAll();}
        else {
            resultEntries = resultEntryService.getAll(order);
        }
        model.addAttribute("resultEntries", resultEntries);

        return "result/list";
    }

    @RequestMapping(value =  {"/poll"}, method = RequestMethod.GET)
    public String poll(){
        Thread thread = new Thread(pollService);
        thread.start();
        return "redirect:list";
    }

    @RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
    public String delete(@PathVariable Long id, ModelMap model) {
        ResultEntry resultEntry = resultEntryService.getById(id);
        if(resultEntry!=null){
            resultEntryService.delete(resultEntry);
        }
        return "redirect:list";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadCSV(HttpServletResponse response)  {

        String csvFileName = "result.csv";

        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

        try (OutputStream outStream = response.getOutputStream();) {

            Writer writer = new OutputStreamWriter(outStream, "UTF-8");
            csvService.writeResultTableCsvIntoWriter(writer);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }



}
