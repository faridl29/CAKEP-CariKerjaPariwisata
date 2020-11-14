package com.mfa.carikerjapariwisata.views.ui.signin

import android.content.Context
import android.util.Log
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class SignInPresenter(private var con: Context) : Presenter<SignInView> {
    private var mView: SignInView? = null
    private lateinit var sharedPrefManager: SharedPrefManager
    private var mInterface: ApiInterface? = null

    override fun onAttach(view: SignInView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun cekLogin() {
        sharedPrefManager = SharedPrefManager(con)
        if (sharedPrefManager.spAlreadySignin) {
            mView?.onLogged()
        }
    }

    fun loginRequest(email: String?, password:String?) {
        mInterface = ApiClient.getClient().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager(con)
        print(email)
        mInterface?.loginRequest(email, password)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    try {
                        val jsonRESULTS = JSONObject(response.body()?.string())
                        if (jsonRESULTS.getString("response_code") == "200") {
                            // Getting response data
                            val id = jsonRESULTS.getString("id")
                            val email = jsonRESULTS.getString("email")
                            val fullname = jsonRESULTS.getString("fullname")
                            val profile = jsonRESULTS.getString("profile")

                            sharedPrefManager?.saveSPString(SharedPrefManager.SP_ID, id)
                            sharedPrefManager?.saveSPString(SharedPrefManager.SP_EMAIL, email)
                            sharedPrefManager?.saveSPString(SharedPrefManager.SP_FULLNAME, fullname)
                            sharedPrefManager?.saveSPString(SharedPrefManager.SP_PROFILE, profile)

                            // this sharedpreference for trigger a session login
                            sharedPrefManager?.saveSPBoolean(SharedPrefManager.SP_ALREADY_SIGNIN, true)
                            mView?.onSuccess()
                        } else {
                            mView?.onFailed(jsonRESULTS.getString("message"))
                        }
                    } catch (e: JSONException) {
                        mView?.onFailed(e.toString())
                    } catch (e: IOException) {
                        mView?.onFailed(e.toString())
                    }
                } else {
                }
            }

            override fun onFailure(
                call: Call<ResponseBody?>,
                t: Throwable
            ) {
                Log.e("debug", "onFailure: ERROR > $t")
                mView?.onFailed(t.toString())
            }
        })
    }

}