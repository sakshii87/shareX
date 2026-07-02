package com.sentinal.view;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;

public class ReceiverProgressView {

    // Public UI references for external access
    public ProgressBar progressBar;
    public Label percentageLabel;
    public Label detailsLabel;
    public Label connectionStatusLabel;
    public Label fileNameLabel;
    public Button cancelButton;

    private VBox root;

    public ReceiverProgressView(String senderIP, Stage primaryStage) {
        root = new VBox(60);
        root.setStyle("-fx-background-color: #f0f8ff;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefWidth(400);

        Text title = new Text("ShareX by Sentinal3");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setFill(Color.DARKBLUE);

        connectionStatusLabel = new Label("Connected to sender: " + senderIP);
        connectionStatusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        connectionStatusLabel.setTextFill(Color.web("#2c3e50"));
        connectionStatusLabel.setStyle(
                "-fx-background-color: #d6eaf8;" +
                        "-fx-padding: 10 15 10 15;" +
                        "-fx-background-radius: 6;" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;");

        Label receivingFileStatic = new Label("Receiving File");
        receivingFileStatic.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        receivingFileStatic.setTextFill(Color.web("#34495e"));
        Label dots = new Label(".");
        dots.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        dots.setTextFill(Color.web("#e67e22"));

        Timeline dotTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> dots.setText(".")),
                new KeyFrame(Duration.seconds(0.5), e -> dots.setText("..")),
                new KeyFrame(Duration.seconds(1), e -> dots.setText("...")),
                new KeyFrame(Duration.seconds(1.5), e -> dots.setText("....")));
        dotTimeline.setCycleCount(Timeline.INDEFINITE);
        dotTimeline.play();

        HBox animatedTextBox = new HBox(receivingFileStatic, dots);
        animatedTextBox.setAlignment(Pos.CENTER);
        animatedTextBox.setSpacing(5);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);

        percentageLabel = new Label("0%");
        percentageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        HBox progressRow = new HBox(10, progressBar, percentageLabel);
        progressRow.setAlignment(Pos.CENTER);

        detailsLabel = new Label("Received: 0MB / 0MB\nSpeed: 0MB/s\nTime Remaining: --");
        detailsLabel.setFont(Font.font("Arial", 14));
        detailsLabel.setPadding(new Insets(10, 0, 10, 0));

        VBox progressBox = new VBox(10, progressRow, detailsLabel);
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

        cancelButton = new Button("Cancel Transfer");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight:bold;");
        cancelButton.setMaxWidth(200);

        VBox innerContent = new VBox(20,
                animatedTextBox,
                connectionStatusLabel,
                progressBox,
                cancelButton);
        innerContent.setAlignment(Pos.CENTER);
        innerContent.setPadding(new Insets(20));

        VBox footerWrapper = new VBox(new FooterView(primaryStage));
        footerWrapper.setAlignment(Pos.BOTTOM_CENTER);
        footerWrapper.setPickOnBounds(false);

        root.getChildren().addAll(
                NavBar.createNavBar(primaryStage),
                innerContent,
                footerWrapper);

        VBox.setVgrow(innerContent, Priority.ALWAYS);

    }

    public VBox getView() {
        return root;
    }

    public void updateProgress(long bytesReceived, long fileSize, long startTimeMillis, String fileName) {
        Platform.runLater(() -> {
            double progress = (double) bytesReceived / fileSize;
            progressBar.setProgress(progress);
            percentageLabel.setText(String.format("%.1f%%", progress * 100));

            long elapsedMs = System.currentTimeMillis() - startTimeMillis;
            double speed = bytesReceived / (elapsedMs / 1000.0); // bytes/sec
            long remainingBytes = fileSize - bytesReceived;
            long timeRemainingSec = (long) (remainingBytes / speed);

            detailsLabel.setText(String.format(
                    "Received: %.2fMB / %.2fMB\nSpeed: %.2fMB/s\nTime Remaining: %ds",
                    bytesReceived / (1024.0 * 1024),
                    fileSize / (1024.0 * 1024),
                    speed / (1024.0 * 1024),
                    timeRemainingSec));

        });
    }
}