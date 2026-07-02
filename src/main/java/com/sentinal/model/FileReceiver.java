package com.sentinal.model;

import java.io.*;
import java.net.Socket;

public class FileReceiver {

    private final Socket socket;
    private final String saveDirectory;

    public FileReceiver(Socket socket, String saveDirectory) {
        this.socket = socket;
        this.saveDirectory = saveDirectory;
    }

    public void receiveFiles() {
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // Step 1: Read number of files
            int fileCount = dis.readInt();
            System.out.println("Receiving " + fileCount + " file(s)...");

            // Step 2: Receive each file
            for (int i = 0; i < fileCount; i++) {
                String fileName = dis.readUTF();
                long fileSize = dis.readLong();

                File file = new File(saveDirectory, fileName);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4096];
                    long remaining = fileSize;

                    while (remaining > 0) {
                        int bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                        if (bytesRead == -1) break;
                        fos.write(buffer, 0, bytesRead);
                        remaining -= bytesRead;
                    }
                }

                System.out.println("Received: " + fileName);
            }

            dis.close();
            socket.close();

            System.out.println("✅ All files received successfully.");
        } catch (IOException e) {
            System.err.println("❌ Error while receiving files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}