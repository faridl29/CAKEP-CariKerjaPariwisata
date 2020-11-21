package com.mfa.carikerjapariwisata.views.ui.apply_job

import com.mfa.carikerjapariwisata.views.base.View

interface ApplyJobView : View {
    fun onSuccess(message: String)
    fun onFailed(error: String)
}