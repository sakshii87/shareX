package com.sentinal.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class NavBar {

    public static HBox createNavBar(Stage primaryStage) {
        // 🌐 ShareX Logo
        Label share = new Label("🌐 Share");
        share.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        share.setTextFill(Color.web("#f39c12"));

        Label x = new Label("X");
        x.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        x.setTextFill(Color.web("#2980b9"));

        HBox titleBox = new HBox(5, share, x);

        Label subtitle = new Label("by Sentinel3");
        subtitle.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 12));
        subtitle.setTextFill(Color.web("#1abc9c"));
        subtitle.setLayoutX(60);
        subtitle.setLayoutY(28);

        Pane logoPane = new Pane();
        logoPane.getChildren().addAll(titleBox, subtitle);

        // 🔵 Profile Picture Button
        Image profileImg = new Image("https://www.w3schools.com/howto/img_avatar.png", 35, 35, true, true);
        ImageView profileView = new ImageView(profileImg);
        profileView.setFitWidth(35);
        profileView.setFitHeight(35);

        Button profileBtn = new Button();
        profileBtn.setGraphic(profileView);
        profileBtn.setMinSize(35, 35);
        profileBtn.setMaxSize(35, 35);
        profileBtn.setStyle("-fx-background-color: transparent;");
        profileBtn.setClip(new Circle(17.5, 17.5, 17.5));
        profileBtn.setCursor(Cursor.HAND);

        // 🔽 Dropdown Popup for Profile
        VBox dropdown = new VBox(5);
        dropdown.setPadding(new Insets(10));
        dropdown.setStyle(
                "-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 6; -fx-border-radius: 6;");
        dropdown.setPrefWidth(120);

        Label viewProfile = new Label("👤 View Profile");
        viewProfile.setTextFill(Color.BLACK);
        viewProfile.setCursor(Cursor.HAND);
        viewProfile.setPadding(new Insets(5));

        Label logout = new Label("🚪 Logout");
        logout.setTextFill(Color.BLACK);
        logout.setCursor(Cursor.HAND);
        logout.setPadding(new Insets(5));

        dropdown.getChildren().addAll(viewProfile, logout);

        Popup popup = new Popup();
        popup.getContent().add(dropdown);
        popup.setAutoHide(true);

        // 🔘 Profile Button Click = Show dropdown below button
        profileBtn.setOnAction(e -> {
            if (!popup.isShowing()) {
                double xPos = profileBtn.localToScreen(profileBtn.getBoundsInLocal()).getMinX();
                double yPos = profileBtn.localToScreen(profileBtn.getBoundsInLocal()).getMaxY();
                popup.show(profileBtn, xPos - 60, yPos + 5);
            } else {
                popup.hide();
            }
        });

        // 🎯 View Profile Click
        viewProfile.setOnMouseClicked(e -> {
            popup.hide();
            ProfilePage profilePage = new ProfilePage();
            profilePage.start(primaryStage);
        });

        logout.setOnMouseClicked(e -> {
            popup.hide();
            WelcomePage welcomePage = new WelcomePage();
            welcomePage.start(primaryStage);
        });

        // 🕘 History Button
        Button historyBtn = new Button("History");
        historyBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14;");

        historyBtn.setOnAction(e -> {
            HistoryPage historyPage = new HistoryPage();
            historyPage.start(primaryStage);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navBar = new HBox(10, logoPane, spacer, historyBtn, profileBtn);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: #2c3e50;");
        navBar.setAlignment(Pos.CENTER_LEFT);

        return navBar;
    }

}
