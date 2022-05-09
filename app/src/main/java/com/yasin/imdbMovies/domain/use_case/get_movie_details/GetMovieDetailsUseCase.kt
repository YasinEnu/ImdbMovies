package com.yasin.imdbMovies.domain.use_case.get_movie_details

import com.yasin.imdbMovies.common.Resource
import com.yasin.imdbMovies.data.remote.dto.MovieDetailsDto
import com.yasin.imdbMovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: String): Flow<Resource<MovieDetailsDto>> = flow {
        try {
            emit(Resource.Loading())
            val movieDetails = repository.getMovieDetails(movieId)
            emit(Resource.Success(movieDetails))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Server error occurred"))
        } catch(e: IOException) {
            emit(Resource.Error("Check internet connection."))
        }
    }

}