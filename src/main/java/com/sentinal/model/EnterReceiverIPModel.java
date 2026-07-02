package com.sentinal.model;

import java.io.File;
import java.util.List;

public class EnterReceiverIPModel {
    private final List<File> selectedFiles;

    public EnterReceiverIPModel(List<File> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }
}