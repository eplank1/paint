package com.example.sprint1ms;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class TabManager {
    protected TabPane tabPane;
    protected Easel currentEasel;
    protected ToolBarManager toolBarManager;

    public TabManager(ToolBarManager toolbar) {
        tabPane = new TabPane();
        toolBarManager = toolbar;
        currentEasel = addNewTab( "Untitled1");

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, selectedTab) -> {
            if (selectedTab != null) {
                currentEasel = (Easel) selectedTab.getUserData();
            }
        });

    }

    protected Easel addNewTab(String title) {
        Easel easel = new Easel(toolBarManager);
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
