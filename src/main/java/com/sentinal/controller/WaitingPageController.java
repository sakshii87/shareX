package com.sentinal.controller;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sentinal.view.AcceptReject;
import com.sentinal.view.HomePage;

import javafx.application.Platform;
import javafx.scene.Scene;
// import javafx.scene.control.Alert;
//import javafx.stage.DirectoryChooser;
// import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class WaitingPageController {
    public Stage waitStage;

    public WaitingPageController(Stage waitStage) {
        this.waitStage = waitStage;
    }

    public void returnToHomePage() {
        try {
            HomePage home = new HomePage();
            Scene homeScene = home.createScene(waitStage);
            waitStage.setScene(homeScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServerSocket serverSocket;
    private final int PORT = 8888; // Common port like 5555

    public void startListeningForSender() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.println("📡 Receiver listening on port 8888...");

                while (true) {
                    Socket senderSocket = serverSocket.accept();
                    System.out.println("🔗 Sender connected!");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(senderSocket.getOutputStream(), true);

                    String signal = reader.readLine();

                    if ("REQUEST".equals(signal)) {
                        List<String> fileList = new ArrayList<>();
                        // String senderName = reader.readLine();
                        String line;

                        while ((line = reader.readLine()) != null && !"END".equals(line)) {
                            fileList.add(line); // format: filename::size
                        }

                        Platform.runLater(() -> {
                            try {
                                AcceptReject page = new AcceptReject(senderSocket, fileList, waitStage, senderSocket);
                                Scene scene = page.getScene();
                                waitStage.setScene(scene);
                                waitStage.setTitle("Accept or Reject File");
                                waitStage.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                    } else {
                        System.out.println("❌ Unexpected signal: " + signal);
                        senderSocket.close();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // private void showAlert(Alert.AlertType type, String title, String header, String content) {
    //     Platform.runLater(() -> {
    //         Alert alert = new Alert(type);
    //         alert.setTitle(title);
    //         alert.setHeaderText(header);
    //         alert.setContentText(content);
    //         alert.showAndWait();
    //     });
    // }

}
