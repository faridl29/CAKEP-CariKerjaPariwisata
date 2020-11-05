package com.mfa.carikerjapariwisata.api

import com.mfa.carikerjapariwisata.model.GetJobs
import com.mfa.carikerjapariwisata.model.GetPlaces
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {
    @GET("Place")
    fun getPlaceList(): Call<GetPlaces?>?

    @GET("Job")
    fun getJobList(): Call<GetJobs?>?

    @Multipart
    @POST("Job")
    open fun crete_job(
        @Part("job_title") job_title: String?,
        @Part file: MultipartBody.Part?,
        @Part("job_place") job_place: String?,
        @Part("job_date_end") job_date_end: String?,
        @Part("job_sallary") job_sallary: String?,
        @Part("job_city") job_city: String?,
        @Part("job_desc") job_desc: String?,
        @Part("photo") photo: String?,
        @Part("user_id") user_id: String?
    ): Call<ResponseBody?>?
}