package com.yasin.imdbMovies.domain.repository

import com.yasin.imdbMovies.data.remote.dto.MovieDetailsDto
import com.yasin.imdbMovies.data.remote.dto.MovieListDto
import com.yasin.imdbMovies.data.remote.dto.PopularMoviesDto

interface MovieRepository {

    suspend fun getPopularMovies(): PopularMoviesDto

    suspend fun searchMovieByName(movieName: String): MovieListDto

    suspend fun getMovieDetails(movieId: String): MovieDetailsDto

}