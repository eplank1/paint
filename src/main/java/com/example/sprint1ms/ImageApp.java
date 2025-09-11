package com.example.sprint1ms;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


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
        primaryStage.setTitle("Image Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}