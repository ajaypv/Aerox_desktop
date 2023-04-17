package com.example.demo;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PrinterSupporter {

    public static Connection connection;
    public static void updateOnlinePrinterData(WebsocketDataClient c){
        try {
            connection = database.connectDb();
            String sql = "SELECT * FROM PRINTERS WHERE PrinterPower = 'Online'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int printerID = result.getInt("PrinterID");
                String printerName = result.getString("PrinterName");
                String PrinterOptionSelect = result.getString("PrinterOptionSelect");
                String printerStatus = result.getString("PrinterCurrentStatus");
                String printerColorSupport = result.getString("PrinterColorSupport");
                String printerPower = result.getString("Printerpower");
                int printerSpeedPerPage = result.getInt("PrinterSpeedPerPage");
                int printerPrintsCount = result.getInt("PrinterPrintsCount");
                int totalPages = result.getInt("Totalpages");
                int waitingTime = result.getInt("waitingTime");
                PrinterData printer = new PrinterData(printerID, printerName,PrinterOptionSelect, printerStatus, printerColorSupport, printerPower, printerSpeedPerPage, printerPrintsCount, totalPages, waitingTime);
                Gson gson = new Gson();
                String json = gson.toJson(printer);
                JSONObject message = new JSONObject();
                message.put("StoreID", "nhce1");
                message.put("action", "onMessage");
                message.put("data", json);
                c.sendMessage(message.toString());
            }





        }catch(SQLException e){
            System.out.println(e);
        }

    }





}
