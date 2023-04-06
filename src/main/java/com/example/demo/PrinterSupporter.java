package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class PrinterSupporter {

    public static ArrayList<PrinterModel> list = new ArrayList<>();

    public static void addPrinter(){



            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);


            for (PrintService printService : printServices) {
                PrinterModel printer = new PrinterModel();
                printer.printerName = printService.getName();
                printer.printerId = String.valueOf(printService.hashCode());
                Preferences preferences = Preferences.userRoot().node("printers").node(String.valueOf(printService.hashCode()));
                preferences.put("PrinterName",printService.getName());
                preferences.put("PrinterId",String.valueOf(printService.hashCode()));
            }



    }

    public static void retrievePrinterListPreferences() {


        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

        for (PrintService printService : printServices) {
            PrinterModel printer = new PrinterModel();
            printer.printerName = printService.getName();
            printer.printerId = String.valueOf(printService.hashCode());
            Preferences preferences = Preferences.userRoot().node("printers").node(String.valueOf(printService.hashCode()));
            System.out.println(preferences.get("PrinterName",""));
            System.out.println(preferences.get("PrinterId",""));
            PrinterModel printerModel = new PrinterModel();
            printerModel.printerId = preferences.get("PrinterId","");
            printerModel.printerName = preferences.get("PrinterName","");
            list.add(printerModel);
        }


    }




}
