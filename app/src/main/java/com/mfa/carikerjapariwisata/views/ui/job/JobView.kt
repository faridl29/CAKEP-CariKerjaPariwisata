package com.mfa.carikerjapariwisata.views.ui.job

import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.views.base.View

interface JobView : View {
    fun onSuccess(result: List<Jobs>)
    fun onFailed(error: String)
    fun onEmpty()
    fun onSuccessBookmarkJob(status: Boolean)
    fun onFailedBookmarkJob(error: String)
}