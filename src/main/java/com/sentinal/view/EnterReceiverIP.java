package com.sentinal.view;

import com.sentinal.controller.EnterReceiverIPController;
import com.sentinal.model.EnterReceiverIPModel;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class EnterReceiverIP {

    private final List<File> selectedFiles;
    private final Stage stage;

    public EnterReceiverIP(List<File> selectedFiles, Stage stage) {
        this.selectedFiles = selectedFiles;
        this.stage = stage;
    }

    public Scene getScene() {
        EnterReceiverIPModel model = new EnterReceiverIPModel(selectedFiles);
        EnterReceiverIPController controller = new EnterReceiverIPController(model, stage, selectedFiles);
        return controller.buildScene();
    }
}
