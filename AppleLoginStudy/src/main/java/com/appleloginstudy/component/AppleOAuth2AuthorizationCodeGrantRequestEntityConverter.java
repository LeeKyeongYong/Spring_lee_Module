package com.appleloginstudy.component;

import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Component
public class AppleOAuth2AuthorizationCodeGrantRequestEntityConverter extends OAuth2AuthorizationCodeGrantRequestEntityConverter {

    private static final String APPLE_REGISTRATION_ID = "apple";
    private static final String CLIENT_SECRET_KEY = "client_secret";

    private final AppleSecretGenerator appleSecretGenerator;

    public AppleOAuth2AuthorizationCodeGrantRequestEntityConverter(
            AppleSecretGenerator appleSecretGenerator
    ) {
        this.appleSecretGenerator = appleSecretGenerator;
    }

    @Override
    protected MultiValueMap<String, String> createParameters(
            OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest
    ) {
        var clientRegistrationId = authorizationCodeGrantRequest.getClientRegistration().getRegistrationId();
        if (APPLE_REGISTRATION_ID.equalsIgnoreCase(clientRegistrationId)) { // 1
            var encryptedPrivateKey = appleSecretGenerator.createClientSecret();
            var parameter = super.createParameters(authorizationCodeGrantRequest);
            parameter.put(CLIENT_SECRET_KEY, List.of(encryptedPrivateKey));
            return parameter;
        }
        return super.createParameters(authorizationCodeGrantRequest); // 2
    }
}