package com.mfa.carikerjapariwisata.views.ui.create_job

import android.content.Context
import android.widget.Toast
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class CreateJobPresenter(private var con: Context) : Presenter<CreateJobView> {
    private var mView:CreateJobView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private  var sharedPrefManager = SharedPrefManager(con)

    override fun onAttach(view: CreateJobView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun createJob(jobs: Jobs, file: String?){
        val file = File(file)

        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        )

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        mInterface.crete_job(
            jobs.job_title,
            body,
            jobs.job_place,
            jobs.job_date_end,
            jobs.job_sallary,
            jobs.job_city,
            jobs.job_desc,
            jobs.photo,
            sharedPrefManager.spId
        )?.enqueue(object : retrofit2.Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailed(t.toString())
            }

            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                mView?.onSuccess()
            }

        })
    }

}