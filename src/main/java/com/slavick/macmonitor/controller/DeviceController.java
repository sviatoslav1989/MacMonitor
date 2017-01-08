/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.controller;

import com.slavick.macmonitor.model.devices.Device;
import com.slavick.macmonitor.model.devices.entries.ArpTable;
import com.slavick.macmonitor.model.devices.entries.MacTable;
import com.slavick.macmonitor.model.devices.orm.credentials.Credential;
import com.slavick.macmonitor.model.devices.orm.credentials.SnmpCredential;
import com.slavick.macmonitor.model.devices.orm.crossocket.CrossEntry;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3AuthProtocol;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3PrivProtocol;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpV3SecurityLevel;
import com.slavick.macmonitor.model.snmp.snmpenums.SnmpVersion;
import com.slavick.macmonitor.service.device.CredentialServiceImpl;
import com.slavick.macmonitor.service.device.CrossEntryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by apanasyonok on 12.12.2016.
 */

@Controller
@RequestMapping("/device")

public class DeviceController {



    @Autowired
    CredentialServiceImpl credentialService;

    @Autowired
    CrossEntryServiceImpl crossEntryService;

    @RequestMapping(value =  {"/"}, method = RequestMethod.GET)
    public String redirect(){
        return "redirect:list";
    }

    @RequestMapping(value =  "", method = RequestMethod.GET)
    public String redirect1(){
        return "redirect:device/list";
    }

    @RequestMapping(value =  "/list", method = RequestMethod.GET)
    public String listDevices(ModelMap model) {

        List<Credential> credentials = credentialService.findAllCredentials();
        model.addAttribute("credentials", credentials);
        model.addAttribute("type","snmp");
        return "device/list";
    }

    @RequestMapping(value = { "/add" }, method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.put("credential", new SnmpCredential());
        model.put("snmpV3SecurityLevel", SnmpV3SecurityLevel.values());
        model.put("snmpV3AuthProtocol", SnmpV3AuthProtocol.values());
        model.put("snmpV3PrivProtocol", SnmpV3PrivProtocol.values());
        model.put("snmpVersion", SnmpVersion.values());
        return "device/add";
    }

    @RequestMapping(value = { "/add" }, method = RequestMethod.POST)
    public String submit(@ModelAttribute SnmpCredential credential) {
        credentialService.saveCredential(credential);
        return "redirect:list";
    }

    @RequestMapping(value = { "/copy-{id}" }, method = RequestMethod.GET)
    public String copy(@PathVariable Long id,ModelMap model) {
        Credential credential=credentialService.findById(id);
        credential.setCredId(null);
        model.put("credential", credential);
        model.put("snmpV3SecurityLevel", SnmpV3SecurityLevel.values());
        model.put("snmpV3AuthProtocol", SnmpV3AuthProtocol.values());
        model.put("snmpV3PrivProtocol", SnmpV3PrivProtocol.values());
        model.put("snmpVersion", SnmpVersion.values());
        return "device/copy";
    }

    @RequestMapping(value = { "/copy-{id}" }, method = RequestMethod.POST)
    public String copySubmit(@ModelAttribute SnmpCredential credential) {
        credentialService.saveCredential(credential);
        return "redirect:list";
    }

    @RequestMapping(value = { "/edit-{id}" }, method = RequestMethod.GET)
    public String edit(@PathVariable Long id,ModelMap model) {
        Credential credential=credentialService.findById(id);
        model.put("credential",credential);
        model.put("snmpV3SecurityLevel", SnmpV3SecurityLevel.values());
        model.put("snmpV3AuthProtocol", SnmpV3AuthProtocol.values());
        model.put("snmpV3PrivProtocol", SnmpV3PrivProtocol.values());
        model.put("snmpVersion", SnmpVersion.values());
        return "device/edit";
    }



    @RequestMapping(value = { "/edit-{id}" }, method = RequestMethod.POST)
    public String edit1(@ModelAttribute("credential") SnmpCredential credential) {
        credentialService.update(credential);
        return "redirect:list";
    }

    @RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
    public String delete(@PathVariable Long id,ModelMap model) {
       Credential credential= credentialService.getWithLazyData(id);
        if(credential!=null){
            credentialService.deleteCredentialById(id);
        }
        return "redirect:list";
    }

    @RequestMapping(value = { "/arp-{id}" }, method = RequestMethod.GET)
    public String showArp(@PathVariable Long id, ModelMap model) {
        Credential credential = credentialService.findById(id);
        Device device = Device.create(credential);

        ArpTable arpTable = device.getArpTable();
        model.addAttribute("arpTable",arpTable);
        return "device/arp";
    }

    @RequestMapping(value = { "/mac-{id}" }, method = RequestMethod.GET)
    public String showMac(@PathVariable Long id, ModelMap model) {
        Credential credential = credentialService.findById(id);
        Device device = Device.create(credential);

        MacTable macTable = device.getMacTable();
        model.addAttribute("macTable",macTable);
        return "device/mac";
    }

    @RequestMapping(value = { "/cross-{id}" }, method = RequestMethod.GET)
    public String showCross(@PathVariable Long id, ModelMap model) {
        Credential credential = credentialService.findById(id);
        List<CrossEntry> crossEntries = crossEntryService.getAllForCredential(credential);

        model.addAttribute("crossEntries",crossEntries);
        model.addAttribute("credential", credential);
        return "device/cross";
    }



}
