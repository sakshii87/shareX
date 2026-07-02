package com.sentinal.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import com.sentinal.model.TransferProgress;

public class SenderProgressBar {


    private Stage primaryStage;
    private TransferProgress progressModel;

    private ProgressBar progressBar;
    private Label percentage;
    private Label details;
    private Label receivingFile;
    private Label dots;

    private Button cancelButton;
    private Button homeButton;

    private String receiverIP;

    public SenderProgressBar(Stage stage, TransferProgress model, String ip) {
        this.primaryStage = stage;
        this.progressModel = model;
        this.receiverIP = ip;
    }

    public Scene getScene() {
        // Title and connection info
        Text title = new Text("SHAREX by Sentinal3");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setFill(Color.DARKBLUE);

        Label connectionStatus = new Label("Connected to Receiver: " + receiverIP);
        connectionStatus.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        connectionStatus.setTextFill(Color.web("#2c3e50"));
        connectionStatus.setStyle(
                "-fx-background-color: #d6eaf8; " +
                        "-fx-padding: 10 15 10 15; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-color: #3498db; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 6;");

        // Transfer status text with dots animation
        receivingFile = new Label("Sending File");
        receivingFile.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        receivingFile.setTextFill(Color.web("#34495e"));

        dots = new Label(".");
        dots.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        dots.setTextFill(Color.web("#e67e22"));

        Timeline dotTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> dots.setText(".")),
                new KeyFrame(Duration.seconds(0.5), e -> dots.setText("..")),
                new KeyFrame(Duration.seconds(1), e -> dots.setText("...")),
                new KeyFrame(Duration.seconds(1.5), e -> dots.setText("....")));
        dotTimeline.setCycleCount(Timeline.INDEFINITE);
        dotTimeline.play();

        HBox animatedStatus = new HBox(receivingFile, dots);
        animatedStatus.setAlignment(Pos.CENTER);
        animatedStatus.setSpacing(5);

        // Progress bar + percentage
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        percentage = new Label("0%");
        percentage.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        progressBar.progressProperty().bind(progressModel.progressProperty());
        percentage.textProperty().bind(progressModel.percentageTextProperty());

        // Transfer info
        details = new Label();
        details.setFont(Font.font("Arial", 14));
        details.setPadding(new Insets(10, 0, 10, 0));
        details.textProperty().bind(Bindings.concat(
                progressModel.sizeTextProperty(), "\n",
                "Speed: ", progressModel.speedTextProperty(), "\n",
                "Time Remaining: ", progressModel.timeRemainingTextProperty(), "\n",
                progressModel.fileCountTextProperty()));

        // Cancel Transfer Button
        cancelButton = new Button("Cancel Transfer");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight:bold;");
        cancelButton.setOnAction(e -> showCancelConfirmation());

        // Go to Homepage Button
        homeButton = new Button("Go to Homepage");
        homeButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight:bold;");
        homeButton.setDisable(true); // Initially disabled
        homeButton.setOnAction(e -> {
            HomePage homePage = new HomePage();
            Scene homeScene = homePage.createScene(primaryStage);
            primaryStage.setScene(homeScene);
        });

        // Detect transfer completion and update UI
        progressModel.progressProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() >= 1.0) {
                receivingFile.setText("Transfer Completed ✅");
                dots.setText("");
                // cancelButton.setVisible(false); // Hide cancel
                homeButton.setDisable(false); // Enable home
            }
        });

        // Layouts
        HBox progressHBox = new HBox(10, progressBar, percentage);
        progressHBox.setAlignment(Pos.CENTER);

        VBox progressBox = new VBox(10, progressHBox, details);
        progressBox.setPadding(new Insets(15));
        progressBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #b0c4de;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0.2, 0, 2);");
        progressBox.setMaxWidth(400);
        progressBox.setAlignment(Pos.CENTER_LEFT);

        HBox buttonBox = new HBox(20, cancelButton, homeButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox content = new VBox(20, animatedStatus, connectionStatus, progressBox, buttonBox);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        
        VBox footerWrapper = new VBox(new FooterView(primaryStage));
        footerWrapper.setAlignment(Pos.BOTTOM_CENTER);
        footerWrapper.setPickOnBounds(false);

        VBox root = new VBox(60, NavBar.createNavBar(primaryStage), content);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f0f8ff;");
        root.setPrefWidth(400);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(root);
        mainLayout.setBottom(footerWrapper);

        primaryStage.setTitle("Sender Progress");
        return new Scene(mainLayout, 1350, 700);
    }

    private void showCancelConfirmation() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Transfer");
        confirm.setHeaderText("Are you sure you want to cancel?");
        confirm.setContentText("This will terminate the connection and redirect you to the homepage.");

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                progressModel.cancelTransfer(); // 🔥 Trigger cancellation
                receivingFile.setText("Transfer Cancelled ❌");
                dots.setText("");
                homeButton.setDisable(false);

                HomePage homePage = new HomePage();
                Scene homeScene = homePage.createScene(primaryStage);
                primaryStage.setScene(homeScene);
                primaryStage.show();
            }
        });
    }
}
