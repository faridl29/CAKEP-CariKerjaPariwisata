package com.mfa.carikerjapariwisata.views.ui.profile

import android.content.Context
import android.content.SharedPreferences
import com.mfa.carikerjapariwisata.model.SharedPrefManager
import com.mfa.carikerjapariwisata.views.base.Presenter

class ProfilePresenter(private var con: Context) : Presenter<ProfileView> {
    private var mView:ProfileView? = null

    override fun onAttach(view: ProfileView) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

    fun getDataUser(){
        val sharedPreferences = SharedPrefManager(con)
        val name = sharedPreferences.spFullName
        val profile = sharedPreferences.spProfile
        val email = sharedPreferences.spEmail
        mView?.onSuccessGetData(name, profile, email)
    }

    fun logout() {
        val sharedPrefManager = SharedPrefManager(con)
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ALREADY_SIGNIN, false)
        mView?.onLogout()
    }

}