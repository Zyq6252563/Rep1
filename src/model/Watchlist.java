package model;

import java.util.ArrayList;
import java.util.List;

public class Watchlist {

    // 存储电影的列表
    private ArrayList<Movie> movies;

//constructor
    public Watchlist() {
        this.movies = new ArrayList<Movie>();
    }

//用于添加电影到观看列表
    public void addMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }
        movies.add(movie);
    }

//从观看列表中删除指定电影
    public boolean removeMovie(String movieId) {

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (movie.getId().equals(movieId)) {
                movies.remove(i);
                return true;
            }
        }
        return false;
    }

// 检查观看列表中是否包含指定ID的电影

    public boolean contains(String movieId) {
        // 遍历列表查找指定ID的电影
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (movie.getId().equals(movieId)) {
                return true;
            }
        }
        return false;
    }

    //getter
    public List<Movie> getMovies() {
        return new ArrayList<Movie>(movies);

    }
    public int size() {
        return movies.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Watchlist [").append(movies.size()).append(" movies]:\n");

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            sb.append("  ").append(movie.toString()).append("\n");
        }

        return sb.toString();
    }

//比较两个观看列表是否相等

    @Override
    public boolean equals(Object obj) {
        // 检查是否为同一个对象
        if (this == obj) {
            return true;
        }

        // 检查对象是否为null或类型不匹配
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // 比较电影列表是否相等
        Watchlist watchlist = (Watchlist) obj;
        return movies.equals(watchlist.movies);
    }

    //生成观看列表的Hashcode
    @Override
    public int hashCode() {
        return movies.hashCode();
    }
}
