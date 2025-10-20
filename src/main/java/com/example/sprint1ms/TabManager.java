package com.example.sprint1ms;

import com.sun.net.httpserver.HttpServer;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.InetSocketAddress;


public class TabManager {
    protected TabPane tabPane;
    protected Easel currentEasel;
    protected ToolBarManager toolBarManager;
    protected HttpServer server;
    protected HttpHandling httpHandler;

    public TabManager(ToolBarManager toolbar) {
        tabPane = new TabPane();
        toolBarManager = toolbar;
        currentEasel = addNewTab( "Untitled1");
        httpHandler = new HttpHandling(currentEasel);
        try {server = HttpServer.create(new InetSocketAddress(8000),0);}catch (IOException e){e.printStackTrace();}
        server.createContext("/Images", httpHandler);
        server.start();

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, selectedTab) -> {
            if (selectedTab != null) {
                currentEasel = (Easel) selectedTab.getUserData();
                httpHandler.easel = currentEasel;
                toolBarManager.logHelper.addLog("Tab "+ selectedTab.getText() + " selected.");
            }
            else  {
                httpHandler.easel = null;
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
        toolBarManager.logHelper.addLog("New Tab, " + title + ", created.");
        return easel;
    }
}
