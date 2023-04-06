package com.example.demo;

import java.net.URI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import com.google.gson.Gson;
import org.json.JSONObject;

public class WebsocketDataClient extends WebSocketClient {

  private Connection connection;

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
  try {
    connection = database.connectDb();
    String sql = "SELECT * FROM PRINTERS WHERE PrinterPower = 'on'";
    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet result = statement.executeQuery();
    while (result.next()) {
      int printerID = result.getInt("PrinterID");
      String printerName = result.getString("PrinterName");
      String printerStatus = result.getString("PrinterCurrentStatus");
      String printerColorSupport = result.getString("PrinterColorSupport");
      String printerPower = result.getString("Printerpower");
      int printerSpeedPerPage = result.getInt("PrinterSpeedPerPage");
      int printerPrintsCount = result.getInt("PrinterPrintsCount");
      int totalPages = result.getInt("Totalpages");
      int waitingTime = result.getInt("waitingTime");
      PrinterData printer = new PrinterData(printerID, printerName, printerStatus, printerColorSupport, printerPower, printerSpeedPerPage, printerPrintsCount, totalPages, waitingTime);
      Gson gson = new Gson();
      String json = gson.toJson(printer);
      JSONObject message = new JSONObject();
      message.put("StoreID", "nhce1");
      message.put("action", "onMessage");
      message.put("data", json);
      send(message.toString());
    }





  }catch(SQLException e){
    System.out.println(e);
  }

    System.out.println("opened connection");
  }

  @Override
  public void onMessage(String message) {
    System.out.println("received: " + message);
    try {
      Gson gson = new Gson();
      WebsocketData websocketData = gson.fromJson(message, WebsocketData.class);

      if (websocketData.route.equals("printPDf")) {
        System.out.println(websocketData.pdfUUID);
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
  }

  public void sendMessage(String message) {
    send(message);
  }


  @Override
  public void onError(Exception ex) {
    ex.printStackTrace();

  }



}