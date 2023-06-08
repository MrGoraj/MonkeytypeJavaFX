package me.goraj.monkeytypejavafx.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LanguageSelector {

    private ObservableList<String> languages;
    private StringProperty selectedLanguage;

    public LanguageSelector() {
        languages = FXCollections.observableArrayList();
        selectedLanguage = new SimpleStringProperty();

        loadLanguages();
    }

    public ObservableList<String> getLanguages() {
        return languages;
    }

    public String getSelectedLanguage() {
        return selectedLanguage.get();
    }

    public StringProperty selectedLanguageProperty() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String language) {
        selectedLanguage.set(language);
    }

    private void loadLanguages() {
        try (Stream<Path> paths = Files.walk(Paths.get(getDictionaryDirectory()))) {
            List<String> languageList = paths.filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(fileName -> fileName.substring(0, fileName.lastIndexOf('.')))
                    .collect(Collectors.toList());

            languages.addAll(languageList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load languages");
        }
    }

    private String getDictionaryDirectory() {
        String currentDirectory = System.getProperty("user.dir");
        return currentDirectory + File.separator + "dictionary";
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
