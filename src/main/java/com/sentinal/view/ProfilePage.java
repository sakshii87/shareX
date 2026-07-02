package com.sentinal.view;

import com.sentinal.model.RegistrationModel;
import com.sentinal.service.UserService;
import com.sentinal.session.Session;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProfilePage extends Application {

    private RegistrationModel currentUser;
    private File selectedImageFile = null;

    @Override
    public void start(Stage primaryStage) {

        try {
            currentUser = Session.getCurrentUser();

            if (currentUser == null) {
                new Alert(Alert.AlertType.ERROR, "User is not logged in.").show();
                return;
            }

            // Labels
            Label nameLabel = new Label("Name:");
            Label emailLabel = new Label("Email:");
            Label passwordLabel = new Label("Password:");

            nameLabel.setFont(Font.font(16));
            emailLabel.setFont(Font.font(16));
            passwordLabel.setFont(Font.font(16));

            // Fields
            TextField nameField = new TextField(currentUser.getName());
            TextField emailField = new TextField(currentUser.getEmail());
            PasswordField passwordField = new PasswordField();
            passwordField.setText(currentUser.getPassword());

            emailField.setEditable(false);
            nameField.setEditable(false);
            passwordField.setEditable(false);

            nameField.setPrefWidth(300);
            emailField.setPrefWidth(300);
            passwordField.setPrefWidth(300);

            // Image
            ImageView profileImage = new ImageView();
            profileImage.setFitHeight(100);
            profileImage.setFitWidth(100);
            profileImage.setPreserveRatio(true);

            if (currentUser.getProfileImageUrl() != null && !currentUser.getProfileImageUrl().isEmpty()) {
                profileImage.setImage(new Image(currentUser.getProfileImageUrl()));
            } else {
                profileImage.setImage(new Image("https://cdn-icons-png.flaticon.com/512/149/149071.png")); // default
            }

            profileImage.setOnMouseClicked(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Profile Image");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    selectedImageFile = file;
                    profileImage.setImage(new Image(file.toURI().toString()));
                }
            });

            Label clickToChange = new Label("Click to change image");
            clickToChange.setFont(Font.font(12));
            clickToChange.setStyle("-fx-text-fill: gray;");

            VBox imageBox = new VBox(5, profileImage, clickToChange);
            imageBox.setAlignment(Pos.CENTER);

            // Form layout
            GridPane formGrid = new GridPane();
            formGrid.setHgap(10);
            formGrid.setVgap(15);
            formGrid.setPadding(new Insets(20));
            formGrid.setAlignment(Pos.CENTER);

            formGrid.add(nameLabel, 0, 0);
            formGrid.add(nameField, 1, 0);
            formGrid.add(emailLabel, 0, 1);
            formGrid.add(emailField, 1, 1);
            formGrid.add(passwordLabel, 0, 2);
            formGrid.add(passwordField, 1, 2);

            // Add border around form
            VBox borderedBox = new VBox(formGrid);
            borderedBox.setPadding(new Insets(30));
            borderedBox.setAlignment(Pos.CENTER);
            borderedBox.setStyle(
                    "-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 12; -fx-background-radius: 12;");
            borderedBox.setMaxWidth(500);

            // Buttons
            Button editBtn = new Button("Edit");
            Button saveBtn = new Button("Save");
            Button backBtn = new Button("← Back");

            editBtn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
            saveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
            backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

            saveBtn.setDisable(true);

            editBtn.setOnAction(e -> {
                nameField.setEditable(true);
                passwordField.setEditable(true);
                saveBtn.setDisable(false);
            });

            saveBtn.setOnAction(e -> {
                currentUser.setName(nameField.getText());
                currentUser.setPassword(passwordField.getText());

                if (selectedImageFile != null) {
                    currentUser.setProfileImageUrl(selectedImageFile.toURI().toString()); // Or keep old one if not
                                                                                          // changed
                }

                try {
                    UserService.updateUser(currentUser);
                    showAlert(Alert.AlertType.INFORMATION, "Profile updated successfully.");
                    nameField.setEditable(false);
                    passwordField.setEditable(false);
                    saveBtn.setDisable(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Failed to update profile.");
                }
            });

            backBtn.setOnAction(e -> {
                try {
                    HomePage homePage = new HomePage();
                    homePage.start(primaryStage);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            HBox buttonBox = new HBox(20, editBtn, saveBtn, backBtn);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(20));

            VBox mainBox = new VBox(30, imageBox, borderedBox, buttonBox);
            mainBox.setAlignment(Pos.CENTER);
            mainBox.setPadding(new Insets(40));
            mainBox.setStyle("-fx-background-color: #ecf0f1;");

            Scene scene = new Scene(mainBox, 1350, 700);
            primaryStage.setTitle("Profile Page");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
