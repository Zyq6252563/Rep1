package service;

import model.Movie;
import model.User;
import java.util.List;

public class HistoryService {
    // set original values
    private MovieService movieService;

    public HistoryService(MovieService movieService) {
        this.movieService = movieService;
    }

    public List<Movie> getHistory(User user) {
        return user.getHistory().getMovies();
    }

    // method to add movie to history
    public boolean addMovieToHistory(User user, String movieId) {
        Movie movie = movieService.findMovieById(movieId);
        if (movie == null) {
            return false;
        }
        boolean added = user.getHistory().addMovie(movie);
        if (added) {
            //if it is added removing it
            user.getWatchlist().removeMovie(movieId);
        }
        return added;
    }

    // save original method
    public boolean markedWatch(User user, String movieId) {
        return addMovieToHistory(user, movieId);
    }

    public void viewHistory(User user) {
        user.getHistory();
    }
}