package com.britishbroadcast.frinder.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HangoutPlace(
    val business_status: String,
    val geometry: Geometry,
    val icon: String,
    @ColumnInfo(name = "name") val name: String,
    val opening_hours: OpeningHours,
    val photos: List<Photo>,
    @PrimaryKey(autoGenerate = true) val place_id: String,
    val plus_code: PlusCode,
    val rating: Double,
    val reference: String,
    val scope: String,
    val types: List<String>,
    val user_ratings_total: Int,
    @ColumnInfo(name = "vicinity") val vicinity: String
) {
    constructor(name: String, vicinity: String)
}