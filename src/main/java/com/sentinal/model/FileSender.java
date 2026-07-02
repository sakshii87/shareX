package com.sentinal.model;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class FileSender {

    private final Socket socket;
    private final List<File> filesToSend;

    public FileSender(Socket socket, List<File> filesToSend) {
        this.socket = socket;
        this.filesToSend = filesToSend;
    }

    public void sendFiles() {
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // Step 1: Send total number of files
            dos.writeInt(filesToSend.size());

            // Step 2: Send each file
            for (File file : filesToSend) {
                String fileName = file.getName();
                long fileSize = file.length();

                // Send file name and file size
                dos.writeUTF(fileName);
                dos.writeLong(fileSize);

                // Send file content
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, bytesRead);
                    }
                }

                System.out.println("Sent: " + fileName);
            }

            dos.flush();
            dos.close();
            socket.close();

            System.out.println("✅ All files sent successfully.");
        } catch (IOException e) {
            System.err.println("❌ Error while sending files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
