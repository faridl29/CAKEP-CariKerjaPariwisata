package com.mfa.carikerjapariwisata.views.ui.all_place

import com.mfa.carikerjapariwisata.model.Jobs
import com.mfa.carikerjapariwisata.model.Place
import com.mfa.carikerjapariwisata.views.base.View

interface AllPlaceView : View {
    fun onSuccess(result: List<Place>)
    fun onFailed(error: String)
    fun onEmpty()
    fun onSuccessLikePlace(status: Boolean)
    fun onFailedLikePlace(error: String)
    fun onShowLoading()
    fun onHideLoading()
}