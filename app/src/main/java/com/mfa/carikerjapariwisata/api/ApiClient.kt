package com.mfa.carikerjapariwisata.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    var BASE_URL = "http://192.168.43.85/CAKEP-Backend/api/"

    fun getClient(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }

    object create {
        val service = getClient().create(ApiClient::class.java)
    }

}