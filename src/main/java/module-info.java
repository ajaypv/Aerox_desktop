module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires fontawesomefx;
    requires java.desktop;
    requires aws.java.sdk.cognitoidentity;
    requires aws.java.sdk.core;
    requires software.amazon.awssdk.services.ivs;
    requires software.amazon.awssdk.services.cognitoidentityprovider;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.auth;

    requires Java.WebSocket;
    requires com.google.gson;
    requires json;
    requires java.prefs;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.Printers;
    opens com.example.demo.Printers to javafx.fxml;
}