package com.tenants.tenants.api

import com.tenants.tenants.models.GroupsResponse
import com.tenants.tenants.models.LoginResponse
import com.tenants.tenants.models.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("/auth/login")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("/auth/register")
    fun userRegister(
        @Field("username") username:String,
        @Field("email") email:String,
        @Field("password") password: String,
        @Field("bank_account_number") bankAccountNumber: String
    ):Call<RegisterResponse>

    @GET("/users/groups")
    fun getUserGroups():Call<GroupsResponse>

}