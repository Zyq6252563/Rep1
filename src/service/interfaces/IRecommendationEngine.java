package service.interfaces;

import model.Movie;
import model.User;
import java.util.List;

/**
 * 推荐引擎接口 - 定义电影推荐功能
 * 根据用户的偏好和历史记录生成电影推荐
 */
public interface IRecommendationEngine {
    /**
     * 为用户生成电影推荐列表
     * @param user 用户对象
     * @param topN 返回推荐电影的数量
     * @return 推荐的电影列表
     */
    List<Movie> getRecommendations(User user, int topN);
}