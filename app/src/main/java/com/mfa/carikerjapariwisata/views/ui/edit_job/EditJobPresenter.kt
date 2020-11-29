package com.mfa.carikerjapariwisata.views.ui.edit_job

import android.content.Context
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

class EditJobPresenter(private var con: Context) : Presenter<EditJobView> {
    private var mView: EditJobView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private  var sharedPrefManager = SharedPrefManager(con)

    override fun onAttach(view: EditJobView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun editJob(job_id: String?, jobs: Jobs, file: String?){
        val body: MultipartBody.Part

        if(file == null){
            val attachmentEmpty =
                RequestBody.create(MediaType.parse("text/plain"), "")

            body = MultipartBody.Part.createFormData("file", "", attachmentEmpty)
        }else{
            val file = File(file)

            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
            )

            body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        }

        mInterface.edit_job(
            job_id,
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