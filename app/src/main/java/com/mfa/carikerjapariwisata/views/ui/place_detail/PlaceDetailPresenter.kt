package com.mfa.carikerjapariwisata.views.ui.place_detail

import android.content.Context
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import com.mfa.carikerjapariwisata.views.ui.home.HomeView
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class PlaceDetailPresenter(private var con: Context) : Presenter<PlaceDetailView>{
    private var mView: PlaceDetailView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private var sharedPrefManager = SharedPrefManager(con)
    override fun onAttach(view: PlaceDetailView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun like_place(place_id: String?){
        val callPlaceLike: retrofit2.Call<ResponseBody?>? =
            mInterface.like_place(sharedPrefManager.spId, place_id)

        callPlaceLike?.enqueue(object: retrofit2.Callback<ResponseBody?>{
            override fun onFailure(call: retrofit2.Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailedLikePlace("Tidak dapat terhubung ke server")
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