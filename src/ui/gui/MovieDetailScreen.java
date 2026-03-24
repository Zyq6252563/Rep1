package ui.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Movie;
import model.User;
import service.WatchListService;
import service.HistoryService;

/**
 * This class creates a pop-up window to show detailed information about a movie.
 * It also has buttons to add the movie to the watchlist or mark it as watched.
 */
public class MovieDetailScreen {
    private final Movie movie;
    private final User currentUser;
    private final WatchListService watchlistService;
    private final HistoryService historyService;
    private final MainScreen mainScreen;

    /**
     * Constructor to set up the movie detail screen.
     *
     * @param movie the movie to show details for
     * @param currentUser the user who is currently logged in
     * @param watchlistService service to handle watchlist operations
     * @param historyService service to handle history operations
     * @param mainScreen the main screen, used to refresh tabs after changes
     */
    public MovieDetailScreen(Movie movie, User currentUser,
                           WatchListService watchlistService,
                           HistoryService historyService,
                           MainScreen mainScreen) {
        this.movie = movie;
        this.currentUser = currentUser;
        this.watchlistService = watchlistService;
        this.historyService = historyService;
        this.mainScreen = mainScreen;
    }

    /**
     * Creates and shows the movie detail pop-up window.
     */
    public void show() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Movie Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Movie details
        addDetailRow(grid, 0, "ID:", String.valueOf(movie.getId()));
        addDetailRow(grid, 1, "Title:", movie.getTitle());
        addDetailRow(grid, 2, "Genre:", movie.getGenre());
        addDetailRow(grid, 3, "Year:", String.valueOf(movie.getYear()));
        addDetailRow(grid, 4, "Rating:", String.format("%.1f/10.0", movie.getRating()));

        // Action buttons
        Button addToWatchlistButton = new Button("Add to Watchlist");
        addToWatchlistButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    // try to add the movie to the user's watchlist
                    if (watchlistService.addMovieToWatchlist(currentUser, movie.getId())) {
                        showInfoAlert("Success", "Movie added to watchlist!");
                        mainScreen.refreshWatchlistTab();
                    } else {
                        showErrorAlert("Failed", "Movie may already be in watchlist.");
                    }
                } catch (Exception ex) {
                    showErrorAlert("Error", ex.getMessage());
                }
            }
        });

        Button markAsWatchedButton = new Button("Mark as Watched");
        markAsWatchedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    // try to add the movie to the user's history
                    if (historyService.addMovieToHistory(currentUser, movie.getId())) {
                        showInfoAlert("Success", "Movie marked as watched!");
                        // Remove from watchlist if present
                        watchlistService.removeMovieFromWatchlist(currentUser, movie.getId());
                        mainScreen.refreshHistoryTab();
                        mainScreen.refreshWatchlistTab();
                    } else {
                        showErrorAlert("Failed", "Movie may already be in history.");
                    }
                } catch (Exception ex) {
                    showErrorAlert("Error", ex.getMessage());
                }
            }
        });


        Button closeButton = new Button("Close");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialog.close();
            }
        });

        // Button container
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addToWatchlistButton, markAsWatchedButton, closeButton);

        grid.add(buttonBox, 0, 6, 2, 1);

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.showAndWait();  // show the window and wait for it to be closed
    }

    /**
     * Adds a row with a label and its value to the grid.
     *
     * @param grid the grid to add the row to
     * @param row the row number (0-based)
     * @param label the label text (e.g., "Title:")
     * @param value the value to show next to the label
     */
    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelControl = new Label(label);
        labelControl.setStyle("-fx-font-weight: bold"); // make the label bold

        Label valueControl = new Label(value);

        grid.add(labelControl, 0, row);
        grid.add(valueControl, 1, row);
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