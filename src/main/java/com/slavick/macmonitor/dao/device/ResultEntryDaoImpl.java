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
import com.slavick.macmonitor.dao.device.sortenum.ResultEntrySortOrderEnum;
import com.slavick.macmonitor.model.devices.orm.network.ResultEntry;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by apanasyonok on 02.12.2016.
 */
@Repository
public class ResultEntryDaoImpl extends AbstractDao<Long,ResultEntry> {

    public Long getIdByValueIgnoreTime(ResultEntry resultEntry){
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("credential", resultEntry.getCredential()));
        criteria.add(Restrictions.eq("mvidEntry", resultEntry.getMvidEntry()));
        criteria.add(Restrictions.eq("crossEntry", resultEntry.getCrossEntry()));
        List<ResultEntry> list = criteria.list();

        if(list.size()==0){
            return null;
        }

        Map<Date,ResultEntry> map = new HashMap<>();

        for (ResultEntry entry: list
             ) {
           map.put(entry.getTimeEntry().getDate(),entry);
        }

        Date maxDate = Collections.max(map.keySet());

        return map.get(maxDate).getId();
    }

    @Override
    public List<ResultEntry> getAll(){


        return getAll(ResultEntrySortOrderEnum.MAC);
    }




    public List<ResultEntry> getAll(ResultEntrySortOrderEnum sortOrder){
        Criteria criteria = createEntityCriteria();
        if(sortOrder==null) return criteria.list();
        switch (sortOrder){
            case IP:
                criteria.createAlias("mvidEntry","a1");
                criteria.addOrder( Order.asc("a1.ipAddress") );break;
            case VLAN:
                criteria.createAlias("mvidEntry","a1");
                criteria.addOrder(Order.asc("a1.vlan") );
                criteria.addOrder( Order.asc("a1.ipAddress") );break;
            case MAC:
                criteria.createAlias("mvidEntry","a1");
                criteria.addOrder( Order.asc("a1.macAddress") );
                criteria.createAlias("timeEntry","a4");
                criteria.addOrder( Order.desc("a4.date") );break;
            case DEVICE_IP:
                criteria.createAlias("mvidEntry","a1");
                criteria.createAlias("credential","a2");
                criteria.createAlias("crossEntry","a3");
                criteria.addOrder(Order.asc("a2.ipAddress"));
                criteria.addOrder(Order.asc("a3.iface"));
                criteria.addOrder( Order.asc("a1.macAddress") );
                criteria.addOrder( Order.asc("a1.ipAddress") );
                break;
            case DATE:
                criteria.createAlias("timeEntry","a4");
                criteria.addOrder( Order.asc("a4.date") );
                criteria.createAlias("mvidEntry","a1");
                criteria.addOrder( Order.asc("a1.macAddress") );break;

        }
        return criteria.list();
    }
}
