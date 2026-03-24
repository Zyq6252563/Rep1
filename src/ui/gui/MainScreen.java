package ui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Movie;
import model.User;
import service.MovieService;
import service.WatchListService;
import service.HistoryService;
import service.RecommendationEngine;
import service.AuthService;
import service.GenreBasedStrategy;
import service.RatingBasedStrategy;
import service.YearBasedStrategy;

import java.util.List;

/**
 * This class creates the main screen of the application, which has multiple tabs:
 * Browse Movies, My Watchlist, Viewing History, Recommendations, and Settings.
 */
public class MainScreen {
    private final Stage primaryStage;
    private final MovieService movieService;
    private final WatchListService watchlistService;
    private final HistoryService historyService;
    private final RecommendationEngine recommendationEngine;
    private final AuthService authService;
    private LoginScreen loginScreen;

    private User currentUser;
    private TabPane tabPane;

    /**
     * Constructor to set up the main screen.
     *
     * @param primaryStage the main window of the application
     * @param movieService service to handle movie data
     * @param watchlistService service to handle watchlist operations
     * @param historyService service to handle history operations
     * @param recommendationEngine engine to generate movie recommendations
     * @param authService service to handle user authentication
     * @param loginScreen the login screen, used to go back to login
     */
    public MainScreen(Stage primaryStage, MovieService movieService,
                      WatchListService watchlistService, HistoryService historyService,
                      RecommendationEngine recommendationEngine, AuthService authService,
                      LoginScreen loginScreen) {
        this.primaryStage = primaryStage;
        this.movieService = movieService;
        this.watchlistService = watchlistService;
        this.historyService = historyService;
        this.recommendationEngine = recommendationEngine;
        this.authService = authService;
        this.loginScreen = loginScreen;
    }

    /**
     * Sets the login screen (used to fix circular dependency).
     *
     * @param loginScreen the login screen
     */
    public void setLoginScreen(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user the user who is logged in
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Creates and shows the main screen with tabs.
     */
    public void show() {
        //  Set title, showing user type (Premium or Basic)
        String userTypeLabel = currentUser.isPremium() ? " [PREMIUM]" : " [BASIC]";
        primaryStage.setTitle("Movie Recommendation System - Welcome, " + currentUser.getUsername() + userTypeLabel);

        // Create tab pane
        tabPane = new TabPane();

        // Create tabs
        Tab browseTab = createBrowseTab();
        Tab watchlistTab = createWatchlistTab();
        Tab historyTab = createHistoryTab();
        Tab recommendationsTab = createRecommendationsTab();
        Tab settingsTab = createSettingsTab();

        tabPane.getTabs().addAll(browseTab, watchlistTab, historyTab, recommendationsTab, settingsTab);

        // Load watchlist and history data
        refreshWatchlistTab();
        refreshHistoryTab();

        // Add logout button and user info in the top
        Label userInfoLabel = new Label("User: " + currentUser.getUsername() + " | Type: " +
            (currentUser.isPremium() ? "PREMIUM" : "BASIC"));
        userInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                logout();
            }
        });

        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.getChildren().addAll(userInfoLabel, logoutButton);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        root.setTop(topBar);

        Scene scene = new Scene(root, 900, 600);

        // Apply theme (different for Premium and Basic users)
        applyTheme(scene, root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Applies a theme (dark for Premium, light for Basic) to the scene.
     *
     * @param scene the scene to apply the theme to
     * @param root the root node of the scene
     */
    private void applyTheme(Scene scene, BorderPane root) {
        if (currentUser.isPremium()) {
            // Premium user: dark theme with white text
            root.setStyle("-fx-background-color: #1a1a1a;");
            tabPane.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #1a1a1a;");

            // Recursively apply dark theme to all child nodes
            applyPremiumStyleToNode(root);
            applyPremiumStyleToNode(tabPane);
        } else {
            // Basic user: light theme with black text (default)
            root.setStyle("-fx-background-color: white;");
            tabPane.setStyle("-fx-background-color: white;");

            // Recursively apply light theme to all child nodes
            applyBasicStyleToNode(root);
            applyBasicStyleToNode(tabPane);
        }
    }

    /**
     * Recursively applies the premium (dark) style to a node and its children.
     *
     * @param node the node to apply the style to
     */
    private void applyPremiumStyleToNode(javafx.scene.Node node) {
        if (node == null) return;

        // Set styles for different types of controls
        if (node instanceof Label) {
            node.setStyle("-fx-text-fill: white;");
        } else if (node instanceof javafx.scene.text.Text) {
            javafx.scene.text.Text text = (javafx.scene.text.Text) node;
            text.setFill(javafx.scene.paint.Color.WHITE);
        } else if (node instanceof Button) {
            node.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white; -fx-border-color: #5a5a5a;");
        } else if (node instanceof TextField) {
            node.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-prompt-text-fill: #999999;");
        } else if (node instanceof TextArea) {
            node.setStyle("-fx-control-inner-background: #2a2a2a; -fx-text-fill: white; -fx-background-color: #2a2a2a;");
        } else if (node instanceof TableView) {
            TableView tableView = (TableView) node;
            tableView.setStyle(
                "-fx-background-color: #2a2a2a; " +
                "-fx-control-inner-background: #2a2a2a; " +
                "-fx-control-inner-background-alt: #252525; " +
                "-fx-table-cell-border-color: #3a3a3a; " +
                "-fx-text-fill: white; " +
                "-fx-text-background-color: white;"
            );
            // Set white text for TableView columns
            tableView.setStyle(tableView.getStyle() +
                "-fx-table-column-border-color: #3a3a3a;");
        } else if (node instanceof ComboBox) {
            node.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white;");
        } else if (node instanceof TabPane) {
            TabPane tabPane = (TabPane) node;
            node.setStyle("-fx-background-color: #1a1a1a;");
            // Set style for each Tab
            for (Tab tab : tabPane.getTabs()) {
                tab.setStyle("-fx-background-color: #2a2a2a;");
            }
        } else if (node instanceof BorderPane || node instanceof VBox || node instanceof HBox) {
            node.setStyle("-fx-background-color: #1a1a1a;");
        } else if (node instanceof javafx.scene.layout.GridPane) {
            node.setStyle("-fx-background-color: #1a1a1a;");
        }

        // Recursively process child nodes
        if (node instanceof javafx.scene.Parent) {
            javafx.scene.Parent parent = (javafx.scene.Parent) node;
            for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                applyPremiumStyleToNode(child);
            }
        }
    }

    /**
     * Recursively applies the basic (light) style to a node and its children.
     *
     * @param node the node to apply the style to
     */
    private void applyBasicStyleToNode(javafx.scene.Node node) {
        if (node == null) return;

        // Set styles for different types of controls
        if (node instanceof Label) {
            node.setStyle("-fx-text-fill: black;");
        } else if (node instanceof javafx.scene.text.Text) {
            javafx.scene.text.Text text = (javafx.scene.text.Text) node;
            text.setFill(javafx.scene.paint.Color.BLACK);
        } else if (node instanceof Button) {
            node.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: black;");
        } else if (node instanceof TextField) {
            node.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        } else if (node instanceof TextArea) {
            node.setStyle("-fx-control-inner-background: white; -fx-text-fill: black; -fx-background-color: white;");
        } else if (node instanceof TableView) {
            node.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        } else if (node instanceof ComboBox) {
            node.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        } else if (node instanceof BorderPane || node instanceof VBox || node instanceof HBox) {
            node.setStyle("-fx-background-color: white;");
        } else if (node instanceof javafx.scene.layout.GridPane) {
            node.setStyle("-fx-background-color: white;");
        }

        // Recursively process child nodes
        if (node instanceof javafx.scene.Parent) {
            javafx.scene.Parent parent = (javafx.scene.Parent) node;
            for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                applyBasicStyleToNode(child);
            }
        }
    }

    /**
     * Creates the "Browse Movies" tab, which shows a table of all movies.
     *
     * @return the created tab
     */
    private Tab createBrowseTab() {
        Tab tab = new Tab("Browse Movies");
        tab.setClosable(false);

        TableView<Movie> tableView = createMovieTableView();

        try {
            List<Movie> movies = movieService.getAllMovies();
            ObservableList<Movie> movieData = FXCollections.observableArrayList(movies);
            tableView.setItems(movieData);
        } catch (Exception e) {
            showErrorAlert("Error loading movies.csv", e.getMessage());
        }

        BorderPane pane = new BorderPane();
        pane.setCenter(tableView);
        tab.setContent(pane);

        return tab;
    }

    /**
     * Creates the "My Watchlist" tab, which shows the user's watchlist.
     *
     * @return the created tab
     */
    private Tab createWatchlistTab() {
        Tab tab = new Tab("My Watchlist");
        tab.setClosable(false);

        TableView<Movie> tableView = createMovieTableView();

        // Add action buttons
        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Movie selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        if (watchlistService.removeMovieFromWatchlist(currentUser, selected.getId())) {
                            refreshWatchlistTab();
                        }
                    } catch (Exception ex) {
                        showErrorAlert("Error removing movie", ex.getMessage());
                    }
                }
            }
        });

        BorderPane pane = new BorderPane();
        pane.setCenter(tableView);
        pane.setBottom(removeButton);
        BorderPane.setAlignment(removeButton, javafx.geometry.Pos.CENTER);

        tab.setContent(pane);

        return tab;
    }

    /**
     * Creates the "Viewing History" tab, which shows the user's watched movies.
     *
     * @return the created tab
     */
    private Tab createHistoryTab() {
        Tab tab = new Tab("Viewing History");
        tab.setClosable(false);

        TableView<Movie> tableView = createMovieTableView();

        BorderPane pane = new BorderPane();
        pane.setCenter(tableView);
        tab.setContent(pane);

        return tab;
    }

    /**
     * Creates the "Recommendations" tab, which shows movie recommendations.
     *
     * @return the created tab
     */
    private Tab createRecommendationsTab() {
        Tab tab = new Tab("Recommendations");
        tab.setClosable(false);

        TableView<Movie> tableView = createMovieTableView();

        // Controls for recommendations
        Label countLabel = new Label("Number of recommendations:");

        // Use TextField instead of Spinner for better compatibility
        TextField countField = new TextField("5");
        countField.setPrefWidth(50);

        Button refreshButton = new Button("Refresh");

        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    int topN = 5; // default value
                    try {
                        topN = Integer.parseInt(countField.getText().trim());
                        if (topN < 1) topN = 1;
                        if (topN > 50) topN = 50; // set upper limit
                    } catch (NumberFormatException ex) {
                        showErrorAlert("Invalid number", "Please enter a valid number between 1 and 50");
                        return;
                    }

                    List<Movie> recommendations = recommendationEngine.getRecommendations(
                            currentUser, topN);
                    ObservableList<Movie> recommendationData = FXCollections.observableArrayList(recommendations);
                    tableView.setItems(recommendationData);
                } catch (Exception ex) {
                    showErrorAlert("Error getting recommendations", ex.getMessage());
                }
            }
        });

        // Create HBox for controls
        HBox controls = new HBox();
        controls.setSpacing(10);
        controls.setAlignment(javafx.geometry.Pos.CENTER);
        controls.getChildren().addAll(countLabel, countField, refreshButton);

        BorderPane pane = new BorderPane();
        pane.setCenter(tableView);
        pane.setTop(controls);

        tab.setContent(pane);

        // Load initial recommendations
        refreshButton.fire();

        return tab;
    }

    /**
     * Creates the "Settings" tab, which has account settings and recommendation strategy settings.
     *
     * @return the created tab
     */
    private Tab createSettingsTab() {
        Tab tab = new Tab("Settings");
        tab.setClosable(false);

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);

        // Title
        Label titleLabel = new Label("Account & System Settings");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Separator
        Separator separator1 = new Separator();

        // Change password section
        Label passwordSectionLabel = new Label("Change Password");
        passwordSectionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        GridPane passwordGrid = new GridPane();
        passwordGrid.setHgap(10);
        passwordGrid.setVgap(10);
        passwordGrid.setAlignment(Pos.CENTER);

        Label oldPasswordLabel = new Label("Current Password:");
        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Enter current password");

        Label newPasswordLabel = new Label("New Password:");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");

        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");

        Button changePasswordButton = new Button("Change Password");

        passwordGrid.add(oldPasswordLabel, 0, 0);
        passwordGrid.add(oldPasswordField, 1, 0);
        passwordGrid.add(newPasswordLabel, 0, 1);
        passwordGrid.add(newPasswordField, 1, 1);
        passwordGrid.add(confirmPasswordLabel, 0, 2);
        passwordGrid.add(confirmPasswordField, 1, 2);
        passwordGrid.add(changePasswordButton, 1, 3);

        // Change password button event
        changePasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String oldPassword = oldPasswordField.getText();
                String newPassword = newPasswordField.getText();
                String confirmPassword = confirmPasswordField.getText();

                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    showErrorAlert("Change Password Failed", "All fields are required.");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    showErrorAlert("Change Password Failed", "New passwords do not match!");
                    return;
                }

                try {
                    if (authService.changePassword(currentUser, oldPassword, newPassword)) {
                        showInfoAlert("Success", "Password changed successfully!");
                        oldPasswordField.clear();
                        newPasswordField.clear();
                        confirmPasswordField.clear();
                    } else {
                        showErrorAlert("Change Password Failed", "Current password is incorrect.");
                    }
                } catch (Exception ex) {
                    showErrorAlert("Change Password Failed", "Error: " + ex.getMessage());
                }
            }
        });

        // Separator
        Separator separator2 = new Separator();

        // Recommendation strategy section
        Label strategySectionLabel = new Label("Recommendation Strategy");
        strategySectionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        VBox strategyBox = new VBox(10);
        strategyBox.setAlignment(Pos.CENTER);

        Label strategyDescLabel = new Label("Choose how movies are recommended to you:");

        ToggleGroup strategyGroup = new ToggleGroup();

        RadioButton genreRadio = new RadioButton("Genre-based (by favorite genres)");
        genreRadio.setToggleGroup(strategyGroup);
        genreRadio.setSelected(true);

        RadioButton ratingRadio = new RadioButton("Rating-based (highest rated first)");
        ratingRadio.setToggleGroup(strategyGroup);

        RadioButton yearRadio = new RadioButton("Year-based (newest movies first)");
        yearRadio.setToggleGroup(strategyGroup);

        Button applyStrategyButton = new Button("Apply Strategy");

        // Apply strategy button event
        applyStrategyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (genreRadio.isSelected()) {
                    recommendationEngine.setStrategy(new GenreBasedStrategy());
                    showInfoAlert("Strategy Changed", "Recommendation strategy set to Genre-based.");
                } else if (ratingRadio.isSelected()) {
                    recommendationEngine.setStrategy(new RatingBasedStrategy());
                    showInfoAlert("Strategy Changed", "Recommendation strategy set to Rating-based.");
                } else if (yearRadio.isSelected()) {
                    recommendationEngine.setStrategy(new YearBasedStrategy());
                    showInfoAlert("Strategy Changed", "Recommendation strategy set to Year-based.");
                }
            }
        });

        strategyBox.getChildren().addAll(
                strategyDescLabel,
                genreRadio,
                ratingRadio,
                yearRadio,
                applyStrategyButton
        );

        // Add all components to the main container
        content.getChildren().addAll(
                titleLabel,
                separator1,
                passwordSectionLabel,
                passwordGrid,
                separator2,
                strategySectionLabel,
                strategyBox
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        tab.setContent(scrollPane);
        return tab;
    }

    /**
     * Creates a table view for displaying movies.
     *
     * @return the created table view
     */
    private TableView<Movie> createMovieTableView() {
        TableView<Movie> tableView = new TableView<>();

        TableColumn<Movie, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<Movie, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(250);

        TableColumn<Movie, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(150);

        TableColumn<Movie, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);

        TableColumn<Movie, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.setPrefWidth(80);

        tableView.getColumns().addAll(idCol, titleCol, genreCol, yearCol, ratingCol);

        // Double-click to show details
        tableView.setRowFactory(new Callback<TableView<Movie>, TableRow<Movie>>() {
            @Override
            public TableRow<Movie> call(TableView<Movie> tv) {
                TableRow<Movie> row = new TableRow<Movie>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            Movie movie = row.getItem();
                            showMovieDetail(movie);
                        }
                    }
                });
                return row;
            }
        });

        return tableView;
    }

    /**
     * Refreshes the watchlist tab by reloading the watchlist data.
     */
    public void refreshWatchlistTab() {
        Tab watchlistTab = tabPane.getTabs().get(1);
        BorderPane pane = (BorderPane) watchlistTab.getContent();
        TableView<Movie> tableView = (TableView<Movie>) pane.getCenter();

        try {
            List<Movie> watchlist = watchlistService.getWatchlist(currentUser);
            ObservableList<Movie> watchlistData = FXCollections.observableArrayList(watchlist);
            tableView.setItems(watchlistData);
        } catch (Exception e) {
            showErrorAlert("Error loading watchlist", e.getMessage());
        }
    }

    /**
     * Refreshes the history tab by reloading the history data.
     */
    public void refreshHistoryTab() {
        Tab historyTab = tabPane.getTabs().get(2);
        BorderPane pane = (BorderPane) historyTab.getContent();
        TableView<Movie> tableView = (TableView<Movie>) pane.getCenter();

        try {
            List<Movie> history = historyService.getHistory(currentUser);
            ObservableList<Movie> historyData = FXCollections.observableArrayList(history);
            tableView.setItems(historyData);
        } catch (Exception e) {
            showErrorAlert("Error loading history", e.getMessage());
        }
    }

     /**
     * Shows the movie detail pop-up for a given movie.
     *
     * @param movie the movie to show details for
     */
    private void showMovieDetail(Movie movie) {
        MovieDetailScreen detailScreen = new MovieDetailScreen(
                movie, currentUser, watchlistService, historyService, this);
        detailScreen.show();
    }

    /**
     * Logs out the current user and goes back to the login screen.
     */
    private void logout() {
        currentUser = null;
        loginScreen.show();
    }

    /**
     * Shows an error alert pop-up.
     *
     * @param title the title of the alert window
     * @param message the error message to show
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information alert pop-up.
     *
     * @param title the title of the alert window
     * @param message the information message to show
     */
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}