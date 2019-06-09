package com.tenants.tenants.api

import com.tenants.tenants.models.*
import okhttp3.ResponseBody
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

    @POST("/groups/{group_id}/debts/{debt_id}/paid")
    fun setDebtAsPaid(
        @Path(value = "group_id", encoded = true) groupId: String?,
        @Path(value = "debt_id", encoded = true) debtId: String?
    ):Call<ResponseBody>

    @FormUrlEncoded
    @POST("/groups/{group_id}/debts")
    fun addNewDebt(
        @Path(value = "group_id", encoded = true) groupId: String?,
        @Field("name") name: String,
        @Field("value") value: Int,
        @Field("debtor") debtor: String
    ):Call<ResponseBody>

    @GET("/users/groups")
    fun getUserGroups():Call<GroupsResponse>

    @GET("/groups/{group_id}/debts")
    fun getUserDebts(@Path(value = "group_id", encoded = true) groupId: String?):Call<DebtsResponse>

}