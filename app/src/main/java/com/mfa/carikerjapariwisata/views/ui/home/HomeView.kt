package com.mfa.carikerjapariwisata.views.ui.home

import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.views.base.View

interface HomeView : View {
    fun onSuccess(result: List<Place>)
    fun onSuccessGetFavoritestPlace(result: List<Place>)
    fun onFailed(error: String)
    fun onEmpty()
    fun onSuccessLikePlace(status: Boolean)
    fun onFailedLikePlace(error: String)
}