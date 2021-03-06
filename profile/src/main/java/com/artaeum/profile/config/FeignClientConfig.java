package com.artaeum.profile.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FeignClientConfig {

    private Environment env;

    public FeignClientConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("storage", this.env.getProperty("STORAGE_SERVICE_PASSWORD"));
    }
}
