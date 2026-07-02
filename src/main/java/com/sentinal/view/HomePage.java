package com.sentinal.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import com.sentinal.controller.HomePageController;

public class HomePage {

    private Stage mainStage;

    public Scene createScene(Stage homeStage) {
        this.mainStage = homeStage;
        homeStage.setTitle("ShareX Home Page");

        // Welcome Messages
        Text msgLine1 = new Text("You can share your files here");
        msgLine1.setFont(Font.font("Arial", 24));
        msgLine1.setFill(Color.web("#050505ff"));
        msgLine1.setTextAlignment(TextAlignment.RIGHT);

        Text msgLine2 = new Text("ShareX is an easy and free way to share your files");
        msgLine2.setFont(Font.font("Arial", 24));
        msgLine2.setFill(Color.web("#060606ff"));

        VBox messageBox = new VBox(10, msgLine1, msgLine2);
        messageBox.setAlignment(Pos.CENTER);

        // Send Button
        Button sendBtn = new Button("Send");
        sendBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        sendBtn.setStyle("""
                    -fx-background-color: linear-gradient(to right, #4facfe, #00f2fe);
                    -fx-text-fill: white;
                    -fx-background-radius: 30;
                    -fx-border-radius: 30;
                    -fx-padding: 10 20 10 20;
                    -fx-border-color: #2196f3;
                    -fx-border-width: 2;
                    -fx-cursor: hand;
                """);
        sendBtn.setMinWidth(120);

        // Receive Button
        Button receiveBtn = new Button("Receive");
        receiveBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        receiveBtn.setStyle("""
                    -fx-background-color: linear-gradient(to right, #43e97b, #38f9d7);
                    -fx-text-fill: white;
                    -fx-background-radius: 30;
                    -fx-border-radius: 30;
                    -fx-padding: 10 20 10 20;
                    -fx-border-color: #2196f3;
                    -fx-border-width: 2;
                    -fx-cursor: hand;
                """);
        receiveBtn.setMinWidth(120);

        Label selectedFileLabel = new Label();
        selectedFileLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0c0c0c;");

        HomePageController controller = new HomePageController(mainStage, selectedFileLabel);
        sendBtn.setOnAction(e -> controller.handleSendButton());
        receiveBtn.setOnAction(e -> controller.receiveButtonClicked());

        HBox sendReceiveBox = new HBox(15, sendBtn, receiveBtn);
        sendReceiveBox.setAlignment(Pos.CENTER);

        VBox fileBox = new VBox(10, sendReceiveBox, selectedFileLabel);
        fileBox.setAlignment(Pos.CENTER);
        VBox.setMargin(sendBtn, new Insets(0, 0, 250, 0));

        VBox rightContent = new VBox(20, messageBox, fileBox);
        rightContent.setAlignment(Pos.CENTER);
        rightContent.setMaxWidth(Region.USE_COMPUTED_SIZE);
        rightContent.setPadding(new Insets(20));

        // StackPane leftPane = new StackPane();
        // try {
        // Image image = new Image(getClass().getResource("").toExternalForm());
        // ImageView imageView = new ImageView(image);
        // imageView.setFitWidth(300);
        // System.out.println("Left image not found.");
        // }

        HBox contentBox = new HBox(20, rightContent);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        VBox centerWrapper = new VBox(contentBox);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPrefHeight(Region.USE_COMPUTED_SIZE);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerWrapper);
        BorderPane.setAlignment(centerWrapper, Pos.CENTER);
        borderPane.setTop(NavBar.createNavBar(homeStage));

        VBox footerWrapper = new VBox(new FooterView(homeStage));
        footerWrapper.setAlignment(Pos.BOTTOM_CENTER);
        footerWrapper.setPickOnBounds(false);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(0);
        root.getChildren().addAll(borderPane, footerWrapper);
        VBox.setVgrow(borderPane, Priority.ALWAYS);

        return new Scene(root, 1350, 700);
    }

    public void start(Stage primaryStage) {
        Scene homeScene = createScene(primaryStage);
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }
    
}
