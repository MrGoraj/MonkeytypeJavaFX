package me.goraj.monkeytypejavafx.service;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TestDurationSelector {

    private ObservableList<Integer> durations;
    private IntegerProperty selectedDurationSeconds;

    public TestDurationSelector() {
        durations = FXCollections.observableArrayList();
        durations.addAll(15, 20, 45, 60, 90, 120, 300);
        selectedDurationSeconds = new SimpleIntegerProperty();

        // Ustawiamy domyślną wartość na 0
        selectedDurationSeconds.set(0);
    }

    public ObservableList<Integer> getDurations() {
        return durations;
    }

    public void setSelectedDurationSeconds(Integer durationSeconds) {
        selectedDurationSeconds.set(durationSeconds);
    }

    public Integer getSelectedDurationSeconds() {
        return selectedDurationSeconds.get();
    }

    public IntegerProperty selectedDurationSecondsProperty() {
        return selectedDurationSeconds;
    }
}
