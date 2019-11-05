package com.swipr.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Some boilerplate code to help out with web security in OAuth
    private static final String[] permittedEndpoints = {
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",        
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(permittedEndpoints).permitAll()
            .antMatchers("/**").authenticated().and().oauth2Login().defaultSuccessUrl("/", true).failureUrl("/fail");
        
    }
}