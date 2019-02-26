package com.finalproject.it.travelfriend.Model;

public class UserData {
    String email, name, surname, phone, password, gender, profile_image, province, district, citizen_image;

    public UserData(String email, String name, String phone, String password, String gender, String profile_image, String surname, String province, String district, String citizen_image) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.profile_image = profile_image;
        this.province = province;
        this.district = district;
        this.citizen_image = citizen_image;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    @Override
    public String toString() {
        return "UserData{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", citizen_image='" + citizen_image + '\'' +
                '}';
    }
}
