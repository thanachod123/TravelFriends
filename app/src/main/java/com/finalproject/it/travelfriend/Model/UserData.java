package com.finalproject.it.travelfriend.Model;

public class UserData {
    String email, name, surname, phone, gender, profile_image, province, district, citizen_image, role , device_token;

    public UserData(String email, String name, String surname, String phone, String gender, String profile_image, String province, String district, String citizen_image, String role, String device_token) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.gender = gender;
        this.profile_image = profile_image;
        this.province = province;
        this.district = district;
        this.citizen_image = citizen_image;
        this.role = role;
        this.device_token = device_token;
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

    public String getCitizen_image() {
        return citizen_image;
    }

    public void setCitizen_image(String citizen_image) {
        this.citizen_image = citizen_image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", citizen_image='" + citizen_image + '\'' +
                ", role='" + role + '\'' +
                ", device_token='" + device_token + '\'' +
                '}';
    }
}
