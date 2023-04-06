package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.standard.*;
import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;
import java.sql.*;


public class PrinterData {
    public int PrinterID;
    public String PrinterName;
    public String PrinterStatus;
    public String PrinterPower;

    public String getPrinterColorSupport() {
        return PrinterColorSupport;
    }

    public void setPrinterColorSupport(String printerColorSupport) {
        PrinterColorSupport = printerColorSupport;
    }

    public int getPrinterSpeedPerPage() {
        return PrinterSpeedPerPage;
    }

    public void setPrinterSpeedPerPage(int printerSpeedPerPage) {
        PrinterSpeedPerPage = printerSpeedPerPage;
    }

    public String PrinterColorSupport;
    public int PrinterSpeedPerPage;
    public int PrinterPrintsCount;
    public int TotalPages;
    public int waitingTime;

    public int getPrinterID() {
        return PrinterID;
    }

    public void setPrinterID(int printerID) {
        PrinterID = printerID;
    }

    public String getPrinterName() {
        return PrinterName;
    }

    public void setPrinterName(String printerName) {
        PrinterName = printerName;
    }

    public String getPrinterStatus() {
        return PrinterStatus;
    }

    public void setPrinterStatus(String printerStatus) {
        PrinterStatus = printerStatus;
    }

    public String getPrinterPower() {
        return PrinterPower;
    }

    public void setPrinterPower(String printerPower) {
        PrinterPower = printerPower;
    }

    public int getPrinterPrintsCount() {
        return PrinterPrintsCount;
    }

    public void setPrinterPrintsCount(int printerPrintsCount) {
        PrinterPrintsCount = printerPrintsCount;
    }

    public int getTotalPages() {
        return TotalPages;
    }

    public void setTotalPages(int totalPages) {
        TotalPages = totalPages;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    private static Connection connect;
    private static PreparedStatement preparedStatement;
    private static PreparedStatement prepare;
    private static ResultSet result;


    public PrinterData(int printerID, String printerName, String printerStatus,String PrinterColorSupport, String printerPower, int  PrinterSpeedPerPage, int printerPrintsCount, int totalPages, int waitingTime) {
        PrinterID = printerID;
        PrinterName = printerName;
        PrinterStatus = printerStatus;
        PrinterPower = printerPower;
        PrinterPrintsCount = printerPrintsCount;
        TotalPages = totalPages;
        this.waitingTime = waitingTime;
        this.PrinterColorSupport = PrinterColorSupport;
        this.PrinterSpeedPerPage = PrinterSpeedPerPage;
    }

    public PrinterData() {

    }



    public static void UpdatePrinterPower(boolean power,String PrinterName) throws SQLException {
        connect = database.connectDb();
        Statement stmt = connect.createStatement();
        if(power){
            String query = "UPDATE printers SET  Printerpower  = 'on' WHERE PrinterName = '" +PrinterName+ "'";
            stmt.executeUpdate(query);
        }else{
            String query = "UPDATE printers SET  Printerpower = 'off' WHERE PrinterName = '" +PrinterName+ "'";
            stmt.executeUpdate(query);
        }
        System.out.println("in printer data class");


        connect.close();


    }



    public static void UpdatePrinters() throws SQLException {

        connect = database.connectDb();
        Statement stmt = connect.createStatement();

        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            for (PrintService printService : printServices) {
                String printerStatus;
                boolean isPrinterOnline = printService.isAttributeCategorySupported(PrinterIsAcceptingJobs.class);
                System.out.println(isPrinterOnline);

                if (printService.isAttributeCategorySupported(PrinterState.class)) {
                    Attribute printerState = printService.getAttribute(PrinterState.class);
                    if (printerState != null) {
                        System.out.println("Printer Status: " + printerState);
                        printerStatus = String.valueOf(printerState);
                    } else {
                        System.out.println("Printer Status: Unknown");
                        printerStatus = "Printer Status: Unknown";
                    }
                } else {
                    printerStatus = "Printer Status: Not Supported";
                }
                AttributeSet attrs = printService.getAttributes();

                String printerState = String.valueOf(attrs.get(PrinterState.class));
                String isAcceptingJobs = String.valueOf(attrs.get(PrinterIsAcceptingJobs.class));
                String colorSupported = String.valueOf(attrs.get(ColorSupported.class));
                String queuedJobCount = String.valueOf(attrs.get(QueuedJobCount.class));
                String printerStateReason = String.valueOf(attrs.get(PrinterStateReason.class));
                String printerMoreInfoManufacturer = String.valueOf(attrs.get(PrinterMoreInfoManufacturer.class));
                String printerStateReasons = String.valueOf(attrs.get(PrinterStateReasons.class));

                System.out.println("Printer state: " + printerState);
                System.out.println("Is accepting jobs: " + isAcceptingJobs);
                System.out.println("Color supported: " + colorSupported);
                System.out.println("Queued job count: " + queuedJobCount);
                System.out.println("Printer state reason: " + printerStateReason);
                System.out.println("More info manufacturer: " + printerMoreInfoManufacturer);
                System.out.println("Printer state reasons: " + printerStateReasons);


//                "PrinterID INT PRIMARY KEY AUTO_INCREMENT," +
//                        "PrinterName VARCHAR," +
//                        "PrinterCurrentStatus VARCHAR," +
//                        "PrinterColorSupport VARCHAR," +
//                        "Printerpower VARCHAR," +
//                        "PrinterSpeedPerPage INTEGER," +
//                        "PrinterPrintsCount  INTEGER," +
//                        "Totalpages INTEGER," +
//                        "waitingTime INTEGER);";

                // Check if the printer already exists in the table
                ResultSet result = stmt.executeQuery("SELECT * FROM printers WHERE PrinterName = '" + printService.getName() + "'");
                if (!result.next()) {
                    String sql = "INSERT INTO Printers(PrinterName, PrinterCurrentStatus,PrinterColorSupport,Printerpower,PrinterSpeedPerPage,PrinterPrintsCount,Totalpages,waitingTime) " + "VALUES (?, ?,?, ?,?, ?,?, ?);";
                    int printsCount = 0; // you'll need to increment this value in your code
                    int totalPages = 0; // you'll need to calculate this value in your code
                    int waitingTime = 0; // you'll need to calculate this value in your code
                    int speedPerPage =0;
                    preparedStatement = connect.prepareStatement(sql);
                    preparedStatement.setString(1, printService.getName());
                    preparedStatement.setString(2, printerState);
                    preparedStatement.setString(3, colorSupported);
                    preparedStatement.setString(4, isAcceptingJobs);
                    preparedStatement.setInt(5, speedPerPage);
                    preparedStatement.setInt(6, printsCount);
                    preparedStatement.setInt(7, totalPages);
                    preparedStatement.setInt(8, waitingTime);
                    preparedStatement.executeUpdate();

                } else {
                    // Update the status of the printer

                }
            }
            connect.close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }




}
