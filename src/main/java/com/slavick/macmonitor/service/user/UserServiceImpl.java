/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.service.user;

import com.slavick.macmonitor.dao.user.UserDaoImp;
import com.slavick.macmonitor.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by sviatoslav on 23.12.2016.
 */

@Transactional
@Service
public class UserServiceImpl {

    @Autowired
    UserDaoImp dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return dao.getAll();
    }

    public void persist(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.persist(user);
    }

    public void deleteById(Long id){
        User user = getById(id);

        if(user!=null) {
            dao.deleteById(id);
        }
    }

    public User getUserByLogin(String login){
        return dao.getUserByLogin(login);
    }

    public Set<User> getAllUnique(){
        return dao.getAllUnique();
    }

    public User getById(Long id) {
        return dao.getById(id);
    }

   /* public void update(User user) {
        dao.update(user);
    }*/

    public void updateWithoutPassword(User user) {
        if(user.getLogin().isEmpty())
            return;
        User userToUpdate = dao.getById(user.getId());
        userToUpdate.setLogin(user.getLogin());
        userToUpdate.setDescription(user.getDescription());
        userToUpdate.setUserRoles(user.getUserRoles());
        dao.update(userToUpdate);
    }

    public void updatePassword(Long id, String password) {
        if(password==null||password.isEmpty())return;

        User user = dao.getById(id);
        user.setPassword(passwordEncoder.encode(password));
        dao.update(user);
    }
}
