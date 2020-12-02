package com.mfa.carikerjapariwisata.views.ui.edit_profile

import com.mfa.carikerjapariwisata.views.base.View

interface EditProfileView : View {
    fun onSuccess(name:String?, email:String?, telepon:String?, profile:String?)
    fun onFailed(error: String)
    fun onLoading()
}