package com.socialplatformapi.client;

import com.socialplatformapi.dto.external.ReqresResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "reqres-client",
        url = "${reqres.api.base-url:https://reqres.in/api}"
)
public interface ReqresClient {
    @GetMapping("/users")
    ReqresResponse getUsers(
            @RequestHeader("x-api-key") String apiKey,
            @RequestParam(value = "page", required = false) Integer page
    );
}
