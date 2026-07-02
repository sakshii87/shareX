package com.sentinal.controller;

import com.sentinal.model.RegistrationModel;
import com.sentinal.session.Session;
import com.sentinal.view.HomePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewProfileController {

    private Stage stage;

    public ViewProfileController(Stage stage) {
        this.stage = stage;
        showProfile();
    }

    public void showProfile() {
        RegistrationModel user = Session.getCurrentUser();

        if (user == null) {
            showAlert("Error", "No user session found. Please log in.");
            return;
        }

        // Profile ImageView (default avatar)
        ImageView profileImageView = new ImageView(
                new Image("https://www.w3schools.com/howto/img_avatar.png", 100, 100, true, true));
        profileImageView.setFitWidth(100);
        profileImageView.setFitHeight(100);
        profileImageView.setClip(new Circle(50, 50, 50)); // Circular clipping

        Text nameText = new Text("Name: " + user.getName());
        Text emailText = new Text("Email: " + user.getEmail());
        Text passwordText = new Text("Password: " + user.getPassword());

        nameText.setFont(Font.font(18));
        emailText.setFont(Font.font(18));
        passwordText.setFont(Font.font(18));

        VBox userDetailsBox = new VBox(10, nameText, emailText, passwordText);
        userDetailsBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("← Back");
        backButton.setOnAction(e -> {
            try {
                new HomePage().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to load HomePage.");
            }
        });

        VBox mainLayout = new VBox(20, profileImageView, userDetailsBox, backButton);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: #f4f4f4;");

        Scene scene = new Scene(mainLayout, 1350, 700);
        stage.setTitle("View Profile");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
