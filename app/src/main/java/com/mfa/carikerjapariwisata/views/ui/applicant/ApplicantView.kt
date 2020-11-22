package com.mfa.carikerjapariwisata.views.ui.applicant

import com.mfa.carikerjapariwisata.model.Applicants
import com.mfa.carikerjapariwisata.views.base.View

interface ApplicantView : View{
    fun onSuccess(result: List<Applicants>)
    fun onFailed(error: String)
    fun onEmpty()
}