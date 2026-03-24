package service.interfaces;

import model.Movie;
import model.User;
import java.util.List;

/**
 * 历史记录服务接口 - 定义观看历史管理操作
 * 包括添加电影到历史记录和获取历史记录
 */
public interface IHistoryService {
    /**
     * 添加电影到用户的观看历史记录
     * @param user 用户对象
     * @param movieId 要添加的电影ID
     * @return 如果添加成功返回true，否则返回false
     */
    boolean addMovieToHistory(User user, int movieId);

    /**
     * 获取用户的观看历史记录
     * @param user 用户对象
     * @return 用户观看过的电影列表
     */
    List<Movie> getHistory(User user);
}