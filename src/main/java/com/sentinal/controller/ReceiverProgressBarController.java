package com.sentinal.controller;

import com.sentinal.view.ReceiverProgressView;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

public class ReceiverProgressBarController {

    private final ReceiverProgressView view;
    private final Socket socket;
    private final File destinationFile;
    private final long totalFileSize;

    public ReceiverProgressBarController(ReceiverProgressView view, Socket socket, File destinationFile, long totalFileSize) {
        this.view = view;
        this.socket = socket;
        this.destinationFile = destinationFile;
        this.totalFileSize = totalFileSize;

        attachCancelListener();
        startReceiving();
    }

    private void attachCancelListener() {
        view.cancelButton.setOnAction(event -> {
            try {
                if (!socket.isClosed()) socket.close();
                System.out.println("Transfer cancelled by user.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private long bytesReceived;

    private void startReceiving() {
        
        new Thread(() -> {
            // long bytesReceived = 0;
            try (InputStream inputStream = socket.getInputStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(destinationFile)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                
                long startTime = System.nanoTime();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    bytesReceived += bytesRead;

                    double progress = (double) bytesReceived / totalFileSize;
                    double elapsedSeconds = (System.nanoTime() - startTime) / 1_000_000_000.0;
                    double speedMBps = (bytesReceived / (1024.0 * 1024)) / elapsedSeconds;
                    long remainingBytes = totalFileSize - bytesReceived;
                    long estimatedTimeSec = speedMBps > 0 ?
                            (long) ((remainingBytes / (1024.0 * 1024)) / speedMBps) : 0;

                    Platform.runLater(() -> updateProgressUI(progress, bytesReceived, speedMBps, estimatedTimeSec));

                    if (socket.isClosed()) break; // In case cancel was triggered
                }

                fileOutputStream.flush();
                Platform.runLater(() -> view.detailsLabel.setText("Transfer complete ✅"));

            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> view.detailsLabel.setText("Transfer failed ❌"));
            }
        }).start();
    }

    private void updateProgressUI(double progress, long bytesReceived, double speedMBps, long estimatedTimeSec) {
        view.progressBar.setProgress(progress);
        view.percentageLabel.setText((int)(progress * 100) + "%");

        view.detailsLabel.setText(String.format(
            "Received: %.2fMB / %.2fMB\nSpeed: %.2fMB/s\nTime Remaining: ~%d sec",
            bytesReceived / (1024.0 * 1024),
            totalFileSize / (1024.0 * 1024),
            speedMBps,
            estimatedTimeSec
        ));
    }
}