package com.sentinal.view;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class ReceivedSuccess extends Application {

    // Utility to get emoji by file extension
    private String getEmojiForFile(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".png") || lower.endsWith(".jpeg") || lower.endsWith(".gif")) return "📷 ";
        if (lower.endsWith(".pdf") || lower.endsWith(".doc") || lower.endsWith(".txt")) return "📄 ";
        if (lower.endsWith(".mp4") || lower.endsWith(".avi") || lower.endsWith(".mov")) return "🎬 ";
        if (lower.endsWith(".mp3") || lower.endsWith(".wav") || lower.endsWith(".m4a")) return "🎵 ";
        if (lower.endsWith(".zip") || lower.endsWith(".rar")) return "📦 ";
        return "📁 "; // Default icon
    }

    @Override
    public void start(Stage primaryStage) {
        Label headingLabel = new Label("✅ Files Received Successfully");
        headingLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        headingLabel.setTextFill(Color.web("#2e7d32"));

        // 📂 Set the folder path
        File folder = new File("C:/share_Files");
        File[] files = folder.exists() && folder.isDirectory() ? folder.listFiles() : new File[0];

        VBox fileList = new VBox(10);
        fileList.setPadding(new Insets(10));

        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = getEmojiForFile(file.getName()) + file.getName();

                    Label fileLabel = new Label(fileName);
                    fileLabel.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 15));
                    fileLabel.setTextFill(Color.web("#37474F"));
                    fileLabel.setPadding(new Insets(8));
                    fileLabel.setMaxWidth(Double.MAX_VALUE);
                    fileLabel.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 8;");
                    fileList.getChildren().add(fileLabel);
                }
            }
        } else {
            Label noFilesLabel = new Label("No files found in inbox.");
            noFilesLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
            noFilesLabel.setTextFill(Color.GRAY);
            fileList.getChildren().add(noFilesLabel);
        }

        ScrollPane scrollPane = new ScrollPane(fileList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);
        scrollPane.setStyle("-fx-background: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10;");

        // 🏠 Home Button
        Button homeButton = new Button("🏠 Back to Home");
        homeButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        homeButton.setTextFill(Color.WHITE);
        homeButton.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 8;");
        homeButton.setCursor(javafx.scene.Cursor.HAND);
        homeButton.setOnMouseEntered(e -> homeButton.setStyle("-fx-background-color: #1976D2; -fx-background-radius: 8; -fx-text-fill: white;"));
        homeButton.setOnMouseExited(e -> homeButton.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 8; -fx-text-fill: white;"));

        // Uncomment if you have a real HomePage to go to:
        // homeButton.setOnAction(e -> new HomePage().start(primaryStage));

        // 📂 Open Folder Button
        Button openFolderButton = new Button("📂 Open Folder Location");
        openFolderButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        openFolderButton.setTextFill(Color.WHITE);
        openFolderButton.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 8;");
        openFolderButton.setCursor(javafx.scene.Cursor.HAND);
        openFolderButton.setOnMouseEntered(e -> openFolderButton.setStyle("-fx-background-color: #388E3C; -fx-background-radius: 8; -fx-text-fill: white;"));
        openFolderButton.setOnMouseExited(e -> openFolderButton.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 8; -fx-text-fill: white;"));

        openFolderButton.setOnAction(e -> {
            try {
                Desktop.getDesktop().open(folder);
            } catch (IOException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to open folder.");
                alert.show();
            }
        });

        // 🔳 Both buttons side by side
        HBox buttonBox = new HBox(20, homeButton, openFolderButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        VBox layout = new VBox(20, NavBar.createNavBar(primaryStage), headingLabel, scrollPane, buttonBox);

        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #f0f4f8;");

        Scene scene = new Scene(layout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Received Files");
        primaryStage.show();
    }

    // Dummy HomePage class for demo
    public static class HomePage extends Application {
        @Override
        public void start(Stage stage) {
            Label label = new Label("🏡 Welcome to Home Page");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            VBox box = new VBox(label);
            box.setAlignment(Pos.CENTER);
            Scene scene = new Scene(box, 400, 300);
            stage.setScene(scene);
            stage.setTitle("Home Page");
            stage.show();
        }
    }

}

