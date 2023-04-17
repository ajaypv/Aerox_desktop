/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;


public class DashboardController implements Initializable {






    static DashboardController instance;

    public void setDashboardController(DashboardController controller) {
        instance = controller;
    }
    @FXML
    GridPane PrinterGrid;

    @FXML
    private ChoiceBox<String> myChoiceBox;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;
    @FXML
    private ImageView PrinterImage;

    @FXML
    private Label PrinterName;

    @FXML
    private Label PrinterStatus;

    @FXML
    private Button minimize;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button coaches_btn;

    @FXML
    private Button members_btn;

    @FXML
    private Button logout;

    @FXML
    private Button payment_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_NM;

    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_TI;
    public void minimize() {

        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);

    }




    public void close() {

        javafx.application.Platform.exit();

    }



    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private AnchorPane coaches_form;

    @FXML
    private TextField coaches_coachID;

    @FXML
    private TextField coaches_name;

    @FXML
    private TextArea coaches_address;

    @FXML
    private ComboBox<?> coaches_gender;

    @FXML
    private TextField coaches_phoneNum;

    @FXML
    private Button coaches_addBtn;

    @FXML
    private Button coaches_updateBtn;

    @FXML
    private Button coaches_resetBtn;

    @FXML
    private Button coaches_deleteBtn;

    @FXML
    private ComboBox<?> coaches_status;

    @FXML
    private TableView<coachData> coaches_tableView;

    @FXML
    private TableColumn<coachData, String> coaches_col_coachID;

    @FXML
    private TableColumn<coachData, String> coaches_col_name;

    @FXML
    private TableColumn<coachData, String> coaches_col_address;

    @FXML
    private TableColumn<coachData, String> coaches_col_gender;

    @FXML
    private TableColumn<coachData, String> coaches_col_phoneNum;

    @FXML
    private TableColumn<coachData, String> coaches_col_status;

    @FXML
    private AnchorPane members_form;

    @FXML
    private TextField members_customerId;

    @FXML
    private TextField members_name;

    @FXML
    private TextArea members_caddress;

    @FXML
    private TextField members_phoneNum;

    @FXML
    private ComboBox<?> members_gender;

    @FXML
    private ComboBox<?> members_schedule;

    @FXML
    private DatePicker members_startDate;

    @FXML
    private DatePicker members_endDate;

    @FXML
    private ComboBox<?> members_status;

    @FXML
    private Button members_addBtn;

    @FXML
    private Button members_clearBtn;

    @FXML
    private Button members_updateBtn;

    @FXML
    private Button members_deleteBtn;

    @FXML
    private TableView<PdfDataModel> members_tableView;

    @FXML
    private TableColumn<memberData, Integer> members_col_customerID;

    @FXML
    private TableColumn<memberData, String> members_col_name;

    @FXML
    private TableColumn<memberData, String> members_col_address;

    @FXML
    private TableColumn<memberData, String> members_col_phoneNum;

    @FXML
    private TableColumn<memberData, Integer> members_col_gender;

    @FXML
    private TableColumn<memberData, Integer> members_col_schedule;

    @FXML
    private TableColumn<memberData, Integer> members_col_startDate;

    @FXML
    private TableColumn<memberData, Integer> members_col_endDate;

    @FXML
    private TableColumn<memberData, String> members_col_status;

    @FXML
    private AnchorPane payment_Form;

    @FXML
    private TableView<memberData> payment_tableView;

    @FXML
    private TableColumn<memberData, String> payment_col_customerID;

    @FXML
    private TableColumn<memberData, String> payment_col_name;

    @FXML
    private TableColumn<memberData, String> payment_col_startDate;

    @FXML
    private TableColumn<memberData, String> payment_col_endDate;

    @FXML
    private TableColumn<memberData, String> payment_col_status;

    @FXML
    private ComboBox<?> payment_customerID;

    @FXML
    private ComboBox<?> payment_name;

    @FXML
    private Label payment_total;

    @FXML
    private TextField payment_amount;

    @FXML
    private Label payment_change;

    @FXML
    private Button payment_payBtn;

    @FXML
    private Label username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    private PreparedStatement preparedStatement = null;

    private List<PrinterData> printerData;

    public DashboardController() {
    }




    public void dattapush(){

    }


    public void close(ActionEvent actionEvent) {
        javafx.application.Platform.exit();
    }

    public void minimize(ActionEvent actionEvent) {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);

    }

    public void switchForm(ActionEvent event) throws SQLException, BackingStoreException {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            coaches_form.setVisible(false);
            members_form.setVisible(false);
            payment_Form.setVisible(false);
            PrintListShowData();





        } else if (event.getSource() == coaches_btn) {

            dashboard_form.setVisible(false);
            coaches_form.setVisible(true);
            members_form.setVisible(false);
            payment_Form.setVisible(false);
            showPrinters();




        } else if (event.getSource() == members_btn) {

            dashboard_form.setVisible(false);
            coaches_form.setVisible(false);
            members_form.setVisible(true);
            payment_Form.setVisible(false);



        } else if (event.getSource() == payment_btn) {

            dashboard_form.setVisible(false);
            coaches_form.setVisible(false);
            members_form.setVisible(false);
            payment_Form.setVisible(true);



        }
    }

    public void logout(ActionEvent actionEvent) {
    }


    public void membersAddBtn(ActionEvent actionEvent) {
    }

    public void membersClear(ActionEvent actionEvent) {
    }

    public void membersUpdate(ActionEvent actionEvent) {
    }

    public void membersDelete(ActionEvent actionEvent) {
    }

    public void membersSelect(MouseEvent mouseEvent) {
    }

    public void paymentMemberId(ActionEvent actionEvent) {
    }

    public void paymentName(ActionEvent actionEvent) {
    }

    public void paymentAmount(ActionEvent actionEvent) {
    }

    public void paymentPayBtn(ActionEvent actionEvent) {
    }
    public void emptyFields() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please fill all blank fields");
        alert.showAndWait();
    }



    public static ObservableList<PdfDataModel> printListData = FXCollections.observableArrayList();
    public static ObservableList<PrinterData> printerListData = FXCollections.observableArrayList();




    //UsersDisplay

    public  ObservableList<PdfDataModel> PrintDisplayList() throws SQLException {

        String sql = "SELECT * FROM PDFPRINTS ORDER BY pdfprintid DESC";
        connect = database.connectDb();
        System.out.println(connect);

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            printListData.clear();

            PdfDataModel pdfDataModelObject;

            while (result.next()) {
                pdfDataModelObject = new PdfDataModel(result.getInt("pdfprintid"),
                        result.getString("pdfprintid"),
                        result.getString("username"),
                        result.getString("color"),
                        result.getString("phoneNum"),
                        result.getString("Binding"),
                        result.getInt("pages"),
                        result.getInt("amount"),
                        result.getInt("waitingtime"),
                        result.getString("status"));

                printListData.add(pdfDataModelObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        connect.close();
        return printListData;

    }


    //printer List
    public  ObservableList<PrinterData> PrintersDataList() throws SQLException {

        String sql = "SELECT * FROM PRINTERS";

        connect = database.connectDb();
        printerListData.clear();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            PrinterData md;
            while (result.next()) {


                md = new PrinterData(result.getInt("PrinterID"),
                        result.getString("PrinterName"),
                        result.getString("PrinterOptionSelect"),
                        result.getString("PrinterCurrentStatus"),
                        result.getString("PrinterColorSupport"),
                        result.getString("Printerpower"),
                        result.getInt("PrinterSpeedPerPage"),
                        result.getInt("PrinterPrintsCount"),
                        result.getInt("Totalpages"),
                        result.getInt("waitingTime")
                );

                printerListData.add(md);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        connect.close();
        return printerListData;
    }


    private static  ObservableList<PdfDataModel> DisplayPrinterListData;
    private static List<PrinterData> DisplayPrinterList;

    public void PrintListShowData() throws SQLException {
        DisplayPrinterListData = PrintDisplayList();
        members_col_customerID.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        members_col_name.setCellValueFactory(new PropertyValueFactory<>("username"));
        members_col_address.setCellValueFactory(new PropertyValueFactory<>("color"));
        members_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        members_col_gender.setCellValueFactory(new PropertyValueFactory<>("binding"));
        members_col_schedule.setCellValueFactory(new PropertyValueFactory<>("pages"));
        members_col_startDate.setCellValueFactory(new PropertyValueFactory<>("amount"));
        members_col_endDate.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        members_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        members_tableView.setItems(DisplayPrinterListData);
        highLight();
    }






    public void showPrinters() throws SQLException, BackingStoreException {


        System.out.println("called in the ShowPrinters");
        DisplayPrinterList = PrinterData.getPrinters();


        int colums=0;
        int rows =1;

        for(int i =0 ; i<DisplayPrinterList.size();i++){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("PrinterItem.fxml"));
            try {
                VBox postBox = fxmlLoader.load();
                PrinterController printerController = fxmlLoader.getController();
                printerController.setData(DisplayPrinterList.get(i));
                if(colums==3){
                    colums=0;
                    ++rows;
                }
                PrinterGrid.add(postBox,colums++,rows);
                GridPane.setMargin(postBox,new Insets(10));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDashboardController(this);



        try {
            showPrinters();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
        try {
            PrintListShowData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void highLight() {


        members_tableView.setRowFactory(tv -> new TableRow<PdfDataModel>() {
            @Override
            protected void updateItem(PdfDataModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty && "Printing".equals(item.getStatus())) {
                    setStyle("-fx-background-color: #8fe8df;" +
                            "-fx-control-inner-background: gray;");
                } else if (item != null && !empty && "Printed".equals(item.getStatus())) {
                    setStyle("-fx-background-color: rgb(226,255,223);" +
                            "-fx-control-inner-background: gray;");
                }else if (item != null && !empty && "Pending".equals(item.getStatus())) {
                    setStyle("-fx-background-color: rgb(255,223,209);" +
                            "-fx-control-inner-background: gray;");
                }


                else {

                    setStyle("");
                }
            }
        });


    }
}

