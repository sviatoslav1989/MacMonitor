/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.service.user;

import com.slavick.macmonitor.dao.user.RoleDaoImpl;
import com.slavick.macmonitor.dao.user.UserDaoImp;
import com.slavick.macmonitor.model.user.Role;
import com.slavick.macmonitor.model.user.RoleType;
import com.slavick.macmonitor.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sviatoslav on 23.12.2016.
 */

//this works every time after the program starts
@Component
public class DefaultUserAndRoleCreation {
    @Autowired
    UserDaoImp userDao;

    @Autowired
    RoleDaoImpl roleDao;

    @Autowired
    PasswordEncoder passwordEncoder;



    //create roles(ADMIN,USER,EDITOR) and default user admin with password admin and role ADMIN if not exist
    @Scheduled(fixedRate = Long.MAX_VALUE)
    @Transactional
    public void createRolesAndUser(){
        User user = userDao.getUserByLogin("admin");

        if(user==null){
            user = new User();
            user.setLogin("admin");
            user.setPassword(passwordEncoder.encode("admin"));
        }


        for (RoleType roleType:RoleType.values()) {
            if(roleDao.getByType(roleType)==null){
                roleDao.persist(new Role(roleType));
            }
        }


        Role roleAdmin = roleDao.getByType(RoleType.ADMIN);
        user.getUserRoles().add(roleAdmin);
        user.setDescription("Embedded administrator");

        if(user.getId()==null){
            userDao.persist(user);
        }else {
            userDao.update(user);
        }

    }


}
