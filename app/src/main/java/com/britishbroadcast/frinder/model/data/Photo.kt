package com.britishbroadcast.frinder.model.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Photo(
    val height: Int,
    val html_attributions: List<String>,
    val photo_reference: String,
    val width: Int
)