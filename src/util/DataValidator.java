package util;

import model.Movie;
import model.User;
import java.util.Map;

/**
 * 数据验证器类 - 验证各种数据的有效性
 * 提供电影、用户、用户名、密码等数据的验证方法
 */
public class DataValidator {

    /**
     * 验证电影对象是否有效
     * @param movie 要验证的电影对象
     * @return 如果电影有效返回true，否则返回false
     */
    public static boolean isValidMovie(Movie movie) {
        if (movie == null) return false;
        if (movie.getId() == null || movie.getId().trim().isEmpty()) return false;
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) return false;
        if (movie.getGenre() == null || movie.getGenre().trim().isEmpty()) return false;
        if (movie.getYear() < 1900 || movie.getYear() > 2100) return false;
        double rating = movie.getRating();
        return rating >= 0.0 && rating <= 10.0;
    }

    /**
     * 验证用户对象是否有效
     * @param user 要验证的用户对象
     * @return 如果用户有效返回true，否则返回false
     */
    public static boolean isValidUser(User user) {
        if (user == null) return false;
        String username = user.getUsername();
        if (username == null || username.trim().isEmpty()) return false;
        String password = user.getPassword();
        return password != null && !password.trim().isEmpty();
    }

    /**
     * 验证用户名是否有效
     * 用户名必须至少3个字符，且只能包含字母、数字和下划线
     * @param username 要验证的用户名
     * @return 如果用户名有效返回true，否则返回false
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * 验证密码是否有效
     * 密码必须至少6个字符
     * @param password 要验证的密码
     * @return 如果密码有效返回true，否则返回false
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * 验证电影ID是否有效
     * @param movieId 要验证的电影ID
     * @return 如果电影ID有效返回true，否则返回false
     */
    public static boolean isValidMovieId(String movieId) {
        return movieId != null && !movieId.trim().isEmpty();
    }
}