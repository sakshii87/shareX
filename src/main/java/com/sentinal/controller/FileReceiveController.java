package com.sentinal.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.sentinal.view.HistoryLogger;
import com.sentinal.view.HistoryPage;
import com.sentinal.view.HomePage;
import com.sentinal.view.ReceiverProgressView;
import java.awt.Desktop;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class FileReceiveController {
    private static volatile boolean popupShown = false;
    private volatile boolean isCancelled = false;

    private Socket socket;
    private final File saveDirectory;
    private final ReceiverProgressView progressView;
    private final Stage primaryStage;

    public FileReceiveController(Socket socket, File saveDirectory, ReceiverProgressView progressView,
            Stage primaryStage) {
        this.socket = socket;
        this.saveDirectory = saveDirectory;
        this.progressView = progressView;
        this.primaryStage = primaryStage;

        this.progressView.cancelButton.setOnAction(e -> {
            isCancelled = true;
            Platform.runLater(() -> {
                this.progressView.detailsLabel.setText("Transfer Cancelled ❌");
            });
            try {
                if (this.socket != null && !this.socket.isClosed()) {
                    this.socket.close();
                }
            } catch (IOException ex) {
                System.err.println("Error closing socket on cancel: " + ex.getMessage());
            }
        });
    }

    public boolean startReceiving() throws IOException {
        long startTime = System.currentTimeMillis();
        try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            // Step 1: Read Metadata
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            File destinationFile = new File(saveDirectory, fileName);
            try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                byte[] buffer = new byte[4096];
                long bytesReceived = 0;

                // Step 3: File Transfer Loop
                while (bytesReceived < fileSize) {
                    if (isCancelled) {
                        break;
                    }
                    int bytesToRead = (int) Math.min(buffer.length, fileSize - bytesReceived);
                    int bytesRead = dis.read(buffer, 0, bytesToRead);

                    if (bytesRead == -1)
                        break;

                    fos.write(buffer, 0, bytesRead);
                    bytesReceived += bytesRead;

                    long finalBytes = bytesReceived;
                    Platform.runLater(() -> {
                        if (!isCancelled) {
                            progressView.updateProgress(finalBytes, fileSize, startTime, fileName);
                        }
                    });
                }
            }

            if (isCancelled) {
                if (destinationFile.exists()) {
                    destinationFile.delete();
                }
                System.out.println("❌ File reception cancelled: " + fileName);
                return false;
            }

            System.out.println("✅ File received: " + fileName);
             HistoryLogger.logHistory(fileName, "Received"); 
            // showCompletionPopup(saveDirectory);
            return true;

        } catch (IOException e) {
            if (isCancelled) {
                System.out.println("❌ File reception was cancelled.");
                return false;
            } else {
                System.err.println("❌ File reception failed: " + e.getMessage());
                throw e;
            }
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    void showCompletionPopup(File receivedFolder) {
        if (popupShown)
            return;
        popupShown = true;
        Platform.runLater(() -> {
            Stage popupStage = new Stage();
            popupStage.setTitle("Transfer Complete");

            Label message = new Label("✅ File received successfully!");
            message.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            Button nextBtn = new Button("Next");
            nextBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
            nextBtn.setOnAction(e -> {
                popupStage.close();
                try {
                    new HistoryPage().start(primaryStage);
                } catch (Exception ex) {
                    System.err.println("❌ Failed to load HistoryPage: " + ex.getMessage());
                }
            });

            Button openFolderBtn = new Button("Open Folder Location");
            openFolderBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
            openFolderBtn.setOnAction(e -> {
                popupStage.close();
                try {
                    Desktop.getDesktop().open(receivedFolder);
                    HomePage home = new HomePage();
                    Scene homeScene = home.createScene(primaryStage);
                    primaryStage.setScene(homeScene);
                } catch (IOException ex) {
                    System.err.println("❌ Could not open folder: " + ex.getMessage());
                }
            });

            HBox buttonBox = new HBox(20, nextBtn, openFolderBtn);
            buttonBox.setAlignment(Pos.CENTER);

            VBox layout = new VBox(20, message, buttonBox);
            layout.setPadding(new Insets(20));
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout, 400, 150);
            popupStage.setScene(scene);
            popupStage.show();
        });
    }

    void showFailurePopup() {
        if (popupShown)
            return;
        popupShown = true;
        Platform.runLater(() -> {
            Stage popupStage = new Stage();
            popupStage.setTitle("Transfer Cancelled");

            Label message = new Label("❌ File transfer was cancelled.");
            message.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            Button okBtn = new Button("OK");
            okBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            okBtn.setOnAction(e -> {
                popupStage.close();
                try {
                    HomePage home = new HomePage();
                    Scene homeScene = home.createScene(primaryStage);
                    primaryStage.setScene(homeScene);
                } catch (Exception ex) {
                    System.err.println("❌ Failed to load HomePage: " + ex.getMessage());
                }
            });

            VBox layout = new VBox(20, message, okBtn);
            layout.setPadding(new Insets(20));
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout, 400, 150);
            popupStage.setScene(scene);
            popupStage.show();
        });
    }

}
