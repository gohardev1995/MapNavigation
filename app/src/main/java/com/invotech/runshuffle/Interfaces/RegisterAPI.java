package com.invotech.runshuffle.Interfaces;


import com.invotech.runshuffle.Models.RegisterUser;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterAPI {
    @POST("registerUser")
    Call<RegisterUser> RegisterUser(

            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password


    );

}
