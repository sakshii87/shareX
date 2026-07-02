package com.sentinal.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReceiversProgressBar extends Application {
    Stage stage;

    @Override
    public void start(Stage primaryStage) {
        // Top Title
        Text title = new Text("SHAREX by Sentinal3");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setFill(Color.DARKBLUE);

        // Connection status
        String senderIP = "192.05.06.102";
        Label connectionStatus = new Label("Connected to sender: " + senderIP);
        connectionStatus.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        connectionStatus.setTextFill(Color.web("#2c3e50"));
        connectionStatus.setStyle(
                "-fx-background-color: #d6eaf8; " +
                        "-fx-padding: 10 15 10 15; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-color: #3498db; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 6;");

        // Receiving file label
        Label receivingFile = new Label("Receiving File");
        receivingFile.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        receivingFile.setTextFill(Color.web("#34495e"));
        Label dots = new Label(".");
        dots.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        dots.setTextFill(Color.web("#e67e22"));

        HBox animatedTextBox = new HBox(receivingFile, dots);
        animatedTextBox.setAlignment(Pos.CENTER);
        animatedTextBox.setSpacing(5);

        Timeline dotTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> dots.setText(".")),
                new KeyFrame(Duration.seconds(0.5), e -> dots.setText("..")),
                new KeyFrame(Duration.seconds(1), e -> dots.setText("...")),
                new KeyFrame(Duration.seconds(1.5), e -> dots.setText("....")));
        dotTimeline.setCycleCount(Timeline.INDEFINITE);
        dotTimeline.play();

        // ProgressBar
        ProgressBar progressBar = new ProgressBar(0.5);
        progressBar.setPrefWidth(300);

        // Percentage
        Label percentage = new Label("50%");
        percentage.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // HBox for ProgressBar and Percentage
        HBox hbox = new HBox(10, progressBar, percentage);
        hbox.setAlignment(Pos.CENTER);

        // Progress details
        Label details = new Label("Received: 5MB / 10MB\nSpeed: 1.2MB/s\nTime Remaining: 5 sec");
        details.setFont(Font.font("Arial", 14));
        details.setPadding(new Insets(10, 0, 10, 0));

        // Box for progress section
        VBox progressBox = new VBox(10, hbox, details);
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

        // Cancel Button
        Button cancelButton = new Button("Cancel Transfer");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight:bold;");
        cancelButton.setMaxWidth(200);
        cancelButton.setOnAction(e -> showCancelAlert(primaryStage));

        // Main content box
        VBox vbox1 = new VBox(20, animatedTextBox, connectionStatus, progressBox, cancelButton);
        vbox1.setAlignment(Pos.CENTER);

        // Main VBox Layout
        VBox vbox = new VBox(60, NavBar.createNavBar(primaryStage), vbox1);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #f0f8ff;");
        vbox.setPrefWidth(400);


        // FooterView footerView = new FooterView(primaryStage);
        // // ✅ Use BorderPane for layout
        // BorderPane root = new BorderPane();
        // root.setCenter(vbox); // Middle content
        // root.setBottom(footerView); // Footer at the bottom
        // BorderPane.setMargin(footerView, new Insets(10));
        // BorderPane.setAlignment(footerView, Pos.CENTER);

        // Scene
        Scene scene = new Scene(vbox, 1200, 800);
        primaryStage.setTitle("Receiver Progress");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showCancelAlert(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transfer Cancelled");
        alert.setHeaderText("Connection Failed");
        alert.setContentText("You will be redirected to the default page.");

        alert.showAndWait();

        // Redirect simulation (could be a new scene/page)
        stage.close();
        System.out.println("Redirecting to default page...");
    }

}
