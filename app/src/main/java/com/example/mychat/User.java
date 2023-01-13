package com.example.mychat;

public class User {
    private String email;
    private String img;
    private String name;
    private String phone;

    User(String img2, String name2, String email2, String phone2) {
        this.img = img2;
        this.name = name2;
        this.email = email2;
        this.phone = phone2;
    }

    public String getImg() {
        return this.img;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }
}
