package com.sentinal.controller;

import java.io.File;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// import com.sentinal.view.AboutUs;
import com.sentinal.view.HomePage;
import com.sentinal.view.ReceiverWaitingPage;
import com.sentinal.view.SelectedFiles;

public class HomePageController {

    private Stage homeStage;
    private Scene waitingScene;

    // variables for home page sendBtn
    // private Stage stage;
    private Label selectedFileLabel;
    private List<File> selectedFiles;

    // when receve button is clicked we just pass the stage
    public HomePageController(Stage homeStage) {
        this.homeStage = homeStage;
    }

    // controller for send button where we pass two parameters stage for stage and
    // selectedFileLabel to show selected files
    public HomePageController(Stage stage, Label selectedFileLabel) {
        this.homeStage = stage;
        this.selectedFileLabel = selectedFileLabel;
    }

    // directs to receiver waiting page by returning scene
    public Scene directToReceiverWaiting() {
        ReceiverWaitingPage waiting = new ReceiverWaitingPage(homeStage);
        waiting.setReceiverStage(homeStage);

        Scene receiverScene = new Scene(waiting.loadReceiverScene(() -> waitingToHome()), 1350, 700);
        waiting.setReceiverWScene(receiverScene);
        return receiverScene;
    }

    private void waitingToHome() {
        System.out.println("Switching to Home Page");
        HomePage homePage = new HomePage();
        Scene homeScene = homePage.createScene(homeStage);
        homeStage.setScene(homeScene);
    }

    // method which handels send button
    public void handleSendButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file(s) to send");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        // Allow multiple selection
        selectedFiles = fileChooser.showOpenMultipleDialog(homeStage);
        System.out.println(selectedFiles);

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            StringBuilder fileNames = new StringBuilder("Selected: ");
            for (File file : selectedFiles) {
                fileNames.append(file.getName()).append(", "); // list of files
            }

            selectedFileLabel.setText(fileNames.toString());

            // Navigate to selected files preview page
            goToSelectedFilesPage();
        } else {
            selectedFileLabel.setText("");

            showAlert(Alert.AlertType.WARNING, "No File Selected", "Please select at least one file.");

            // Optionally reload home page scene
            reloadHomePage();
        }
    }

    // Triggered when Receive button is clicked
    public void receiveButtonClicked() {
        ReceiverWaitingPage receiverWait = new ReceiverWaitingPage(homeStage);
        VBox waitingContent = receiverWait.loadReceiverScene(() -> {
            // Return to Home logic
            receiverWait.setWaitingScene(waitingScene); // or HomePage.getHomeScene()
        });

        Scene waitingScene = new Scene(waitingContent, 1350, 700);
        homeStage.setScene(waitingScene);
    }

    // 📄 Show alert box
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Return to Home Page
    private void reloadHomePage() {
        HomePage homePage = new HomePage();
        try {
            Scene homeScene = homePage.createScene(homeStage);
            homeStage.setScene(homeScene);
        } catch (Exception e) {
            System.err.println("Error reloading HomePage.");
        }
    }

    // Navigate to SelectedFiles View
    private void goToSelectedFilesPage() {
        SelectedFiles selectedFilesView = new SelectedFiles(selectedFiles, homeStage);
        Scene selectedFileScene = selectedFilesView.getScene();
        homeStage.setScene(selectedFileScene);
    }
}
