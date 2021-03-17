package com.britishbroadcast.frinder.model.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Geometry(
    val location: Location,
    val viewport: Viewport
)