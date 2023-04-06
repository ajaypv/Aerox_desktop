package com.example.demo;

public class PdfDataModel {
    public PdfDataModel(Integer pdfprintid, String memberId, String username, String color, String phoneNum, String binding, Integer pages, Integer amount, Integer waitingTime, String status) {
        this.pdfprintid = pdfprintid;
        this.memberId = memberId;
        this.Username = username;
        this.Color = color;
        this.phoneNum = phoneNum;
        this.Binding = binding;
        this.pages = pages;
        this.Amount = amount;
        this.WaitingTime = waitingTime;
        this.status = status;
    }

    private Integer pdfprintid;
    private String memberId;
    private String Username;
    private String Color;
    private String phoneNum;
    private String Binding;
    private Integer pages;
    private Integer Amount;
    private Integer WaitingTime;
    private String status;

    public PdfDataModel() {

    }

    public Integer getPdfprintid() {
        return pdfprintid;
    }

    public void setPdfprintid(Integer pdfprintid) {
        this.pdfprintid = pdfprintid;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBinding() {
        return Binding;
    }

    public void setBinding(String binding) {
        Binding = binding;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public Integer getWaitingTime() {
        return WaitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        WaitingTime = waitingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
