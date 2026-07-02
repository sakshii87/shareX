package com.sentinal.controller;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.sentinal.view.ReceiverProgressView;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

//import com.sentinal.config.NetworkConfig;

public class ReceiverListener {
  private ServerSocket serverSocket;
  private volatile boolean listening = true;
  private final File saveDirectory;
  private final Stage primaryStage;
  

  public ReceiverListener(File saveDirectory, Stage primaryStage) {
    this.saveDirectory = saveDirectory;
    this.primaryStage = primaryStage;
    try {
      serverSocket = new ServerSocket(9999);
      System.out.println("🎧 Receiver listening on port " + 9999);
    } catch (IOException ex) {
      System.err.println("❌ Failed to start receiver socket listener.");
      ex.printStackTrace();
    }

    new Thread(this::startListening).start();
  }

  private void startListening() {
    while (listening) {
      try {
        Socket clientSocket = serverSocket.accept();
        String senderIP = clientSocket.getInetAddress().getHostAddress();

        ReceiverProgressView progressView = new ReceiverProgressView(senderIP, primaryStage);
        Scene scene = new Scene(progressView.getView(), 1350, 700);

        Platform.runLater(() -> primaryStage.setScene(scene));

        FileReceiveController handler = new FileReceiveController(clientSocket, saveDirectory, progressView,
            primaryStage);
        new Thread(() -> {
          try {
            boolean success = handler.startReceiving();
            if (success) {
                Platform.runLater(() -> handler.showCompletionPopup(saveDirectory));
            } else {
                Platform.runLater(() -> handler.showFailurePopup());
            }
          } catch (IOException e) {
            System.err.println("❌ Error receiving file:");
            e.printStackTrace();
            Platform.runLater(() -> handler.showFailurePopup());
          }
        }).start();
      } catch (IOException e) {
        System.err.println("⚠ Error accepting file socket");
        e.printStackTrace();
      }
    }
  }

  public void stopListening() {
    listening = false;
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
