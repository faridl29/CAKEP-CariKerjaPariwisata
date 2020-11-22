package com.mfa.carikerjapariwisata.api

import com.mfa.carikerjapariwisata.model.GetApplicants
import com.mfa.carikerjapariwisata.model.GetJobs
import com.mfa.carikerjapariwisata.model.GetPlaces
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @Multipart
    @POST("Auth/login")
    fun loginRequest(
        @Part("email") email: String?,
        @Part("password") password: String?
    ): Call<ResponseBody?>?

    @Multipart
    @POST("Auth/register")
    fun registerRequest(
        @Part("email") email: String?,
        @Part("password") password: String?,
        @Part("fullname") fullname: String
    ): Call<ResponseBody>

    @GET("Place/get")
    fun getPlaceList(): Call<GetPlaces?>?

//    @Headers(
//        "Accept: application/json",
//        "Content-type:application/json"
//    )
    @GET("Job/get")
    fun getJobList(): Call<GetJobs?>?

    @Multipart
    @POST("Job/create")
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

    @Multipart
    @POST("Job/apply_job")
    open fun apply_job(
        @Part("email") email: String?,
        @Part("no_telp") no_telp: String?,
        @Part("user_id") user_id: String?,
        @Part("job_id") job_id: String?,
        @Part("applicant_desc") applicant_desc: String?,
        @Part attachments: List<MultipartBody.Part>
    ): Call<ResponseBody?>?

    @GET("Job/get_posted_job/{user_id}")
    fun getPostedJob(@Path("user_id") user_id: String?): Call<GetJobs?>?

    @GET("Job/get_applicant/{job_id}")
    fun getApplicant(@Path("job_id") job_id: String?): Call<GetApplicants?>?

    @Streaming
    @GET
    fun downloadFileWithDynamicUrlAsync(@Url fileUrl: String?): Call<ResponseBody>?
}