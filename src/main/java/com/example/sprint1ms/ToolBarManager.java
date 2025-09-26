package com.example.sprint1ms;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/*
* The ToolBarManager class is used to contain all the toolbar functions and variables in once place.
* Properties within the ToolBarManager class are passed to the easel for drawing.
 */
public class ToolBarManager {

    protected Color currentColor;
    protected double lineWidth;
    protected ColorPicker colorPicker;
    protected Slider lineWidthSlider;
    protected HBox toolBar;
    protected Tool currentTool;
    protected boolean dashed;
    protected int sides;
    protected TextField text;

    protected enum Tool {
        PENCIL, LINE, SQUARE, RECT, CIRCLE, ELLIPSE, TRIANGLE, HEX, ERASER, EYEDROPPER, RTRIANGLE, OCT, POLYGON, TEXT, SELECT, MOVE, PASTE, COPY
    }
    /*
    * Default Constructor for ToolBarManager, used to initialize the toolbar with drawing buttons and tools,
     */
    public ToolBarManager() {
        currentColor = Color.BLACK;
        lineWidth = 2.0;
        colorPicker = new ColorPicker(Color.BLACK);
        lineWidthSlider = new Slider(1, 20, 2);
        lineWidthSlider.setShowTickLabels(true);
        lineWidthSlider.setShowTickMarks(true);
        colorPicker.setOnAction(e -> currentColor = colorPicker.getValue());
        lineWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> lineWidth = newVal.doubleValue());
        CheckBox dashedBox = new CheckBox("Dashed");
        dashedBox.setOnAction(e -> dashed = dashedBox.isSelected());
        TextField polygonSides = new TextField("");
        polygonSides.setPrefWidth(50);
        polygonSides.textProperty().addListener((obs, oldVal, newVal) -> {
            try{
                if (Integer.parseInt(newVal) >= 3) {
                    sides = Integer.parseInt(newVal);
                }
            }catch(NumberFormatException e){System.out.println("NumberFormatException");}//Print if exception is caught (text still in field)
        });
        text =  new TextField("Text Here");
        toolBar = new HBox(10, new Label("Line Width:"), lineWidthSlider, colorPicker, buildToolBar(), dashedBox, new Label ("Polygon Sides:"), polygonSides,
        new Label ("Text:"), text);//Adding tools to toolbar
    }
    /*
    * Creates a Toolbar with drawing tool buttons attached.
    * @return  the Toolbar created by the buildToolBar() function.
     */
    private ToolBar buildToolBar() {
        ToggleGroup group = new ToggleGroup();//Adding buttons to a toggle group makes one button activatable at a time

        ToggleButton pencilBtn = PaintUtility.makeToolButton("Pencil", Tool.PENCIL, group, true, this);
        ToggleButton lineBtn = PaintUtility.makeToolButton("Line", Tool.LINE, group, false, this);
        ToggleButton textBtn = PaintUtility.makeToolButton("Text", Tool.TEXT, group, false, this);
        ToggleButton rectBtn = PaintUtility.makeToolButton("Rect", Tool.RECT, group, false, this);
        ToggleButton squareBtn = PaintUtility.makeToolButton("Square",Tool.SQUARE, group, false, this);
        ToggleButton circleBtn = PaintUtility.makeToolButton("Circle", Tool.CIRCLE, group, false, this);
        ToggleButton ellipseBtn = PaintUtility.makeToolButton("Oval", Tool.ELLIPSE, group, false, this);
        ToggleButton triangleBtn = PaintUtility.makeToolButton("Triangle", Tool.TRIANGLE, group, false, this);
        ToggleButton rtriangleBtn = PaintUtility.makeToolButton("R-Triangle", Tool.RTRIANGLE, group, false, this);
        ToggleButton hexBtn = PaintUtility.makeToolButton("Hex", Tool.HEX, group, false, this);
        ToggleButton octBtn = PaintUtility.makeToolButton("Oct", Tool.OCT, group, false, this);
        ToggleButton eraserBtn = PaintUtility.makeToolButton("Eraser", Tool.ERASER, group, false, this);
        ToggleButton dropperBtn = PaintUtility.makeToolButton("Dropper", Tool.EYEDROPPER, group, false, this);
        ToggleButton polygonBtn = PaintUtility.makeToolButton("Polygon", Tool.POLYGON, group, false, this);
        ToggleButton selectBtn =PaintUtility. makeToolButton("Select", Tool.SELECT, group, false, this);
        ToggleButton copyBtn = PaintUtility.makeToolButton("Copy", Tool.COPY, group, false, this);
        ToggleButton moveBtn = PaintUtility.makeToolButton("Move", Tool.MOVE, group, false, this);
        ToggleButton pasteBtn = PaintUtility.makeToolButton("Paste", Tool.PASTE, group, false, this);


        return new ToolBar(
                pencilBtn, lineBtn, textBtn, rectBtn, squareBtn, circleBtn, ellipseBtn,
                triangleBtn, rtriangleBtn, hexBtn, octBtn, polygonBtn, selectBtn, moveBtn,
                copyBtn, pasteBtn, eraserBtn, dropperBtn
        );
    }

}
