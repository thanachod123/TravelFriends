package com.finalproject.it.travelfriend.Model;

public class GuideData {
    String email, name, surname, phone, password, gender, profile_image, certificate_image, license_image,citizen_image, province, district;

    public GuideData(String email, String name, String surname, String phone, String password, String gender, String profile_image, String certificate_image, String license_image, String citizen_image, String province, String district) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.profile_image = profile_image;
        this.certificate_image = certificate_image;
        this.license_image = license_image;
        this.citizen_image = citizen_image;
        this.province = province;
        this.district = district;
        }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getCertificate_image() {
        return certificate_image;
    }

    public void setCertificate_image(String certificate_image) {
        this.certificate_image = certificate_image;
    }

    public String getLicense_image() {
        return license_image;
    }

    public void setLicense_image(String license_image) {
        this.license_image = license_image;
    }

    public String getCitizen_image() {
        return citizen_image;
    }

    public void setCitizen_image(String citizen_image) {
        this.citizen_image = citizen_image;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "GuideData{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", certificate_image='" + certificate_image + '\'' +
                ", license_image='" + license_image + '\'' +
                ", citizen_image='" + citizen_image + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
