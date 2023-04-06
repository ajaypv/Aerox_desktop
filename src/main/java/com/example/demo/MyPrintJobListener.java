package com.example.demo;

import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

class MyPrintJobListener implements PrintJobListener {
    @Override
    public void printDataTransferCompleted(PrintJobEvent event) {
        System.out.println("Print data transfer completed");
    }
    @Override
    public void printJobCanceled(PrintJobEvent event) {
        System.out.println("Print job canceled"+event.getPrintEventType());
    }
    @Override
    public void printJobCompleted(PrintJobEvent event) {
        System.out.println("Print job completed");
    }
    @Override
    public void printJobFailed(PrintJobEvent event) {
        System.out.println("Print job failed");
    }
    @Override
    public void printJobNoMoreEvents(PrintJobEvent event) {
        System.out.println("No more events for print job");
    }

    @Override
    public void printJobRequiresAttention(PrintJobEvent pje) {
        System.out.println(" error has occurred ");
    }
}







