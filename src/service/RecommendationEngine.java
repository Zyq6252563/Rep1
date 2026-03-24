package service;

import model.Movie;
import model.User;
import service.interfaces.IRecommendationStrategy;
import java.util.*;

public class RecommendationEngine {
    private MovieService movieService;
    private HistoryService historyService;
    private IRecommendationStrategy strategy;

    public RecommendationEngine(MovieService movieService, HistoryService historyService) {
        this.movieService = movieService;
        this.historyService = historyService;
        this.strategy = new GenreBasedStrategy(); // 默认策略
    }

    public void setStrategy(IRecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public IRecommendationStrategy getStrategy() {
        return this.strategy;
    }

    public List<Movie> getRecommendations(User user, int topN) {
        List<Movie> allMovies = movieService.getAllMovies();
        return strategy.recommend(user, allMovies, topN);
    }
}
