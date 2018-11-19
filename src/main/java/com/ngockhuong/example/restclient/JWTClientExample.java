package com.ngockhuong.example.restclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
public class JWTClientExample {
    static final String URL_LOGIN = "http://localhost:8080/login";
    static final String URL_STUDENTS = "http://localhost:8080/students";

    public static void main(String[] args) {
        String username = "khuong";
        String password = "123456";

        String authorizationString = postLogin(username, password);
        log.info("Authorization String=" + authorizationString);

        // call rest api
        callRestApi(URL_STUDENTS, authorizationString);
    }

    private static void callRestApi(String restUrl, String authorizationString) {
        // HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        // Authorization String
        headers.set("Authorization", authorizationString);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity<String>
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method and headers
        ResponseEntity<String> response = restTemplate.exchange(restUrl, HttpMethod.GET, entity, String.class);

        String result = response.getBody();
        log.info(result);
    }

    private static String postLogin(String username, String password) {
        // Request Header
        HttpHeaders headers = new HttpHeaders();

        // Request Body
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<>();
        parametersMap.add("username", username);
        parametersMap.add("password", password);

        // Request Entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parametersMap, headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Post login
        ResponseEntity<String> response = restTemplate.exchange(URL_LOGIN, HttpMethod.POST, requestEntity, String.class);
        HttpHeaders responseHeaders = response.getHeaders();
        List<String> list = responseHeaders.get("Authorization");

        return list == null || list.isEmpty() ? null : list.get(0);
    }
}
