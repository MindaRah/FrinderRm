package com.britishbroadcast.frinder.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PlusCode(
    val compound_code: String,
    val global_code: String
)