package com.invotech.runshuffle.Models;

import com.google.gson.annotations.SerializedName;

public class RegisterUser {

    public RegisterUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /*username, bdate, location, email, password, c_password, tagname, app_token, city, country*/
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
}
