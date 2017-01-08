/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.controller;

import com.slavick.macmonitor.model.user.Role;
import com.slavick.macmonitor.model.user.User;
import com.slavick.macmonitor.service.user.RoleServiceimpl;
import com.slavick.macmonitor.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by sviatoslav on 23.12.2016.
 */

@Controller
@RequestMapping("/user")
@SessionAttributes("roles")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RoleServiceimpl roleService;


    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String redirect() {
        return "redirect:list";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String redirect1() {
        return "redirect:user/list";
    }

    @RequestMapping(value = {"/list",}, method = RequestMethod.GET)
    public String listSockets(ModelMap model) {
        Set<User> users =userService.getAllUnique();
        model.addAttribute("users",users);
        return "user/list";
    }

    @RequestMapping(value = { "/add" }, method = RequestMethod.GET)
    public String add(ModelMap model) {

        model.put("user", new User());
        return "user/add";
    }

    @RequestMapping(value = { "/add" }, method = RequestMethod.POST)
    public String addSubmit(@ModelAttribute User user,ModelMap model){

        //if  name or password are empty, or if password and retype password aren't equals
        if(!user.getPassword().equals(user.getRetypePassword())||
                user.getPassword().equals("")||user.getLogin().equals("")){
            return "redirect:add";
        }

        if(user==null){
            return "redirect:list";
        }

        if(user.getLogin().equals("admin")||user.getLogin().isEmpty()){
            return "redirect:list";
        }

        userService.persist(user);
        return "redirect:list";
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String delete(@PathVariable Long id) {
        User user = userService.getById(id);
        if(!user.getLogin().equals("admin"))
            userService.deleteById(id);
        return "redirect:list";
    }

    @RequestMapping(value = { "/edit-{id}" }, method = RequestMethod.GET)
    public String edit(@PathVariable Long id,ModelMap model) {
        User user=userService.getById(id);
        user.setPassword("");
        model.put("user",user);
        return "user/edit";
    }



    @RequestMapping(value = { "/edit-{id}" }, method = RequestMethod.POST)
    public String editPost(@ModelAttribute("user") User user) {
        User user1 = userService.getById(user.getId());
        if(user1!=null && !user1.getLogin().equals("admin"))
            userService.updateWithoutPassword(user);
        return "redirect:list";
    }

    @RequestMapping(value = { "/changepassword-{id}" }, method = RequestMethod.GET)
    public String changePassword(@PathVariable Long id,ModelMap model) {
        User user=userService.getById(id);
        user.setPassword("");
        user.setRetypePassword("");
        model.put("user",user);
        return "user/changepassword";
    }



    @RequestMapping(value = { "/changepassword-{id}" }, method = RequestMethod.POST)
    public String changePasswordSubmit(@ModelAttribute("user") User user) {

        if(user.getPassword().equals(user.getRetypePassword()))
            userService.updatePassword(user.getId(),user.getPassword());

        return "redirect:list";
    }

    @ModelAttribute("roles")
    public List<Role> initializeRoles() {
        return roleService.getAll();
    }
}
