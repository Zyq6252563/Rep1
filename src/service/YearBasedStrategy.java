package service;

import model.Movie;
import model.User;
import service.interfaces.IRecommendationStrategy;
import java.util.*;

public class YearBasedStrategy implements IRecommendationStrategy {

    @Override
    public List<Movie> recommend(User user, List<Movie> allMovies, int topN) {
        List<Movie> recommendations = new ArrayList<>();
        List<Movie> history = user.getHistory().getMovies();

        // 排除已经看过的电影
        Set<String> watchedIds = new HashSet<>();
        for (int i = 0; i < history.size(); i++) {
            watchedIds.add(history.get(i).getId());
        }

        for (int i = 0; i < allMovies.size(); i++) {
            Movie movie = allMovies.get(i);
            if (!watchedIds.contains(movie.getId())) {
                recommendations.add(movie);
            }
        }

        // 按年份从新到旧排序，相同年份按评分排序
        Collections.sort(recommendations, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                if (m2.getYear() != m1.getYear()) {
                    return m2.getYear() - m1.getYear();
                }
                return Double.compare(m2.getRating(), m1.getRating());
            }
        });

        // 限制推荐数量
        if (recommendations.size() > topN) {
            recommendations = recommendations.subList(0, topN);
        }

        return recommendations;
    }
}
