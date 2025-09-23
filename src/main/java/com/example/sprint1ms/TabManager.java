package com.example.sprint1ms;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
/*
 * The TabManager class is used to create a tab pane that organizes tabs of easels.
 */
public class TabManager {
    protected TabPane tabPane;
    protected Easel currentEasel;
    protected ToolBarManager toolBarManager;

    /*
     * The default constructor for creating the TabManager
     *
     * @param toolbar Tab manager is passed a toolbar so that tools are initialized to all easels in the tab manager.
     */
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
    /*
    * The addNewTab() method is used to add new tabs to the tab manager, utilized within the menu manager
    *
    * @param title Sets the title of the tab to the string passed to the method.
     */
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
