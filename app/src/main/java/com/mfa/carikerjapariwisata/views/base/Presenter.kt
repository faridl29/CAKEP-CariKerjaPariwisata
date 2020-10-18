package com.mfa.carikerjapariwisata.views.base

import android.view.View

interface Presenter<T : View?> {
    fun onAttach(view: T)
    fun onDetach()
}
