package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertData {
    private static Connection connect;

    public static void PdfDataInsert(PdfDetails pdfDetails) throws SQLException {
        String sql = "INSERT INTO pdfprints (memberId, username, color, phoneNum, binding, pages, amount, waitingTime,pdfuuid, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
        connect = database.connectDb();
        try {
            PreparedStatement pstmt = connect.prepareStatement(sql);
            pstmt.setString(1,pdfDetails.pdfId);
            pstmt.setString(2, pdfDetails.userName);
            pstmt.setString(3, pdfDetails.selectedColor);
            pstmt.setString(4,pdfDetails.userPhoneNumber );
            pstmt.setString(5, pdfDetails.selectedBinding);
            if(pdfDetails.allPagesSelected.equals("All-Pages")){
                pstmt.setInt(6, Integer.parseInt(pdfDetails.numberPages));
            }else{
                pstmt.setInt(6, Integer.parseInt(pdfDetails.customPagesCount));
            }


            pstmt.setInt(7, Integer.parseInt(pdfDetails.printAmount));
            pstmt.setInt(8, 54);
            pstmt.setString(9, pdfDetails.pdfUUID);
            pstmt.setString(10, "Pending");
            pstmt.executeUpdate();

        }catch (Exception e){
            System.out.println(e);
        }
        DashboardController reload = new DashboardController();
        reload.PrintDisplayList();

        synchronized (DataRecevingBlockingQueue.class) {
            DownloadPdfBlockingQueueExample.addData(pdfDetails);
        }

        connect.close();


    }



}
