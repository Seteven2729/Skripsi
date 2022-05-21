package com.example.skripsi.model;

public class User {

    private int id;
    private String name, email, gender,phone;

    public User(int id,String name,String email,String gender,String phone){
        this.id = id;
        this.name = name;
        this.email= email;
        this.gender = gender;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }
}
