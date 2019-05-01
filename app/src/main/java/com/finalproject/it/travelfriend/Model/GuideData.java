package com.finalproject.it.travelfriend.Model;

public class GuideData {
    String email, name, surname, phone, gender, profile_image, certificate_image, license_image,citizen_image, province, district, age, role,status_allow , device_token;

    public GuideData() {
    }

    public GuideData(String email, String name, String surname, String phone, String gender, String profile_image, String certificate_image, String license_image, String citizen_image, String province, String district, String age, String role, String status_allow, String device_token) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.gender = gender;
        this.profile_image = profile_image;
        this.certificate_image = certificate_image;
        this.license_image = license_image;
        this.citizen_image = citizen_image;
        this.province = province;
        this.district = district;
        this.age = age;
        this.role = role;
        this.status_allow = status_allow;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus_allow() {
        return status_allow;
    }

    public void setStatus_allow(String status_allow) {
        this.status_allow = status_allow;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    @Override
    public String toString() {
        return "GuideData{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", certificate_image='" + certificate_image + '\'' +
                ", license_image='" + license_image + '\'' +
                ", citizen_image='" + citizen_image + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", age='" + age + '\'' +
                ", role='" + role + '\'' +
                ", status_allow='" + status_allow + '\'' +
                ", device_token='" + device_token + '\'' +
                '}';
    }
}
