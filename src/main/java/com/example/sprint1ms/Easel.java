package com.example.sprint1ms;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;

public class Easel {
    protected Canvas canvas;
    protected GraphicsContext gc;
    protected double lastX, lastY;
    protected boolean isDirty;
    protected File currentFile = null;

    public Easel(ToolBarManager toolbar) {
        // Drawing with mouse
        canvas = new Canvas(1000,800);
        gc = canvas.getGraphicsContext2D();


        canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });

        canvas.setOnMouseDragged(e -> {
            gc.setStroke(toolbar.currentColor);
            gc.setLineWidth(toolbar.lineWidth);
            gc.strokeLine(lastX, lastY, e.getX(), e.getY());
            lastX = e.getX();
            lastY = e.getY();
            isDirty = true;
        });
    }

}
