/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;


import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class database {
    static boolean PdfPrintTableabAvalible;
    static boolean PrinterTableAvalible;
    static boolean ReceivedPdfData;


    public static boolean Check() throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connect = null;


        try {
            Class.forName("org.h2.Driver");
            connect = DriverManager.getConnection("jdbc:h2:./resources/Aerox", "", "");
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SHOW TABLES");

            System.out.println(resultSet);
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getString(1).equalsIgnoreCase("pdfprints")) {
                        System.out.println("Table Named  pdfprints exist");
                        PdfPrintTableabAvalible = true;
                    }
                    if (resultSet.getString(1).equalsIgnoreCase("PRINTERS")) {
                        System.out.println(" printer Table  exist");
                        PrinterTableAvalible = true;
                    }

                    if (resultSet.getString(1).equalsIgnoreCase("ReceivedPdfData")) {
                        System.out.println(" ReceivedPdfData Table  exist");
                        ReceivedPdfData = true;
                    }
                }
            }
            if(!PdfPrintTableabAvalible){
                String createTableSql = "CREATE TABLE IF NOT EXISTS PDFPRINTS(" +
                        "pdfprintid INT PRIMARY KEY AUTO_INCREMENT," +
                        "memberId VARCHAR," +
                        "username VARCHAR," +
                        "color VARCHAR," +
                        "phoneNum VARCHAR," +
                        "binding VARCHAR," +
                        "pages INTEGER," +
                        "amount INTEGER," +
                        "waitingTime INTEGER," +
                        "pdfUUID VARCHAR," +
                        "status VARCHAR);";


                System.out.println("Table 'PDFPRINTS' created successfully.");
                statement.execute(createTableSql);

            }
            if(!PrinterTableAvalible){
                String createTableSql = "CREATE TABLE IF NOT EXISTS PRINTERS(" +
                        "PrinterID INT PRIMARY KEY AUTO_INCREMENT," +
                        "PrinterName VARCHAR," +
                        "PrinterCurrentStatus VARCHAR," +
                        "PrinterColorSupport VARCHAR," +
                        "Printerpower VARCHAR," +
                        "PrinterSpeedPerPage INTEGER," +
                        "PrinterPrintsCount  INTEGER," +
                        "Totalpages INTEGER," +
                        "waitingTime INTEGER);";
                System.out.println("Table 'Printer' created successfully.");
                statement.execute(createTableSql);

            }
            if(!ReceivedPdfData){
                String createTableSql = "CREATE TABLE IF NOT EXISTS ReceivedPdfData(" +
                        "pdfkeyID INT PRIMARY KEY AUTO_INCREMENT," +
                        "pdfId VARCHAR," +
                        "numberPages INTEGER," +
                        "pdfLink VARCHAR," +
                        "pdfName VARCHAR," +
                        "userUid VARCHAR);";



                statement.execute(createTableSql);
                System.out.println("Table 'ReceivedPdfData' created successfully.");

            }
            if(ReceivedPdfData && PrinterTableAvalible && PdfPrintTableabAvalible){
                System.out.println("all avilable");
                return  false;
            }else{
                return true;
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Error loading H2 database driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error creating table 'pdfprints': " + e.getMessage());
        }
        connect.close();
        return true;

    }



    public static Connection connectDb() {

        Connection connect = null;
        try {
            Class.forName("org.h2.Driver");
            connect = DriverManager.getConnection("jdbc:h2:./resources/Aerox", "", "");
            return connect;
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading H2 database driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error creating table 'pdfprints': " + e.getMessage());
        }
        return null;



    }

}






