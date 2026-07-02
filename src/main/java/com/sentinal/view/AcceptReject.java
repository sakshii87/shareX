package com.sentinal.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.Socket;
import java.util.List;
// import java.util.ArrayList;

import com.sentinal.controller.AcceptRejectController;
import com.sentinal.controller.ReceiverListener;

public class AcceptReject {

    private final AcceptRejectController controller;
    private Stage stage;
    private final List<String> incommingFileList;
    private final Socket senderSocket;

    public AcceptReject(Socket socket, List<String> fileList, Stage stage, Socket senderSocket) {
        this.stage = stage;
        this.controller = new AcceptRejectController(socket, stage);
        this.incommingFileList = fileList;
        this.senderSocket = senderSocket;
    }

    public Scene getScene() {
        // ---- File Details Scrollable List ----
        VBox fileListVBox = new VBox(10);
        fileListVBox.setPadding(new Insets(10));

        if (incommingFileList != null && !incommingFileList.isEmpty()) {
            for (int i = 0; i < incommingFileList.size(); i++) {
                String fileMeta = incommingFileList.get(i);
                System.out.println("File #" + i + ": " + fileMeta);

                String[] parts = fileMeta.split("::");
                if (parts.length == 2) {
                    String fileName = parts[0].trim();
                    long fileSizeBytes = Long.parseLong(parts[1].trim());
                    String fileSizeFormatted = formatSize(fileSizeBytes);

                    Label fileLabel = new Label("📄 " + fileName + " | " + fileSizeFormatted);
                    fileLabel.setStyle(
                            "-fx-background-color: #116377ff; -fx-padding: 8 12 8 12; -fx-background-radius: 6;");
                    fileListVBox.getChildren().add(fileLabel);
                }
            }
        } else {
            fileListVBox.getChildren().add(new Label("No files received."));
        }

        ScrollPane scrollPane = new ScrollPane(fileListVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // ---- Save Location ----
        String defaultDownloadPath = System.getProperty("user.home") + File.separator + "Downloads";
        TextField saveLocationField = new TextField(defaultDownloadPath);
        saveLocationField.setEditable(false);
        saveLocationField.setPrefWidth(350);
        saveLocationField.setFocusTraversable(false);

        Button browseBtn = new Button("Browse");
        browseBtn.setStyle("-fx-background-color: #97c946ff; -fx-text-fill: white; -fx-font-weight: bold;");
        browseBtn.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selected = directoryChooser.showDialog(stage);
            if (selected != null) {
                saveLocationField.setText(selected.getAbsolutePath());
            }
            System.out.println(selected);
        });

        Text saveLocationText = new Text("📁 Save Location:");
        HBox saveBox = new HBox(10, saveLocationText, saveLocationField, browseBtn);
        saveBox.setAlignment(Pos.CENTER_LEFT);

        // ---- Incoming Message with Animation ----
        Label incomingText = new Label("Incoming File Transfer Request");
        incomingText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        incomingText.setTextFill(Color.web("#34495e"));

        Label dots = new Label(".");
        dots.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        dots.setTextFill(Color.web("#e67e22"));

        HBox animatedTextBox = new HBox(incomingText, dots);
        animatedTextBox.setAlignment(Pos.CENTER_LEFT);
        animatedTextBox.setSpacing(5);

        Timeline dotTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> dots.setText(".")),
                new KeyFrame(Duration.seconds(0.5), e -> dots.setText("..")),
                new KeyFrame(Duration.seconds(1), e -> dots.setText("...")),
                new KeyFrame(Duration.seconds(1.5), e -> dots.setText("....")));
        dotTimeline.setCycleCount(Timeline.INDEFINITE);
        dotTimeline.play();

        // ---- Accept / Reject ----
        Button acceptBtn = new Button("Accept Files");
        acceptBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        acceptBtn.setPrefWidth(200);
        acceptBtn.setOnAction(e -> {
            String chosenPath = saveLocationField.getText(); // your UI field with the user's folder

            if (chosenPath == null || chosenPath.isEmpty()) {
                // Add error dialog or warning
                System.out.println("⚠ No folder selected.");
                return;
            }

            File baseDir = new File(chosenPath);
            File targetDir = new File(baseDir, "ReceivedFiles");

            if (!targetDir.exists()) {
                boolean created = targetDir.mkdirs();
                if (!created) {
                    System.out.println("❌ Failed to create ReceivedFiles folder.");
                    return;
                }
            }

            controller.sendResponseToSender("ACCEPTED", senderSocket);
            new ReceiverListener(targetDir, stage);
        });

        Button rejectBtn = new Button("Reject Files");
        rejectBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        rejectBtn.setPrefWidth(200);
        rejectBtn.setOnAction(e -> {
            controller.sendResponseToSender("REJECT", senderSocket);
        });

        HBox actionBox = new HBox(30, acceptBtn, rejectBtn);
        actionBox.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox(15,
                animatedTextBox,
                scrollPane,
                saveBox);
        contentBox.setPadding(new Insets(25));
        contentBox.setStyle(
                "-fx-background-color: #f7f7f7; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, gray, 4, 0.3, 1, 1);");

        VBox footerWrapper = new VBox(new FooterView(stage));
        footerWrapper.setAlignment(Pos.BOTTOM_CENTER);
        footerWrapper.setPickOnBounds(false);

        VBox root = new VBox(20, contentBox, actionBox);
        root.setPadding(new Insets(20));

        VBox mainVbox = new VBox();
        mainVbox.getChildren().addAll(
                NavBar.createNavBar(stage),
                root,
                footerWrapper);

        VBox.setVgrow(root, Priority.ALWAYS);

        return new Scene(mainVbox, 1350, 700);
    }

    private String formatSize(long sizeInBytes) {
        if (sizeInBytes >= 1024 * 1024) {
            return String.format("%.2f MB", sizeInBytes / (1024.0 * 1024));
        } else if (sizeInBytes >= 1024) {
            return String.format("%.2f KB", sizeInBytes / 1024.0);
        } else {
            return sizeInBytes + " B";
        }
    }
}
