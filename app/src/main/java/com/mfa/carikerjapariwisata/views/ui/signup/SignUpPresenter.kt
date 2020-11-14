package com.mfa.carikerjapariwisata.views.ui.signup

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.views.base.Presenter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class SignUpPresenter(private var con: Context) : Presenter<SignUpView> {
    private var mView: SignUpView? = null

    override fun onAttach(view: SignUpView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun signUpRequest(
        email: String,
        password: String,
        fullname: String
    ){
        var mInterface = ApiClient.getClient().create(ApiInterface::class.java)
        mInterface?.registerRequest(email, password, fullname)?.enqueue(object : retrofit2.Callback<ResponseBody?>{
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if(response.isSuccessful){
                    mView?.onSuccess()
                }else{
                    mView?.onFailed("Registrasi gagal, harap coba kembali!")
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                mView?.onFailed(t.toString())
            }
        })
    }

}