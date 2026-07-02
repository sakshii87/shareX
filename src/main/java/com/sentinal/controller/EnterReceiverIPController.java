package com.sentinal.controller;

import com.sentinal.config.NetworkConfig;
import com.sentinal.model.EnterReceiverIPModel;
import com.sentinal.model.TransferProgress;
import com.sentinal.view.FooterView;
import com.sentinal.view.HistoryLogger;
import com.sentinal.view.HomePage;
import com.sentinal.view.NavBar;
// import com.sentinal.model.FileSender;
import com.sentinal.view.SenderProgressBar;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
// import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class EnterReceiverIPController {

    private final EnterReceiverIPModel model;
    private final Stage stage;
    private ListView<String> filesListView;
    private final List<File> selectedFiles;
    String ip;

    public EnterReceiverIPController(EnterReceiverIPModel model, Stage stage, List<File> selectedFiles) {
        this.model = model;
        this.stage = stage;
        this.selectedFiles = selectedFiles;
    }

    public Scene buildScene() {
        Text title = new Text("Enter Receiver IP");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        title.setFill(javafx.scene.paint.Color.web("#2c3e50"));

        // Receiver IP input
        Label ipLabel = new Label("Receiver IP:");
        ipLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextField ipInput = new TextField();
        ipInput.setStyle("-fx-font: bold 12pt 'Arial';");
        ipInput.setPromptText("e.g. 192.168.0.102");
        ipInput.setPrefWidth(200);

        Button verifyBtn = new Button("Verify");
        verifyBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox ipBox = new HBox(10, ipLabel, ipInput, verifyBtn);
        ipBox.setAlignment(Pos.CENTER);

        // Sender IP
        Label senderIpLabel = new Label("Your IP: " + getLocalIP());
        senderIpLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 12));
        senderIpLabel.setStyle("-fx-text-fill: red;");

        // Files section
        Label filesLabel = new Label("Selected Files:");
        filesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        filesListView = new ListView<>();
        filesListView.setPrefHeight(100);
        populateFiles();

        VBox filesSection = new VBox(5, filesLabel, filesListView);
        filesSection.setPadding(new Insets(10, 0, 10, 0));
        filesSection.setAlignment(Pos.CENTER_LEFT);

        // Buttons
        Button sendBtn = new Button("Send Files");
        sendBtn.setDisable(true);
        sendBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        sendBtn.setOnAction(event -> {
            String ip = ipInput.getText().trim();
            sendConnectionRequest(ip);
        });

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(20, backBtn, sendBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Verify Button Action
        verifyBtn.setOnAction(e -> {
            String ip = ipInput.getText().trim();
            verifyReceiverIP(ip, verifyBtn, sendBtn);
        });

        // Back Button Action
        backBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancel Confirmation");
            alert.setHeaderText("Are you sure you want to cancel?");
            alert.setContentText("This will take you back to the Home Page.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    com.sentinal.view.HomePage homePage = new com.sentinal.view.HomePage();
                    Scene homeScene = homePage.createScene(stage);
                    stage.setScene(homeScene);
                    stage.show();
                }
            });
        });

        VBox conBox = new VBox(20, filesSection, title, ipBox, senderIpLabel, buttonBox);
        conBox.setAlignment(Pos.CENTER);
        conBox.setPadding(new Insets(20));

        VBox root = new VBox(20,
                NavBar.createNavBar(stage),
                conBox);

        VBox footerWrapper = new VBox(new FooterView(stage));
        footerWrapper.setAlignment(Pos.BOTTOM_CENTER);
        footerWrapper.setPickOnBounds(false);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(root);
        mainLayout.setBottom(footerWrapper);

        mainLayout.setStyle("-fx-background-color: #f8f8f8;");
        return new Scene(mainLayout, 1350, 700);

    }

    private void populateFiles() {
        for (File file : model.getSelectedFiles()) {
            filesListView.getItems().add(file.getName());
        }
    }

    private boolean isValidIP(String ip) {
        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(regex);
    }

    private String getLocalIP() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "Unavailable";
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // <verify button functionality

    private void verifyReceiverIP(String ip, Button verifyBtn, Button sendBtn) {
        if (!isValidIP(ip)) {
            sendBtn.setDisable(true);
            showAlert(Alert.AlertType.ERROR, "Invalid IP Address.");
            return;
        }

        verifyBtn.setText("Sending Metadata...");
        verifyBtn.setDisable(true);

        new Thread(() -> {
            try (
                    Socket socket = new Socket(ip, 8888);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                // String senderIP = InetAddress.getLocalHost().getHostAddress();
                // int senderPort = socket.getLocalPort();
                out.println("REQUEST");
                // out.println(senderIP + "::" + senderPort);
                for (File file : selectedFiles) {
                    String name = file.getName();
                    long size = file.length();
                    out.println(name + "::" + size);
                }
                out.println("END");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();

                Platform.runLater(() -> {
                    verifyBtn.setText("Verified");
                    verifyBtn.setDisable(false);

                    if ("ACCEPTED".equals(response)) {
                        sendBtn.setDisable(false);
                        showAlert(Alert.AlertType.INFORMATION, "✅ Receiver accepted the request!");
                    } else if ("REJECTED".equals(response)) {
                        sendBtn.setDisable(true);
                        showAlert(Alert.AlertType.ERROR, "❌ Receiver rejected the file transfer.");
                        navigateToHome();
                    } else {
                        sendBtn.setDisable(true);
                        showAlert(Alert.AlertType.ERROR, "Unexpected response from receiver.");
                    }
                });

            } catch (IOException e) {
                Platform.runLater(() -> {
                    verifyBtn.setText("Verify");
                    verifyBtn.setDisable(false);
                    showAlert(Alert.AlertType.ERROR, "❌ Unable to connect to receiver.");
                });
            }
        }).start();
    }

    private void navigateToHome() {
        com.sentinal.view.HomePage homePage = new com.sentinal.view.HomePage();
        Scene homeScene = homePage.createScene(stage);
        stage.setScene(homeScene);
        stage.show();
    }

    public void sendConnectionRequest(String ip) {
        new Thread(() -> {
            TransferProgress progressModel = new TransferProgress();

            Platform.runLater(() -> {
                SenderProgressBar sender = new SenderProgressBar(stage, progressModel, ip);
                Scene scene = sender.getScene();
                stage.setScene(scene);
            });

            final boolean[] wasCancelled = { false };

            for (File file : selectedFiles) {
                if (progressModel.isCancelled()) {
                    System.out.println("🚫 Transfer cancelled before sending: " + file.getName());
                    wasCancelled[0] = true;
                    break;
                }
                try {
                    Socket transferSocket = new Socket(ip, NetworkConfig.ACTUAL_PORT);
                    System.out.println("🔗 Connected to receiver for file: " + file.getName());

                    FileTransferController transfer = new FileTransferController(file, transferSocket, progressModel);
                    boolean success = transfer.startTransfer();

                    if (success) {
                        System.out.println("✅ File sent successfully: " + file.getName());
                        HistoryLogger.logHistory(file.getName(), "Sent");
                    } else {
                        System.out.println("🚫 File transfer cancelled: " + file.getName());
                        wasCancelled[0] = true;
                    }

                    transferSocket.close();

                } catch (IOException ex) {
                    System.err.println("❌ Error while sending file: " + file.getName());
                    ex.printStackTrace();
                    wasCancelled[0] = true;
                }
            }

            Platform.runLater(() -> {
                if (wasCancelled[0] || progressModel.isCancelled()) {
                    showAlert(Alert.AlertType.WARNING, "⚠️ Transfer cancelled. Redirecting to Home Page...");
                    HomePage homePage = new HomePage();
                    Scene homeScene = homePage.createScene(stage);
                    stage.setScene(homeScene);
                    stage.show();
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "🎉 All files sent!");
                }
            });
        }).start();
    }

}
