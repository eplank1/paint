package com.example.sprint1ms;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class TabManager {
    protected TabPane tabPane;
    protected Easel currentEasel;

    public TabManager(ToolBarManager toolbar) {
        tabPane = new TabPane();
        currentEasel = addNewTab(toolbar, "Untitled1");

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, selectedTab) -> {
            if (selectedTab != null) {
                System.out.println("Switched to tab: " + selectedTab.getText());
                currentEasel = (Easel) selectedTab.getUserData();
            }
            if (oldTab != null) {
                System.out.println("Left tab: " + oldTab.getText());
            }
        });

    }

    protected Easel addNewTab(ToolBarManager toolbar, String title) {
        Easel easel = new Easel(toolbar);
        // Scrollable image + canvas
        ScrollPane scrollPane = new ScrollPane();
        BorderPane imagePane = new BorderPane();
        imagePane.setCenter(easel.canvas);
        scrollPane.setContent(imagePane);
        Tab tab = new Tab(title, scrollPane);
        tab.setUserData(easel);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        return easel;
    }
}
