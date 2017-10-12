package lucas.guardafilme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_search_movie.*
import lucas.guardafilme.adapter.SearchMovieAdapter
import lucas.guardafilme.data.MoviesProvider
import lucas.guardafilme.model.Movie


/**
 * Created by lucassantos on 06/08/17.
 */
class SearchMovieActivity: AppCompatActivity(), SearchView.OnQueryTextListener {

    private val ARG_MOVIES = "ARG_MOVIES"

    private lateinit var mAdapter: SearchMovieAdapter
    private lateinit var mSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)

        val moviesRecyclerView = movies_recycler_view
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = SearchMovieAdapter(this)
        moviesRecyclerView.adapter = mAdapter

        if (savedInstanceState != null) {
            val movies = savedInstanceState.getParcelableArray(ARG_MOVIES)
            if (movies != null) {
                @Suppress("UNCHECKED_CAST")
                mAdapter.setItems(movies.asList() as List<Movie>)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        mSearchView = menu.findItem(R.id.search_item).actionView as SearchView
        mSearchView.setOnQueryTextListener(this)

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArray(ARG_MOVIES, mAdapter.movies.toTypedArray())

        super.onSaveInstanceState(outState)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        mSearchView.clearFocus()
        movies_recycler_view.visibility = View.GONE
        loading_view.visibility = View.VISIBLE
        MoviesProvider.searchMovies(this, query, { movies: List<Movie> ->
            mAdapter.setItems(movies)
            movies_recycler_view.visibility = View.VISIBLE
            loading_view.visibility = View.GONE
        })

        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SearchMovieActivity::class.java)
        }
    }
}