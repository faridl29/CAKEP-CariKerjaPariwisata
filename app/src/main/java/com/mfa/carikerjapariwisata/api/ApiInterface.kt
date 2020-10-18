package com.mfa.carikerjapariwisata.api

import com.mfa.carikerjapariwisata.model.GetPlaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("Place")
    fun getPlaceList(): Call<GetPlaces?>?
}