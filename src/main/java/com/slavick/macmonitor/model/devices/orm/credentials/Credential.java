/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.devices.orm.credentials;

import com.slavick.macmonitor.model.devices.orm.crossocket.CrossEntry;
import com.slavick.macmonitor.model.devices.orm.network.ResultEntry;
import com.slavick.macmonitor.model.networkentities.IpAddress;
import com.slavick.macmonitor.model.networkentities.Port;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apanasyonok on 10.11.2016.
 */

@Entity
@Table(name = "credential")
@Inheritance(strategy= InheritanceType.JOINED)
@Access( AccessType.FIELD )
public abstract class Credential {

    @Id
    @GeneratedValue
    @Column(name = "cred_id")
    protected Long credId;

    @Column(name = "description")
    protected String description;



    @Embedded
    protected IpAddress ipAddress;

    @Embedded
    protected Port port;

    @OneToMany(mappedBy = "credential" ,orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<ResultEntry> resultEntries = new ArrayList<>();


    @OneToMany(mappedBy = "credential" ,orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<CrossEntry>  crossEntries = new ArrayList<>();

    public List<ResultEntry> getResultEntries() {
        return resultEntries;
    }

    public void setResultEntries(List<ResultEntry> resultEntries) {
        this.resultEntries = resultEntries;
    }

    public List<CrossEntry> getCrossEntries() {
        return crossEntries;
    }

    public void setCrossEntries(List<CrossEntry> crossEntries) {
        this.crossEntries = crossEntries;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getCredId() {
        return credId;
    }

    public void setCredId(Long credId) {
        this.credId = credId;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }


    //for spring mvc device list
    public String getType(){
        return getClass().getSimpleName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SnmpCredential)) return false;

        SnmpCredential that = (SnmpCredential) o;

        return ipAddress.equals(that.ipAddress);

    }

    @Override
    public int hashCode() {
        return ipAddress.hashCode();
    }
}
