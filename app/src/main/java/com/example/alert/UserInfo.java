package com.example.alert;

/**
 * Created by GloTech on 23-07-2017.
 */

public class UserInfo {

    private String email;
    private String password;
    private String name;
    private String Address;
    private String phone;

    public UserInfo(String email, String password, String name, String address, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        Address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

