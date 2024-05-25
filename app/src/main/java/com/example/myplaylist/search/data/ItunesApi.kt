package com.example.myplaylist.search.data

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ItunesApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") term: String): Response<ResponseClass>
}