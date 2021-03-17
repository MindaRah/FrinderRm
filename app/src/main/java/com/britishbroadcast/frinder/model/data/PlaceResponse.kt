package com.britishbroadcast.frinder.model.data

//import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    val html_attributions: List<Any>,
    val results: List<HangoutPlace>,
    val status: String
)