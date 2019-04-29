package com.finalproject.it.travelfriend.Model;

public class PackageData {
    private String guideId,name,description,image,province,package_type,vehicle_type,schedule,number_tourist,price_per_person,bank,bank_number,language,package_status,status_type,lat,lng,location_name,average_rating;

    public PackageData(){
    }

    public PackageData(String guideId, String name, String description, String image, String province, String package_type, String vehicle_type, String schedule, String number_tourist, String price_per_person, String bank, String bank_number, String language, String package_status, String status_type, String lat, String lng, String location_name, String average_rating) {
        this.guideId = guideId;
        this.name = name;
        this.description = description;
        this.image = image;
        this.province = province;
        this.package_type = package_type;
        this.vehicle_type = vehicle_type;
        this.schedule = schedule;
        this.number_tourist = number_tourist;
        this.price_per_person = price_per_person;
        this.bank = bank;
        this.bank_number = bank_number;
        this.language = language;
        this.package_status = package_status;
        this.status_type = status_type;
        this.lat = lat;
        this.lng = lng;
        this.location_name = location_name;
        this.average_rating = average_rating;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getNumber_tourist() {
        return number_tourist;
    }

    public void setNumber_tourist(String number_tourist) {
        this.number_tourist = number_tourist;
    }

    public String getPrice_per_person() {
        return price_per_person;
    }

    public void setPrice_per_person(String price_per_person) {
        this.price_per_person = price_per_person;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBank_number() {
        return bank_number;
    }

    public void setBank_number(String bank_number) {
        this.bank_number = bank_number;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public String getStatus_type() {
        return status_type;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    @Override
    public String toString() {
        return "PackageData{" +
                "guideId='" + guideId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", province='" + province + '\'' +
                ", package_type='" + package_type + '\'' +
                ", vehicle_type='" + vehicle_type + '\'' +
                ", schedule='" + schedule + '\'' +
                ", number_tourist='" + number_tourist + '\'' +
                ", price_per_person='" + price_per_person + '\'' +
                ", bank='" + bank + '\'' +
                ", bank_number='" + bank_number + '\'' +
                ", language='" + language + '\'' +
                ", package_status='" + package_status + '\'' +
                ", status_type='" + status_type + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", location_name='" + location_name + '\'' +
                ", average_rating='" + average_rating + '\'' +
                '}';
    }
}