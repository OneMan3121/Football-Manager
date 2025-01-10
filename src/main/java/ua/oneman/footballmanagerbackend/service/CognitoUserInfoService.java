package ua.oneman.footballmanagerbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CognitoUserInfoService {

    private final RestTemplate restTemplate;
    private final String userInfoEndpoint;

    public CognitoUserInfoService(RestTemplate restTemplate, @Value("${cognito.user-info-endpoint}") String userInfoEndpoint) {
        this.restTemplate = restTemplate;
        this.userInfoEndpoint = userInfoEndpoint;
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoEndpoint,
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        System.out.println(response.getBody());
        return response.getBody();
    }
}