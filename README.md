# ShareX

### Desktop File Sharing Application

ShareX is a JavaFX-based desktop application that allows users to send and receive files directly over a local network.

## Features

* User registration and login
* Send and receive files
* Send multiple files
* Accept or reject incoming file transfers
* Real-time file transfer progress
* Display transfer speed and estimated time
* Cancel file transfers
* Save received files to a selected location
* Transfer history
* User profile
* JavaFX graphical user interface
* Socket-based file transfer

## How It Works

1. Users log in to the application.
2. The sender connects to the receiver using the receiver's IP address and port.
3. The sender selects one or more files to transfer.
4. The receiver can accept or reject the incoming transfer.
5. If accepted, the files are transferred through a socket connection.
6. The application displays the transfer progress, speed, and estimated time.
7. Transfer details can be viewed in the history.

## Technologies Used

* Java 21
* JavaFX
* Socket Programming
* Firebase Authentication
* Firebase Firestore
* Firebase Storage
* Maven
* Scene Builder
* MVC Architecture

## Project Structure

```text
src/
├── controller/
├── model/
├── view/
└── resources/
```

## Purpose

ShareX was developed to provide a simple way to transfer files directly between devices over a local network while allowing users to monitor the progress of the transfer.
