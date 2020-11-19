package com.mfa.carikerjapariwisata.views.ui.all_job

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.GetJobs
import com.mfa.carikerjapariwisata.views.base.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class AllJobPresenter(private var con: Context) : Presenter<AllJobView> {
    private var mView: AllJobView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private var job = Job()

    override fun onAttach(view: AllJobView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
        job.cancel()
    }

    fun getJobList(){
        val uiScope = CoroutineScope(job + Dispatchers.Main)
        uiScope.launch {
            try {
                val callJobList: retrofit2.Call<GetJobs?>? =
                    mInterface.getJobList()

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
            } catch (t: Throwable){
                mView?.onFailed(t.toString())
            }
        }

    }

    fun onCleared() {
        job.cancel()
    }

}