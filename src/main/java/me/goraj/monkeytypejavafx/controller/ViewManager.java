package me.goraj.monkeytypejavafx.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import me.goraj.monkeytypejavafx.service.LanguageSelector;
import me.goraj.monkeytypejavafx.service.TestDurationSelector;

public class ViewManager {

    private SimpleBooleanProperty testRunning;
    private SimpleBooleanProperty testPaused;
    private SimpleIntegerProperty testDurationSeconds;
    private TestController testController;
    private LanguageSelector languageSelector;
    private TestDurationSelector durationSelector;
    private Label statusLabel;
    private TextArea textArea;

    public ViewManager(SimpleBooleanProperty testRunning, SimpleBooleanProperty testPaused,
                       SimpleIntegerProperty testDurationSeconds, TestController testController,
                       LanguageSelector languageSelector, TestDurationSelector durationSelector) {
        this.testRunning = testRunning;
        this.testPaused = testPaused;
        this.testDurationSeconds = testDurationSeconds;
        this.testController = testController;
        this.languageSelector = languageSelector;
        this.durationSelector = durationSelector;
        this.statusLabel = new Label();
        this.textArea = new TextArea();

        testController.setTextArea(textArea);
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Language menu
        Menu languageMenu = new Menu("Language");
        languageSelector.getLanguages().forEach(language -> {
            MenuItem menuItem = new MenuItem(language);
            menuItem.setOnAction(event -> {
                languageSelector.setSelectedLanguage(language);
            });
            languageMenu.getItems().add(menuItem);
        });
        menuBar.getMenus().add(languageMenu);

        // Test duration menu
        Menu durationMenu = new Menu("Test Duration");
        MenuItem oneMinuteItem = new MenuItem("1 minute");
        oneMinuteItem.setOnAction(event -> durationSelector.setSelectedDurationSeconds(60));
        MenuItem twoMinutesItem = new MenuItem("2 minutes");
        twoMinutesItem.setOnAction(event -> durationSelector.setSelectedDurationSeconds(120));
        MenuItem fiveMinutesItem = new MenuItem("5 minutes");
        fiveMinutesItem.setOnAction(event -> durationSelector.setSelectedDurationSeconds(300));
        durationMenu.getItems().addAll(oneMinuteItem, twoMinutesItem, fiveMinutesItem);
        menuBar.getMenus().add(durationMenu);

        return menuBar;
    }

    public Node createCenterPane() {
        if (testRunning.get()) {
            textArea.setEditable(true);
            textArea.setOnKeyReleased(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    String[] words = testController.getCurrentParagraph().split(" ");
                    int currentWordIndex = testController.getCurrentWordIndex();
                    if (currentWordIndex < words.length) {
                        String currentWord = words[currentWordIndex];
                        String enteredWord = textArea.getText().trim();
                        handleWordTyped(currentWord, enteredWord);
                        testController.setCurrentWordIndex(currentWordIndex + 1);
                        testController.updateTextArea();
                    }
                }
            });
            return textArea;
        } else {
            VBox centerPane = new VBox();
            centerPane.setAlignment(Pos.CENTER);
            centerPane.setSpacing(20);

            // Language Selection
            ComboBox<String> languageComboBox = new ComboBox<>();
            languageComboBox.setItems(languageSelector.getLanguages());
            languageComboBox.setPromptText("Select Language");

            // Duration Selection
            ComboBox<Integer> durationComboBox = new ComboBox<>();
            durationComboBox.setItems(durationSelector.getDurations());
            durationComboBox.setPromptText("Select Duration (seconds)");

            // Start Test Button
            Button startButton = new Button("Start Test");
            startButton.setOnAction(event -> {
                String selectedLanguage = languageComboBox.getSelectionModel().getSelectedItem();
                int selectedDuration = durationComboBox.getSelectionModel().getSelectedItem();
                if (selectedLanguage == null || selectedDuration == 0) {
                    // language or duration not selected
                    statusLabel.setText("Please select language and duration.");
                } else {
                    // start test
                    testController.startTest(selectedLanguage, selectedDuration);
                }
            });

            centerPane.getChildren().addAll(languageComboBox, durationComboBox, startButton);
            return centerPane;
        }
    }

    public Node createBottomPane() {
        if (testRunning.get()) {
            return statusLabel;
        } else {
            Button stopButton = new Button("Stop Test");
            stopButton.setOnAction(event -> {
                testController.resetTest();
            });
            return stopButton;
        }
    }

    private void handleWordTyped(String currentWord, String enteredWord) {
        if (testController.checkWord(enteredWord)) {
            statusLabel.setText("Correct word!");
        } else {
            statusLabel.setText("Incorrect word. The correct word was " + currentWord);
        }
    }
}