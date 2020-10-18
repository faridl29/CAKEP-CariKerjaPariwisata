package com.mfa.carikerjapariwisata.views.ui.home

import android.telecom.Call
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.GetPlaces
import com.mfa.carikerjapariwisata.model.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private var _mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private val _data = MutableLiveData<List<Place>>()
    val data : LiveData<List<Place>>
        get() = _data

    private val _response = MutableLiveData<String>()
    val response : LiveData<String>
        get() = _response

    private var job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    init {
        _response.value = ""
        initData()
    }

    fun initData() {
        uiScope.launch {
            try {
                val callTagihan: retrofit2.Call<GetPlaces?>? =
                    _mInterface.getPlaceList()

                callTagihan?.enqueue(object: retrofit2.Callback<GetPlaces?>{
                    override fun onFailure(call: retrofit2.Call<GetPlaces?>?, t: Throwable?) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(
                        call: retrofit2.Call<GetPlaces?>?,
                        response: Response<GetPlaces?>?
                    ) {
                        val result: List<Place>? = response?.body()?.placeList
                        if (result?.isNotEmpty()!!) {
                            _data.value = result
                            _response.value = "Berhasil fetch data!"
                        } else {
                            _response.value = "Data negara kosong!"
                        }
                    }

                })
            } catch (t: Throwable){
                _response.value = "Tidak ada koneksi internet!"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}