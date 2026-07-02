package com.sentinal.controller;

import com.sentinal.view.NavBar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

import com.sentinal.view.EnterReceiverIP;
import com.sentinal.view.FooterView;
import com.sentinal.view.HomePage;

public class SelectedFilesController {

  private final List<File> selectedFiles;
  private final Map<File, Boolean> fileSelectionMap = new HashMap<>();
  private VBox fileListContainer;
  private CheckBox selectAllCheckBox;
  private Stage stage;

  public SelectedFilesController(List<File> selectedFiles, Stage stage) {
    this.selectedFiles = selectedFiles;
    this.stage = stage;
    for (File file : selectedFiles) {
      fileSelectionMap.put(file, true); // default unselected
    }
  }

  public Scene buildScene() {
    VBox root = new VBox();
    root.setStyle("-fx-background-color: #f4f4f4;");
    // root.setPadding(new Insets(20));
    root.setPrefHeight(700); // Ensure fixed height for layout

    Label titleLabel = new Label("Selected Files");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

    Button browseButton = new Button("📂 Browse More Files");
    browseButton.setStyle(
        "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 8 16 8 16;" +
            "-fx-background-radius: 8;");
    browseButton.setOnAction(e -> openFileChooser());

    selectAllCheckBox = new CheckBox("Select All");
    selectAllCheckBox.setStyle("-fx-font-size: 14px;");
    selectAllCheckBox.setOnAction(e -> handleSelectAll());

    HBox topBar = new HBox(15, browseButton, selectAllCheckBox);
    topBar.setAlignment(Pos.CENTER_LEFT);

    fileListContainer = new VBox(10);
    fileListContainer.setPadding(new Insets(10));
    fileListContainer.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5px;");
    updateFileList();

    ScrollPane scrollPane = new ScrollPane(fileListContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setStyle("-fx-background: #f4f4f4;");
    VBox.setVgrow(scrollPane, Priority.ALWAYS); // Let scrollPane expand

    Button cancelButton = new Button("❌ Cancel");
    cancelButton.setStyle(
        "-fx-background-color: #e74c3c; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 8 16 8 16;" +
            "-fx-background-radius: 8;");
    cancelButton.setOnAction(e -> handleCancel());

    Button nextButton = new Button("➡ Next");
    nextButton.setStyle(
        "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 8 16 8 16;" +
            "-fx-background-radius: 8;");
    nextButton.setOnAction(e -> handleNext());

    HBox bottomBar = new HBox(15, cancelButton, nextButton);
    bottomBar.setAlignment(Pos.CENTER_RIGHT);
    bottomBar.setPadding(new Insets(10, 0, 20, 0));

    VBox mainContent = new VBox(20, titleLabel, topBar, scrollPane, bottomBar);
    mainContent.setPadding(new Insets(20));
    VBox.setVgrow(mainContent, Priority.ALWAYS); // Let main content grow

    VBox footerWrapper = new VBox(new FooterView(stage));
    footerWrapper.setAlignment(Pos.BOTTOM_CENTER);
    footerWrapper.setPickOnBounds(false);

    root.getChildren().addAll(NavBar.createNavBar(stage), mainContent, footerWrapper);

    return new Scene(root, 1350, 700);
  }

  private void handleNext() {
    List<File> checkedFiles = getCheckedFiles();

    if (checkedFiles.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("No Files Selected");
      alert.setHeaderText(null);
      alert.setContentText("Please select at least one file to proceed.");
      alert.showAndWait();
      return;
    } else {
      System.out.println("Next clicked. Checked files:");
      for (File file : checkedFiles) {
        System.out.println(file.getAbsolutePath());
      }

      showEnterReceiverIP(checkedFiles);
    }

  }

  public void showEnterReceiverIP(List<File> checkedFiles) {
    EnterReceiverIP enterReceiverIP = new EnterReceiverIP(checkedFiles, stage);
    Scene scene = enterReceiverIP.getScene();
    stage.setScene(scene);
  }

  private void handleCancel() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Cancel Confirmation");
    alert.setHeaderText("Are you sure you want to cancel?");
    alert.setContentText("This will take you back to the Home Page.");

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        HomePage homePage = new HomePage();
        try {
          Scene homeScene = homePage.createScene(stage);
          stage.setScene(homeScene);
          stage.show();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
  }

  private void updateFileList() {
    fileListContainer.getChildren().clear();

    for (File file : selectedFiles) {
      CheckBox fileCheckBox = new CheckBox(file.getName());
      fileCheckBox.setSelected(fileSelectionMap.getOrDefault(file, false));

      fileCheckBox.setOnAction(e -> {
        fileSelectionMap.put(file, fileCheckBox.isSelected());
        updateSelectAllStatus();
      });

      fileListContainer.getChildren().add(fileCheckBox);
    }

    updateSelectAllStatus(); // Sync selectAllCheckBox status
  }

  private void handleSelectAll() {
    boolean isSelected = selectAllCheckBox.isSelected();
    for (File file : selectedFiles) {
      fileSelectionMap.put(file, isSelected);
    }
    updateFileList();
  }

  private void updateSelectAllStatus() {
    boolean allSelected = true;

    for (File file : selectedFiles) {
      if (!fileSelectionMap.getOrDefault(file, false)) {
        allSelected = false;
        break;
      }
    }

    selectAllCheckBox.setSelected(allSelected);
  }

  public List<File> getCheckedFiles() {
    List<File> checked = new ArrayList<>();
    for (Map.Entry<File, Boolean> entry : fileSelectionMap.entrySet()) {
      if (entry.getValue()) {
        checked.add(entry.getKey());
      }
    }
    return checked;
  }

  private void openFileChooser() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Add More Files");

    // Allow all file types (including .mp4)
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("All Files", "*.*"),
        new FileChooser.ExtensionFilter("Text Files", "*.txt"),
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
        new FileChooser.ExtensionFilter("Video Files", "*.mp4"));

    // Use stage from the current scene (to prevent null dialog)
    List<File> newFiles = fileChooser.showOpenMultipleDialog(stage);

    if (newFiles != null) {
      boolean anyNewFileAdded = false;

      for (File newFile : newFiles) {
        if (!selectedFiles.contains(newFile)) {
          selectedFiles.add(newFile);
          fileSelectionMap.put(newFile, true);
          anyNewFileAdded = true;
          System.out.println("✅ File Added: " + newFile.getAbsolutePath()); // Debug
        } else {
          System.out.println("⚠️ File Already Exists: " + newFile.getAbsolutePath());
        }
      }

      if (anyNewFileAdded) {
        updateFileList(); // Refresh UI
      } else {
        System.out.println("❌ No new files added.");
      }
    } else {
      System.out.println("🚫 File chooser cancelled or no files selected.");
    }
  }
}
