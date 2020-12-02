package com.mfa.carikerjapariwisata.views.ui.create_job

import com.mfa.carikerjapariwisata.views.base.View

interface CreateJobView : View{
    fun onSuccess()
    fun onFailed(error: String)
    fun onShowLoading()
    fun onHideLoading()
}