package com.mfa.carikerjapariwisata.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val BASE_URL = "http://192.168.43.85/CAKEP-Backend/api/"
    val IMAGE_URL = "http://192.168.43.85/CAKEP-Backend/images/"

    fun getClient(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }

}