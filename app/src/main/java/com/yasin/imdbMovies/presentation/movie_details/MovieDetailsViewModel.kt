package com.yasin.imdbMovies.presentation.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasin.imdbMovies.common.Resource
import com.yasin.imdbMovies.data.remote.dto.MovieDetailsDto
import com.yasin.imdbMovies.domain.use_case.get_movie_details.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieDetailsUseCase
): ViewModel()  {

    val progressBarLiveData = MutableLiveData<Boolean>()
    val getDetailSuccessfulData = MutableLiveData<MovieDetailsDto>()
    val getDetailFailedData = MutableLiveData<String>()

    fun getMovieDetails(movieId:String) {
        getMovieUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    getDetailSuccessfulData.postValue(result.data)
                    progressBarLiveData.postValue(false)
                }
                is Resource.Error -> {
                    getDetailFailedData.postValue(result.message ?: "Unexpected error occurred")
                    progressBarLiveData.postValue(false)
                }
                is Resource.Loading -> {
                    progressBarLiveData.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }

}