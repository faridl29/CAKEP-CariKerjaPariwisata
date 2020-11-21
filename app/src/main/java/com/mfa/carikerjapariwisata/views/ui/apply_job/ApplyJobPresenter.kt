package com.mfa.carikerjapariwisata.views.ui.apply_job

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import com.mfa.carikerjapariwisata.views.ui.all_job.AllJobView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File

class ApplyJobPresenter(private var con:Context) : Presenter<ApplyJobView> {
    private var mView: ApplyJobView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private  var sharedPrefManager = SharedPrefManager(con)

    override fun onAttach(view: ApplyJobView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun applyJob(data: Map<String, Any>){
        print(data)
        mInterface.apply_job(
            data["email"] as String,
            data["no_telp"] as String,
            sharedPrefManager.spId,
            data["job_id"] as String,
            data["applicant_desc"] as String,
            data["attachments"] as List<MultipartBody.Part>
        )?.enqueue(object : retrofit2.Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailed(t.toString())
            }

            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                val jsonRESULTS = JSONObject(response?.body()?.string())
                mView?.onSuccess(jsonRESULTS.getString("message"))
            }

        })
    }

}