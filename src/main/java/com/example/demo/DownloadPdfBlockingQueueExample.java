package com.example.demo;


import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DownloadPdfBlockingQueueExample {
    private static Connection connect;
    public WebsocketDataClient c;

    public DownloadPdfBlockingQueueExample(WebsocketDataClient c) {
        this.c = c;
    }
    private static BlockingQueue<PdfDetails> downloadPdfQueue = new ArrayBlockingQueue<>(100);

    public static void addData(PdfDetails pdfDetails) {
        try {
            downloadPdfQueue.put(pdfDetails);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startConsumer(WebsocketDataClient c) {
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    PdfDetails item = downloadPdfQueue.take();
                    String pdfDownloadLink = item.pdfLink;
                    String pdfName = item.pdfName;
                    String pdfDirectory = "pdfFiles/" + item.pdfUUID + "/";
                    synchronized (DownloadPdfBlockingQueueExample.class) {
                        downloadFile(pdfDownloadLink, pdfName, pdfDirectory,item,c);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        consumer.start();
    }

    private static void downloadFile(String downloadLink, String fileName, String directory, PdfDetails pdfDetails,WebsocketDataClient c) {
        DashboardController reload = new DashboardController();
        try {
            try {

                connect = database.connectDb();
                String sql = "UPDATE pdfprints SET status = ? WHERE pdfprintid = ?";
                PreparedStatement pstmt = connect.prepareStatement(sql);
                pstmt.setString(1, "downloading");
                Gson gson = new Gson();
                JSONObject message = new JSONObject();
                message.put("StoreID", "nhce1");
                message.put("action", "waititingUpadter");
                message.put("transactionKey",pdfDetails.transactionKey);
                message.put("userUid",pdfDetails.userUid);
                message.put("WaitingTime","0");
                message.put("printStatus","downloading");
                message.put("FileId",pdfDetails.pdfId);
                c.sendMessage(message.toString());

                pstmt.setString(2, pdfDetails.pdfUUID);
                pstmt.executeUpdate();

                reload.PrintDisplayList();

            }catch (Exception e){
                System.out.println(e);
            }




            URL url = new URL(downloadLink);
            InputStream inputStream = url.openStream();
            FileOutputStream outputStream = new FileOutputStream(directory + fileName);

            byte[] buffer = new byte[2048];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            System.out.println("File downloaded successfully!" + fileName);
            System.out.println(pdfDetails.PrinterSelection);
            String sql = "UPDATE pdfprints SET status = ? WHERE pdfUUID = ?";
            PreparedStatement pstmt = connect.prepareStatement(sql);
            pstmt.setString(1, "downloaded");
            pstmt.setString(2, pdfDetails.pdfUUID);
            pstmt.executeUpdate();



            long WaitingTime = Integer.parseInt(pdfDetails.numberPages) * 2000;
            System.out.println(WaitingTime+"Time to wait ");

            String printerName = pdfDetails.PrinterSelection;

            String sqlForUpdatePrinterWaitingTime = "UPDATE PRINTERS SET waitingTime = waitingTime + ? WHERE PrinterName = ?";
            PreparedStatement pstmt2 = connect.prepareStatement(sqlForUpdatePrinterWaitingTime);

            pstmt2.setInt(1, (int) WaitingTime);
            pstmt2.setString(2, pdfDetails.PrinterSelection);
            pstmt2.executeUpdate();

            String sqlWaittime = "SELECT waitingTime FROM PRINTERS WHERE PrinterName = ?";
            PreparedStatement pstmt1 = connect.prepareStatement(sqlWaittime);
            pstmt1.setString(1, printerName);
            ResultSet rs = pstmt1.executeQuery();
            JSONObject message = new JSONObject();
            message.put("StoreID", "nhce1");
            message.put("action", "waititingUpadter");
            message.put("transactionKey",pdfDetails.transactionKey);
            message.put("userUid",pdfDetails.userUid);
            message.put("printStatus","downloaded");
            message.put("FileId",pdfDetails.pdfId);
            JSONObject message2 = new JSONObject();
            message2.put("action", "shopTimeUpdater");
            if (rs.next()) {
                int waitingTime = rs.getInt("waitingTime");
                System.out.println("Waiting time for " + printerName + ": " + waitingTime/1000 + " seconds");

                message.put("WaitingTime",String.valueOf(waitingTime));
                message2.put("WaitingTime",String.valueOf(waitingTime));
            } else {
                System.out.println("Printer not found: " );

            }




            c.sendMessage(message2.toString());
            c.sendMessage(message.toString());
            connect.close();
            reload.PrintDisplayList();
            synchronized (DownloadPdfBlockingQueueExample.class) {
                PrintPDFBlockingQueue.addData(pdfDetails);
            }
        } catch (IOException e) {
            System.out.println("Failed to download file: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
