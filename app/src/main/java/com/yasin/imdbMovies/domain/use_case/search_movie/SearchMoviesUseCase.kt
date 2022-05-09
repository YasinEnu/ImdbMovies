package com.yasin.imdbMovies.domain.use_case.search_movie

import com.yasin.imdbMovies.common.Resource
import com.yasin.imdbMovies.data.remote.dto.MovieListDto
import com.yasin.imdbMovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository:MovieRepository
) {
    operator fun invoke(movieName: String): Flow<Resource<MovieListDto>> = flow {
        try {
            emit(Resource.Loading())
            val searchMovies = repository.searchMovieByName(movieName)
            emit(Resource.Success(searchMovies))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Server error occurred"))
        } catch(e: IOException) {
            emit(Resource.Error("Check internet connection."))
        }
    }

}