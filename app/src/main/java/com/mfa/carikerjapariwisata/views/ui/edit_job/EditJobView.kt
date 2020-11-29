package com.mfa.carikerjapariwisata.views.ui.edit_job

import com.mfa.carikerjapariwisata.views.base.View

interface EditJobView : View {
    fun onSuccess()

    fun onFailed(error: String)
}