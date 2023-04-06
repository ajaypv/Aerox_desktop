package com.example.demo.Model;

public class PrinterModel {
    private String PrinterImage;
    private String PrinterName;
    public PrinterModel(String printerName, String isPrinterOnline) {
        this.PrinterName =printerName;
        this.PrinterStatus = isPrinterOnline;
    }

    public PrinterModel() {
    }

    public String getPrinterImage() {
        return PrinterImage;
    }

    public void setPrinterImage(String printerImage) {
        PrinterImage = printerImage;
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

    private String PrinterStatus;

}
