package util;

import model.Movie;
import model.User;
import java.io.*;
import java.util.*;

/**
 * 文件管理类 - 实现IFileManager接口
 * 负责电影和用户数据的文件读写操作
 * 支持CSV格式的数据文件
 */
public class FileManager implements IFileManager {

    /**
     * 从CSV文件加载电影数据
     * 文件格式：ID,标题,类型,年份,评分
     * 第一行为标题行，将被跳过
     *
     * @param filePath 电影数据文件路径
     * @return 加载的电影对象列表
     */
    public List<Movie> loadMovies(String filePath) {
        List<Movie> movies = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;
            boolean firstLine = true;
            // 逐行读取文件
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // 跳过标题行
                if (line.trim().isEmpty()) continue; // 跳过空行

                // 使用逗号分隔，-1参数保留空字符串
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue; // 数据不完整，跳过

                try {
                    // 解析电影数据
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    String genre = parts[2].trim();
                    int year = Integer.parseInt(parts[3].trim());
                    double rating = Double.parseDouble(parts[4].trim());

                    // 创建电影对象并添加到列表
                    Movie movie = new Movie(id, title, genre, year, rating);
                    movies.add(movie);
                } catch (NumberFormatException e) {
                    // 数字格式错误，跳过此行
                    continue;
                }
            }
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        } finally {
            // 关闭文件读取器
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    ExceptionHandler.handleException(e);
                }
            }
        }
        return movies;
    }

    /**
     * 从CSV文件加载用户数据
     * 文件格式：用户名,密码,用户类型,观看列表,历史记录
     * 观看列表和历史记录使用分号分隔电影ID
     * 第一行为标题行，将被跳过
     *
     * @param filePath 用户数据文件路径
     * @param movieMap 电影ID到电影对象的映射，用于构建观看列表和历史记录
     * @return 用户名到用户对象的映射
     */
    public Map<String, User> loadUsers(String filePath, Map<String, Movie> movieMap) {
        Map<String, User> users = new HashMap<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;
            boolean firstLine = true;
            // 逐行读取文件
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // 跳过标题行
                if (line.trim().isEmpty()) continue; // 跳过空行

                // 使用逗号分隔，-1参数保留空字符串
                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue; // 数据不完整，跳过

                // 解析基本用户信息
                String username = parts[0].trim();
                String password = parts[1].trim();
                String userType = parts[2].trim();

                // 根据用户类型创建相应的User对象
                User user;
                if ("premium".equalsIgnoreCase(userType)) {
                    user = new model.PremiumUser(username, password);
                } else {
                    user = new User(username, password);
                }

                // 新格式: username,password,userType,watchlist,history
                if (parts.length >= 5) {
                    // 解析watchlist（索引3）
                    if (!parts[3].trim().isEmpty()) {
                        String watchlistStr = parts[3].trim();
                        // 使用分号分隔多个电影ID
                        String[] watchlistIds = watchlistStr.contains(";") ?
                            watchlistStr.split(";") : new String[]{watchlistStr};
                        // 遍历每个电影ID并添加到观看列表
                        for (int i = 0; i < watchlistIds.length; i++) {
                            String movieId = watchlistIds[i].trim();
                            Movie movie = movieMap.get(movieId);
                            if (movie != null) {
                                user.getWatchlist().addMovie(movie);
                            }
                        }
                    }

                    // 解析history（索引4）
                    if (!parts[4].trim().isEmpty()) {
                        String historyStr = parts[4].trim();
                        // 使用分号分隔多个电影ID
                        String[] historyIds = historyStr.contains(";") ?
                            historyStr.split(";") : new String[]{historyStr};
                        // 遍历每个电影ID并添加到历史记录
                        for (int i = 0; i < historyIds.length; i++) {
                            String movieId = historyIds[i].trim();
                            Movie movie = movieMap.get(movieId);
                            if (movie != null) {
                                user.getHistory().addMovie(movie);
                            }
                        }
                    }
                }

                // 将用户添加到映射中
                users.put(username, user);
            }
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        } finally {
            // 关闭文件读取器
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    ExceptionHandler.handleException(e);
                }
            }
        }
        return users;
    }

    /**
     * 将用户数据保存到CSV文件
     * 文件格式：用户名,密码,用户类型,观看列表,历史记录
     * 观看列表和历史记录中的电影ID使用分号分隔
     * 第一行写入标题行
     *
     * @param users 用户名到用户对象的映射
     * @param filePath 保存用户数据的文件路径
     */
    public void saveUsers(Map<String, User> users, String filePath) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            // 写入标题行
            bw.write("Username,Password,UserType,Watchlist,History");
            bw.newLine();

            // 遍历所有用户
            Iterator<User> iterator = users.values().iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                StringBuilder sb = new StringBuilder();

                // 写入用户名和密码
                sb.append(user.getUsername()).append(",");
                sb.append(user.getPassword()).append(",");

                // 添加用户类型
                if (user.isPremium()) {
                    sb.append("premium");
                } else {
                    sb.append("basic");
                }
                sb.append(",");

                // Watchlist IDs (使用分号分隔)
                List<Movie> watchlist = user.getWatchlist().getMovies();
                for (int i = 0; i < watchlist.size(); i++) {
                    sb.append(watchlist.get(i).getId());
                    if (i < watchlist.size() - 1) sb.append(";");
                }
                sb.append(",");

                // History IDs (使用分号分隔)
                List<Movie> history = user.getHistory().getMovies();
                for (int i = 0; i < history.size(); i++) {
                    sb.append(history.get(i).getId());
                    if (i < history.size() - 1) sb.append(";");
                }

                // 写入用户数据行
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        } finally {
            // 关闭文件写入器
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    ExceptionHandler.handleException(e);
                }
            }
        }
    }
}