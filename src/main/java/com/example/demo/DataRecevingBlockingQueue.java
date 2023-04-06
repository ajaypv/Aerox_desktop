package com.example.demo;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DataRecevingBlockingQueue {

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
