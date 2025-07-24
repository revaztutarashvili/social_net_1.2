package com.socialplatformapi.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class ReqresClientConfig {

    //  გადმოვიტანე API გასაღები სერვისიდან აქ.
    @Value("${reqres.api.key:reqres-free-v1}")
    private String reqresApiKey;

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.NONE;
    }

    // 2. შევქმენი RequestInterceptor-ის ბინი
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // ყველა მოთხოვნა, რომელიც რექრესზე გაიგზავნება დავამატებ გლობალურად ჰედერს აქ.
                template.header("x-api-key", reqresApiKey);
            }
        };
    }
}