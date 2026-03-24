package ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.User;
import service.AuthService;
import service.RegistrationService;

/**
 * This class creates the login screen for the movie recommendation system.
 * Users can log in, register a new account, or exit the application.
 */
public class LoginScreen {
    private final AuthService authService;
    private final RegistrationService registrationService;
    private final Stage primaryStage;
    private MainScreen mainScreen;

    /**
     * Constructor to set up the login screen.
     *
     * @param authService service to handle user authentication (login)
     * @param registrationService service to handle user registration
     * @param primaryStage the main window of the application
     * @param mainScreen the main screen, shown after successful login
     */
    public LoginScreen(AuthService authService, RegistrationService registrationService,
                       Stage primaryStage, MainScreen mainScreen) {
        this.authService = authService;
        this.registrationService = registrationService;
        this.primaryStage = primaryStage;
        this.mainScreen = mainScreen;
    }

    /**
     * Creates and shows the login screen.
     */
    public void show() {
        primaryStage.setTitle("Movie Recommendation System - Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome");
        sceneTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        // Button container
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Button exitButton = new Button("Exit");

        buttonBox.getChildren().addAll(loginButton, registerButton, exitButton);
        grid.add(buttonBox, 0, 4, 2, 1);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 0, 6, 2, 1);

        //  Login button event
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String username = userTextField.getText();
                String password = passwordField.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    actionTarget.setText("Please enter username and password.");
                    return;
                }

                try {
                    User user = authService.login(username, password);
                    if (user != null) {
                        actionTarget.setText("");
                        mainScreen.setCurrentUser(user);
                        mainScreen.show();
                    } else {
                        actionTarget.setText("Invalid username or password.");
                    }
                } catch (Exception ex) {
                    actionTarget.setText("Error during login: " + ex.getMessage());
                }
            }
        });

        // Register button event
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showRegisterDialog();
            }
        });

        //  Exit button event
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Exiting application...");
                primaryStage.close();
                System.exit(0);
            }
        });

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(grid);

        Scene scene = new Scene(root, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Shows a dialog for registering a new user.
     */
    private void showRegisterDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Register New User");
        dialog.setHeaderText("Create a new account");

        // Set up buttons
        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        // Create registration form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        //  Add user type selection
        CheckBox premiumCheckBox = new CheckBox("Register as Premium User");
        premiumCheckBox.setSelected(false);

        Label infoLabel = new Label("Premium users: With Unlimited watchlist ");
        infoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666666;");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Confirm:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.add(premiumCheckBox, 0, 3, 2, 1);
        grid.add(infoLabel, 0, 4, 2, 1);

        dialog.getDialogPane().setContent(grid);

        //  Request focus on username field
        usernameField.requestFocus();

        //  Handle registration when the register button is clicked
        dialog.setResultConverter(new javafx.util.Callback<ButtonType, ButtonType>() {
            @Override
            public ButtonType call(ButtonType dialogButton) {
                if (dialogButton == registerButtonType) {
                    String username = usernameField.getText().trim();
                    String password = passwordField.getText();
                    String confirmPassword = confirmPasswordField.getText();
                    boolean isPremium = premiumCheckBox.isSelected();

                    if (username.isEmpty() || password.isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Registration Failed",
                                "Username and password cannot be empty.");
                        return null;
                    }

                    if (!password.equals(confirmPassword)) {
                        showAlert(Alert.AlertType.ERROR, "Registration Failed",
                                "Passwords do not match!");
                        return null;
                    }

                    try {
                        if (registrationService.registerUser(username, password, isPremium)) {
                            String accountType = isPremium ? "Premium" : "Basic";
                            showAlert(Alert.AlertType.INFORMATION, "Registration Successful",
                                    "Account created successfully as " + accountType + " user!\nYou can now login.");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Registration Failed",
                                    "Username already exists or invalid credentials.");
                        }
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Registration Failed",
                                "Error: " + ex.getMessage());
                    }
                }
                return dialogButton;
            }
        });

        dialog.showAndWait();
    }

    /**
     * Shows an alert pop-up.
     *
     * @param alertType the type of alert (ERROR, INFORMATION, etc.)
     * @param title the title of the alert window
     * @param message the message to show
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
