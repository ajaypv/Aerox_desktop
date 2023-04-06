package com.example.demo;

public class PrinterModel {

    public String printerName;

    public PrinterModel(String printerName, String printerId, String printerPower, String waitingTime, String totalPages, String printerSpeed) {
        this.printerName = printerName;
        this.printerId = printerId;
        PrinterPower = printerPower;
        WaitingTime = waitingTime;
        TotalPages = totalPages;
        PrinterSpeed = printerSpeed;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public String printerId;
    public String PrinterPower;
    public String WaitingTime;
    public String TotalPages;

    public String PrinterSpeed;



    public PrinterModel() {

    }


    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterPower() {
        return PrinterPower;
    }

    public void setPrinterPower(String printerPower) {
        PrinterPower = printerPower;
    }

    public String getWaitingTime() {
        return WaitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        WaitingTime = waitingTime;
    }

    public String getTotalPages() {
        return TotalPages;
    }

    public void setTotalPages(String totalPages) {
        TotalPages = totalPages;
    }

    public String getPrinterSpeed() {
        return PrinterSpeed;
    }

    public void setPrinterSpeed(String printerSpeed) {
        PrinterSpeed = printerSpeed;
    }




}