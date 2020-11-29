package com.mfa.carikerjapariwisata.views.ui.job_detail

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import com.mfa.carikerjapariwisata.views.ui.job.JobView
import com.mfa.carikerjapariwisata.views.ui.place_detail.PlaceDetailView
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class JobDetailPresenter(private var con: Context) : Presenter<JobDetailView>{
    private var mView: JobDetailView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private var sharedPrefManager = SharedPrefManager(con)

    override fun onAttach(view: JobDetailView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun bookmark_job(job_id: String?){
        val callJobBookmark: retrofit2.Call<ResponseBody?>? =
            mInterface.bookmark_job(sharedPrefManager.spId, job_id)

        callJobBookmark?.enqueue(object: retrofit2.Callback<ResponseBody?>{
            override fun onFailure(call: retrofit2.Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailedBookmarkJob("Tidak dapat terhubung ke server")
            }

            override fun onResponse(call: retrofit2.Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                val jsonRESULTS = JSONObject(response?.body()?.string())
                if(jsonRESULTS.getString("response_code") == "200"){
                    mView?.onSuccessBookmarkJob(jsonRESULTS.getBoolean("status"))
                }else{
                    mView?.onFailedBookmarkJob(jsonRESULTS.getString("message"))
                }
            }

        })
    }

}