package com.example.demo;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DataRecevingBlockingQueue {

    private static Connection connect;

    private static BlockingQueue<PdfDetails> queue = new ArrayBlockingQueue<>(100);

    public static void addData(PdfDetails pdfDetails) {
        try {
            queue.put(pdfDetails);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startConsumer() {
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    PdfDetails item = queue.take();
                    String folderName = "./pdfFiles/"+item.pdfUUID;
                    File folder = new File(folderName);

                    if (!folder.exists()) {
                        boolean created = folder.mkdirs();
                        if (created) {
                            System.out.println("Folder created successfully!");
                        } else {
                            System.out.println("Failed to create folder!");
                        }
                    } else {
                        System.out.println("Folder already exists!");
                    }
                   connect = database.connectDb();
                    if(item.selectedColor.equals("Black & White")){
                        if(item.printSide.equals("Single-Side")){
                            String sqlWaittime = "SELECT PrinterName FROM PRINTERS WHERE PrinterOptionSelect = ? AND Printerpower = ?";
                            PreparedStatement pstmt1 = connect.prepareStatement(sqlWaittime);
                            pstmt1.setString(1, "Single-Side");
                            pstmt1.setString(2, "Online");
                            ResultSet rs = pstmt1.executeQuery();
                            if (rs.next()) {
                                item.PrinterSelection  = rs.getString("PrinterName");
                            } else {
                                System.out.println("Printer not found: " );
                            }

                        } else if (item.printSide.equals("Double-Sided")) {
                            String sqlWaittime = "SELECT PrinterName FROM PRINTERS WHERE PrinterOptionSelect = ? AND Printerpower = ?";
                            PreparedStatement pstmt1 = connect.prepareStatement(sqlWaittime);
                            pstmt1.setString(1, "Double-Side");
                            pstmt1.setString(2, "Online");
                            ResultSet rs = pstmt1.executeQuery();
                            if (rs.next()) {
                                item.PrinterSelection  = rs.getString("PrinterName");
                            } else {
                                System.out.println("Printer not found: " );
                            }

                        }

                    } else if (item.selectedColor.equals("Color")) {
                        String sqlWaittime = "SELECT PrinterName FROM PRINTERS WHERE PrinterOptionSelect = ? AND Printerpower = ?";
                        PreparedStatement pstmt1 = connect.prepareStatement(sqlWaittime);
                        pstmt1.setString(1, "colorPinter");
                        pstmt1.setString(2, "Online");
                        ResultSet rs = pstmt1.executeQuery();
                        if (rs.next()) {
                            item.PrinterSelection  = rs.getString("PrinterName");
                        } else {
                            System.out.println("Printer not found: " );
                        }

                    }

                    synchronized (DataRecevingBlockingQueue.class) {
                        InsertData.PdfDataInsert(item);

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        consumer.start();
    }
}
