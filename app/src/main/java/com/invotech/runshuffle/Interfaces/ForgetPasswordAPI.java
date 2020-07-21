package com.invotech.runshuffle.Interfaces;

import com.invotech.runshuffle.Models.ForgetUser;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ForgetPasswordAPI {
    @POST("forgetPassword")
    Call<ForgetUser> ForgetUser(
            @Query("email") String email
    );

}
