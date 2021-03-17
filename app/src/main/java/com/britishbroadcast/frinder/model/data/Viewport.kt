package com.britishbroadcast.frinder.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)