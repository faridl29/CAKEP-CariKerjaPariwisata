package com.mfa.carikerjapariwisata.views.ui.posted_job

import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.views.base.View

interface PostedJobView : View {
    fun onSuccess(result: List<Jobs>)
    fun onFailed(error: String)
    fun onEmpty()
}