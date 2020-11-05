package com.mfa.carikerjapariwisata.views.ui.create_job

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.Jobs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class CreateJobViewModel : ViewModel() {

    private var _mInterface = ApiClient.getClient().create(ApiInterface::class.java)

    private val _response = MutableLiveData<String>()
    val response : LiveData<String>
        get() = _response

    private var job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)
    fun create_data(job: Jobs, file: String?) {
        _response.value = ""
        initData(job, file)
    }

    fun initData(job: Jobs, file: String?) {
        val file = File(file)
        //create RequestBody instance from file
        //create RequestBody instance from file
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        ) //allow image and any other file

        // MultipartBody.Part is used to send also the actual file name

        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        uiScope.launch {
            try {
                val callTagihan: retrofit2.Call<ResponseBody?>? =
                    _mInterface.crete_job(
                        job.job_title,
                        body,
                        job.job_place,
                        job.job_date_end,
                        job.job_sallary,
                        job.job_city,
                        job.job_desc,
                        job.photo,
                        job.user_id
                    )

                callTagihan?.enqueue(object: retrofit2.Callback<ResponseBody?>{
                    override fun onFailure(call: retrofit2.Call<ResponseBody?>?, t: Throwable?) {
                        _response.value = "Tidak dapat terhubung ke server!"
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseBody?>?,
                        response: Response<ResponseBody?>?
                    ) {

//                        if (result?.isNotEmpty()!!) {
////                            _response.value = "Berhasil fetch data!"
////                        } else {
////                            _response.value = "Data negara kosong!"
////                        }
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