package com.mfa.carikerjapariwisata.views.base

interface Presenter<T : View?> {
    fun onAttach(view: T)
    fun onDetach()
}
