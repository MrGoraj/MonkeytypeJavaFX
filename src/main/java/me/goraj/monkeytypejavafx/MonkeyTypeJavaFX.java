package me.goraj.monkeytypejavafx;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Platform;
import me.goraj.monkeytypejavafx.controller.TestController;
import me.goraj.monkeytypejavafx.controller.ViewManager;
import me.goraj.monkeytypejavafx.service.LanguageSelector;
import me.goraj.monkeytypejavafx.service.TestDurationSelector;

public class MonkeyTypeJavaFX extends Application {
    private static final String TITLE = "MonkeyType";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final SimpleBooleanProperty isTestRunning = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty isTestPaused = new SimpleBooleanProperty(false);
    private final SimpleIntegerProperty testDurationInSeconds = new SimpleIntegerProperty(0);

    private LanguageSelector languageSelector;
    private TestDurationSelector durationSelector;
    private TestController testController;
    private ViewManager viewManager;
    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) {
        initializeComponents();

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        root.setTop(viewManager.createMenuBar());
        root.setCenter(viewManager.createCenterPane());

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> terminateApplication());
        primaryStage.show();
    }

    private void initializeComponents() {
        languageSelector = new LanguageSelector();
        durationSelector = new TestDurationSelector();
        textArea = new TextArea();

        testController = new TestController();
        viewManager = new ViewManager(isTestRunning, isTestPaused, testDurationInSeconds, testController, languageSelector, durationSelector);

        Label statusLabel = new Label();
        viewManager.setStatusLabel(statusLabel);
    }

    private void terminateApplication() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

