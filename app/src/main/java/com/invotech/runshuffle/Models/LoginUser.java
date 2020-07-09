package com.invotech.runshuffle.Models;

import com.google.gson.annotations.SerializedName;

public class LoginUser {
    public LoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
}
