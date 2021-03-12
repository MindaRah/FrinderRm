package com.britishbroadcast.frinder.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.britishbroadcast.frinder.model.data.HangoutPlace
import com.britishbroadcast.frinder.model.data.PlaceResponse
import com.britishbroadcast.frinder.model.network.PlacesRetrofit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FrinderViewModel: ViewModel(){

    private val placesRetrofit = PlacesRetrofit()
    val placeLiveData: MutableLiveData<List<HangoutPlace>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    fun findHangoutPlace(location: String, radius: Int, type: String){

        compositeDisposable.add(
            placesRetrofit
                .getNearByHangouts(location, radius, type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                    it.results
                }
                .subscribe({ hangoutPlaces ->

                    placeLiveData.postValue(hangoutPlaces)
                    clearDisposable()

                }, { throwable ->

                    Log.d("TAG_X", throwable.localizedMessage)

                })
        )
    }

    private fun clearDisposable() {
        compositeDisposable.clear()
    }


}