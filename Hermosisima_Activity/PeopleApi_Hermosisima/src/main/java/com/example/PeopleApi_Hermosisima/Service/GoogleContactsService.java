package com.example.PeopleApi_Hermosisima.Service;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Service
public class GoogleContactsService {
    
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplate restTemplate = new RestTemplate();

    public GoogleContactsService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    // 🔹 Fetch Contacts from Google People API
    public Map<String, Object> getContacts(OAuth2User principal) {
        String accessToken = getAccessToken(principal);

        String url = UriComponentsBuilder.fromHttpUrl("https://people.googleapis.com/v1/people/me/connections")
                .queryParam("personFields", "names,emailAddresses")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    // 🔹 Get OAuth2 Access Token
    private String getAccessToken(OAuth2User principal) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService
                .loadAuthorizedClient("google", principal.getName());
        return authorizedClient.getAccessToken().getTokenValue();
    }

    // 🔹 Create a Contact
    public void createContact(OAuth2User principal, Map<String, Object> newContact) {
        String accessToken = getAccessToken(principal);
        String url = "https://people.googleapis.com/v1/people:createContact";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(newContact, headers);

        restTemplate.postForEntity(url, entity, Map.class);
    }

    // 🔹 Delete a Contact
    public void deleteContact(OAuth2User principal, String resourceName) {
        String accessToken = getAccessToken(principal);
        String url = "https://people.googleapis.com/v1/" + resourceName + ":deleteContact";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public void updateContact(OAuth2User principal, String resourceName, Map<String, Object> updatedContact) {
        String accessToken = getAccessToken(principal);
        String url = "https://people.googleapis.com/v1/" + resourceName + ":updateContact";
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updatedContact, headers);
    
        restTemplate.patchForObject(url, entity, Map.class);
    }
}