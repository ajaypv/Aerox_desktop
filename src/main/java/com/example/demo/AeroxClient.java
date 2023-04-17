/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


/**
 *
 * @author WINDOWS 10
 */
public class AeroxClient extends Application implements Runnable{

    private Timer heartbeatTimer;
    private static Connection connect;

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);

        root.setOnMousePressed((MouseEvent event) ->{
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) ->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

            stage.setOpacity(.8);
        });

        root.setOnMouseReleased((MouseEvent event) ->{
            stage.setOpacity(1);
        });

        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException {
        AeroxClient runnable = new AeroxClient();
        Thread thread = new Thread(runnable);
        thread.start();
        launch(args);


    }



    @Override
    public void run() {
        PrinterSupporter printerSupporter = new PrinterSupporter();

        System.out.println("Hello from MyRunnable!");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "done");
        headers.put("API-Key", "aerox100");
        headers.put("storeId","nhce1");
        headers.put("Username", "shopnhce");
        headers.put("StoreName", "NHCE AEROX");
        headers.put("Location", "Bangalore");
        headers.put("Distance", "5 km");
        headers.put("BwPrice", "2");
        headers.put("Color", "Yes");
        headers.put("ColorPrice", "10");
        headers.put("WaitingTime", "0 mins");
        headers.put("ServicesOffered", "Printing, Scanning, Copying");
        headers.put("Power", "On");
        headers.put("Status", "Open");
        WebsocketDataClient c = null;
        try {
            c = new WebsocketDataClient(new URI("wss://65s7ninik1.execute-api.ap-south-1.amazonaws.com/production"),headers);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }



        c.connect();

        if(c.isOpen()){
            heartbeatTimer = new Timer();
            WebsocketDataClient finalC1 = c;
            heartbeatTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finalC1.sendPing();
                }
            }, 0, 30000);

        }

        boolean databaseCheck;
        try {
            databaseCheck = database.Check();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(!databaseCheck){
            System.out.println("databases there");
        }else{
            System.out.println("databases error");
        }

        connect = database.connectDb();
        String updateSql = "UPDATE PRINTERS SET waitingTime = 0";
        PreparedStatement pstmt = null;
        try {
            pstmt = connect.prepareStatement(updateSql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try {
            PrinterData.UpdatePrinters();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataRecevingBlockingQueue.startConsumer();
        DownloadPdfBlockingQueueExample.startConsumer(c);
        PrintPDFBlockingQueue.startConsumer(c);


    }
}
