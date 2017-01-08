/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.dao.device;

import com.slavick.macmonitor.dao.AbstractDao;
import com.slavick.macmonitor.model.devices.orm.network.MVIDEntry;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by apanasyonok on 05.12.2016.
 */
@Repository
public class MvidDaoImpl extends AbstractDao<Long,MVIDEntry> {

    public Long getIdByValue(MVIDEntry mvidEntry){
        Criteria crit = createEntityCriteria();

        if(mvidEntry.getIpAddress()==null){
            crit.add(Restrictions.isNull("ipAddress"));
        }
        else {
            crit.add(Restrictions.eq("ipAddress",mvidEntry.getIpAddress()));
        }

        crit.add(Restrictions.eq("macAddress",mvidEntry.getMacAddress()));
        crit.add(Restrictions.eq("vlan",mvidEntry.getVlan()));

        MVIDEntry e = (MVIDEntry) crit.uniqueResult();
        if(e!=null){
            return e.getMvidId();
        }
        return null;

    }
}
