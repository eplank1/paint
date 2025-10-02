package com.example.sprint1ms;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    protected Image selectImg;
    protected Image copyImg;
    protected static String originalFormat;
    protected static String convertFormat;
    /*
    * The default constructor for the easel. Creates the canvas and mouse handler events
    *
    * @param toolbar  A ToolBarManager object that is used to check which tool is being used and to
    *                 apply drawing settings to shapes and lines.
     */
    public Easel(ToolBarManager toolbar) {
        // Drawing with mouse
        canvas = new Canvas(1000,800);
        originalFormat = "png"; //set default format to be png for an empty canvas
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,1000,800);
        gc.setFill(Color.BLACK);//setting fill color back to black so text appears on canvas

        canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
            undoStack.push(canvas.snapshot(null, null));//Adds canvas snapshot to stack before modifications
            if (!redoStack.isEmpty()) {redoStack.clear();}//Clears redo stack if edits are made
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
                    case STAR -> drawStar(toolbar, lastX, lastY, e.getX(), e.getY(), toolbar.sides);
                    case MOVE -> gc.drawImage(selectImg, e.getX(), e.getY());
                    case PASTE -> gc.drawImage(copyImg, e.getX(), e.getY());
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
                case STAR -> drawStar(toolbar, lastX, lastY, e.getX(), e.getY(), toolbar.sides);
                case SELECT -> selectImg = select(lastX, lastY,  e.getX(), e.getY());
                case MOVE -> {
                    if (selectImg!= null) {
                        gc.drawImage(selectImg, e.getX(), e.getY());
                        selectImg = null;
                    }
                }
                case COPY -> copyImg = copy(lastX, lastY, e.getX(), e.getY());
                case PASTE -> gc.drawImage(copyImg, e.getX(), e.getY());
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
    private void drawStar(ToolBarManager toolbar, double x1, double y1, double x2, double y2, int points){
        applyStrokeStyle(toolbar);
        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;
        double outerRadius = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)) / 2;
        double innerRadius = outerRadius * 0.5;

        int totalPoints = points * 2;
        double[] xpoints = new double[totalPoints];
        double[] ypoints = new double[totalPoints];

        double angleStep = Math.PI / points;

        for (int i = 0; i < totalPoints; i++) {
            double angle = i * angleStep;
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xpoints[i] = centerX + radius * Math.cos(angle);
            ypoints[i] = centerY + radius * Math.sin(angle);
        }

        gc.strokePolygon(xpoints, ypoints, totalPoints);
        isDirty=true;
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
    private Image select(double x1, double y1, double x2, double y2) {
        int minX = (int) Math.min(x1, x2);
        int minY = (int) Math.min(y1, y2);
        int width = (int) Math.abs(x2 - x1);
        int height = (int) Math.abs(y2 - y1);
        // Take snapshot of canvas
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);
        // Extract the selected region
        Image subImage = new WritableImage(snapshot.getPixelReader(), minX, minY, width, height);
        //clear the selected area
        gc.setFill(Color.WHITE);
        gc.fillRect(minX, minY, width, height);
        gc.setFill(Color.BLACK);

        return subImage;
    }
    private Image copy(double x1, double y1, double x2, double y2) {
        int minX = (int) Math.min(x1, x2);
        int minY = (int) Math.min(y1, y2);
        int width = (int) Math.abs(x2 - x1);
        int height = (int) Math.abs(y2 - y1);
        // Take snapshot of canvas
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);
        // Extract the selected region
        Image subImage = new WritableImage(snapshot.getPixelReader(), minX, minY, width, height);

        return subImage;
    }

}
