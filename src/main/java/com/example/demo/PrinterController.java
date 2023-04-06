package com.example.demo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PrinterController implements Initializable {

                private Connection connect;

                @FXML
                private Label ColourSupport;

                @FXML
                private Label PendingPrints;

                @FXML
                private ChoiceBox<String> myChoiceBox;

                @FXML
                private TextField PrintSpeedperPage;

                @FXML
                private Label PrinterName;

                @FXML
                private VBox PrinterObject;

                @FXML
                private ToggleButton PrinterPowerButton;

                @FXML
                private Label PrinterPowerStatus;

                @FXML
                private Label PrinterStatus;

                @FXML
                private TextField TotalPages;

                @FXML
                private Label WaitingTime;




        public void setData(PrinterData printer){
            System.out.println("called in setdata printer");
                PrinterName.setText(printer.getPrinterName());
                PrinterStatus.setText(printer.getPrinterStatus());
                WaitingTime.setText(String.valueOf(printer.getWaitingTime()));
                TotalPages.setText(String.valueOf(printer.getTotalPages()));
                PrinterPowerStatus.setText(printer.getPrinterPower());
                WaitingTime.setText(String.valueOf(printer.getWaitingTime()));
                PendingPrints.setText(String.valueOf(printer.getPrinterPrintsCount()));
                ColourSupport.setText(printer.getPrinterColorSupport());
                PrintSpeedperPage.setText(String.valueOf(printer.getPrinterSpeedPerPage()));

            boolean isOn = printer.getPrinterStatus().equals("on");

            if (isOn) {
                PrinterName.setStyle("-fx-text-fill: green;");
                PrinterPowerButton.setSelected(true);
            } else {
                PrinterName.setStyle("-fx-text-fill: red;");
                PrinterPowerButton.setSelected(false);
            }

        }


        public void handlePrinterPower(ActionEvent actionEvent) throws SQLException {
                boolean isOn = PrinterPowerButton.isSelected();
                String printerName = PrinterName.getText();
                if (isOn) {
                        System.out.println(printerName + " is turned on");
                        PrinterName.setStyle("-fx-text-fill: green;");
                        PrinterData.UpdatePrinterPower(isOn,printerName);
                        PrinterPowerStatus.setText("on");

                } else {
                        System.out.println(printerName + " is turned off");
                        PrinterName.setStyle("-fx-text-fill: red;");
                        PrinterData.UpdatePrinterPower(isOn,printerName);
                        PrinterPowerStatus.setText("off");
                }

        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            String[] printerOptions = {"colorPinter", "SinglePage", "DoublePage"};
            myChoiceBox.getItems().addAll(printerOptions);

        }

    public void setToatlPages(KeyEvent keyEvent) throws SQLException {
            System.out.println("clicled");
            try {
                String totalVlaue = TotalPages.getText();

                if (totalVlaue != null) {
                    int totalPages = Integer.parseInt(totalVlaue);
                    String printerName = PrinterName.getText();

                    connect = database.connectDb();
                    try {

                        connect = database.connectDb();
                        String sql = "UPDATE printers SET Totalpages  = ? WHERE PrinterName = ?";
                        PreparedStatement pstmt = connect.prepareStatement(sql);
                        pstmt.setInt(1, totalPages);
                        pstmt.setString(2, printerName);
                        pstmt.executeUpdate();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    connect.close();
                }
            }catch (Exception e){
                System.out.println(e);
            }


    }
}
