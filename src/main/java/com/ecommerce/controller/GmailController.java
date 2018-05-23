package com.ecommerce.controller;

import java.io.IOException;
import java.util.Collections;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.GmailScopes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class GmailController {

    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;
    GoogleClientSecrets googleClientSecrets;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Value("${security.oauth2.client.redirectUri}")
    private String redirectUri;
    
    @RequestMapping(value = "/login/gmail", method = RequestMethod.GET)
    public RedirectView authentication() throws Exception {
        return new RedirectView(authorize());
    }

    @RequestMapping(value = "/login/gmailCallback", method = RequestMethod.GET, params = "code")
    public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code) throws IOException {

        TokenResponse tokenResponse = googleAuthorizationCodeFlow
            .newTokenRequest(code)
            .setRedirectUri("http://localhost:3000/login/gmailCallback")
            .execute();
        
        Credential credential = googleAuthorizationCodeFlow
            .createAndStoreCredential(tokenResponse, "userId");

        new com.google.api.services.gmail.Gmail.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName("applicationName").build();

        return new ResponseEntity<>("It1s working", HttpStatus.OK);
    }

    private String authorize() throws Exception {

        AuthorizationCodeRequestUrl authorizationUrl;
        
        Details details = new Details();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);

        googleClientSecrets = new GoogleClientSecrets().setWeb(details);

        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow
            .Builder(httpTransport, JSON_FACTORY, googleClientSecrets, 
                Collections.singleton(GmailScopes.GMAIL_READONLY))
            .build();

        authorizationUrl = googleAuthorizationCodeFlow.newAuthorizationUrl()
            .setRedirectUri(redirectUri);

        return authorizationUrl.build();
    }
}