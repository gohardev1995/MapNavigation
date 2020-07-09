package com.invotech.runshuffle.Interfaces;


import com.invotech.runshuffle.Models.LoginUser;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginAPI {
    @POST("loginUser")
    Call<LoginUser> createUser(
            @Query("email") String email,
            @Query("password") String password
    );
}
