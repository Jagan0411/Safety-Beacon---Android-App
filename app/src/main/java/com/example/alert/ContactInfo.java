package com.example.alert;

/**
 * Created by GloTech on 23-07-2017.
 */

public class ContactInfo {
    private String email;
    private String phone;
    private String name;


    public ContactInfo(String name, String phone, String email) {
        this.email = email;
        this.phone = phone;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
