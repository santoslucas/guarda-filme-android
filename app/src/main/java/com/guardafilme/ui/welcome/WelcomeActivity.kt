package com.guardafilme.ui.welcome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.guardafilme.ui.main.MainActivity
import com.guardafilme.R
import com.guardafilme.data.AuthProvider
import com.guardafilme.model.WatchedMovie
import com.guardafilme.ui.UiUtils
import com.guardafilme.ui.searchmovie.SearchMovieActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.activity_welcome.view.*
import javax.inject.Inject

/**
 * Created by lucassantos on 05/08/17.
 */
class WelcomeActivity: AppCompatActivity(), WelcomeContract.View {
    companion object {
        val ADD_MOVIE_REQUEST = 435

        fun createIntent(context: Context): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            return intent
        }
    }

    private lateinit var mAdapter: WatchedMoviesAdapter

    @Inject
    lateinit var presenter: WelcomeContract.Presenter

    @Inject
    lateinit var authProvider: AuthProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        presenter.attach(this)

        // Setup view
        setContentView(R.layout.activity_welcome)
        supportActionBar?.title = getString(R.string.title_watched_movies)
        fab.setOnClickListener {
            presenter.addMovie()
        }

        // Setup RecyclerView
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == 4) {
                    return 2
                }

                return 1
            }
        }
        val moviesRecyclerView = watched_movies_recycler_view
        moviesRecyclerView.layoutManager = layoutManager
        mAdapter = WatchedMoviesAdapter(this, object : WatchedMoviesAdapter.WatchedMovieCallback {
            override fun editClicked(watchedMovie: WatchedMovie) {
                editWatchedMovie(watchedMovie)
            }

            override fun removeClicked(watchedMovie: WatchedMovie) {
                presenter.deleteMovie(watchedMovie)
            }
        })
        moviesRecyclerView.adapter = mAdapter

        presenter.load()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout_item) {
            authProvider.logoutUser(this, {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            })
        } else if (item.itemId == R.id.privacy_policy_item) {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("http://santoslucas.github.io/guardafilme.html")
            startActivity(openURL)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_MOVIE_REQUEST && resultCode == Activity.RESULT_OK) {
            presenter.load()
        }
    }

    override fun addWatchedMovies(watchedMovies: List<WatchedMovie>?) {
        mAdapter.setItems(watchedMovies)
    }

    override fun showLoading() {
        main_layout.visibility = View.GONE
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
        main_layout.visibility = View.VISIBLE
    }

    override fun showMoviesList() {
        watched_movies_recycler_view.visibility = View.VISIBLE
    }

    override fun hideMoviesList() {
        watched_movies_recycler_view.visibility = View.GONE
    }

    override fun showTooltip() {
        main_layout.tooltip_view.visibility = View.VISIBLE
    }

    override fun hideTooltip() {
        main_layout.tooltip_view.visibility = View.GONE
    }

    override fun showAddMovie() {
        val intent = SearchMovieActivity.createIntent(this)
        startActivityForResult(intent, ADD_MOVIE_REQUEST)
    }

    override fun scrollToTop() {
        watched_movies_recycler_view.scrollToPosition(0)
    }

    private fun editWatchedMovie(watchedMovie: WatchedMovie) {
        UiUtils.showDatePickerDialog(this, { date ->
            UiUtils.showRateDialog(this, { rate ->
                presenter.editMovie(watchedMovie, date, rate)
            }, {
                presenter.editMovie(watchedMovie, date)
            }, watchedMovie.rate)
        }, watchedMovie.getWatchedDateAsCalendar())
    }
}