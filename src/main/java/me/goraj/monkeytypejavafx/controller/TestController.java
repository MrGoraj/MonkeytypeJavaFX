package me.goraj.monkeytypejavafx.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextArea;

import java.util.Timer;
import java.util.TimerTask;

public class TestController {

    private SimpleIntegerProperty currentWordIndex;
    private String currentParagraph;
    private TextArea textArea;
    private Timer timer;

    public TestController() {
        this.currentWordIndex = new SimpleIntegerProperty(0);
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public boolean startTest(String language, int durationSeconds) {
        // fetch a paragraph in the selected language and set it to currentParagraph
        currentParagraph = getParagraph(language);
        updateTextArea();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int countdown = durationSeconds;

            @Override
            public void run() {
                if (countdown <= 0) {
                    timer.cancel();
                    return;
                }
                countdown--;
                // update UI
            }
        }, 0, 1000);
        return true;
    }

    public String getCurrentParagraph() {
        return currentParagraph;
    }

    public int getCurrentWordIndex() {
        return currentWordIndex.get();
    }

    public void setCurrentWordIndex(int index) {
        this.currentWordIndex.set(index);
    }

    public boolean checkWord(String typedWord) {
        String[] words = currentParagraph.split(" ");
        if (currentWordIndex.get() < words.length) {
            String correctWord = words[currentWordIndex.get()];
            if (correctWord.equals(typedWord)) {
                setCurrentWordIndex(currentWordIndex.get() + 1);
                return true;
                // maybe provide some user feedback that the word was correct
            }
        }
        return false;
    }

    public void resetTest() {
        if (timer != null) {
            timer.cancel();
        }
        setCurrentWordIndex(0);
        currentParagraph = null;
        updateTextArea();
    }

    private String getParagraph(String language) {
        // placeholder method that should return a paragraph in the selected language
        return "This is a test paragraph";
    }

    void updateTextArea() {
        textArea.setText(currentParagraph);
    }
}
