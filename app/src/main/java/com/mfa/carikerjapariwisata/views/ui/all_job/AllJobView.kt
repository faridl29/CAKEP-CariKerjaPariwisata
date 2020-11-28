package com.mfa.carikerjapariwisata.views.ui.all_job

import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.views.base.View

interface AllJobView : View {
    fun onSuccess(result: List<Jobs>)
    fun onFailed(error: String)
    fun onEmpty()
    fun onSuccessBookmarkJob(status: Boolean)
    fun onFailedBookmarkJob(error: String)
}