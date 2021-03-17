package com.britishbroadcast.frinder.model.network

import com.britishbroadcast.frinder.model.data.PlaceResponse
import com.britishbroadcast.frinder.util.Constants.Companion.GET_PLACES
import com.britishbroadcast.frinder.util.Constants.Companion.KEY
import com.britishbroadcast.frinder.util.Constants.Companion.LOCATION
import com.britishbroadcast.frinder.util.Constants.Companion.RADIUS
import com.britishbroadcast.frinder.util.Constants.Companion.TYPE
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesService {
    //RxJava - React X implementation for Java
    //Observables - Main
    //* Single<T> - it emits a single value
    //* Observable<T> - emits 0 to many values
    //* Flowable<T> - emits 0 to many values but - has back pressure handling

    //* Completable - emit only the method onCompleted or onError
    //* Maybe - Decides whether a subscription can be completed ot not. Value, no value or exception

   // @GET(GET_PLACES)
   // fun getPlaces(@Query(LOCATION) location: String, @Query(RADIUS) radius: Int, @Query(TYPE) type: String, @Query(KEY) apiKey: String): Single<PlaceResponse>


    @GET(GET_PLACES)
    fun getPlaces(@Query(LOCATION) location: String, @Query(RADIUS) radius: Int, @Query(TYPE) type: String, @Query(KEY) apiKey: String): Single<PlaceResponse>
}