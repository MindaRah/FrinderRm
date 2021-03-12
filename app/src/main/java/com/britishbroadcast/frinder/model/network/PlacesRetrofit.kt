package com.britishbroadcast.frinder.model.network

import com.britishbroadcast.frinder.model.data.PlaceResponse
import com.britishbroadcast.frinder.util.Constants.Companion.BASE_URL
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class PlacesRetrofit {

    private val placesService = buildPlaceService(createRetrofit())

    private fun buildPlaceService(createRetrofit: Retrofit): PlacesService =
            createRetrofit.create(PlacesService::class.java)

    private fun createRetrofit(): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun getNearByHangouts(location: String, radius: Int, type: String): Single<PlaceResponse> = placesService.getPlaces(location, radius, type, "AIzaSyA5rJh9FeteViW7n9M3iORS7d8L7W75wVI")

}