package ui;

import model.Movie;
import model.User;
import service.*;
import java.util.List;
import java.util.Scanner;

public class MenuSystem implements iMenuSystem {
    private final AuthService authService;
    private final RegistrationService registrationService;
    private final MovieService movieService;
    private final WatchListService watchlistService;
    private final HistoryService historyService;
    private final RecommendationEngine recommendationEngine;
    private final Scanner scanner;
    private User currentUser;

    public MenuSystem(AuthService authService, RegistrationService registrationService,
                      MovieService movieService, WatchListService watchlistService,
                      HistoryService historyService, RecommendationEngine recommendationEngine) {
        this.authService = authService;
        this.registrationService = registrationService;
        this.movieService = movieService;
        this.watchlistService = watchlistService;
        this.historyService = historyService;
        this.recommendationEngine = recommendationEngine;
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }

    @Override
    public void showMainMenu(User currentUser) {
        this.currentUser = currentUser;

        if (this.currentUser == null) {
            showUnauthenticatedMenu();
        } else {
            showAuthenticatedMenu();
        }
    }

    private void showUnauthenticatedMenu() {
        while (true) {
            System.out.println("\n=== Movie Recommendation System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Please choose an option (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "3":
                    System.out.println("Thank you for using Movie Recommendation System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
    }

    private void showAuthenticatedMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Browse movies");
            System.out.println("2. Add movie to watchlist");
            System.out.println("3. Remove movie from watchlist");
            System.out.println("4. View watchlist");
            System.out.println("5. Mark movie as watched");
            System.out.println("6. View history");
            System.out.println("7. Get recommendations");
            System.out.println("8. Change recommendation strategy");
            System.out.println("9. Change password");
            System.out.println("10. Logout");
            System.out.print("Please choose an option (1-10): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    browseMovies();
                    break;
                case "2":
                    addMovieToWatchlist();
                    break;
                case "3":
                    removeMovieFromWatchlist();
                    break;
                case "4":
                    viewWatchlist();
                    break;
                case "5":
                    markMovieAsWatched();
                    break;
                case "6":
                    viewHistory();
                    break;
                case "7":
                    getRecommendations();
                    break;
                case "8":
                    changeRecommendationStrategy();
                    break;
                case "9":
                    changePassword();
                    break;
                case "10":
                    System.out.println("Logging out...");
                    this.currentUser = null;
                    showUnauthenticatedMenu();
                    return;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 10.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = authService.login(username, password);
            if (user != null) {
                this.currentUser = user;
                System.out.println("Login successful! Welcome, " + user.getUsername());
                showAuthenticatedMenu();
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    private void handleRegister() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter new password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        try {
            if (registrationService.registerUser(username, password)) {
                System.out.println("Registration successful! You can now login.");
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private void changePassword() {
        System.out.print("Enter current password: ");
        String oldPassword = scanner.nextLine().trim();

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        try {
            if (authService.changePassword(currentUser, oldPassword, newPassword)) {
                System.out.println("Password changed successfully!");
            } else {
                System.out.println("Failed to change password.");
            }
        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
        }
    }

    private void changeRecommendationStrategy() {
        System.out.println("\n=== Recommendation Strategies ===");
        System.out.println("1. Genre-based (recommend by favorite genre)");
        System.out.println("2. Rating-based (recommend highest rated)");
        System.out.println("3. Year-based (recommend newest movies)");
        System.out.print("Choose strategy (1-3): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                recommendationEngine.setStrategy(new GenreBasedStrategy());
                System.out.println("Strategy changed to Genre-based.");
                break;
            case "2":
                recommendationEngine.setStrategy(new RatingBasedStrategy());
                System.out.println("Strategy changed to Rating-based.");
                break;
            case "3":
                recommendationEngine.setStrategy(new YearBasedStrategy());
                System.out.println("Strategy changed to Year-based.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void browseMovies() {
        try {
            List<Movie> movies = movieService.getAllMovies();

            if (movies.isEmpty()) {
                System.out.println("No movies.csv available.");
                return;
            }

            System.out.println("\n=== All Movies ===");
            printMovieTable(movies);

        } catch (Exception e) {
            System.out.println("Error browsing movies.csv: " + e.getMessage());
        }
    }

    private void addMovieToWatchlist() {
        try {
            System.out.print("Enter Movie ID to add to watchlist: ");
            String movieId = scanner.nextLine().trim();

            if (watchlistService.addMovieToWatchlist(currentUser, movieId)) {
                System.out.println("Movie added to watchlist successfully.");
            } else {
                System.out.println("Failed to add movie to watchlist. Movie may already be in watchlist.");
            }
        } catch (Exception e) {
            System.out.println("Error adding movie to watchlist: " + e.getMessage());
        }
    }

    private void removeMovieFromWatchlist() {
        try {
            System.out.print("Enter Movie ID to remove from watchlist: ");
            String movieId = scanner.nextLine().trim();

            if (watchlistService.removeMovieFromWatchlist(currentUser, movieId)) {
                System.out.println("Movie removed from watchlist successfully.");
            } else {
                System.out.println("Movie not found in watchlist.");
            }
        } catch (Exception e) {
            System.out.println("Error removing movie from watchlist: " + e.getMessage());
        }
    }

    private void viewWatchlist() {
        try {
            List<Movie> watchlist = watchlistService.getWatchlist(currentUser);

            if (watchlist.isEmpty()) {
                System.out.println("Your watchlist is empty.");
                return;
            }

            System.out.println("\n=== Your Watchlist ===");
            printMovieTable(watchlist);

        } catch (Exception e) {
            System.out.println("Error viewing watchlist: " + e.getMessage());
        }
    }

    private void markMovieAsWatched() {
        try {
            System.out.print("Enter Movie ID to mark as watched: ");
            String movieId = scanner.nextLine().trim();

            if (historyService.addMovieToHistory(currentUser, movieId)) {
                System.out.println("Movie marked as watched successfully.");

                // Remove from watchlist if present
                watchlistService.removeMovieFromWatchlist(currentUser, movieId);
            } else {
                System.out.println("Failed to mark movie as watched. Movie may not exist.");
            }
        } catch (Exception e) {
            System.out.println("Error marking movie as watched: " + e.getMessage());
        }
    }

    private void viewHistory() {
        try {
            List<Movie> history = historyService.getHistory(currentUser);

            if (history.isEmpty()) {
                System.out.println("Your history is empty.");
                return;
            }

            System.out.println("\n=== Your Viewing History ===");
            printMovieTable(history);

        } catch (Exception e) {
            System.out.println("Error viewing history: " + e.getMessage());
        }
    }

    private void getRecommendations() {
        try {
            System.out.print("Enter number of recommendations (N): ");
            int topN = Integer.parseInt(scanner.nextLine().trim());

            List<Movie> recommendations = recommendationEngine.getRecommendations(currentUser, topN);

            if (recommendations.isEmpty()) {
                System.out.println("No recommendations available.");
                return;
            }

            System.out.println("\n=== Recommended Movies ===");
            printMovieTable(recommendations);

        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Please enter a positive integer.");
        } catch (Exception e) {
            System.out.println("Error getting recommendations: " + e.getMessage());
        }
    }

    private void printMovieTable(List<Movie> movies) {
        // Print table header
        System.out.printf("%-8s %-40s %-20s %-8s %-8s%n",
                "ID", "Title", "Genre", "Year", "Rating");
        System.out.println("---------------------------------------------------------------------------");

        // Print movie data
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.printf("%-8d %-40s %-20s %-8d %-8.1f%n",
                    movie.getId(),
                    movie.getTitle().length() > 40 ? movie.getTitle().substring(0, 37) + "..." : movie.getTitle(),
                    movie.getGenre(),
                    movie.getYear(),
                    movie.getRating());
        }
    }
}