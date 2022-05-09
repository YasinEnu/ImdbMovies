package com.yasin.imdbMovies.presentation.movies_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasin.imdbMovies.common.Resource
import com.yasin.imdbMovies.data.remote.dto.ResultsItemSearchMovie
import com.yasin.imdbMovies.data.remote.dto.ResultsItemPopularMovies
import com.yasin.imdbMovies.domain.use_case.get_popular_movies_list.GetPopularMoviesListUseCase
import com.yasin.imdbMovies.domain.use_case.search_movie.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPopularMoviesListUseCase: GetPopularMoviesListUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
):ViewModel() {

    val progressBarLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val getPopularMoviesListSuccessfulData = MutableLiveData<List<ResultsItemPopularMovies?>>()
    val requestFailedData = MutableLiveData<String>()
    val searchMoviesListSuccessfulData = MutableLiveData<List<ResultsItemSearchMovie?>>()




    init {
        getPopularMovies()
    }

    private fun getPopularMovies() {
        getPopularMoviesListUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    getPopularMoviesListSuccessfulData.postValue(result.data?.results ?: emptyList())
                    progressBarLiveData.postValue(false)
                }
                is Resource.Error -> {
                    requestFailedData.postValue(result.message ?: "Unexpected error occurred")
                    progressBarLiveData.postValue(false)
                }
                is Resource.Loading -> {
                    progressBarLiveData.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun searchMovies(moviesKeyword: String) {
        searchMoviesUseCase(moviesKeyword).onEach {
            when (it) {
                is Resource.Success -> {
                    searchMoviesListSuccessfulData.postValue(it.data?.results ?: emptyList())
                    loadingLiveData.postValue(false)
                }
                is Resource.Error -> {
                    requestFailedData.postValue(it.message ?: "Unexpected error occurred")
                    loadingLiveData.postValue(false)
                }
                is Resource.Loading -> {
                    loadingLiveData.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }
}