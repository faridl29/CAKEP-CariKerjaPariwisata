package com.mfa.carikerjapariwisata.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {
    val URL = "http://192.168.43.85"
    val BASE_URL = URL+"/public/api/"
    val IMAGE_URL = URL+"/images/"
    val ATTACHMENT_URL = IMAGE_URL+"applicant_attachment/"
    val JOB_URL = IMAGE_URL+"job/"

    fun getClient(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }

}