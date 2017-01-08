/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.devices.orm.network;

import com.slavick.macmonitor.model.devices.orm.credentials.Credential;
import com.slavick.macmonitor.model.devices.orm.crossocket.CrossEntry;
import com.slavick.macmonitor.model.poll.orm.TimeEntry;

import javax.persistence.*;


/**
 * Created by apanasyonok on 02.12.2016.
 */
@Entity(name = "result_table")
public class ResultEntry {

    public ResultEntry() {
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    long id;

    @ManyToOne
    @JoinColumn(name = "credential_id_fk"
           /* foreignKey = @ForeignKey(name = "credential_id_fk")*/)
    Credential credential;

    @ManyToOne
    @JoinColumn(name = "mvid_entry_id_fk"
            /*foreignKey = @ForeignKey(name = "mvid_entry_id_fk")*/)
    MVIDEntry mvidEntry;

    @ManyToOne
    @JoinColumn(name = "cross_id_fk"/*,
            foreignKey = @ForeignKey(name = "cross_id_fk")*/)
    CrossEntry crossEntry;

    @ManyToOne
    @JoinColumn(name = "time_id_fk"/*,
            foreignKey = @ForeignKey(name = "time_id_fk")*/)
    TimeEntry timeEntry;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public MVIDEntry getMvidEntry() {
        return mvidEntry;
    }

    public void setMvidEntry(MVIDEntry mvidEntry) {
        this.mvidEntry = mvidEntry;
    }

    public CrossEntry getCrossEntry() {
        return crossEntry;
    }

    public void setCrossEntry(CrossEntry crossEntry) {
        this.crossEntry = crossEntry;
    }

    public TimeEntry getTimeEntry() {
        return timeEntry;
    }

    public void setTimeEntry(TimeEntry timeEntry) {
        this.timeEntry = timeEntry;
    }

    @Override
    public String toString(){
        return credential.getCredId()+"|"+ credential.getIpAddress()+"|"+
                "|"+ mvidEntry.getMacAddress()+mvidEntry.getIpAddress()+"|"
                +mvidEntry.getVlan()+"|"+ timeEntry.getDate();
    }

}
