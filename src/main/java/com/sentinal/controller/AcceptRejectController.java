package com.sentinal.controller;

import java.io.BufferedWriter;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

// import com.sentinal.model.FileReceiver;
// import com.sentinal.model.FileReceiver;
// import com.sentinal.view.ReceiverProgressPage;
//import com.sentinal.view.ReceiversProgressBar;

//import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AcceptRejectController {

    private Socket socket;
    private Stage stage;
    private Scene progressScene;

    public AcceptRejectController(Socket socket, Stage stage) {
        this.socket = socket;
        this.stage = stage;
    }

    public void sendResponseToSender(String response, Socket senderSocket) {
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(senderSocket.getOutputStream()))) {
        writer.write(response);
        writer.newLine(); // Ensures complete line transmission
        writer.flush();
        System.out.println("Sent response: " + response);
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Error sending response: " + e.getMessage());
    }
}
}