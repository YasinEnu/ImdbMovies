package com.yasin.imdbMovies.presentation.movies_list

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yasin.imdbMovies.R
import com.yasin.imdbMovies.common.Constants
import com.yasin.imdbMovies.common.UniversalRecyclerViewAdapter
import com.yasin.imdbMovies.data.remote.dto.ResultsItemSearchMovie
import com.yasin.imdbMovies.data.remote.dto.ResultsItemPopularMovies
import com.yasin.imdbMovies.presentation.movie_details.MovieDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popular_movie_item_design.view.*
import kotlinx.android.synthetic.main.popular_movie_item_design.view.profileIV
import kotlinx.android.synthetic.main.search_movie_item_design.view.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog

    lateinit var popularMoviesAdapter: UniversalRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)

        showCustomUI()

        val typeface = ResourcesCompat.getFont(applicationContext, R.font.advent_pro)
        searchView.setTypeFace(typeface)
        searchView.disableCloseButton()

        cancel_button.setOnClickListener {
            searchView.setQuery("",false)
            moviesListRV.layoutManager = GridLayoutManager(this, 2)
            moviesListRV.adapter = popularMoviesAdapter
            (moviesListRV.adapter as UniversalRecyclerViewAdapter).notifyDataSetChanged()
            popularTV.text="Popular"
        }

        searchView.setOnQueryTextListener(object : DelayedOnQueryTextListener() {
            override fun onDelayerQueryTextChange(query: String?) {
                if (query!!.length>2){
                    viewModel.searchMovies(query)
                }
            }
        })

        moviesListRV.itemAnimator = DefaultItemAnimator()

        viewModel.getPopularMoviesListSuccessfulData.observe(this, Observer {
            popularTV.text="Popular"
            moviesListRV.layoutManager = GridLayoutManager(this, 2)
            popularMoviesAdapter=UniversalRecyclerViewAdapter(this@MainActivity,it,R.layout.popular_movie_item_design)
            {universalViewHolder, itemData, position ->
                universalViewHolder.itemView.shimmer_view_container.startShimmer()
                universalViewHolder.itemView.movieNameTV.text=(itemData as ResultsItemPopularMovies).title
                universalViewHolder.itemView.movieType.text=itemData.originalTitle
                Glide.with(applicationContext)
                    .addDefaultRequestListener(object : RequestListener<Any> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Any>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            universalViewHolder.itemView.shimmer_view_container.stopShimmer()
                            universalViewHolder.itemView.shimmer_view_container.visibility= View.GONE
                            return false
                        }
                        override fun onResourceReady(
                            resource: Any?,
                            model: Any?,
                            target: Target<Any>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            universalViewHolder.itemView.shimmer_view_container.stopShimmer()
                            universalViewHolder.itemView.shimmer_view_container.visibility= View.GONE
                            return false
                        }

                    })
                    .load(Constants.POSTER_URL+itemData.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(universalViewHolder.itemView.profileIV)


                universalViewHolder.itemView.setOnClickListener {
                    var data=ResultsItemSearchMovie(itemData.overview,itemData.originalLanguage,itemData.originalTitle,
                        itemData.video,itemData.title,itemData.genreIds,itemData.posterPath,itemData.backdropPath,
                        itemData.releaseDate,itemData.popularity,itemData.voteAverage,itemData.id,itemData.adult,
                        itemData.voteCount)
                    startActivity(Intent(this@MainActivity, MovieDetailsActivity::class.java).putExtra("movie_details",data))
                }
            }
            moviesListRV.adapter=popularMoviesAdapter
        })
        viewModel.searchMoviesListSuccessfulData.observe(this, Observer {
            popularTV.text="Search Result"
            moviesListRV.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
            moviesListRV.adapter=UniversalRecyclerViewAdapter(this@MainActivity,it,R.layout.search_movie_item_design)
            {universalViewHolder, itemData, position ->
                universalViewHolder.itemView.search_shimmer_view_container.startShimmer()
                universalViewHolder.itemView.searchMovieNameTV.text=(itemData as ResultsItemSearchMovie).title
                universalViewHolder.itemView.searchMovieTypeTV.text=itemData.originalTitle
                universalViewHolder.itemView.searchMovieRatingTV.text=itemData.voteAverage.toString()
                Glide.with(applicationContext)
                    .addDefaultRequestListener(object : RequestListener<Any> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Any>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            universalViewHolder.itemView.search_shimmer_view_container.stopShimmer()
                            universalViewHolder.itemView.search_shimmer_view_container.visibility= View.GONE
                            return false
                        }
                        override fun onResourceReady(
                            resource: Any?,
                            model: Any?,
                            target: Target<Any>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            universalViewHolder.itemView.search_shimmer_view_container.stopShimmer()
                            universalViewHolder.itemView.search_shimmer_view_container.visibility= View.GONE
                            return false
                        }

                    })
                    .load(Constants.POSTER_URL+itemData.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(universalViewHolder.itemView.profileIV)


                universalViewHolder.itemView.setOnClickListener {
                    startActivity(Intent(this@MainActivity, MovieDetailsActivity::class.java).putExtra("movie_details",itemData))
                }
            }
        })
        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                searchProgressBar.visibility=View.VISIBLE
            } else {
                searchProgressBar.visibility=View.GONE
            }
        })
        viewModel.requestFailedData.observe(this, Observer {
            Toast.makeText(this@MainActivity,it,Toast.LENGTH_LONG).show()
        })
        viewModel.progressBarLiveData.observe(this, Observer {
            if (it) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })
    }
    fun SearchView.setTypeFace(typeface: Typeface?) {
        val id = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val searchText = searchView.findViewById(id) as TextView
        searchText.typeface = typeface
        searchText.setTextColor(resources.getColor(R.color.white))
    }
    fun SearchView.disableCloseButton() {
        val id = searchView.context.resources.getIdentifier("android:id/search_close_btn", null, null)
        val closeButton = searchView.findViewById(id) as ImageView
        closeButton.visibility=View.GONE
        closeButton.isEnabled=false
    }
    private fun showCustomUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}
abstract class DelayedOnQueryTextListener : SearchView.OnQueryTextListener {
    private val handler: Handler = Handler(object : Handler.Callback{
        override fun handleMessage(msg: Message): Boolean {
            if (msg.what == 100) {
                onDelayerQueryTextChange(msg.data.getString("data"))
            }
            return false
        }
    })
    private var runnable: Runnable? = null
    override fun onQueryTextSubmit(s: String): Boolean {
        return false
    }
    override fun onQueryTextChange(s: String): Boolean {
        runnable = Runnable { onDelayerQueryTextChange(s) }
        handler.removeMessages(100)
        var container=Bundle()
        container.putString("data",s)
        var msg=Message()
        msg.what=100
        msg.data=container
        handler.sendMessageDelayed(msg,500)
        return true
    }
    abstract fun onDelayerQueryTextChange(query: String?)
}