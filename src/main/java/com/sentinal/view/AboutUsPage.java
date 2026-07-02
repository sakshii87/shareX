package com.sentinal.view;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AboutUsPage {
    private Scene scene;

    public AboutUsPage(Stage stage, Scene previousScene) {
        VBox root = new VBox(25);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #001F3F, #8548c2ff);");

        //  Logo
        ImageView logo = new ImageView(new Image("assets\\images\\Core2Web_Logo.jpg")); // Update path if needed
        logo.setFitWidth(100);
        logo.setFitHeight(300);
        logo.setPreserveRatio(true);

        // Title and Slogan
        Label title = new Label("Core2web Technologies");
        title.setFont(Font.font("Arial", 32));
        title.setTextFill(Color.CYAN);

        Label slogan = new Label("\"You Must Know The Code, Till The Core!\"");
        slogan.setFont(Font.font("Arial", 18));
        slogan.setTextFill(Color.LIGHTYELLOW);

        VBox titleBox = new VBox(5, logo, title, slogan);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setSpacing(10);

        //Sir Section
        ImageView sirImage = new ImageView(new Image("assets\\images\\Shashi_Sir2.jpg")); 
                                                                                               
        sirImage.setFitWidth(800);
        sirImage.setFitHeight(200);
        sirImage.setPreserveRatio(true);
        sirImage.setSmooth(true);
        sirImage.setStyle("-fx-background-radius: 15;");

        VBox textBox = new VBox(8);
        Label sirName = new Label("Shashi Sir");
        sirName.setFont(Font.font("Arial", 22));
        sirName.setTextFill(Color.YELLOW);

        Label sirRole = new Label("Founder");
        sirRole.setFont(Font.font("Arial", 16));
        sirRole.setTextFill(Color.WHITE);

        Label sirMessage = new Label(
                "The way Shashi Sir teaches code till the core is truly inspiring — he breaks down even the most\n" +
                        "complex concepts into simple logic, making every line of code meaningful and understandable.\n"
                        +
                        "Shashi Sir and the whole team of Core2Web is incredible.\n" +
                        "Special thanks to Sachin Sir, Pramod Sir, and mentor for their constant guidance and support.");
        sirMessage.setTextFill(Color.WHITE);
        sirMessage.setFont(Font.font("Arial", 16));
        sirMessage.setWrapText(true);

        textBox.getChildren().addAll(sirName, sirRole, sirMessage);
        textBox.setAlignment(Pos.TOP_LEFT);

        HBox sirCard = new HBox(20, sirImage, textBox);
        sirCard.setPadding(new Insets(50));
        sirCard.setAlignment(Pos.CENTER_LEFT);
        sirCard.setBackground(
                new Background(new BackgroundFill(Color.rgb(46, 102, 190, 1), new CornerRadii(20), Insets.EMPTY)));
        sirCard.setEffect(new DropShadow(10, Color.DARKGRAY));
        sirCard.maxWidth(350);
        sirCard.setAlignment(Pos.CENTER);

        //  Project Section
        Label projectLabel = new Label(" Project: ShareX");
        projectLabel.setFont(Font.font("Arial", 20));
        projectLabel.setTextFill(Color.YELLOW);

        Label developedBy = new Label("Developed by team Sentinel3");
        developedBy.setFont(Font.font("Arial", 16));
        developedBy.setTextFill(Color.LIGHTGRAY);

        Label projectDesc = new Label(
                "ShareX is a peer-to-peer file sharing desktop application built with JavaFX, Firebase, and socket programming.\n"
                        +
                        "It enables real-time file transfer between authenticated users without relying on third-party servers.\n\n"
                        +
                    
                        "ShareX solves the need for secure, simple, and efficient file transfers between systems, especially useful\n"
                        +
                        "in controlled environments like labs, classrooms, and offices without the complexity of external drives or email.");
        projectDesc.setFont(Font.font("Arial", 15));
        projectDesc.setTextFill(Color.WHITE);
        projectDesc.setWrapText(true);

        //  Team
        Label teamLabel = new Label("Team Members: Sakshi. Shubham. Yogesh");
        teamLabel.setFont(Font.font("Arial", 16));
        teamLabel.setTextFill(Color.LIGHTGRAY);

        //  Back Button
        Button backBtn = new Button("← Back");
        backBtn.setFont(Font.font("Arial", 16));
        backBtn.setStyle("-fx-background-color: #294B8C; -fx-text-fill: white; -fx-background-radius: 10;");
        backBtn.setOnAction(e -> stage.setScene(previousScene));
        backBtn.setPrefWidth(100);

        //  Layout
        VBox contentBox = new VBox(25);
        contentBox.getChildren().addAll(
                titleBox,
                sirCard,
                projectLabel,
                developedBy,
                projectDesc,
                teamLabel,
                backBtn);
        contentBox.setAlignment(Pos.TOP_CENTER);

        root.getChildren().add(contentBox);

        scene = new Scene(root, 1350, 700);
    }

    public Scene getScene() {
        return scene;
    }
}
