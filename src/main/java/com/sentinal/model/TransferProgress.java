package com.sentinal.model;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransferProgress {
  private final DoubleProperty progress = new SimpleDoubleProperty(0);
  private final StringProperty percentageText = new SimpleStringProperty("0%");
  private final StringProperty speedText = new SimpleStringProperty("");
  private final StringProperty timeRemainingText = new SimpleStringProperty("");
  private final StringProperty sizeText = new SimpleStringProperty("");
  private final StringProperty fileCountText = new SimpleStringProperty("");
  private final AtomicBoolean isCancelled = new AtomicBoolean(false);

  // Property accessors
  public void cancelTransfer() {
    isCancelled.set(true);
  }

  public boolean isCancelled() {
    return isCancelled.get();
  }

  public DoubleProperty progressProperty() {
    return progress;
  }

  public StringProperty percentageTextProperty() {
    return percentageText;
  }

  public StringProperty speedTextProperty() {
    return speedText;
  }

  public StringProperty timeRemainingTextProperty() {
    return timeRemainingText;
  }

  public StringProperty sizeTextProperty() {
    return sizeText;
  }

  public StringProperty fileCountTextProperty() {
    return fileCountText;
  }

  // Update method
  public void update(long bytesSent, long totalBytes, double speed, long timeRemaining, int filesSent, int totalFiles) {
    double percent = (double) bytesSent / totalBytes;
    progress.set(percent);
    percentageText.set(String.format("%.0f%%", percent * 100));
    speedText.set(String.format("%.2f MB/s", speed));
    timeRemainingText.set(String.format("%d sec", timeRemaining));
    sizeText.set(String.format("%.2fMB / %.2fMB", bytesSent / 1_000_000.0, totalBytes / 1_000_000.0));
    fileCountText.set(String.format("%d / %d files", filesSent, totalFiles));
  }
}
