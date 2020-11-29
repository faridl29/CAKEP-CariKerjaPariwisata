package com.mfa.carikerjapariwisata.views.ui.job_detail

import com.mfa.carikerjapariwisata.views.base.View

interface JobDetailView : View {
    fun onSuccessBookmarkJob(status: Boolean)
    fun onFailedBookmarkJob(error: String)
}