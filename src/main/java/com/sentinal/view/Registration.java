package com.sentinal.view;

import com.sentinal.controller.RegistrationController;
import com.sentinal.model.RegistrationModel;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Registration extends Application {
    private Scene signUpScene;

    @Override
    public void start(Stage primaryStage) {
        // Title
        Text shareText = new Text("Share");
        shareText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        shareText.setFill(Color.DARKCYAN);

        Text xText = new Text("X");
        xText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        xText.setFill(Color.DARKRED);

        HBox titleBox = new HBox(shareText, xText);
        titleBox.setAlignment(Pos.CENTER);

        // Subtitle
        Text createAccount = new Text("Create an account");
        createAccount.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 18));

        // Input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        VBox formFields = new VBox(10,
                new VBox(new Text("Name:"), nameField),
                new VBox(new Text("Email:"), emailField),
                new VBox(new Text("Password:"), passwordField),
                new VBox(new Text("Confirm Password:"), confirmPasswordField));

        // Sign Up button
        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        signUpButton.setMaxWidth(Double.MAX_VALUE);

        signUpButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            // RegistrationModel regModel = new RegistrationModel();
            // RegistrationModel user = regModel.new RegistrationModel(name, email,
            // password);
            RegistrationModel user = new RegistrationModel(name, email, password);

            RegistrationController controller = new RegistrationController();

            try {

                int response = controller.registerUser(user);

                if (response == 200) {
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to register. Try again.");
                }

            } catch (Exception ex) {
                // Show validation or duplicate error
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }

            try {
                controller.validateUser(user, confirmPassword);

                int responseCode = controller.registerUser(user);
                if (responseCode == 200) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Registration successful!");
                    alert.show();

                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                    delay.setOnFinished(event -> {
                        alert.close();
                        LoginPage loginPage = new LoginPage();
                        loginPage.start(primaryStage);
                    });
                    delay.play();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to register. Code: " + responseCode);
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });

        // Already have account
        Label alreadyAccount = new Label("Already have an account?");
        Hyperlink signInLink = new Hyperlink("Sign In");
        signInLink.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.start(primaryStage);
        });

        HBox signInBox = new HBox(5, alreadyAccount, signInLink);
        signInBox.setAlignment(Pos.CENTER);

        // Main form
        VBox vbox = new VBox(12, titleBox, createAccount, formFields, signUpButton, signInBox);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxWidth(320);
        vbox.setStyle(
                "-fx-border-color: white; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: white;");
        vbox.setEffect(new DropShadow(10, Color.GRAY));

        VBox centeredWrapper = new VBox(vbox);
        centeredWrapper.setAlignment(Pos.TOP_CENTER);
        centeredWrapper.setPadding(new Insets(120, 0, 0, 0));

        BorderPane root = new BorderPane(centeredWrapper);
        signUpScene = new Scene(root, 1350, 700);

        primaryStage.setTitle("Register - ShareX");
        primaryStage.setScene(signUpScene);
        primaryStage.show();

        // Fade in effect
        vbox.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), vbox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public Scene getScene(Stage stage) {
        return signUpScene;
    }
}
