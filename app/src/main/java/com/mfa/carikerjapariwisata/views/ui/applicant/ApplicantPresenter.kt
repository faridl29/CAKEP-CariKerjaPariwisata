package com.mfa.carikerjapariwisata.views.ui.applicant

import android.content.Context
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.GetApplicants
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter
import retrofit2.Call
import retrofit2.Response

class ApplicantPresenter(private var con: Context) : Presenter<ApplicantView> {
    private var mView: ApplicantView? = null
    private var mInterface = ApiClient.getClient().create(ApiInterface::class.java)

    override fun onAttach(view: ApplicantView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun getApplicant(job_id: String?){

        mInterface.getApplicant(job_id)?.enqueue(object : retrofit2.Callback<GetApplicants?>{
            override fun onResponse(call: Call<GetApplicants?>?, response: Response<GetApplicants?>?) {
                val result = response?.body()?.applicantList
                if (result?.isNotEmpty()!!) {
                    mView?.onSuccess(result)
                } else {
                    mView?.onEmpty()
                }
            }

            override fun onFailure(call: Call<GetApplicants?>?, t: Throwable?) {
                mView?.onFailed(t.toString())
            }
        })
    }

}