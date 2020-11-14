package com.mfa.carikerjapariwisata.views.ui.profile

import com.mfa.carikerjapariwisata.views.base.View

interface ProfileView : View {
    fun onSuccessGetData(
        name: String?,
        profile: String?,
        email: String?
    )
    fun onLogout()
}