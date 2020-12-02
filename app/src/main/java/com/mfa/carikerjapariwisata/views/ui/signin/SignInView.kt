package com.mfa.carikerjapariwisata.views.ui.signin

import com.mfa.carikerjapariwisata.views.base.View

interface SignInView : View {
    fun onSuccess()
    fun onFailed(error: String)
    fun onLogged()
    fun onLoading()
}