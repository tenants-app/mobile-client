package com.tenants.tenants.api

import android.content.Context
import com.tenants.tenants.storage.SharedPrefManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient constructor(context: Context) {

    private val TOKEN = SharedPrefManager.getInstance(context).token
    //private val TOKEN =  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjOTE1NjRhYmI2ZDdmNTc4MGQwNzJmZiIsInVzZXJuYW1lIjoiZXhhbXBsZTEyMiIsImVtYWlsIjoiZXhhbXBsZUBleG1wbGUuY29tIiwiZXhwIjoxNTYwMDEyMzQxLCJpYXQiOjE1NTQ4MjgzNDF9.OF3BMrhQzxhWqcQFtod1MEyWrGh3wkKIresra_6fGvY"
    private val BASE_URL = "http://10.0.2.2:3000"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", "Bearer " + TOKEN)
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    val instance: Api by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(Api::class.java)
    }

}