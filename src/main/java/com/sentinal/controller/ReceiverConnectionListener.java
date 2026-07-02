package com.sentinal.controller;

// package com.sentinal.network;

import javafx.application.Platform;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiverConnectionListener implements Runnable {

    private final int port;
    private final Runnable onRequestReceived;

    public ReceiverConnectionListener(int port, Runnable onRequestReceived) {
        this.port = port;
        this.onRequestReceived = onRequestReceived;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("✅ Receiver listening on port: " + port);

            Socket senderSocket = serverSocket.accept(); // blocking
            System.out.println("📥 Connection request received!");

            BufferedReader in = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
            String message = in.readLine();

            System.out.println("📨 Received message: " + message);

            if ("REQUEST".equalsIgnoreCase(message.trim())) {
                Platform.runLater(onRequestReceived); // switch to AcceptReject.java
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    

