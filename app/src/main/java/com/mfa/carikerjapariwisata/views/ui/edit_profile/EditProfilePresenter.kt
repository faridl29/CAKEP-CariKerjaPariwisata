package com.mfa.carikerjapariwisata.views.ui.edit_profile

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class EditProfilePresenter (private var con: Context) : Presenter<EditProfileView> {
    private var mView : EditProfileView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private  var sharedPrefManager = SharedPrefManager(con)

    override fun onAttach(view: EditProfileView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun editProfile(name: String?, email: String?, telepon: String?, photo: String?, file: String?){
        mView?.onLoading()
        val body: MultipartBody.Part

        if(file == null){
            val attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "")
            body = MultipartBody.Part.createFormData("file", "", attachmentEmpty)
        }else{
            val file = File(file)
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
            )

            body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        }

        mInterface.edit_profile(
            sharedPrefManager.spId,
            name,
            email,
            telepon,
            photo,
            body
        )?.enqueue(object : retrofit2.Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailed("Tidak dapat terhubung ke server!")
            }

            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                mView?.onSuccess(name, email, telepon, photo)
            }

        })
    }
}