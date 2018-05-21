package com.ecommerce.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.web.filter.CompositeFilter;

public class SSOFilter {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    private Filter sso() {

        CompositeFilter filter = new CompositeFilter();
        List filters = new ArrayList<>();
        
        // Google authenticate
        OAuth2ClientAuthenticationProcessingFilter googleFilter = 
            new OAuth2ClientAuthenticationProcessingFilter("/connect/google");
        OAuth2RestTemplate googlTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());

        googleFilter.setRestTemplate(googlTemplate);
        googleFilter.setTokenServices(tokenServices);

        filters.add(googleFilter);
        filter.setFilters(filters);

        return filter;
    }

    @Bean
    @ConfigurationProperties("google.client")
    private AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    private ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }
}