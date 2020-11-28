package com.mfa.carikerjapariwisata.views.ui.job

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.GetJobs
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class JobViewModel(application: Application) : AndroidViewModel(application) {
    private var _mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private var sharedPrefManager = SharedPrefManager(getApplication<Application>().applicationContext)
    private val _data = MutableLiveData<List<Jobs>>()
    val data : LiveData<List<Jobs>>
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
                val callJob: retrofit2.Call<GetJobs?>? =
                    _mInterface.getJobList(sharedPrefManager.spId)

                callJob?.enqueue(object: retrofit2.Callback<GetJobs?>{
                    override fun onFailure(call: retrofit2.Call<GetJobs?>?, t: Throwable?) {
                        _response.value = "Tidak dapat terhubung ke server!"
                    }

                    override fun onResponse(
                        call: retrofit2.Call<GetJobs?>?,
                        response: Response<GetJobs?>?
                    ) {
                        val result: List<Jobs>? = response?.body()?.jobsList
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