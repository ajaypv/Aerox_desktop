package com.example.demo.User;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

public class User{
    public static void main(String[] args) {
        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.create();

        String userPoolId = "ap-south-1_ZOJnC7M9T";
        String clientId = "234567654324567543refdvfgbzx";

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(clientId)
                .username("+917899993939")
                .password("ajaypv@10ii")
                .userAttributes(
                        AttributeType.builder().name("phone_number").value("+917899993939").build(),
                        AttributeType.builder().name("email").value("ajaypv44@gmail.com").build()
                )
                .build();

        SignUpResponse signUpResponse = cognitoClient.signUp(signUpRequest);
        System.out.println(signUpResponse);
    }
}
