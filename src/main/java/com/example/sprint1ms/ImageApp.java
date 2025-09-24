package com.example.sprint1ms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ImageApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        ToolBarManager toolBarHelper = new ToolBarManager();
        TabManager tabManager = new TabManager(toolBarHelper);
        MenuManager menuManager = new MenuManager(tabManager, primaryStage, toolBarHelper);

        root.setTop(menuManager.menuBar);
        root.setBottom(toolBarHelper.toolBar);
        root.setCenter(tabManager.tabPane);

        Scene scene = new Scene(root, 1000, 700);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.isControlDown()){
                if (e.getCode() == KeyCode.S) menuManager.saveImage(false);
            }
        });
        Label timerLabel = new Label("Next save in 300s");
        final int[] counter = {300};
        Timeline autoSave = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    counter[0]--;
                    timerLabel.setText("Next save in " +  counter[0]+"s");
                    if (counter[0] <= 0) {
                        menuManager.saveImage(false);
                        counter[0] = 300;
                    }//Saves Image and resets counter
                })

        );
        ToggleButton showTimerButton = new ToggleButton("Show Timer");
        toolBarHelper.toolBar.getChildren().add(showTimerButton);
        showTimerButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            timerLabel.setVisible(newVal);
        });
        ToolBar timerBar = new ToolBar(timerLabel, showTimerButton);
        autoSave.setCycleCount(Timeline.INDEFINITE);
        autoSave.play();
        root.setRight(timerBar);
        primaryStage.setTitle("Image Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}