package com.finalproject.it.travelfriend.Model;


public class MessageModel {


    private String packageId;
    private String guideId;
    private String bookingId;
    private String touristId;
    private String date;
    private String type;

    public MessageModel(){

    }

    public MessageModel(String packageId, String guideId, String bookingId, String touristId , String date , String type) {

        this.packageId = packageId;
        this.guideId = guideId;
        this.bookingId = bookingId;
        this.touristId = touristId;
        this.date = date;
        this.type = type;
    }


    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        bookingId = bookingId;
    }

    public String getTouristId() {
        return touristId;
    }

    public void setTouristId(String touristId) {
        this.touristId = touristId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "packageId='" + packageId + '\'' +
                ", guideId='" + guideId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", touristId='" + touristId + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
