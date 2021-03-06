package com.artaeum.profile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                    .headers()
                    .frameOptions()
                    .disable()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers("/actuator/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/subscriptions/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/posts/**").permitAll()
                    .antMatchers(HttpMethod.PUT).authenticated()
                    .antMatchers(HttpMethod.POST).authenticated()
                    .antMatchers(HttpMethod.DELETE).authenticated()
                    .antMatchers("/**").authenticated();
    }
}
