package com.sentinal.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class FooterView extends HBox {
  Stage stage;

  public FooterView(Stage stage) {
    this.stage = stage;
    setAlignment(Pos.CENTER);
    setStyle("-fx-background-color: #0E1E25;");
    setPrefHeight(60);
    setPadding(new Insets(15, 20, 15, 20));
    setSpacing(20);

    Label copyright = new Label("© 2025 Sentinal3. All rights reserved.");
    copyright.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
    copyright.setTextFill(Color.LIGHTGRAY);

    Hyperlink aboutLink = new Hyperlink("About Us");

    aboutLink.setOnMouseClicked(e -> {
      System.out.println("About Us clicked!");
      AboutUsPage aboutPage = new AboutUsPage(stage,getScene()); // Save current scene to return
                                                                                      
      stage.setScene(aboutPage.getScene());
    });
    aboutLink.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
    aboutLink.setTextFill(Color.LIGHTBLUE);
    aboutLink
        .setStyle("-fx-border-color: transparent; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

    getChildren().addAll(copyright, aboutLink);
  }

}
