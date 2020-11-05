package com.mfa.carikerjapariwisata.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Jobs(
    var id: String? = null,
    var job_title: String? = null,
    var job_place: String? = null,
    var job_date_end: String? = null,
    var job_sallary: String? = null,
    var job_city: String? = null,
    var job_desc: String? = null,
    var photo: String? = null,
    var user_id: String? = null
): Parcelable