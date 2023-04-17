package com.example.demo;


import org.json.JSONObject;


import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobState;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.Sides;
import javax.print.event.PrintJobListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


public class PrintPDFBlockingQueue {
    private static Connection connect;


    private static Map<String, BlockingQueue<PdfDetails>> printerQueues = new HashMap<>();
    private static BlockingQueue<String> printQueue =new ArrayBlockingQueue<>(100);
    public static void addData(PdfDetails pdfDetails) {
        System.out.println("-------------> in addData print");
        BlockingQueue<PdfDetails> printerQueue = printerQueues.get(pdfDetails.PrinterSelection);
        System.out.println("-------------> in addData print"+pdfDetails.PrinterSelection);
        if (printerQueue == null) {
            printerQueue = new ArrayBlockingQueue<>(100);
            printerQueues.put(pdfDetails.PrinterSelection, printerQueue);
            printQueue.add(pdfDetails.PrinterSelection);
            System.out.println("-------------> in addData print"+pdfDetails.PrinterSelection);
        }
        try {
            printerQueue.put(pdfDetails);
            System.out.println("-------------> in addData print232"+pdfDetails.PrinterSelection);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("-------------> in addData print11"+pdfDetails.PrinterSelection);
        }
    }






    public static void PrintDoc(){


    }




    public static void startConsumer(WebsocketDataClient c) {
        Thread consumer = new Thread(() -> {
            while (true) {


                try {
                    String Printer = printQueue.take();
                    System.out.println(Printer);
                    System.out.println("-------------> in addData print 11");
                    if(Printer != null){
                        System.out.println("-------------> in addData print 22");
                        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
                        for (PrintService service : services) {
                            System.out.println("-------------> in addData print");
                            String printerName = service.getName();
                            BlockingQueue<PdfDetails> printPdfQueue = printerQueues.get(printerName);
                            System.out.println(printPdfQueue);
                            if (printPdfQueue != null) {
                                Thread consumer1 = new Thread(() -> {
                                    while (true) {
                                        try {
                                            PdfDetails pdfDetails = printPdfQueue.take();
                                            String pdfFilePath = "./pdfFiles/" + pdfDetails.pdfUUID + "/" + pdfDetails.pdfName;
                                            FileInputStream pdfFile = new FileInputStream(pdfFilePath);


                                            PrintService printService = null;
                                            service.getName().equalsIgnoreCase(pdfDetails.PrinterSelection);
                                            printService = service;


                                            Doc pdfDoc = new SimpleDoc(pdfFile, DocFlavor.INPUT_STREAM.AUTOSENSE, null);


                                            PrintJobListener listener = new MyPrintJobListener();




                                            DocPrintJob printJob = printService.createPrintJob();
                                            PrintJobAttributeSet attributes2 = printJob.getAttributes();


                                            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
                                            if (pdfDetails.allPagesSelected.equals("All-Pages")) {
                                                attributes.add(new PageRanges(1, Integer.parseInt(pdfDetails.numberPages)));
                                            } else {
                                                String pageRangeString = pdfDetails.customPageRange;
                                                for (String range : pageRangeString.split(",")) {
                                                    if (range.contains("-")) {
                                                        String[] startEnd = range.split("-");
                                                        int start = Integer.parseInt(startEnd[0]);
                                                        int end = Integer.parseInt(startEnd[1]);
                                                        attributes.add(new PageRanges(start, end));
                                                    } else {
                                                        int page = Integer.parseInt(range);
                                                        attributes.add(new PageRanges(page, page));
                                                    }
                                                }


                                            }




                                            if (pdfDetails.printSide.equals("Single-Side")) {
                                                attributes.add(Sides.ONE_SIDED);
                                            } else {
                                                attributes.add(Sides.DUPLEX);
                                            }


                                            int numCopies = Integer.parseInt(pdfDetails.numCopies);


                                            attributes.add(new Copies(numCopies));


                                            DashboardController reload = new DashboardController();
                                            printJob.addPrintJobListener(listener);


                                            printJob.print(pdfDoc, attributes);
                                            try {


                                                connect = database.connectDb();
                                                String sql = "UPDATE pdfprints SET status = ? WHERE pdfUUID = ?";
                                                PreparedStatement pstmt = connect.prepareStatement(sql);
                                                pstmt.setString(1, "Printing");
                                                pstmt.setString(2, pdfDetails.pdfUUID);
                                                pstmt.executeUpdate();
                                                reload.PrintDisplayList();


                                            } catch (Exception e) {
                                                System.out.println(e);
                                            }
                                            JobState jobState = (JobState) attributes2.get(JobState.class);
                                            System.out.println(jobState);


                                            long sleepTime;
                                            if (pdfDetails.allPagesSelected.equals("All-Pages")) {
                                                sleepTime = Integer.parseInt(pdfDetails.numberPages) * 2000;
                                            } else {
                                                sleepTime = Integer.parseInt(pdfDetails.customPagesCount) * 2000;
                                            }




                                            final long[] printerWaitingTime = {sleepTime};


                                            CountDownLatch latch = new CountDownLatch(1);


                                            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                                            System.out.println("printer start" + pdfDetails.PrinterSelection);
                                            System.out.println(pdfDetails.pdfName);
                                            System.out.println(pdfDetails.pdfUUID);
                                            executor.scheduleAtFixedRate(() -> {


                                                try {
                                                    String sqlForUpdatePrinterWaitingTime = "UPDATE PRINTERS SET waitingTime = waitingTime - ? WHERE PrinterName = ?";
                                                    PreparedStatement pstmt2 = connect.prepareStatement(sqlForUpdatePrinterWaitingTime);
                                                    pstmt2.setInt(1, 1000);
                                                    pstmt2.setString(2, pdfDetails.PrinterSelection);
                                                    pstmt2.executeUpdate();
                                                    System.out.println("count"+pdfDetails.PrinterSelection);


                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }


                                                if (printerWaitingTime[0] <= 1000) {
                                                    executor.shutdown();
                                                    latch.countDown();
                                                } else {
                                                    printerWaitingTime[0] -= 1000;
                                                }
                                            }, 0, 1000, TimeUnit.MILLISECONDS);


                                            try {
                                                latch.await();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            System.out.println("printer end" + pdfDetails.PrinterSelection);
                                            System.out.println(pdfDetails.pdfName);
                                            System.out.println(pdfDetails.pdfUUID);


                                            String sql = "UPDATE pdfprints SET status = ? WHERE pdfUUID = ?";
                                            PreparedStatement pstmt = connect.prepareStatement(sql);
                                            pstmt.setString(1, "Printed");
                                            pstmt.setString(2, pdfDetails.pdfUUID);
                                            pstmt.executeUpdate();
                                            reload.PrintDisplayList();
                                            JSONObject message = new JSONObject();
                                            message.put("StoreID", pdfDetails.StoreId);
                                            message.put("action", "WaitCountUpdate");
                                            message.put("transactionKey", pdfDetails.transactionKey);
                                            c.sendMessage(message.toString());
                                            System.out.println("after printg");


                                            String sqlWaittime = "SELECT waitingTime FROM PRINTERS WHERE PrinterName = ?";
                                            PreparedStatement pstmt1 = connect.prepareStatement(sqlWaittime);
                                            String printerName1 = pdfDetails.PrinterSelection;
                                            pstmt1.setString(1, printerName1);
                                            ResultSet rs = pstmt1.executeQuery();


                                            JSONObject message2 = new JSONObject();
                                            message2.put("action", "shopTimeUpdater");
                                            if (rs.next()) {
                                                int waitingTime = rs.getInt("waitingTime");
                                                System.out.println("Waiting time for " + printerName1 + ": " + waitingTime / 1000 + " seconds");


                                                message2.put("WaitingTime", String.valueOf(waitingTime));
                                            } else {
                                                System.out.println("Printer not found: ");


                                            }


                                            c.sendMessage(message2.toString());
                                            pdfFile.close();








                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (PrintException e) {
                                            throw new RuntimeException(e);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                                consumer1.start();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }




            }
        });
        consumer.start();
    }








}






