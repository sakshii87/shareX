package com.sentinal.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

import com.sentinal.controller.SelectedFilesController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectedFiles {


    private final List<File> selectedFiles ;
    private Stage stage;

    public SelectedFiles(List<File> selectedFiles, Stage stage) {
        this.selectedFiles = new ArrayList<>(selectedFiles);
        this.stage = stage;
    }

    public Scene getScene() {
        SelectedFilesController controller = new SelectedFilesController(selectedFiles, stage);
        return controller.buildScene(); // no need to pass stage
    }
}
