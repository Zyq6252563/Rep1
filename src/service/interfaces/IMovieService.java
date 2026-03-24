package service.interfaces;

import model.Movie;
import model.User;
import java.util.List;

/**
 * 电影服务接口 - 定义电影查询相关的操作
 * 包括获取所有电影、按ID查找、按类型查找、按年份查找和按评分查找
 */
public interface IMovieService {
    /**
     * 获取所有电影
     * @return 所有电影的列表
     */
    List<Movie> getAllMovies();

    /**
     * 根据电影ID查找电影
     * @param movieId 电影ID
     * @return 找到的电影对象，未找到返回null
     */
    Movie findMovieById(String movieId);

    /**
     * 根据类型查找电影
     * @param genre 电影类型
     * @return 符合该类型的电影列表
     */
    List<Movie> findMoviesByGenre(String genre);

    /**
     * 根据年份查找电影
     * @param year 发行年份
     * @return 该年份发行的电影列表
     */
    List<Movie> findMovieByYear(int year);

    /**
     * 根据最低评分查找电影
     * @param minRating 最低评分
     * @return 评分大于等于最低评分的电影列表
     */
    List<Movie> findMovieByRating(double minRating);
}