package com.tenants.tenants.api

import android.content.Context
import com.tenants.tenants.storage.SharedPrefManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient constructor(context: Context) {

    private val TOKEN = SharedPrefManager.getInstance(context).token
    //private val BASE_URL = "http://10.0.2.2:3000"
    private val BASE_URL = "http://207.154.242.233"



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