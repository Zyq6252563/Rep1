package service.interfaces;

import model.Movie;
import model.User;
import java.util.List;

/**
 * 推荐策略接口 - 定义不同的推荐算法策略
 * 策略模式：允许不同的推荐算法实现
 */
public interface IRecommendationStrategy {
    /**
     * 根据特定策略为用户推荐电影
     * @param user 用户对象
     * @param allMovies 所有可用的电影列表
     * @param topN 返回推荐电影的数量
     * @return 根据该策略推荐的电影列表
     */
    List<Movie> recommend(User user, List<Movie> allMovies, int topN);
}
