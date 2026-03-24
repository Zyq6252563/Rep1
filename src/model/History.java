package model;

import java.util.ArrayList;
import java.util.List;

// History类，管理用户已观看的电影历史

public class History {

// 存储已观看电影的列表
    private ArrayList<Movie> movies;

//constructor
    public History() {
        this.movies = new ArrayList<Movie>();
    }

//添加电影到历史记录
    public boolean addMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }

        // 检查电影是否已经在历史记录里了
        for (int i = 0; i < movies.size(); i++) {
            Movie existingMovie = movies.get(i);
            if (existingMovie.getId().equals(movie.getId())) {
                return false;
            }
        }

        // 添加新电影到历史记录
        movies.add(movie);
        return true;
    }

//从历史记录中删除指定电影
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

//检查历史记录中是否包含指定ID的电影

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

//获取历史记录中的所有电影
    public List<Movie> getMovies() {
        return new ArrayList<Movie>(movies);
    }

//获取历史记录中电影的数量
    public int size() {
        return movies.size();
    }

//将历史记录转换为String

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("History [").append(movies.size()).append(" movies]:\n");

        // 遍历并添加每部电影的信息
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            sb.append("  ").append(movie.toString()).append("\n");
        }

        return sb.toString();
    }

//比较两个历史记录是否相等

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
        History history = (History) obj;
        return movies.equals(history.movies);
    }

    //生成历史记录的Hashcode
    @Override
    public int hashCode() {
        return movies.hashCode();
    }
}
