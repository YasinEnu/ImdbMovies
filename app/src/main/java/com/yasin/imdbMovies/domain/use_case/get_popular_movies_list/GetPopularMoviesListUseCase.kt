package com.yasin.imdbMovies.domain.use_case.get_popular_movies_list

import com.yasin.imdbMovies.common.Resource
import com.yasin.imdbMovies.data.remote.dto.PopularMoviesDto
import com.yasin.imdbMovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPopularMoviesListUseCase @Inject constructor(
    private val repository:MovieRepository
) {
    operator fun invoke(): Flow<Resource<PopularMoviesDto>> = flow {
        try {
            emit(Resource.Loading())
            val popularMovies = repository.getPopularMovies()
            emit(Resource.Success(popularMovies))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Server error occurred"))
        } catch(e: IOException) {
            emit(Resource.Error("Check internet connection."))
        }
    }

}