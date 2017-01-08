/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.poll.orm;


import com.slavick.macmonitor.model.devices.orm.network.ResultEntry;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by apanasyonok on 18.11.2016.
 */
@Entity(name = "time_table")
@Access( AccessType.FIELD )
public class TimeEntry {

    @Id
    @GeneratedValue
    @Column(name = "time_id")
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private static SimpleDateFormat formatter= new SimpleDateFormat("dd.MM.yy HH:mm");
    private static SimpleDateFormat formatterWithSeconds= new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_stamp",nullable = false,unique = true)
    private Date date;

    @OneToMany(mappedBy = "timeEntry",orphanRemoval = true,cascade=CascadeType.REMOVE)
    private List<ResultEntry> resultEntries = new ArrayList<>();

    public List<ResultEntry> getResultEntries() {
        return resultEntries;
    }

    public void setResultEntries(List<ResultEntry> resultEntries) {
        this.resultEntries = resultEntries;
    }

    public TimeEntry(Date date) {
        this.date = date;
    }

    public TimeEntry() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeEntry)) return false;

        TimeEntry timeEntry = (TimeEntry) o;

        return date.equals(timeEntry.date);

    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    public String getDateWithSeconds(){
        return  formatterWithSeconds.format(date);
    }

    @Override
    public String toString() {
        return formatter.format(date);

    }
}
