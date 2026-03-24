package service;

import model.Movie;
import model.User;
import util.ExceptionHandler;
import java.util.List;

public class WatchListService {
    // set original values
    private MovieService movieService;

    public WatchListService(MovieService movieService) {
        this.movieService = movieService;
    }

    // method to add movie to watchlist
    public boolean addMovieToWatchlist(User user, String movieId) {
        Movie movie = movieService.findMovieById(movieId);
        if (movie == null) {
            ExceptionHandler.handleValidationException("Movie ID does not exist!");
            return false;
        }
        if (user.getWatchlist().contains(movieId)) {
            ExceptionHandler.handleValidationException("The movie has existed in the watchlist!");
            return false;
        }

        // check list's size
        // diffrence between premium and ordinary users
        if (!user.isPremium() && user.getWatchlist().size() >= 10) {
            ExceptionHandler.handleValidationException("Basic users can only have up to 10 movies in watchlist! Upgrade to Premium for unlimited access.");
            return false;
        }

        user.getWatchlist().addMovie(movie);
        return true;
    }

    // method to remove movie from watchlist
    public boolean removeMovieFromWatchlist(User user, String movieId) {
        Movie movie = movieService.findMovieById(movieId);
        if (movie == null) {
            ExceptionHandler.handleValidationException("Movie ID does not exist!");
            return false;
        }
        if (!user.getWatchlist().contains(movieId)) {
            ExceptionHandler.handleValidationException("The movie does not existed in the watchlist!");
            return false;
        }
        return user.getWatchlist().removeMovie(movieId);
    }

    // add to ui
    public List<Movie> getWatchlist(User user) {
        return user.getWatchlist().getMovies();
    }

    // keep the original method
    public boolean addToWatchlist(User user, String movieId) {
        return addMovieToWatchlist(user, movieId);
    }
}