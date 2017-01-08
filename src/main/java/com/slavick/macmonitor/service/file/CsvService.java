/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.service.file;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.slavick.macmonitor.model.devices.orm.credentials.Credential;
import com.slavick.macmonitor.model.devices.orm.crossocket.CrossEntry;
import com.slavick.macmonitor.model.devices.orm.network.ResultEntry;
import com.slavick.macmonitor.model.networkentities.IpAddress;
import com.slavick.macmonitor.model.networkentities.NetworkInterface;
import com.slavick.macmonitor.service.device.CredentialServiceImpl;
import com.slavick.macmonitor.service.device.CrossEntryServiceImpl;
import com.slavick.macmonitor.service.device.ResultEntryServiceImpl;
import com.slavick.macmonitor.service.poll.result.ResultTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by apanasyonok on 12.12.2016.
 */

/*The file must be in the following format:(device ip;network interface;cross;socket)
    "10.10.20.100";"Fa0/2";"6/11/13";"12/58"
    "10.10.20.100";"Fa0/4";"6/11/14";"12/59"
    "10.10.20.100";"Fa0/6";"6/11/15";"12/63"*/

//TODO make this service to run in new thread
@Service("csvService")
public class CsvService {

    @Autowired
    CredentialServiceImpl credentialService;


    @Autowired
    CrossEntryServiceImpl crossEntryService;

    @Autowired
    ResultEntryServiceImpl resultEntryService;

    synchronized public void formTables(MultipartFile file) {

        List<String[]> strings = loadFromMultiPartFile(file);
        Set<CsvCrossSocketEntry> csvCrossSocketEntries = new HashSet<>();

        for (String[] string : strings
                ) {
            try {
                csvCrossSocketEntries.add(new CsvCrossSocketEntry(string));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                continue;
            }
        }

        Map<IpAddress, Credential> ipAddressCredentialMap = new HashMap<>();

        for (Credential credential : credentialService.findAllCredentials()
                ) {
            ipAddressCredentialMap.put(credential.getIpAddress(), credential);
        }

        List<CrossEntry> listForUpdate = new ArrayList<>();
        List<CrossEntry> listForSave = new ArrayList<>();

        for (CsvCrossSocketEntry csvCrossSocketEntry : csvCrossSocketEntries
                ) {

            if (csvCrossSocketEntry.getIpAddress() == null) continue;

            NetworkInterface iface = csvCrossSocketEntry.getNetworkInterface();
            if (iface == null) continue;

            Credential credential = ipAddressCredentialMap.get(csvCrossSocketEntry.getIpAddress());

            if (credential == null) continue;

            CrossEntry crossEntry = crossEntryService.findByCredentialAndNetworkInterface(credential, iface);

            if (crossEntry == null) {
                crossEntry = new CrossEntry();
                crossEntry.setCredential(credential);
                crossEntry.setIface(iface);
                crossEntry.setSocket(csvCrossSocketEntry.getSocket());
                crossEntry.setCross(csvCrossSocketEntry.getCross());
                listForSave.add(crossEntry);

            } else {
                crossEntry.setSocket(csvCrossSocketEntry.getSocket());
                crossEntry.setCross(csvCrossSocketEntry.getCross());
                listForUpdate.add(crossEntry);
            }

        }

        crossEntryService.saveBatch(listForSave);
        crossEntryService.updateBatch(listForUpdate);

    }

    public void writeResultTableCsvIntoWriter(Writer writer) throws IOException {

        List<ResultEntry> resultEntries = resultEntryService.getAll();
        try (CSVWriter csvWriter = new CSVWriter(writer, ';');) {

            for (ResultEntry resultEntry :  resultEntries
                    ) {
                String[] entries = new String[8];

                entries[0] = resultEntry.getMvidEntry().getMacAddress().getMacAddress();
                entries[1] = resultEntry.getMvidEntry().getVlan().toString();

                if(resultEntry.getMvidEntry().getIpAddress()!=null)
                    entries[2] = resultEntry.getMvidEntry().getIpAddress().getString();

                entries[3] = resultEntry.getCredential().getIpAddress().getString();
                entries[4] = resultEntry.getCrossEntry().getIface().getInterfaceName();
                entries[5] = resultEntry.getCrossEntry().getCross();
                entries[6] = resultEntry.getCrossEntry().getSocket();
                entries[7] = resultEntry.getTimeEntry().getDateWithSeconds();

                csvWriter.writeNext(entries, true);
            }
        }


    }


    public void writeCrossTableCsvIntoWriter(Writer writer) throws IOException {

        List<CrossEntry> crossEntries = crossEntryService.getAll();
        try (CSVWriter csvWriter = new CSVWriter(writer, ';');) {



            for (CrossEntry crossEntry : crossEntries
                    ) {
                String[] entries = new String[4];
                entries[0] = crossEntry.getCredential().getIpAddress().getString();
                entries[1] = crossEntry.getIface().getInterfaceName();
                entries[2] = crossEntry.getCross();
                entries[3] = crossEntry.getSocket();
                csvWriter.writeNext(entries, true);
            }
        }


    }


    public static List<String[]> loadFromFile(String fileName) {
        //Get the CSVReader instance with specifying the delimiter to be used


        List<String[]> result = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(fileName), ';')) {

            String[] nextLine;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null) {
                result.add(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static List<String[]> loadFromMultiPartFile(MultipartFile file) {
        //Get the CSVReader instance with specifying the delimiter to be used

        List<String[]> result = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), "UTF-8"), ';')) {

            String[] nextLine;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null) {
                result.add(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
