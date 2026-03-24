package util;

import model.Movie;
import model.User;
import java.util.List;
import java.util.Map;

/**
 * 文件管理接口 - 定义文件操作的标准方法
 * 包括加载电影、加载用户和保存用户数据
 */
public interface IFileManager {
    /**
     * 从文件加载电影数据
     * @param filePath 电影数据文件路径
     * @return 电影对象列表
     */
    List<Movie> loadMovies(String filePath);

    /**
     * 从文件加载用户数据
     * @param filePath 用户数据文件路径
     * @param movieMap 电影ID到电影对象的映射，用于关联用户的观看列表和历史记录
     * @return 用户名到用户对象的映射
     */
    Map<String, User> loadUsers(String filePath, Map<String, Movie> movieMap);

    /**
     * 将用户数据保存到文件
     * @param users 用户名到用户对象的映射
     * @param filePath 保存用户数据的文件路径
     */
    void saveUsers(Map<String, User> users, String filePath);
}