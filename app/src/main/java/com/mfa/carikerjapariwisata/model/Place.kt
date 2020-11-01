package com.mfa.carikerjapariwisata.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    var id: String? = null,
    var place_name: String? = null,
    var place_city: String? = null,
    var place_desc: String? = null,
    var place_price: String? = null,
    var place_time: String? = null,
    var md_category_id: String? = null,
    var photo_main: String? = null,
    var galleries: List<Galleries>? = null
) : Parcelable

@Parcelize
data class Galleries(
    var id: String? = null,
    var gallery_name: String? = null,
    var gallery_main: String? = null
) : Parcelable