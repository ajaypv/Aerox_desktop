package com.example.demo;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebsocketDataClient extends WebSocketClient {

  private Connection connection;
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  public WebsocketDataClient(URI serverUri, Draft draft) {
    super(serverUri, draft);
  }

  public WebsocketDataClient(URI serverURI) {
    super(serverURI);
  }

  public WebsocketDataClient(URI serverUri, Map<String, String> httpHeaders) {
    super(serverUri, httpHeaders);
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    PrinterSupporter.updateOnlinePrinterData(this);
    System.out.println("opened connection");
  }

  @Override
  public void onMessage(String message) {
    System.out.println("received: " + message);
    try {

      Gson gson = new Gson();
      WebsocketData websocketData = gson.fromJson(message, WebsocketData.class);

      if (websocketData.route.equals("printPDf")) {
        DataRecevingBlockingQueue.addData(websocketData.data);
      } else if (websocketData.route.equals("otherRoute")) {
        System.out.println("Received data for other route:");
      } else {
        System.out.println("Unknown route: " + websocketData.route);
      }
    }catch (Exception e){
      System.out.println(e);
    }
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println(
        "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
            + reason);

          scheduler.schedule(() -> {
            if (!isClosed()) {
              return;
            }
            System.out.println("Attempting to reconnect...");
            reconnect();
          }, 5, TimeUnit.SECONDS);
  }

  public void sendMessage(String message) {
    send(message);
  }


  @Override
  public void onError(Exception ex) {
    ex.printStackTrace();

  }



}