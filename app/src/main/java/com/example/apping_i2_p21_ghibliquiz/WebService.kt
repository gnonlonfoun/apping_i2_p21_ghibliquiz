package com.example.apping_i2_p21_ghibliquiz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface WebService {
    @GET
    fun filmItem(@Url url:String): Call<MovieItem>

    @GET("people")
    fun listAllPeople():Call<List<CharacterItem>>
}