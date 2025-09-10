package com.example.sprint1ms;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
            if (toolbar.currentTool == ToolBarManager.Tool.EYEDROPPER) {
                WritableImage snap = canvas.snapshot(null, null);
                PixelReader reader = snap.getPixelReader();
                if (reader != null) {
                    toolbar.currentColor = reader.getColor((int) lastX, (int) lastY);
                }//Grabs color from PixelReader at X and Y position.
            }//If statement to handle the eyedropper tool
        });

        canvas.setOnMouseDragged(e -> {
            if (toolbar.currentTool == ToolBarManager.Tool.PENCIL || toolbar.currentTool == ToolBarManager.Tool.ERASER ) {
                applyStrokeStyle(toolbar);
                gc.setStroke(toolbar.currentTool == ToolBarManager.Tool.ERASER ? Color.WHITE : toolbar.currentColor);
                gc.strokeLine(lastX, lastY, e.getX(), e.getY());
                lastX = e.getX();
                lastY = e.getY();
                isDirty = true;
            }
        });
        canvas.setOnMouseReleased(e ->{
            switch (toolbar.currentTool) {
                case LINE -> drawLine(toolbar, lastX, lastY, e.getX(), e.getY());
                case RECT -> drawRect(toolbar, lastX, lastY, e.getX(), e.getY());
                case CIRCLE -> drawCircle(toolbar, lastX, lastY, e.getX(), e.getY());
                case ELLIPSE -> drawEllipse(toolbar, lastX, lastY, e.getX(), e.getY());
                case TRIANGLE -> drawTriangle(toolbar, lastX, lastY, e.getX(), e.getY());
                case HEX -> drawHex(toolbar, lastX, lastY, e.getX(), e.getY());
            }
        });
    }
    private void applyStrokeStyle(ToolBarManager toolBar) {
        gc.setLineWidth(toolBar.lineWidth);
        gc.setStroke(toolBar.currentColor);
        if (toolBar.dashed) gc.setLineDashes(10);
        else gc.setLineDashes(0);
    }
    private void drawLine(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokeLine(x1,y1,x2,y2);
    }
    private void drawRect(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokeRect(Math.min(x1, x2),Math.min(y1,y2),Math.abs(x2-x1),Math.abs(y1-y2));
    }
    private void drawCircle(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        Double r = Math.max(Math.abs(x2-x1), Math.abs(y2-y1));
        gc.strokeOval(Math.min(x1,x2),Math.min(y1,y2),r, r);
    }
    private void drawEllipse(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokeOval(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x2-x1),Math.abs(y2-y1));
    }
    private void drawTriangle(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokePolygon(new double[]{x1,x2,(x1+x2)/2},new double[]{y2,y2,y1},3);
    }
    private void drawHex(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        double centerX = (x1+x2)/2;
        double centerY = (y1+y2)/2;
        double r = Math.max(Math.abs(x2-x1), Math.abs(y2-y1))/2;
        double[] xpoints = new double[6];
        double[] ypoints = new double[6];
        for (int i=0; i<6; i++) {
            xpoints[i] = centerX+r * Math.cos(Math.PI /3*i);
            ypoints[i] = centerY+r * Math.sin(Math.PI/3*i);
        }
        gc.strokePolygon(xpoints,ypoints,6);
    }
}
