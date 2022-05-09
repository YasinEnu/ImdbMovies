package com.yasin.imdbMovies.data.repository

import com.yasin.imdbMovies.common.Constants
import com.yasin.imdbMovies.data.remote.MovieApi
import com.yasin.imdbMovies.data.remote.dto.MovieDetailsDto
import com.yasin.imdbMovies.data.remote.dto.MovieListDto
import com.yasin.imdbMovies.data.remote.dto.PopularMoviesDto
import com.yasin.imdbMovies.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
):MovieRepository {
    override suspend fun getPopularMovies(): PopularMoviesDto {
        return api.getPopularMovies(Constants.API_KEY)
    }

    override suspend fun searchMovieByName(movieName: String): MovieListDto {
        return api.searchMovie(Constants.API_KEY,movieName,false)
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetailsDto {
        return api.getMoviesDetails(movieId,Constants.API_KEY)
    }
}