package com.example.sprint1ms;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Stack;

/*
* The Easel class is used to hold all drawing related functions and properties such as the canvas and graphics context.
 */
public class Easel {
    protected Canvas canvas;
    protected GraphicsContext gc;
    protected double lastX, lastY;
    protected boolean isDirty;
    protected File currentFile = null;
    protected WritableImage canvasSnapshot;//used for live drawing functionality
    protected Stack<WritableImage> undoStack;//Undo stack used for holding snapshots of canvas
    protected Stack<WritableImage> redoStack;
    /*
    * The default constructor for the easel. Creates the canvas and mouse handler events
    *
    * @param toolbar  A ToolBarManager object that is used to check which tool is being used and to
    *                 apply drawing settings to shapes and lines.
     */
    public Easel(ToolBarManager toolbar) {
        // Drawing with mouse
        canvas = new Canvas(1000,800);
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,1000,800);
        gc.setFill(Color.BLACK);//setting fill color back to black so text appears on canvas
        undoStack.push(canvas.snapshot(null,null));//Adds initial canvas to stack

        canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
            switch (toolbar.currentTool) {
                case EYEDROPPER -> {
                    WritableImage snap = canvas.snapshot(null, null);
                    PixelReader reader = snap.getPixelReader();
                    if (reader != null) {
                        toolbar.currentColor = reader.getColor((int) lastX, (int) lastY);
                        toolbar.colorPicker.setValue(toolbar.currentColor);
                    }//Grabs color from PixelReader at X and Y position.
                }
                case TEXT -> gc.fillText(toolbar.text.getText(),lastX,lastY);
            }
            canvasSnapshot = canvas.snapshot(null, null);
            undoStack.push(canvas.snapshot(null, null));//Adds canvas snapshot to stack before modifications
            if (!redoStack.isEmpty()) {redoStack.clear();}//Clears redo stack if edits are made
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
            else {
                gc.drawImage(canvasSnapshot, 0, 0, canvas.getWidth(), canvas.getHeight());
                switch (toolbar.currentTool) {
                    case LINE -> drawLine(toolbar, lastX, lastY, e.getX(), e.getY());
                    case RECT -> drawRect(toolbar, lastX, lastY, e.getX(), e.getY());
                    case SQUARE -> drawSquare(toolbar, lastX, lastY, e.getX(), e.getY());
                    case CIRCLE -> drawCircle(toolbar, lastX, lastY, e.getX(), e.getY());
                    case ELLIPSE -> drawEllipse(toolbar, lastX, lastY, e.getX(), e.getY());
                    case TRIANGLE -> drawTriangle(toolbar, lastX, lastY, e.getX(), e.getY());
                    case HEX -> drawHex(toolbar, lastX, lastY, e.getX(), e.getY());
                    case RTRIANGLE -> drawRTriangle(toolbar, lastX, lastY, e.getX(), e.getY());
                    case OCT -> drawOct(toolbar, lastX, lastY, e.getX(), e.getY());
                    case POLYGON -> drawPolygon(toolbar, lastX,lastY, e.getX(), e.getY(), toolbar.sides);
                }
            }
        });
        canvas.setOnMouseReleased(e ->{
            switch (toolbar.currentTool) {
                case LINE -> drawLine(toolbar, lastX, lastY, e.getX(), e.getY());
                case RECT -> drawRect(toolbar, lastX, lastY, e.getX(), e.getY());
                case SQUARE -> drawSquare(toolbar, lastX, lastY, e.getX(), e.getY());
                case CIRCLE -> drawCircle(toolbar, lastX, lastY, e.getX(), e.getY());
                case ELLIPSE -> drawEllipse(toolbar, lastX, lastY, e.getX(), e.getY());
                case TRIANGLE -> drawTriangle(toolbar, lastX, lastY, e.getX(), e.getY());
                case HEX -> drawHex(toolbar, lastX, lastY, e.getX(), e.getY());
                case RTRIANGLE -> drawRTriangle(toolbar, lastX, lastY, e.getX(), e.getY());
                case OCT -> drawOct(toolbar, lastX, lastY, e.getX(), e.getY());
                case POLYGON -> drawPolygon(toolbar, lastX,lastY, e.getX(), e.getY(), toolbar.sides);
            }
        });
    }
    /*
    *Applies drawing settings that are set in the toolbar, such as the pen's color, width, and whether the line is dashed.
    *
    * @param toolbar  ToolBarManager object that contains necessary data for modifying shapes / drawings.
     */
    private void applyStrokeStyle(ToolBarManager toolBar) {
        gc.setLineWidth(toolBar.lineWidth);
        gc.setStroke(toolBar.currentColor);
        if (toolBar.dashed) gc.setLineDashes(10);
        else gc.setLineDashes(0);
    }
    // -- Shape Drawing Functions --
    private void drawLine(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokeLine(x1,y1,x2,y2);
        isDirty = true;
    }
    private void drawRect(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokeRect(Math.min(x1, x2),Math.min(y1,y2),Math.abs(x2-x1),Math.abs(y1-y2));
        isDirty = true;
    }
    private void drawCircle(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        Double r = Math.max(Math.abs(x2-x1), Math.abs(y2-y1));
        gc.strokeOval(Math.min(x1,x2),Math.min(y1,y2),r, r);
        isDirty = true;
    }
    private void drawEllipse(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokeOval(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x2-x1),Math.abs(y2-y1));
        isDirty = true;
    }
    private void drawTriangle(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokePolygon(new double[]{x1,x2,(x1+x2)/2},new double[]{y2,y2,y1},3);
        isDirty = true;
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
        isDirty = true;
    }
    private void drawOct(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        double centerX = (x1+x2)/2;
        double centerY = (y1+y2)/2;
        double r = Math.max(Math.abs(x2-x1), Math.abs(y2-y1))/2;
        double[] xpoints = new double[8];
        double[] ypoints = new double[8];
        for (int i=0; i<8; i++) {
            xpoints[i] = centerX+r * Math.cos(Math.PI /4*i);
            ypoints[i] = centerY+r * Math.sin(Math.PI/4*i);
        }
        gc.strokePolygon(xpoints,ypoints,8);
        isDirty = true;
    }
    private void drawPolygon(ToolBarManager toolbar, double x1, double y1, double x2, double y2, int sides) {
        applyStrokeStyle(toolbar);
        double centerX = (x1+x2)/2;
        double centerY = (y1+y2)/2;
        double r = Math.max(Math.abs(x2-x1), Math.abs(y2-y1))/2;
        double[] xpoints = new double[sides];
        double[] ypoints = new double[sides];
        double angleStep = 2*Math.PI/sides;
        for (int i=0; i<sides; i++) {
            xpoints[i] = centerX+r * Math.cos(angleStep*i);
            ypoints[i] = centerY+r * Math.sin(angleStep*i);
        }
        gc.strokePolygon(xpoints,ypoints,sides);
        isDirty = true;
    }
    private void drawSquare(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        double sidelength = Math.max(Math.abs(x2-x1),Math.abs(y2-y1));
        gc.strokeRect(Math.min(x1,x2),Math.min(y1,y2),sidelength, sidelength);
        isDirty = true;
    }
    private void drawRTriangle(ToolBarManager toolbar, double x1, double y1, double x2, double y2) {
        applyStrokeStyle(toolbar);
        gc.strokePolygon(new double[]{x1,x2,x1},new double[]{y1,y1,y2},3);
        isDirty = true;
    }
}
