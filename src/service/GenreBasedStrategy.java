package service;

import model.Movie;
import model.User;
import service.interfaces.IRecommendationStrategy;
import java.util.*;

public class GenreBasedStrategy implements IRecommendationStrategy {

    @Override
    public List<Movie> recommend(User user, List<Movie> allMovies, int topN) {
        List<Movie> recommendations = new ArrayList<>();
        List<Movie> history = user.getHistory().getMovies();

        if (!history.isEmpty()) {
            // 统计用户最喜欢的类型
            Map<String, Integer> genreCount = new HashMap<>();
            for (int i = 0; i < history.size(); i++) {
                Movie movie = history.get(i);
                String genre = movie.getGenre();
                genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
            }

            // 找到最受欢迎的类型
            String favoriteGenre = "";
            int maxCount = 0;
            Iterator<Map.Entry<String, Integer>> iterator = genreCount.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    favoriteGenre = entry.getKey();
                }
            }

            // 获取该类型的所有电影，排除已经看过的
            Set<String> watchedIds = new HashSet<>();
            for (int i = 0; i < history.size(); i++) {
                watchedIds.add(history.get(i).getId());
            }

            for (int i = 0; i < allMovies.size(); i++) {
                Movie movie = allMovies.get(i);
                if (movie.getGenre().equalsIgnoreCase(favoriteGenre) &&
                        !watchedIds.contains(movie.getId())) {
                    recommendations.add(movie);
                }
            }

            // 按评分排序
            Collections.sort(recommendations, new Comparator<Movie>() {
                @Override
                public int compare(Movie m1, Movie m2) {
                    return Double.compare(m2.getRating(), m1.getRating());
                }
            });
        } else {
            // 如果没有观看历史，推荐评分最高的电影
            recommendations = new ArrayList<>(allMovies);
            Collections.sort(recommendations, new Comparator<Movie>() {
                @Override
                public int compare(Movie m1, Movie m2) {
                    return Double.compare(m2.getRating(), m1.getRating());
                }
            });
        }

        // 限制推荐数量
        if (recommendations.size() > topN) {
            recommendations = recommendations.subList(0, topN);
        }

        return recommendations;
    }
}
