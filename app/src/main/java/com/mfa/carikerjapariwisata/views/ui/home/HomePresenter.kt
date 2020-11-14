package com.mfa.carikerjapariwisata.views.ui.home

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.GetPlaces
import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.views.base.Presenter
import com.mfa.carikerjapariwisata.views.ui.create_job.CreateJobView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class HomePresenter(private var con: Context) : Presenter<HomeView> {
    private var mView: HomeView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private var job = Job()

    override fun onAttach(view: HomeView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
        job.cancel()
    }

    fun getPlaceList(){
        val uiScope = CoroutineScope(job + Dispatchers.Main)
        uiScope.launch {
            try {
                val callPlaceList: retrofit2.Call<GetPlaces?>? =
                    mInterface.getPlaceList()

                callPlaceList?.enqueue(object: retrofit2.Callback<GetPlaces?>{
                    override fun onFailure(call: retrofit2.Call<GetPlaces?>?, t: Throwable?) {
//                        _response.value = "Tidak dapat terhubung ke server!"
                        mView?.onFailed(t.toString())
                    }

                    override fun onResponse(
                        call: retrofit2.Call<GetPlaces?>?,
                        response: Response<GetPlaces?>?
                    ) {
                        val result: List<Place>? = response?.body()?.placeList
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