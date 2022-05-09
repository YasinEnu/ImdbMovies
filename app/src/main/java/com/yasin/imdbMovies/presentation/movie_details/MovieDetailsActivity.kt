package com.yasin.imdbMovies.presentation.movie_details

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yasin.imdbMovies.R
import com.yasin.imdbMovies.common.Constants
import com.yasin.imdbMovies.data.remote.dto.ResultsItemSearchMovie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_details.*

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    private val viewModel: MovieDetailsViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        showCustomUI()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)

        var movieData=intent.getSerializableExtra("movie_details") as ResultsItemSearchMovie;

        Glide.with(applicationContext)
            .load(Constants.POSTER_URL+movieData.posterPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(bannerIV)
        movieNameTV.setText(movieData.title)

        viewModel.getDetailSuccessfulData.observe(this, Observer {
            movieInfoTV.setText("${it.releaseDate?.substring(0,4)} | ${it.genres?.joinToString(", "){ it?.name.toString()}} | ${it.runtime}min")
            movieDetailsTV.setText(it.overview)
            movieRatingTV.setText(it.voteAverage.toString())
            ratingBar.rating= it.voteAverage?.div(2)?.toFloat() ?: 0.0f
        })
        viewModel.getDetailFailedData.observe(this, Observer {
            Toast.makeText(this@MovieDetailsActivity,it, Toast.LENGTH_LONG).show()
        })
        viewModel.progressBarLiveData.observe(this, Observer {
            if (it) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })

        viewModel.getMovieDetails(movieData.id.toString())

    }
    private fun showCustomUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}