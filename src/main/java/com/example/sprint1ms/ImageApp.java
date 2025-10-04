package com.example.sprint1ms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;


public class ImageApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        LogHelper logHelper = new LogHelper();


        ToolBarManager toolBarHelper = new ToolBarManager();
        TabManager tabManager = new TabManager(toolBarHelper);
        MenuManager menuManager = new MenuManager(tabManager, primaryStage, toolBarHelper);

        root.setTop(menuManager.menuBar);
        root.setBottom(toolBarHelper.toolBar);
        root.setCenter(tabManager.tabPane);

        Scene scene = new Scene(root, 1000, 700);
        //adding handlers for hot-keys
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.isControlDown()){
                if (e.getCode() == KeyCode.S) menuManager.saveImage(false);
                else if (e.getCode() == KeyCode.Z) menuManager.undoChanges();
                else if (e.getCode() == KeyCode.Y) menuManager.redoChanges();
            }
        });

        Label timerLabel = new Label("Next save in "+ menuManager.counter[0]+ "s");
        Timeline autoSave = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    menuManager.counter[0]--;
                    timerLabel.setText("Next save in " +  menuManager.counter[0]+"s");
                    if (menuManager.counter[0] <= 0) {
                        menuManager.saveImage(false);
                        menuManager.counter[0] = 300;
                    }//Saves Image and resets counter
                    else if (menuManager.counter[0] == 120) {
                        try {
                            File file = new File("C:\\Users\\Ethan\\Pictures\\Cyberpunk 2077\\photomode_18082025_104631.png");
                            final TrayIcon trayIcon = new TrayIcon(ImageIO.read(file));
                            trayIcon.displayMessage("Autosave in 2 minutes","The canvas will autosave in 2 minutes",TrayIcon.MessageType.INFO);
                        } catch (Exception ex) {
                            System.out.println("Cant believe you thought that would work");
                        }

                    }
                })

        );
        ToggleButton showTimerButton = new ToggleButton("Show Timer");
        toolBarHelper.toolBar.getChildren().add(showTimerButton);
        showTimerButton.selectedProperty().setValue(true);//Have show timer button be selected by default
        showTimerButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            timerLabel.setVisible(newVal);
            logHelper.addLog("Timer was turned off");
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