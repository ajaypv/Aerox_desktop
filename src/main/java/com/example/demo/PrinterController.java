package com.example.demo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;

public class PrinterController implements Initializable {


                private PrinterData printerModel;

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
            System.out.println(printer.getPrinterPower());
            this.printerModel = printer;
                PrinterName.setText(printer.getPrinterName());
                PrinterPowerStatus.setText(printer.getPrinterPower());
                TotalPages.setText(String.valueOf(printer.getTotalPages()));
                PrinterObject.setUserData(printer);
                String power = printer.getPrinterPower();
                String selectedOption = printer.getPrinterOptionSelect();

            myChoiceBox.getSelectionModel().select(selectedOption);
            if (power.equals("Online")) {
                PrinterName.setStyle("-fx-text-fill: green;");
                PrinterPowerButton.setSelected(true);
            } else {
                PrinterName.setStyle("-fx-text-fill: red;");
                PrinterPowerButton.setSelected(false);
            }


        }





    public void handlePrinterPower(ActionEvent actionEvent) throws SQLException, BackingStoreException, IOException {
                boolean isOn = PrinterPowerButton.isSelected();
                if (isOn) {
                        PrinterName.setStyle("-fx-text-fill: green;");
                        printerModel = (PrinterData) PrinterObject.getUserData();
                        PrinterData.UpdatePrinterPower(true, printerModel.getPrinterName());

                } else {
                        PrinterName.setStyle("-fx-text-fill: red;");
                          System.out.println(printerModel.getPrinterName());
                        printerModel = (PrinterData) PrinterObject.getUserData();
                    PrinterData.UpdatePrinterPower(false, printerModel.getPrinterName());

                }
        }




        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            String[] printerOptions = {"colorPinter", "Single-Side", "Double-Side","AllTypePrints"};
            myChoiceBox.getItems().addAll(printerOptions);
            myChoiceBox.setOnAction(this::getPrinterOption);

        }

    private void getPrinterOption(ActionEvent actionEvent) {
        String selectedOption = myChoiceBox.getValue();
        System.out.println(selectedOption);
        try {
                String printerName = PrinterName.getText();

                try {

                    connect = database.connectDb();
                    String sql = "UPDATE printers SET PrinterOptionSelect  = ? WHERE PrinterName = ?";
                    PreparedStatement pstmt = connect.prepareStatement(sql);
                    pstmt.setString(1,  selectedOption);
                    pstmt.setString(2, printerName);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e);
                }
                connect.close();

        }catch (Exception e){
            System.out.println(e);
        }
    }



    public void setToatlPages(KeyEvent keyEvent) throws SQLException {

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
