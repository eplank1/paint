package com.example.sprint1ms;

import java.awt.*;

/**
 * The type Notify class.
 */
public class NotifyClass {
    /**
     * The Tray.
     */
    protected SystemTray tray;
    /**
     * The Image.
     */
    protected Image image;
    /**
     * The Tray icon.
     */
    protected TrayIcon trayIcon;

    /**
     * Instantiates a new Notify class.
     */
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

    /**
     * Display notification.
     *
     * @param title   the title
     * @param message the message
     */
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
