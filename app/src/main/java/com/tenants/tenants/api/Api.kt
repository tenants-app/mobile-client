package com.tenants.tenants.api

import com.tenants.tenants.models.GroupsResponse
import com.tenants.tenants.models.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("/auth/login")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @GET("/users/groups")
    fun getUserGroups():Call<GroupsResponse>

}