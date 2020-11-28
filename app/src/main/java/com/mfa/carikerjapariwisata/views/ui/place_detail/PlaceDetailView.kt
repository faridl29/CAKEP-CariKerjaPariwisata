package com.mfa.carikerjapariwisata.views.ui.place_detail

import com.mfa.carikerjapariwisata.views.base.View

interface PlaceDetailView : View {
    fun onSuccessLikePlace(status: Boolean)
    fun onFailedLikePlace(error: String)
}