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
        PENCIL, LINE, SQUARE, RECT, CIRCLE, ELLIPSE, TRIANGLE, HEX, ERASER, EYEDROPPER, RTRIANGLE, OCT, POLYGON, TEXT
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

        ToggleButton pencilBtn = makeToolButton("Pencil", Tool.PENCIL, group, true);
        ToggleButton lineBtn = makeToolButton("Line", Tool.LINE, group, false);
        ToggleButton textBtn = makeToolButton("Text", Tool.TEXT, group, false);
        ToggleButton rectBtn = makeToolButton("Rect", Tool.RECT, group, false);
        ToggleButton squareBtn = makeToolButton("Square",Tool.SQUARE, group, false);
        ToggleButton circleBtn = makeToolButton("Circle", Tool.CIRCLE, group, false);
        ToggleButton ellipseBtn = makeToolButton("Oval", Tool.ELLIPSE, group, false);
        ToggleButton triangleBtn = makeToolButton("Triangle", Tool.TRIANGLE, group, false);
        ToggleButton rtriangleBtn = makeToolButton("R-Triangle", Tool.RTRIANGLE, group, false);
        ToggleButton hexBtn = makeToolButton("Hex", Tool.HEX, group, false);
        ToggleButton octBtn = makeToolButton("Oct", Tool.OCT, group, false);
        ToggleButton eraserBtn = makeToolButton("Eraser", Tool.ERASER, group, false);
        ToggleButton dropperBtn = makeToolButton("Dropper", Tool.EYEDROPPER, group, false);
        ToggleButton polygonBtn = makeToolButton("Polygon", Tool.POLYGON, group, false);

        return new ToolBar(
                pencilBtn, lineBtn, textBtn, rectBtn, squareBtn, circleBtn, ellipseBtn,
                triangleBtn, rtriangleBtn, hexBtn, octBtn, polygonBtn, eraserBtn, dropperBtn
        );
    }
    /*
    * Creates a ToggleButton using a button name, tool from the toolbar, toggle-group and a boolean.
    * @param text      The text string applied to the button.
    * @param tool      The tool that the button affects/activates.
    * @param group     The toggle group that the button must be applied to
    * @param selected  Boolean to distinguish whether the button is currently selected or not.
     */
    private ToggleButton makeToolButton(String text, Tool tool, ToggleGroup group, boolean selected) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        if (selected) currentTool = tool;
        btn.setOnAction(e -> currentTool = tool);
        return btn;
    }

}
