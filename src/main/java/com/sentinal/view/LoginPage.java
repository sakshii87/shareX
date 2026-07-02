package com.sentinal.view;

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

import com.sentinal.controller.LoginController;
import com.sentinal.model.LoginModel;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;

public class LoginPage extends Application {
    private Scene loginScene;

    @Override
    public void start(Stage primaryStage) {
        Text titleShare = new Text("Share");
        titleShare.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleShare.setFill(Color.DARKCYAN);

        Text titleX = new Text("X");
        titleX.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleX.setFill(Color.DARKRED);

        HBox titleBox = new HBox(titleShare, titleX);
        titleBox.setAlignment(Pos.CENTER);

        Text subtitle = new Text("Login to your account");
        subtitle.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 18));

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        VBox inputFields = new VBox(10,
                new VBox(new Text("Email:"), emailField),
                new VBox(new Text("Password:"), passwordField));

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please enter email and password.");
                return;
            }

            LoginModel user = new LoginModel("", email, password);
            LoginController controller = new LoginController();

            try {
                boolean loginSuccess = controller.performLogin(user);

                if (loginSuccess) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Login successful!");
                    alert.show();

                    PauseTransition delay = new PauseTransition(Duration.seconds(1));
                    delay.setOnFinished(event -> {
                        alert.close();
                        HomePage homePage = new HomePage();
                        Scene homeScene = homePage.createScene(primaryStage);
                        primaryStage.setScene(homeScene);
                        primaryStage.show();
                    });
                    delay.play();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid email or password.");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Login failed: " + ex.getMessage());
            }
        });

        Label noAccountLabel = new Label("Don't have an account?");
        Hyperlink registerLink = new Hyperlink("Register");
        registerLink.setOnAction(e -> {
            Registration register = new Registration();
            register.start(primaryStage);
        });

        HBox registerBox = new HBox(5, noAccountLabel, registerLink);
        registerBox.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox(20, titleBox, subtitle, inputFields, loginButton, registerBox);
        contentBox.setPadding(new Insets(30));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setMaxWidth(360);
        contentBox.setMaxHeight(450);
        contentBox.setStyle(
                "-fx-border-color: white; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: white;");
        contentBox.setEffect(new DropShadow(10, Color.GRAY));

        VBox centeredWrapper = new VBox(contentBox);
        centeredWrapper.setAlignment(Pos.TOP_CENTER);
        centeredWrapper.setPadding(new Insets(120, 0, 0, 0));

        BorderPane root = new BorderPane(centeredWrapper);

        // Optional background image (uncomment if needed)
        // try {
        // Image bgImage = new
        // Image(getClass().getResource("/assets/images/background.jpeg").toExternalForm());
        // BackgroundImage backgroundImage = new BackgroundImage(
        // bgImage,
        // BackgroundRepeat.NO_REPEAT,
        // BackgroundRepeat.NO_REPEAT,
        // BackgroundPosition.CENTER,
        // new BackgroundSize(100, 100, true, true, true, true)
        // );
        // root.setBackground(new Background(backgroundImage));
        // } catch (Exception e) {
        // root.setStyle("-fx-background-color: linear-gradient(to bottom, #a1c4fd,
        // #c2e9fb);");
        // }

        loginScene = new Scene(root, 1350, 700);

        primaryStage.setTitle("Login - ShareX");
        primaryStage.setScene(loginScene);
        primaryStage.show();

        contentBox.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), contentBox);
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
        return loginScene;
    }
}
