package com.example.sprint1ms;

import java.awt.*;

public class NotifyClass {
    protected SystemTray tray;
    protected Image image;
    protected TrayIcon trayIcon;
    public NotifyClass() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported.");
            return;
        }

        // Get system tray
        tray = SystemTray.getSystemTray();

        // Create an image icon (required for TrayIcon, even if not shown)
        image = Toolkit.getDefaultToolkit().createImage("icon.png");

        trayIcon = new TrayIcon(image, "Java Notification");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Java AWT TrayIcon");

    }
    protected void displayNotification(String title, String message){
        try {
            tray.add(trayIcon);
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);

            // Optional: Wait so user can see notification before program exits
            Thread.sleep(5000);

            tray.remove(trayIcon); // Clean up
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
