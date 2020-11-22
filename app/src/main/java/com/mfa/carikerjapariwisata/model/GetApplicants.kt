package com.mfa.carikerjapariwisata.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetApplicants (
    @SerializedName("response_code")
    var status: String? = null,
    @SerializedName("result")
    var applicantList: List<Applicants>? = null,
    @SerializedName("message")
    var message: String? = null
) : Parcelable