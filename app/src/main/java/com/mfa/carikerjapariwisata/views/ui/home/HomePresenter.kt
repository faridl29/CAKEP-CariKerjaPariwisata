package com.mfa.carikerjapariwisata.views.ui.home

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.GetPlaces
import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import com.mfa.carikerjapariwisata.views.ui.create_job.CreateJobView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class HomePresenter(private var con: Context) : Presenter<HomeView> {
    private var mView: HomeView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private var sharedPrefManager = SharedPrefManager(con)

    override fun onAttach(view: HomeView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun getPlaceList(category: Int?){

        val callPlaceList: retrofit2.Call<GetPlaces?>? =
            mInterface.getPlaceList(sharedPrefManager.spId, if(category != 0) category.toString() else null)

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
    }

    fun getFavoritePlace(){

        val callPlaceList: retrofit2.Call<GetPlaces?>? =
            mInterface.getFavoritePlace()

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
                    mView?.onSuccessGetFavoritestPlace(result)
                } else {
                    mView?.onEmpty()
                }
            }

        })
    }

    fun like_place(place_id: String?){
        val callPlaceLike: retrofit2.Call<ResponseBody?>? =
            mInterface.like_place(sharedPrefManager.spId, place_id)

        callPlaceLike?.enqueue(object: retrofit2.Callback<ResponseBody?>{
            override fun onFailure(call: retrofit2.Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailed(t.toString())
            }

            override fun onResponse(call: retrofit2.Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                val jsonRESULTS = JSONObject(response?.body()?.string())
                if(jsonRESULTS.getString("response_code") == "200"){
                    mView?.onSuccessLikePlace(jsonRESULTS.getBoolean("status"))
                }else{
                    mView?.onFailedLikePlace(jsonRESULTS.getString("message"))
                }
            }

        })
    }

}