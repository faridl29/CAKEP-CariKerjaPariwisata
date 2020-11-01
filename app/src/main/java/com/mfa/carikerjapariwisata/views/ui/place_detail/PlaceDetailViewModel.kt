package com.mfa.carikerjapariwisata.views.ui.place_detail

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.model.Place

class PlaceDetailViewModel : ViewModel() {
    var title: ObservableField<String> = ObservableField()
    var city: ObservableField<String> = ObservableField()
    var like: ObservableField<String> = ObservableField()
    var desc: ObservableField<String> = ObservableField()
    var price: ObservableField<String> = ObservableField()
    var time: ObservableField<String> = ObservableField()

    fun setData(place: Place) {
        title.set(place.place_name)
        city.set(place.place_city)
        desc.set(place.place_desc)
        price.set(place.place_price)
        time.set(place.place_time)
    }
}