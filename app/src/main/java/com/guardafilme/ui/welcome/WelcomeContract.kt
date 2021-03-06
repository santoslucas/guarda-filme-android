package com.guardafilme.ui.welcome

import com.guardafilme.base.BasePresenter
import com.guardafilme.base.BaseView
import com.guardafilme.model.WatchedMovie

/**
 * Created by lucassantos on 30/10/17.
 */
interface WelcomeContract {

    interface View: BaseView {
        fun addWatchedMovies(watchedMovies: List<WatchedMovie>?)
        fun showMoviesList()
        fun hideMoviesList()
        fun showTooltip()
        fun hideTooltip()
        fun showAddMovie()
        fun scrollToTop()
    }

    interface Presenter: BasePresenter<View> {
        fun load()
        fun addMovie()
        fun editMovie(watchedMovie: WatchedMovie, updatedDate: Long, updatedRate: Float = 0F)
        fun deleteMovie(watchedMovie: WatchedMovie)
        fun logout()
    }
}