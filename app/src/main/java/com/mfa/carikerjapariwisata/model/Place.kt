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
    var md_category_id: String? = null
) : Parcelable