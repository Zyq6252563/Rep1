package service;

import model.Movie;
import service.interfaces.IMovieService;

import java.util.ArrayList;
import java.util.List;

public class MovieService implements IMovieService {
    // set original values
    private List<Movie> movies;

    public MovieService(List<Movie> movies) {
        this.movies = movies;
    }
    // get all movies
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }
   // method to find movies by id
    public Movie findMovieById(String movieId) {
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (movie.getId().equals(movieId)) {
                return movie;
            }
        }
        return null;
    }

    public Movie findMoviesById(String movieId) {
        return findMovieById(movieId);
    }
    // method to find movies by genre
    public List<Movie> findMoviesByGenre(String genre) {
        List<Movie> finder = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (movie.getGenre().equals(genre)) {
                finder.add(movie);
            }
        }
        return finder;
    }
    // method to find movies by year
    public List<Movie> findMovieByYear(int year) {
        List<Movie> finder = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (movie.getYear() == year) {
                finder.add(movie);
            }
        }
        return finder;
    }
    // find movies by rating
    public List<Movie> findMovieByRating(double minRating){
        List<Movie> finder = new ArrayList<>();
        for(int i = 0; i < movies.size(); i++){
            Movie movie = movies.get(i);
            if(movie.getRating()>=minRating){
                finder.add(movie);
            }
        }
        return finder;
    }
}