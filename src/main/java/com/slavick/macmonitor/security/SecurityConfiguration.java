package com.slavick.macmonitor.security;

import com.slavick.macmonitor.model.user.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailService")
    UserDetailsService userDetailsService;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().regexMatchers("/static/.*").permitAll().
                regexMatchers("/(device)|(result)|(cross)|(timestamp)|(schedule)/?",
                        "/((device)|(result)|(cross)|(timestamp)|(schedule))" + "/" + "(list)",
                        "/account.*",
                        "/device/mac-.+",
                        "/device/arp-.+",
                        "/device/cross-.+",
                        "/result/poll",
                        "/.+?/download.*").
                hasAnyRole(RoleType.ADMIN.getRoleType(), RoleType.EDITOR.getRoleType(), RoleType.USER.getRoleType()).

                //regexMatchers("/(?<!user).*").hasAnyRole(RoleType.ADMIN.getRoleType(), RoleType.EDITOR.getRoleType()).
                regexMatchers("/.+?/add.*",
                        "/.+?/delete.*",
                        "/.+?/edit.*",
                        "/.+?/copy.*",
                        "/cross/upload.*").
                hasAnyRole(RoleType.ADMIN.getRoleType(),RoleType.EDITOR.getRoleType()).

                regexMatchers("/user.*").hasRole("ADMIN").

                anyRequest().authenticated().

                and().
                formLogin().loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password").
                permitAll()

                .and().
                logout().clearAuthentication(true).logoutSuccessUrl("/login?logout").permitAll()
                .and().csrf();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }


}
