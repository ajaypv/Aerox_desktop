package com.example.demo;

public class SendMessageUpdate {
    private WebsocketDataClient client;

    public SendMessageUpdate(WebsocketDataClient client) {
        this.client = client;
    }

    public void sendMessage(String message) {
        client.send(message);
    }
}
