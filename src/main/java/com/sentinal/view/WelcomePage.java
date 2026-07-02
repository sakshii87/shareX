package com.sentinal.view;
// import com.sentinal.view.FooterView;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomePage extends Application {

    @Override
    public void start(Stage stage) {
        Image bgImage = new Image(getClass().getResource("/assets/Images/Project_icon.jpg").toExternalForm());
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setPreserveRatio(false);

        ScaleTransition zoom = new ScaleTransition(Duration.seconds(10), bgImageView);
        zoom.setFromX(1.0);
        zoom.setFromY(1.0);
        zoom.setToX(1.1);
        zoom.setToY(1.1);
        zoom.setCycleCount(Animation.INDEFINITE);
        zoom.setAutoReverse(false);
        zoom.play();

        Text welcomeText = new Text("");
        welcomeText.setStyle("-fx-fill: #ffffff; -fx-font-size: 48px; -fx-font-weight: bold;");
        String message = "Welcome to ShareX";

        Timeline typingTimeline = new Timeline();
        for (int i = 0; i < message.length(); i++) {
            final int index = i;
            KeyFrame kf = new KeyFrame(Duration.millis(100 * i), e -> {
                welcomeText.setText(message.substring(0, index + 1));
            });
            typingTimeline.getKeyFrames().add(kf);
        }
        typingTimeline.play();

        // Sign in & Sign up Buttons
        Button signInBtn = new Button("Sign In");
        Button signUpBtn = new Button("Sign Up");

        String buttonStyle = "-fx-background-color: #6addc0ff; -fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 10px 30px;";
        signInBtn.setStyle(buttonStyle);
        signUpBtn.setStyle(buttonStyle);

        signUpBtn.setOnAction(e -> {
            Registration registrationPage = new Registration();
            registrationPage.start(stage);

        });
        signInBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.start(stage);

        });

        // Button aboutBtn = new Button("About Us");
        // aboutBtn.setOnAction(e -> {
        //     Scene currentScene = stage.getScene(); // Save current scene
        //     AboutUsPage aboutPage = new AboutUsPage(stage, currentScene);
        //     stage.setScene(aboutPage.getScene());
        // });

        HBox buttonBox = new HBox(20, signInBtn, signUpBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox bottomBox = new VBox(buttonBox);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.setPadding(new Insets(0, 0, 100, 0));

        VBox topBox = new VBox(welcomeText);
        topBox.setAlignment(Pos.TOP_CENTER);
        topBox.setPadding(new Insets(80, 0, 0, 0));

        BorderPane layout = new BorderPane();
        layout.setTop(topBox);
        layout.setBottom(bottomBox);

        VBox footerWrapper = new VBox(new FooterView(stage));
        footerWrapper.setAlignment(Pos.BOTTOM_CENTER);
        // footerWrapper.setPadding(new Insets(10));
        footerWrapper.setPickOnBounds(false);

        StackPane root = new StackPane(bgImageView, layout);

        Scene scene = new Scene(root, 1200, 800);
        bgImageView.fitWidthProperty().bind(scene.widthProperty());
        bgImageView.fitHeightProperty().bind(scene.heightProperty());

        stage.setTitle("Welcome to ShareX");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

}
