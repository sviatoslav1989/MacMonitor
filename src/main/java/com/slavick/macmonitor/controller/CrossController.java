/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.controller;

import com.slavick.macmonitor.model.devices.orm.credentials.Credential;
import com.slavick.macmonitor.model.devices.orm.crossocket.CrossEntry;
import com.slavick.macmonitor.model.networkentities.IpAddress;
import com.slavick.macmonitor.service.device.CredentialServiceImpl;
import com.slavick.macmonitor.service.device.CrossEntryServiceImpl;
import com.slavick.macmonitor.service.file.CsvService;
import com.slavick.macmonitor.service.file.FileBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by apanasyonok on 12.12.2016.
 */

//contoller for work with cross table

@Controller
@RequestMapping("/cross")
public class CrossController {

    @Autowired
    CrossEntryServiceImpl crossEntryService;

    @Autowired
    CredentialServiceImpl credentialService;

    @Autowired
    CsvService csvService;


    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String redirect() {
        return "redirect:list";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String redirect1() {
        return "redirect:cross/list";
    }


    @RequestMapping(value = {"/list",}, method = RequestMethod.GET)
    public String listSockets(ModelMap model) {

        List<CrossEntry> crosses = crossEntryService.getAll();
        model.addAttribute("crosses", crosses);
        return "cross/list";
    }




    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        String ipAddressString = "";
        model.put("cross", new CrossEntry());
        model.put("ipAddressString", ipAddressString);
        return "cross/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String save(@ModelAttribute("cross") CrossEntry cross, @ModelAttribute("ipAddressString") String ipAddressString) {
        try {
            IpAddress ipAddress = new IpAddress(ipAddressString);
            Credential credential = credentialService.findByIp(ipAddress);

            if (credential == null) {
                return "redirect:list";
            }

            cross.setCredential(credential);

            CrossEntry tempCrossEntry = crossEntryService.findByCredentialAndNetworkInterface(credential, cross.getIface());

            // if such entry exist do nothing, else persist cross entry
            if (tempCrossEntry == null) {
                crossEntryService.save(cross);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();

        }
        return "redirect:list";

    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String delete(@PathVariable Long id) {
        crossEntryService.deleteById(id);
        return "redirect:list";
    }

    @RequestMapping(value = {"/edit-{id}"}, method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model) {
        CrossEntry cross = crossEntryService.getById(id);
        model.put("cross", cross);
        model.put("credId", cross.getCredential().getCredId());
        return "cross/edit";
    }

    @RequestMapping(value = {"/edit-{id}"}, method = RequestMethod.POST)
    public String edit1(@ModelAttribute("cross") CrossEntry cross, @ModelAttribute("credId") Long credId) {
        cross.setCredential(credentialService.findById(credId));
        crossEntryService.update(cross);
        return "redirect:list";
    }


    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String loadFromCsv(ModelMap model) {

        FileBucket filecsv = new FileBucket();
        model.addAttribute("filecsv", filecsv);
        return "cross/upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String saveFromCsv(FileBucket filecsv, BindingResult result, ModelMap model) throws IOException {
        csvService.formTables(filecsv.getFile());
        return "redirect:list";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadCSV(HttpServletResponse response) {

        String csvFileName = "crosses.csv";

        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

        try (OutputStream outStream = response.getOutputStream();) {

            Writer writer = new OutputStreamWriter(outStream, "UTF-8");
            csvService.writeCrossTableCsvIntoWriter(writer);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}
