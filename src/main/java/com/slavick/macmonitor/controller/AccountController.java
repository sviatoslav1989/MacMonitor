/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.controller;

import com.slavick.macmonitor.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sviatoslav on 25.12.2016.
 */

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value =  {"/"}, method = RequestMethod.GET)
    public String redirect(){
        return "redirect:main";
    }

    @RequestMapping(value =  "", method = RequestMethod.GET)
    public String redirect1(){
        return "redirect:account/main";
    }

    @RequestMapping(value = "/main",method = RequestMethod.GET)
    public  String main(){
        return "account/main";
    }

    @RequestMapping(value = "/changepassword",method = RequestMethod.GET)
    public  String changePassword(ModelMap model){
        model.put("password","");
        model.put("retypePassword","");
        return "account/changepassword";
    }

    @RequestMapping(value = "/changepassword",method = RequestMethod.POST)
    public  String changePasswordSubmit(@ModelAttribute("password") String password,
                                        @ModelAttribute("retypePassword") String retypePassword,
                                        @AuthenticationPrincipal User activeUser){

        String login = activeUser.getUsername();

        if(!password.isEmpty() && password.equals(retypePassword)) {

            com.slavick.macmonitor.model.user.User userToChange = userService.getUserByLogin(login);

            if(userToChange!= null){
                userService.updatePassword(userToChange.getId(),password);
            }
        }
        return "redirect:main";
    }
}
