import javafx.application.Application;
import javafx.stage.Stage;
import model.Movie;
import model.User;
import service.*;
import ui.gui.LoginScreen;
import ui.gui.MainScreen;
import util.FileManager;
import java.util.*;

public class GUIMain extends Application {
    private static Map<String, User> usersMap;
    private static List<Movie> movies;
    private static String usersFilePath;
    private static FileManager fileManager;

    public static void main(String[] args) {
        System.out.println("=== Movie Recommendation System (GUI Mode) ===");
        System.out.println("Starting system...");

        try {
            // 获取当前工作目录
            String currentDir = System.getProperty("user.dir");
            System.out.println("Current working directory: " + currentDir);

            // 尝试不同的文件路径
            String moviesFilePath = findMoviesFile();
            usersFilePath = findUsersFile();

            System.out.println("Movies file path: " + moviesFilePath);
            System.out.println("Users file path: " + usersFilePath);

            // 初始化文件管理器
            fileManager = new FileManager();

            // 加载电影数据
            try {
                movies = fileManager.loadMovies(moviesFilePath);
                System.out.println("Loaded " + movies.size() + " movies from file.");
            } catch (Exception e) {
                System.err.println("Error loading movies from file: " + e.getMessage());
                System.out.println("Creating default movies data...");
                movies = createDefaultMovies();
            }

            // 创建电影ID到Movie对象的映射
            Map<String, Movie> movieMap = new HashMap<>();
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                movieMap.put(movie.getId(), movie);
            }

            // 加载用户数据
            try {
                usersMap = fileManager.loadUsers(usersFilePath, movieMap);
                System.out.println("Loaded " + usersMap.size() + " users from file.");
            } catch (Exception e) {
                System.err.println("Error loading users from file: " + e.getMessage());
                System.out.println("Creating default user data...");
                usersMap = createDefaultUsers(movieMap);
            }

            // 添加关闭钩子，在程序退出时保存用户数据
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    saveUserData();
                }
            }));

            System.out.println("Launching GUI window...");
            // 启动JavaFX应用
            launch(args);

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("[DEBUG] Starting GUI initialization...");
        try {
            System.out.println("[DEBUG] Creating services...");
            // 创建服务实例
            List<User> usersList = new ArrayList<>(usersMap.values());
            System.out.println("[DEBUG] Users loaded: " + usersList.size());

            MovieService movieService = new MovieService(movies);
            System.out.println("[DEBUG] MovieService created");

            AuthService authService = new AuthService(usersList);
            System.out.println("[DEBUG] AuthService created");

            RegistrationService registrationService = new RegistrationService(usersList, usersMap);
            System.out.println("[DEBUG] RegistrationService created");

            WatchListService watchlistService = new WatchListService(movieService);
            HistoryService historyService = new HistoryService(movieService);
            RecommendationEngine recommendationEngine = new RecommendationEngine(movieService, historyService);
            System.out.println("[DEBUG] All services created successfully");

            System.out.println("[DEBUG] Creating GUI screens...");
            // 创建GUI组件（先创建MainScreen，再创建LoginScreen以避免null引用）
            MainScreen mainScreen = new MainScreen(primaryStage, movieService,
                    watchlistService, historyService, recommendationEngine, authService, null);
            System.out.println("[DEBUG] MainScreen created");

            LoginScreen loginScreen = new LoginScreen(authService, registrationService,
                    primaryStage, mainScreen);
            System.out.println("[DEBUG] LoginScreen created");

            // 设置MainScreen的loginScreen引用（修复循环依赖）
            mainScreen.setLoginScreen(loginScreen);

            System.out.println("[DEBUG] Configuring primary stage...");
            // 配置主窗口
            primaryStage.setTitle("Movie Recommendation System");
            primaryStage.setWidth(900);
            primaryStage.setHeight(600);

            // 防止窗口关闭时程序退出
            primaryStage.setOnCloseRequest(new javafx.event.EventHandler<javafx.stage.WindowEvent>() {
                @Override
                public void handle(javafx.stage.WindowEvent event) {
                    System.out.println("Window closing...");
                    saveUserData();
                    System.exit(0);
                }
            });

            System.out.println("[DEBUG] Showing login screen...");
            // 显示登录界面
            loginScreen.show();

            System.out.println("GUI window opened successfully!");

        } catch (Exception e) {
            System.err.println("Error initializing GUI: " + e.getMessage());
            e.printStackTrace();
            // 显示错误对话框
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Startup Error");
            alert.setHeaderText("Failed to initialize application");
            alert.setContentText("Error: " + e.getMessage() + "\n\nCheck console for details.");
            alert.showAndWait();
        }
    }

    public static void saveUserData() {
        if (usersMap != null && fileManager != null && usersFilePath != null) {
            try {
                System.out.println("\nSaving user data...");
                fileManager.saveUsers(usersMap, usersFilePath);
                System.out.println("User data saved successfully.");
            } catch (Exception e) {
                System.err.println("Error saving user data: " + e.getMessage());
            }
        }
    }

    // 尝试查找电影文件
    private static String findMoviesFile() {
        String[] possiblePaths = {
                "data/movies.csv",
                "./data/movies.csv",
                "../data/movies.csv",
                "movies.csv",
                "src/data/movies.csv"
        };

        for (int i = 0; i < possiblePaths.length; i++) {
            java.io.File file = new java.io.File(possiblePaths[i]);
            if (file.exists() && file.isFile()) {
                return possiblePaths[i];
            }
        }

        // 如果没有找到文件，返回默认路径
        return "src/data/movies.csv";
    }

    // 尝试查找用户文件
    private static String findUsersFile() {
        String[] possiblePaths = {
                "data/users.csv",
                "./data/users.csv",
                "../data/users.csv",
                "users.csv",
                "src/data/users.csv"
        };

        for (int i = 0; i < possiblePaths.length; i++) {
            java.io.File file = new java.io.File(possiblePaths[i]);
            if (file.exists() && file.isFile()) {
                return possiblePaths[i];
            }
        }

        // 如果没有找到文件，返回默认路径
        return "src/data/users.csv";
    }

    private static List<Movie> createDefaultMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("M001", "The Shawshank Redemption", "Drama", 1994, 9.3));
        movies.add(new Movie("M002", "The Godfather", "Crime", 1972, 9.2));
        movies.add(new Movie("M003", "The Dark Knight", "Action", 2008, 9.0));
        movies.add(new Movie("M004", "Pulp Fiction", "Crime", 1994, 8.9));
        movies.add(new Movie("M005", "Forrest Gump", "Drama", 1994, 8.8));
        movies.add(new Movie("M006", "Inception", "Sci-Fi", 2010, 8.8));
        movies.add(new Movie("M007", "Interstellar", "Sci-Fi", 2014, 8.6));
        movies.add(new Movie("M008", "Parasite", "Drama", 2019, 8.6));
        movies.add(new Movie("M009", "Joker", "Drama", 2019, 8.4));
        movies.add(new Movie("M010", "Avengers: Endgame", "Action", 2019, 8.4));
        return movies;
    }

    private static Map<String, User> createDefaultUsers(Map<String, Movie> movieMap) {
        Map<String, User> users = new HashMap<>();

        // 创建默认用户
        User user1 = new User("john_doe", "password123");
        if (movieMap.get("M001") != null) user1.getWatchlist().addMovie(movieMap.get("M001"));
        if (movieMap.get("M003") != null) user1.getWatchlist().addMovie(movieMap.get("M003"));
        if (movieMap.get("M002") != null) user1.getHistory().addMovie(movieMap.get("M002"));
        if (movieMap.get("M004") != null) user1.getHistory().addMovie(movieMap.get("M004"));
        users.put(user1.getUsername(), user1);

        User user2 = new User("jane_smith", "secure456");
        if (movieMap.get("M005") != null) user2.getWatchlist().addMovie(movieMap.get("M005"));
        if (movieMap.get("M001") != null) user2.getHistory().addMovie(movieMap.get("M001"));
        if (movieMap.get("M003") != null) user2.getHistory().addMovie(movieMap.get("M003"));
        users.put(user2.getUsername(), user2);

        User user3 = new User("test", "test123");
        users.put(user3.getUsername(), user3);

        return users;
    }
}
