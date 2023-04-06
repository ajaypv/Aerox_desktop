package com.example.demo.User;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserController {

    public static void main(String[] args) {

        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:./resources/HR";

        //  Database credentials
        final String USER = "";
        final String PASS = "";
        Connection conn = null;
        Statement stmt = null;

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //Class.forName(new org.h2.Driver());

            //STEP 2: Open a connection
            //conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn = DriverManager.getConnection(DB_URL, "sa", "");

            //STEP 3: Execute a query
            stmt = conn.createStatement();

            String sql = "create table users(name varchar)";

            ResultSet rs = stmt.executeQuery(sql);


            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}