package com.sentinal.view;

import com.sentinal.view.FooterView;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.sentinal.controller.WaitingPageController;

public class ReceiverWaitingPage {

    public Scene receiverWScene, acceptRejectScene, waitingScene;
    public Stage receiverStage, waitStage, stage;
    public String receiverIP;
    // private static final int RECEIVER_PORT = 8888; // ✅ Hardcoded Port

    public ReceiverWaitingPage(Stage stage) {
        this.stage = stage;
    }

    public void setWaitStage(Stage waitStage) {
        this.waitStage = waitStage;
    }

    public void setReceiverWScene(Scene receiverWScene) {
        this.receiverWScene = receiverWScene;
    }

    public void setWaitingScene(Scene waitingScene) {
        this.waitingScene = waitingScene;
    }

    public void setAcceptRejectScene(Scene acceptRejectScene) {
        this.acceptRejectScene = acceptRejectScene;
    }

    public void setReceiverStage(Stage receiverStage) {
        this.receiverStage = receiverStage;
    }

    private String getReceiversIP() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress(); // returns IPv4
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown IP";
        }
    }

    public VBox loadReceiverScene(Runnable backToHome) {
        receiverIP = getReceiversIP();

        Text ipText = new Text("Your IP Address:");
        ipText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        ipText.setFill(Color.web("#2c3e50"));

        TextField ipField = new TextField();
        ipField.setText(receiverIP);
        ipField.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        ipField.setFocusTraversable(false);
        ipField.setEditable(false);
        ipField.setMaxWidth(190);

        Tooltip copyTooltip = new Tooltip("Copied!");
        copyTooltip.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        Tooltip.install(ipField, copyTooltip);

        ipField.setOnMouseClicked(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(ipField.getText());
            clipboard.setContent(content);

            copyTooltip.setText("Copied!");
            copyTooltip.show(ipField, event.getScreenX(), event.getScreenY());

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(copyTooltip::hide);
            }).start();
        });

        Label refreshSymbol = new Label("↻");
        refreshSymbol.setStyle("-fx-font-size: 14pt;");

        Button refreshButton = new Button();
        refreshButton.setGraphic(refreshSymbol);
        refreshButton.setStyle("-fx-background-color: #46d441ff;");

        refreshButton.setOnAction(e -> {
            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), refreshSymbol);
            rotate.setByAngle(360);
            rotate.setCycleCount(1);
            rotate.play();

            refreshSymbol.setTextFill(Color.GREEN);

            rotate.setOnFinished(event -> {
                refreshSymbol.setTextFill(Color.WHITESMOKE);
                receiverIP = getReceiversIP();
                ipField.setText(receiverIP);
            });
        });

        HBox ipBox = new HBox(10, ipText, ipField, refreshButton);
        ipBox.setAlignment(Pos.CENTER);

        Label statusLabel = new Label("Status: Waiting for sender to connect...");
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        Timeline dotAnimation = new Timeline();
        dotAnimation.setCycleCount(Timeline.INDEFINITE);
        String baseText = "Status: Waiting for sender to connect";
        dotAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0), e -> statusLabel.setText(baseText + ".")),
                new KeyFrame(Duration.seconds(0.5), e -> statusLabel.setText(baseText + "..")),
                new KeyFrame(Duration.seconds(1), e -> statusLabel.setText(baseText + "...")),
                new KeyFrame(Duration.seconds(1.5), e -> statusLabel.setText(baseText)));
        dotAnimation.play();

        Label noteLabel = new Label("        Please ask sender to connect to your IP");
        noteLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        noteLabel.setTextFill(Color.GRAY);
        noteLabel.setAlignment(Pos.CENTER);

        Label noLabel1 = new Label(" Note: ");
        noLabel1.setAlignment(Pos.CENTER);
        noLabel1.setTextFill(Color.RED);
        noLabel1.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Label notLabel2 = new Label("This connection is only accessible within LAN");
        notLabel2.setTextFill(Color.BLUE);
        notLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        notLabel2.setAlignment(Pos.CENTER);

        HBox h1 = new HBox(noLabel1, notLabel2);
        h1.setAlignment(Pos.CENTER);

        Button cancelButton = new Button("Cancel - Waiting");
        cancelButton.setMaxWidth(200);
        cancelButton.setMaxHeight(50);
        cancelButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");

        WaitingPageController waitController = new WaitingPageController(stage);
        waitController.startListeningForSender();
        // waitController.startVerificationListener();

        cancelButton.setOnAction(e ->{
            System.out.println("Cancel Button Clicked");
            waitController.stopServer();
            waitController.returnToHomePage();
        });

        VBox contentBox = new VBox(20, ipBox, statusLabel, noteLabel, h1, cancelButton);
        contentBox.setAlignment(Pos.CENTER);
        // contentBox.setPadding(new Insets(30));
        contentBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10;");
        contentBox.setEffect(new DropShadow(5, Color.LIGHTGRAY));

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), contentBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        VBox footerWrapper = new VBox(new FooterView(stage));
        footerWrapper.setAlignment(Pos.BOTTOM_CENTER);
        footerWrapper.setPickOnBounds(false);
        // ===== Root Layout =====
        // contentBox
        VBox root = new VBox();
        root.getChildren().addAll(
                NavBar.createNavBar(stage),
                contentBox,
                footerWrapper);

        VBox.setVgrow(contentBox, Priority.ALWAYS);
        // BorderPane root = new BorderPane();

        return root;

        // ✅ Delegate socket logic to the controller
        // WaitingPageController waitController = new
        // WaitingPageController(receiverStage);

    }
}
