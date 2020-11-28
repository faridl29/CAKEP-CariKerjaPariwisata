package com.mfa.carikerjapariwisata.views.ui.all_job

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.GetJobs
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class AllJobPresenter(private var con: Context) : Presenter<AllJobView> {
    private var mView: AllJobView? = null
    private var sharedPrefManager = SharedPrefManager(con)
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)

    override fun onAttach(view: AllJobView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun getJobList(){
        val callJobList: retrofit2.Call<GetJobs?>? =
            mInterface.getJobList(sharedPrefManager.spId)

        callJobList?.enqueue(object: retrofit2.Callback<GetJobs?>{
            override fun onFailure(call: retrofit2.Call<GetJobs?>?, t: Throwable?) {
//                        _response.value = "Tidak dapat terhubung ke server!"
                mView?.onFailed(t.toString())
            }

            override fun onResponse(
                call: retrofit2.Call<GetJobs?>?,
                response: Response<GetJobs?>?
            ) {
                val result = response?.body()?.jobsList
                if (result?.isNotEmpty()!!) {
                    mView?.onSuccess(result)
                } else {
                    mView?.onEmpty()
                }
            }

        })
    }

    fun bookmark_job(job_id: String?){
        val callJobBookmark: retrofit2.Call<ResponseBody?>? =
            mInterface.bookmark_job(sharedPrefManager.spId, job_id)

        callJobBookmark?.enqueue(object: retrofit2.Callback<ResponseBody?>{
            override fun onFailure(call: retrofit2.Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailed(t.toString())
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