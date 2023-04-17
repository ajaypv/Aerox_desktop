package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.standard.*;
import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;


public class PrinterData {

    private static DashboardController dashboardController;

    public int PrinterID;
    public String PrinterName;
    public String PrinterStatus;
    public String PrinterPower;
    public String PrinterColorSupport;
    public int PrinterSpeedPerPage;
    public int PrinterPrintsCount;
    public int TotalPages;
    public int waitingTime;
    private static Connection connect;
    private static PreparedStatement preparedStatement;
    private static PreparedStatement prepare;
    private static ResultSet result;

    public String getPrinterOptionSelect() {
        return PrinterOptionSelect;
    }

    public void setPrinterOptionSelect(String printerOptionSelect) {
        PrinterOptionSelect = printerOptionSelect;
    }

    public String PrinterOptionSelect;


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




    public PrinterData(int printerID, String printerName,String printerOptionSelect, String printerStatus,String PrinterColorSupport, String printerPower, int  PrinterSpeedPerPage, int printerPrintsCount, int totalPages, int waitingTime) {
        PrinterID = printerID;
        PrinterName = printerName;
        PrinterOptionSelect = printerOptionSelect;
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



    public static void UpdatePrinterPower(boolean power,String PrinterName) throws SQLException, IOException, BackingStoreException {
        connect = database.connectDb();
        Statement stmt = connect.createStatement();
        if(power){
            String query = "UPDATE printers SET  Printerpower  = 'Online' WHERE PrinterName = '" +PrinterName+ "'";
            stmt.executeUpdate(query);
        }else{
            String query = "UPDATE printers SET  Printerpower = 'Offline' WHERE PrinterName = '" +PrinterName+ "'";
            stmt.executeUpdate(query);
        }
        System.out.println("in printer data class");


        connect.close();
       

        DashboardController.instance.PrinterGrid.getChildren().clear();
        DashboardController.instance.showPrinters();


    }

    public static List<PrinterData> getPrinters() throws SQLException {
        List<PrinterData> printersList = new ArrayList<>();
        connect = database.connectDb();
        Statement stmt = connect.createStatement();
        String query = "SELECT * FROM Printers";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int printerID = rs.getInt("PrinterID");
            String printerName = rs.getString("PrinterName");
            String PrinterOptionSelect = rs.getString("PrinterOptionSelect");
            String printerStatus = rs.getString("PrinterCurrentStatus");
            String printerPower = rs.getString("Printerpower");
            String printerColorSupport = rs.getString("PrinterColorSupport");
            int printerSpeedPerPage = rs.getInt("PrinterSpeedPerPage");
            int printerPrintsCount = rs.getInt("PrinterPrintsCount");
            int totalPages = rs.getInt("Totalpages");
            int waitingTime = rs.getInt("waitingTime");
            PrinterData printer = new PrinterData(printerID, printerName, PrinterOptionSelect,printerStatus, printerColorSupport, printerPower, printerSpeedPerPage, printerPrintsCount, totalPages, waitingTime);
            printersList.add(printer);
        }
        connect.close();
        return printersList;
    }




    public static void UpdatePrinters() throws SQLException {

        connect = database.connectDb();
        Statement stmt = connect.createStatement();

        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            // Delete printers that are not present in the system but present in the table
            ResultSet printersInDb = stmt.executeQuery("SELECT PrinterName FROM Printers");
            while (printersInDb.next()) {
                String printerName = printersInDb.getString("PrinterName");
                boolean isPrinterInSystem = false;
                for (PrintService printService : printServices) {
                    if (printService.getName().equals(printerName)) {
                        isPrinterInSystem = true;
                        break;
                    }
                }
                if (!isPrinterInSystem) {
                    String deleteSql = "DELETE FROM Printers WHERE PrinterName = ?";
                    preparedStatement = connect.prepareStatement(deleteSql);
                    preparedStatement.setString(1, printerName);
                    preparedStatement.executeUpdate();
                    System.out.println("Deleted printer " + printerName + " from database.");
                }
            }

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



                ResultSet result = stmt.executeQuery("SELECT * FROM printers WHERE PrinterName = '" + printService.getName() + "'");
                if (!result.next()) {
                    String sql = "INSERT INTO Printers(PrinterName, PrinterCurrentStatus,PrinterOptionSelect,PrinterColorSupport,Printerpower,PrinterSpeedPerPage,PrinterPrintsCount,Totalpages,waitingTime) " + "VALUES (?, ?,?,?,?,?, ?,?, ?);";
                    int printsCount = 0;
                    int totalPages = 0;
                    int waitingTime = 0;
                    int speedPerPage =0;
                    preparedStatement = connect.prepareStatement(sql);
                    preparedStatement.setString(1, printService.getName());
                    preparedStatement.setString(2, printerState);
                    preparedStatement.setString(3,"Null");
                    preparedStatement.setString(4,  colorSupported);
                    preparedStatement.setString(5, "Offline");
                    preparedStatement.setInt(6, speedPerPage);
                    preparedStatement.setInt(7, printsCount);
                    preparedStatement.setInt(8, totalPages);
                    preparedStatement.setInt(9, waitingTime);
                    preparedStatement.executeUpdate();

                } else {


                }
            }
            connect.close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }




}
