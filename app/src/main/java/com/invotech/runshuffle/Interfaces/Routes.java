package com.invotech.runshuffle.Interfaces;

import com.google.android.gms.common.internal.HideFirstParty;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApiNotAvailableException;
/*
import com.invotech.runshuffle.Models.KotlinRound;
*/
import com.google.gson.annotations.Expose;
import com.invotech.runshuffle.Models.Options;
import com.invotech.runshuffle.Models.Round;
import com.invotech.runshuffle.Models.Round_trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Routes {

    /*@FormUrlEncoded
    @POST("json")
    Call<Round> getRoundCoordinates (
            @Header("Authorization") String authHeader,
            @Field("coordinate") List<List<Double>> coordinate,
            @Field("length") int lenght,
            @Field("seed") int seed
    );*/
    @POST("directions/foot-walking/json")

    Call<Round> getRoundCoordinates(

            @Header("Authorization") String authHeader,
            @Query("coordinates") List<List<Double>> coordinates,
            @Query("length") String lenght,
            @Query("seed") String seed


    );

}
