package com.mfa.carikerjapariwisata.views.ui.signup

import com.mfa.carikerjapariwisata.views.base.View

interface SignUpView : View {
    fun onSuccess()
    fun onFailed(error: String)
    fun onLoading()
}