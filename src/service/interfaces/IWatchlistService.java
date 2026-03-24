package service.interfaces;

import model.Movie;
import model.User;
import java.util.List;

/**
 * 观看列表服务接口 - 定义观看列表管理操作
 * 包括添加电影到观看列表、从观看列表删除电影和获取观看列表
 */
public interface IWatchlistService {
    /**
     * 添加电影到用户的观看列表
     * @param user 用户对象
     * @param movieId 要添加的电影ID
     * @return 如果添加成功返回true，否则返回false
     */
    boolean addMovieToWatchlist(User user, int movieId);

    /**
     * 从用户的观看列表中删除电影
     * @param user 用户对象
     * @param movieId 要删除的电影ID
     * @return 如果删除成功返回true，否则返回false
     */
    boolean removeMovieFromWatchlist(User user, int movieId);

    /**
     * 获取用户的观看列表
     * @param user 用户对象
     * @return 用户的观看列表中的电影列表
     */
    List<Movie> getWatchlist(User user);
}