package com.finalproject.it.travelfriend.Model;

public class BookingData {
    String guideId, touristId, packageId, booking_date, booking_number_tourist, booking_total_price, booking_money_transfer_slip, status_touristId, status_guideId, request_status, booking_image;

    public BookingData(){
    }

    public BookingData(String guideId, String touristId, String packageId, String booking_date, String booking_number_tourist, String booking_total_price, String booking_money_transfer_slip, String status_touristId, String status_guideId, String request_status, String booking_image) {
        this.guideId = guideId;
        this.touristId = touristId;
        this.packageId = packageId;
        this.booking_date = booking_date;
        this.booking_number_tourist = booking_number_tourist;
        this.booking_total_price = booking_total_price;
        this.booking_money_transfer_slip = booking_money_transfer_slip;
        this.status_touristId = status_touristId;
        this.status_guideId = status_guideId;
        this.request_status = request_status;
        this.booking_image = booking_image;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public String getTouristId() {
        return touristId;
    }

    public void setTouristId(String touristId) {
        this.touristId = touristId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_number_tourist() {
        return booking_number_tourist;
    }

    public void setBooking_number_tourist(String booking_number_tourist) {
        this.booking_number_tourist = booking_number_tourist;
    }

    public String getBooking_total_price() {
        return booking_total_price;
    }

    public void setBooking_total_price(String booking_total_price) {
        this.booking_total_price = booking_total_price;
    }

    public String getBooking_money_transfer_slip() {
        return booking_money_transfer_slip;
    }

    public void setBooking_money_transfer_slip(String booking_money_transfer_slip) {
        this.booking_money_transfer_slip = booking_money_transfer_slip;
    }

    public String getStatus_touristId() {
        return status_touristId;
    }

    public void setStatus_touristId(String status_touristId) {
        this.status_touristId = status_touristId;
    }

    public String getStatus_guideId() {
        return status_guideId;
    }

    public void setStatus_guideId(String status_guideId) {
        this.status_guideId = status_guideId;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    public String getBooking_image() {
        return booking_image;
    }

    public void setBooking_image(String booking_image) {
        this.booking_image = booking_image;
    }

    @Override
    public String toString() {
        return "BookingData{" +
                "guideId='" + guideId + '\'' +
                ", touristId='" + touristId + '\'' +
                ", packageId='" + packageId + '\'' +
                ", booking_date='" + booking_date + '\'' +
                ", booking_number_tourist='" + booking_number_tourist + '\'' +
                ", booking_total_price='" + booking_total_price + '\'' +
                ", booking_money_transfer_slip='" + booking_money_transfer_slip + '\'' +
                ", status_touristId='" + status_touristId + '\'' +
                ", status_guideId='" + status_guideId + '\'' +
                ", request_status='" + request_status + '\'' +
                ", booking_image='" + booking_image + '\'' +
                '}';
    }
}
