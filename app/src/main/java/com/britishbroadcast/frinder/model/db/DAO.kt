package com.britishbroadcast.frinder.model.db

import androidx.room.Dao
import androidx.room.Query
import com.britishbroadcast.frinder.model.data.HangoutPlace


@Dao
interface HangoutPlaceDao {
    @Query("SELECT * FROM hangoutplace ")
    fun getAll(): List<HangoutPlace>

    @Query("SELECT * FROM hangoutplace ORDER BY name ASC")
    fun getAlphabetisedDetails(): List<HangoutPlace>
   // @Insert
    //fun insert(vararg result: Result)
    //fun insertResult(vararg result: Result)
    //@Update
    //fun updateResult(vararg result: Result)
   // @Query("SELECT * from result WHERE resultId LIKE: resultId")
   // fun getResultById(resultId: Int): Result
  //  @Query("SELECT * FROM result")
   // fun getAllResult(): List<Result>

}