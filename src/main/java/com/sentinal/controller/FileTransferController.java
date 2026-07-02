package com.sentinal.controller;

import com.sentinal.model.TransferProgress;
import javafx.application.Platform;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import com.sentinal.view.HistoryLogger;

public class FileTransferController {
    private final File fileToSend;
    private final Socket socket;
    private final TransferProgress progressModel;

    public FileTransferController(File fileToSend, Socket socket, TransferProgress progressModel) {
        this.fileToSend = fileToSend;
        this.socket = socket;
        this.progressModel = progressModel;
    }

    public boolean startTransfer() throws IOException {
        long totalBytes = fileToSend.length();
        long bytesSent = 0;
        long startTime = System.currentTimeMillis();

        try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                FileInputStream fis = new FileInputStream(fileToSend)) {

            // Step 1: Send metadata
            dos.writeUTF(fileToSend.getName());
            dos.writeLong(totalBytes);
            dos.flush();

            // Step 2: Send file in chunks
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                if (progressModel.isCancelled()) {
                    System.out.println("🚫 Transfer cancelled mid-stream: " + fileToSend.getName());
                    return false;
                }
                dos.write(buffer, 0, bytesRead);
                bytesSent += bytesRead;

                long elapsed = System.currentTimeMillis() - startTime;
                double seconds = elapsed / 1000.0;
                double speed = (bytesSent / (1024.0 * 1024)) / (seconds == 0 ? 1 : seconds); // MB/s
                double progress = (double) bytesSent / totalBytes;
                long remainingBytes = totalBytes - bytesSent;
                double estimatedTimeSec = speed == 0 ? 0 : (remainingBytes / (1024.0 * 1024)) / speed;

                String sizeText = String.format("Sent: %.2fMB / %.2fMB",
                        bytesSent / (1024.0 * 1024.0),
                        totalBytes / (1024.0 * 1024.0));
                String speedText = String.format("%.2fMB/s", speed);
                String timeRemaining = String.format("%.1f sec", estimatedTimeSec);
                String fileCountText = "File 1 of 1"; // 🔧 Update if multiple files later

                Platform.runLater(() -> {
                    progressModel.progressProperty().set(progress);
                    progressModel.percentageTextProperty().set(String.format("%.0f%%", progress * 100));
                    progressModel.sizeTextProperty().set(sizeText);
                    progressModel.speedTextProperty().set(speedText);
                    progressModel.timeRemainingTextProperty().set(timeRemaining);
                    progressModel.fileCountTextProperty().set(fileCountText);

                });
            }

            dos.flush();
            System.out.println("🚀 Transfer complete: " + fileToSend.getName());
            return true;

        } catch (IOException e) {
            System.err.println("❌ Error sending file: " + fileToSend.getName());
            throw e;
        } finally {
            try {
                socket.close();
            } catch (IOException ignore) {
            }
        }
    }
}
