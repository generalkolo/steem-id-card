package com.semanientreprise.steemidcard.service;

import com.semanientreprise.steemidcard.models.UserDetails;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by GeneralKolo on 2018/03/17.
 */

public interface APISERVICE {

    //Retrofit For getting details of users
    @FormUrlEncoded
    @POST("/postdata")
    Call<UserDetails> getUserDetails(
            @Field("username") String usernameOne
    );
}