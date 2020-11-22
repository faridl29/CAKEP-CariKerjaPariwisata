package com.mfa.carikerjapariwisata.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Applicants(
    var id: String? = null,
    var applicant_desc: String? = null,
    var user_id: String? = null,
    var job_id: String? = null,
    var no_telp: String? = null,
    var email: String? = null,
    var created_at: String? = null,
    var photo: String? = null,
    var name: String? = null,
    var attachments: List<Attachments>
) : Parcelable

@Parcelize
data class Attachments(
    var id: String? = null,
    var applicant_id: String? = null,
    var attachment: String? = null
) : Parcelable