package com.yasin.imdbMovies.data.remote

import com.yasin.imdbMovies.data.remote.dto.MovieDetailsDto
import com.yasin.imdbMovies.data.remote.dto.MovieListDto
import com.yasin.imdbMovies.data.remote.dto.PopularMoviesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): PopularMoviesDto

    @GET("search/movie")
    suspend fun searchMovie(@Query("api_key") apiKey: String,
                            @Query("query") movieKeyword: String,
                            @Query("include_adult") isAdult: Boolean): MovieListDto

    @GET("movie/{movie_id}")
    suspend fun getMoviesDetails(@Path("movie_id") movieId: String,
                                 @Query("api_key") apiKey: String): MovieDetailsDto

}