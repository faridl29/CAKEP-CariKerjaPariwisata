package com.mfa.carikerjapariwisata.views.ui.posted_job

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.GetJobs
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class PostedJobPresenter(private var con: Context) : Presenter<PostedJobView> {
    private var mView: PostedJobView? = null
    private var sharedPrefManager: SharedPrefManager? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)

    override fun onAttach(view: PostedJobView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun getPostedJob(){
        sharedPrefManager = SharedPrefManager(con)
        var id = sharedPrefManager?.spId

        mInterface.getPostedJob(id)?.enqueue(object : retrofit2.Callback<GetJobs?>{
            override fun onResponse(call: Call<GetJobs?>?, response: Response<GetJobs?>?) {
                val result = response?.body()?.jobsList
                if (result?.isNotEmpty()!!) {
                    mView?.onSuccess(result)
                } else {
                    mView?.onEmpty()
                }
            }

            override fun onFailure(call: Call<GetJobs?>?, t: Throwable?) {
                mView?.onFailed(t.toString())
            }
        })
    }

    fun deleteJob(job_id: String?){
            mInterface.deleteJob(job_id)?.enqueue(object : retrofit2.Callback<ResponseBody?>{
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                val jsonRESULTS = JSONObject(response?.body()?.string())
                if(jsonRESULTS.getString("response_code") == "200"){
                    mView?.onSuccessDeleteJob()
                }else{
                    mView?.onFailedDeleteJob()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailed("Tidak dapat terhubung ke server")
            }
        })
    }

}