package com.example.demo.Printers;
import java.util.ArrayList;
import java.util.List;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterURI;

public class Printer {
    public static void main (String [] args) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Number of print services: " + printServices.length);
        for (PrintService printer : printServices) {
            System.out.println("Printer name: " + printer.getName());
            System.out.println("Printer ID: " + printer.hashCode());
        }
    }

}
