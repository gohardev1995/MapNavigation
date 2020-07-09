package com.invotech.runshuffle.Models;

import com.google.gson.annotations.SerializedName;

public class ForgetUser {

    @SerializedName("email")
    private String email;

    public ForgetUser(String email) {
        this.email = email;

    }
}
