package com.spring.task3_1_4;

import com.spring.task3_1_4.model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@SpringBootApplication
public class Application {
    private static final String API_URL = "http://94.198.50.185:7081/api/users";
    private static String sessionCookie = "";
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
        HttpHeaders headers = response.getHeaders();

        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        sessionCookie = cookies != null ? cookies.get(0) : null;

        User user = new User(3L, "James", "Brown", (byte) 21);
        HttpEntity<User> createUserRequest = new HttpEntity<>(user, activeSessionCookie());
        String createUserResponse = restTemplate.exchange(
                API_URL, HttpMethod.POST, createUserRequest, String.class).getBody();

        user.setName("Thomas");
        user.setLastName("Shelby");
        HttpEntity<User> updateUserRequest = new HttpEntity<>(user, activeSessionCookie());
        String updateUserResponse = restTemplate.exchange(
                API_URL, HttpMethod.PUT, updateUserRequest, String.class).getBody();

        HttpEntity<User> deleteUserRequest = new HttpEntity<>(activeSessionCookie());
        String deleteUserResponse = restTemplate.exchange(
                API_URL + "/3", HttpMethod.DELETE, deleteUserRequest, String.class).getBody();

        String mysteriousCode = createUserResponse + updateUserResponse + deleteUserResponse;
        System.out.println(mysteriousCode);
    }

    private static HttpHeaders activeSessionCookie() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionCookie);
        return headers;
    }
}






