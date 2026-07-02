// package com.sentinal.view;

// import com.day.cq.workflow.exec.HistoryItem;
// import com.google.gson.Gson;
// import com.google.gson.reflect.TypeToken;
// import com.sentinal.model.HistoryModel;
// import com.sentinal.session.HistorySession;

// // import com.sentinal.model.HistoryItem;
// // import com.sentinal.session.HistorySession;
// import javafx.application.Application;
// import javafx.geometry.*;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.layout.*;
// import javafx.scene.paint.Color;
// import javafx.scene.text.*;
// import javafx.stage.Stage;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.lang.reflect.Type;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.*;

// public class HistoryPage extends Application {

//     private final VBox historyItems = new VBox(15);
//     private final Set<HBox> selectedItems = new HashSet<>();
//     private final Label deleteIcon = new Label("\ud83d\uddd1");
//     private final Label cancelIcon = new Label("\u274c");
//     private final List<CheckBox> checkBoxes = new ArrayList<>();
//     private Stage stage;

//     @Override
//     public void start(Stage primaryStage) {
//         this.stage = primaryStage;

//         Text historyText = new Text("History");
//         historyText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

//         Hyperlink selectAllLink = new Hyperlink("Select All");
//         selectAllLink.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

//         HBox topBox = new HBox(10, historyText, selectAllLink, deleteIcon, cancelIcon);
//         topBox.setAlignment(Pos.CENTER);
//         topBox.setPadding(new Insets(15));

//         deleteIcon.setStyle("-fx-cursor: hand;");
//         cancelIcon.setStyle("-fx-cursor: hand;");
//         deleteIcon.setVisible(false);
//         cancelIcon.setVisible(false);

//         historyItems.setPadding(new Insets(10));
//         historyItems.setAlignment(Pos.TOP_CENTER);

//         ScrollPane scrollPane = new ScrollPane(historyItems);
//         scrollPane.setFitToWidth(true);
//         scrollPane.setPadding(new Insets(10));

//         VBox centerLayout = new VBox(10, NavBar.createNavBar(primaryStage), topBox, scrollPane);
//         centerLayout.setPadding(new Insets(10));

//         BorderPane root = new BorderPane();
//         root.setCenter(centerLayout);
//         root.setStyle("-fx-background-color: #f4f4f4;");

//         Scene scene = new Scene(root, 500, 400);
//         primaryStage.setScene(scene);
//         primaryStage.setTitle("History - ShareX");
//         primaryStage.show();

//         // Load data from Firebase
//         loadHistoryFromFirebase();

//         selectAllLink.setOnAction(e -> {
//             if (historyItems.getChildren().isEmpty()) {
//                 showAlert("No data found!");
//                 return;
//             }
//             selectedItems.clear();
//             for (CheckBox box : checkBoxes) {
//                 box.setSelected(true);
//                 Parent parent = box.getParent();
//                 if (parent instanceof HBox hbox) {
//                     hbox.setStyle("-fx-background-color: #d0f0c0; -fx-border-color: black; -fx-border-width: 1;");
//                     selectedItems.add(hbox);
//                 }
//             }
//             deleteIcon.setVisible(true);
//             cancelIcon.setVisible(true);
//         });

//         cancelIcon.setOnMouseClicked(e -> {
//             for (CheckBox box : checkBoxes) {
//                 box.setSelected(false);
//                 Parent parent = box.getParent();
//                 if (parent instanceof HBox hbox) {
//                     hbox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
//                 }
//             }
//             selectedItems.clear();
//             deleteIcon.setVisible(false);
//             cancelIcon.setVisible(false);
//         });

//         deleteIcon.setOnMouseClicked(e -> {
//             if (selectedItems.isEmpty()) {
//                 showAlert("No file selected!");
//                 return;
//             }
//             historyItems.getChildren().removeAll(selectedItems);
//             selectedItems.clear();
//             checkBoxes.removeIf(CheckBox::isSelected);
//             deleteIcon.setVisible(false);
//             cancelIcon.setVisible(false);

//             if (historyItems.getChildren().isEmpty()) {
//                 showAlert("Deleted successfully!\nNo data found.");
//             } else {
//                 showAlert("Deleted successfully!");
//             }
//         });

//         BorderPane root = new BorderPane();
//         root.setCenter(centerLayout);
//         root.setStyle("-fx-background-color: #f4f4f4;");

//         // Add back button at bottom
//         Button backBtn = new Button("← Back");
//         backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
//         backBtn.setPrefWidth(100);
//         backBtn.setOnAction(e -> {
//             try {
//                 new HomePage().start(stage);
//             } catch (Exception ex) {
//                 ex.printStackTrace();
//             }
//         });

//         HBox backBtnBox = new HBox(backBtn);
//         backBtnBox.setAlignment(Pos.CENTER);
//         backBtnBox.setPadding(new Insets(10));

//         root.setBottom(backBtnBox); // ✅ Use existing 'root' here

//     }

//     private void loadHistoryFromFirebase() {
//         String userId = HistorySession.getCurrentUserId();
//         if (userId == null || userId.isEmpty()) {
//             System.out.println("User ID is null or empty.");
//             return;
//         }

//         userId = userId.replace("@", "_").replace(".", "_"); // Firebase-safe format
//         String firebaseUrl = "https://sentinal3-94a9e-default-rtdb.firebaseio.com/history/" + userId + ".json";

//         try {
//             URL url = new URL(firebaseUrl);
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("GET");
//             conn.setRequestProperty("Content-Type", "application/json");

//             int responseCode = conn.getResponseCode();
//             if (responseCode == 200) {
//                 BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                 StringBuilder json = new StringBuilder();
//                 String line;
//                 while ((line = reader.readLine()) != null) {
//                     json.append(line);
//                 }
//                 reader.close();

//                 if (!json.toString().equals("null")) {
//                     Gson gson = new Gson();
//                     Type type = new TypeToken<Map<String, HistoryModel>>() {
//                     }.getType();
//                     Map<String, HistoryModel> historyMap = gson.fromJson(json.toString(), type);

//                     for (HistoryModel item : historyMap.values()) {
//                         addHistoryItem("\ud83d\udcc1", item.getFileName(), item.getStatus());
//                     }

//                     System.out.println("History fetched: " + historyMap.size());
//                 } else {
//                     System.out.println("No history found.");
//                 }

//             } else {
//                 System.out.println("Error: Response Code " + responseCode);
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     private void addHistoryItem(String icon, String name, String status) {
//         String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

//         Label iconLabel = new Label(icon);
//         iconLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

//         Label nameLabel = new Label(name);
//         nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

//         Label statusLabel = new Label(status);
//         statusLabel.setTextFill(Color.GREEN);
//         statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

//         Label dateTimeLabel = new Label(currentDateTime);
//         dateTimeLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 12));
//         dateTimeLabel.setTextFill(Color.GRAY);

//         VBox infoBox = new VBox(3, nameLabel, statusLabel, dateTimeLabel);
//         infoBox.setAlignment(Pos.CENTER_LEFT);

//         CheckBox checkBox = new CheckBox();
//         checkBoxes.add(checkBox);

//         checkBox.setOnAction(e -> {
//             HBox fileBox = (HBox) checkBox.getParent();
//             if (checkBox.isSelected()) {
//                 fileBox.setStyle("-fx-background-color: #d0f0c0; -fx-border-color: black; -fx-border-width: 1;");
//                 selectedItems.add(fileBox);
//                 deleteIcon.setVisible(true);
//                 cancelIcon.setVisible(true);
//             } else {
//                 fileBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
//                 selectedItems.remove(fileBox);
//                 if (selectedItems.isEmpty()) {
//                     deleteIcon.setVisible(false);
//                     cancelIcon.setVisible(false);
//                 }
//             }
//         });

//         Button backBtn = new Button("← Back");
//         backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
//         backBtn.setOnAction(e -> {
//             try {
//                 new HomePage().start(stage);
//             } catch (Exception ex) {
//                 ex.printStackTrace();
//             }
//         });

//         HBox fileBox = new HBox(10, checkBox, iconLabel, infoBox);
//         fileBox.setPadding(new Insets(10));
//         fileBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
//         fileBox.setAlignment(Pos.CENTER_LEFT);
//         fileBox.setMaxWidth(450);
//         historyItems.getChildren().add(fileBox);

//     }

//     private void showAlert(String message) {
//         Alert alert = new Alert(Alert.AlertType.INFORMATION);
//         alert.setHeaderText(null);
//         alert.setTitle("Info");
//         alert.setContentText(message);
//         alert.showAndWait();
//     }
// }

package com.sentinal.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sentinal.model.HistoryModel;
import com.sentinal.service.FirebaseDeleteService;
import com.sentinal.session.HistorySession;
import com.sentinal.session.Session;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HistoryPage extends Application {

    private final VBox historyItems = new VBox(15);
    private final Set<HBox> selectedItems = new HashSet<>();
    private final Label deleteIcon = new Label("\uD83D\uDDD1"); // 🗑
    private final Label cancelIcon = new Label("\u274C"); // ❌
    private final List<CheckBox> checkBoxes = new ArrayList<>();
    private Stage stage;


    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Title and top icons
        Text historyText = new Text("History");
        historyText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Hyperlink selectAllLink = new Hyperlink("Select All");
        selectAllLink.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        deleteIcon.setStyle("-fx-cursor: hand;");
        cancelIcon.setStyle("-fx-cursor: hand;");
        deleteIcon.setVisible(false);
        cancelIcon.setVisible(false);

        HBox topBox = new HBox(10, historyText, selectAllLink, deleteIcon, cancelIcon);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(15));

        historyItems.setPadding(new Insets(10));
        historyItems.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(historyItems);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        VBox centerLayout = new VBox(10, topBox, scrollPane);
        centerLayout.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(centerLayout);
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        backBtn.setPrefWidth(100);
        backBtn.setOnAction(e -> {
            try {
                new HomePage().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox backBtnBox = new HBox(backBtn);
        backBtnBox.setAlignment(Pos.CENTER);
        backBtnBox.setPadding(new Insets(10));
        root.setBottom(backBtnBox);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("History - ShareX");
        primaryStage.show();

        // Load history
        loadHistoryFromFirebase(); // 🔁 Change this if you're still using Firebase

        // Select All Logic
        selectAllLink.setOnAction(e -> {
            if (historyItems.getChildren().isEmpty()) {
                showAlert("No data found!");
                return;
            }

            selectedItems.clear();
            for (CheckBox box : checkBoxes) {
                box.setSelected(true);
                Parent parent = box.getParent();
                if (parent instanceof HBox hbox) {
                    hbox.setStyle("-fx-background-color: #d0f0c0; -fx-border-color: black; -fx-border-width: 1;");
                    selectedItems.add(hbox);
                }
            }
            deleteIcon.setVisible(true);
            cancelIcon.setVisible(true);
        });

        // Cancel Selection
        cancelIcon.setOnMouseClicked(e -> {
            for (CheckBox box : checkBoxes) {
                box.setSelected(false);
                Parent parent = box.getParent();
                if (parent instanceof HBox hbox) {
                    hbox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
                }
            }
            selectedItems.clear();
            deleteIcon.setVisible(false);
            cancelIcon.setVisible(false);
        });

        // Delete Selected
        // Delete Selected
deleteIcon.setOnMouseClicked(e -> {
    if (selectedItems.isEmpty()) {
        showAlert("No file selected!");
        return;
    }

    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmAlert.setTitle("Delete Confirmation");
    confirmAlert.setHeaderText("Are you sure you want to delete selected item(s)?");
    confirmAlert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            // copy selected items so we can modify the set safely
            List<HBox> itemsToDelete = new ArrayList<>(selectedItems);
            String userEmail = Session.getLoggedInEmail();

            // gather firebase keys
            List<String> keysToDelete = new ArrayList<>();
            for (HBox h : itemsToDelete) {
                Object ud = h.getUserData();
                if (ud instanceof String) keysToDelete.add((String) ud);
            }

            // perform network deletes in background thread
            new Thread(() -> {
                for (String key : keysToDelete) {
                    FirebaseDeleteService.deleteHistoryItem(userEmail, key);
                }

                // update UI on FX thread
                Platform.runLater(() -> {
                    historyItems.getChildren().removeAll(itemsToDelete);

                    // clear selection state and checkboxes
                    for (HBox removed : itemsToDelete) {
                        // try to unselect checkbox if present
                        for (Node n : removed.getChildren()) {
                            if (n instanceof CheckBox) ((CheckBox) n).setSelected(false);
                        }
                    }
                    selectedItems.removeAll(itemsToDelete);
                    checkBoxes.removeIf(CheckBox::isSelected);

                    deleteIcon.setVisible(false);
                    cancelIcon.setVisible(false);

                    if (historyItems.getChildren().isEmpty()) {
                        showAlert("Deleted successfully!\nNo data found.");
                    } else {
                        showAlert("Deleted successfully!");
                    }
                });
            }).start();
        }
    });
});


    }

    private void loadHistoryFromFirebase() {
        String userId = HistorySession.getCurrentUserId();
        if (userId == null || userId.isEmpty()) {
            System.out.println("User ID is null or empty.");
            return;
        }

        userId = userId.replace("@", "_").replace(".", "_"); // Firebase-safe format
        String firebaseUrl = "https://sentinal3-94a9e-default-rtdb.firebaseio.com/history/" + userId + ".json";

        try {
            URL url = new URL(firebaseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                reader.close();

                if (!json.toString().equals("null")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String, HistoryModel>>() {
                    }.getType();
                    Map<String, HistoryModel> historyMap = gson.fromJson(json.toString(), type);

                    for (HistoryModel item : historyMap.values()) {
                        addHistoryItem("\ud83d\udcc1", item.getFileName(), item.getStatus());
                    }

                    System.out.println("History fetched: " + historyMap.size());
                } else {
                    System.out.println("No history found.");
                }

            } else {
                System.out.println("Error: Response Code " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addHistoryItem(String icon, String name, String status) {
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label statusLabel = new Label(status);
        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        Label dateTimeLabel = new Label(currentDateTime);
        dateTimeLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 12));
        dateTimeLabel.setTextFill(Color.GRAY);

        VBox infoBox = new VBox(3, nameLabel, statusLabel, dateTimeLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        CheckBox checkBox = new CheckBox();
        checkBoxes.add(checkBox);

        checkBox.setOnAction(e -> {
            HBox fileBox = (HBox) checkBox.getParent();
            if (checkBox.isSelected()) {
                fileBox.setStyle("-fx-background-color: #d0f0c0; -fx-border-color: black; -fx-border-width: 1;");
                selectedItems.add(fileBox);
                deleteIcon.setVisible(true);
                cancelIcon.setVisible(true);
            } else {
                fileBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
                selectedItems.remove(fileBox);
                if (selectedItems.isEmpty()) {
                    deleteIcon.setVisible(false);
                    cancelIcon.setVisible(false);
                }
            }
        });

        HBox fileBox = new HBox(10, checkBox, iconLabel, infoBox);
        fileBox.setPadding(new Insets(10));
        fileBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        fileBox.setAlignment(Pos.CENTER_LEFT);
        fileBox.setMaxWidth(450);

        historyItems.getChildren().add(fileBox);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
