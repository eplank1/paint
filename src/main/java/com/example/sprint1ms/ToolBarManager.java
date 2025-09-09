package com.example.sprint1ms;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class ToolBarManager {

    protected Color currentColor;
    protected double lineWidth;
    protected ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    protected Slider lineWidthSlider = new Slider(1, 20, 2);
    protected HBox toolBar;
    protected Tool currentTool;

    protected enum Tool {
        PENCIL, LINE, RECT, CIRCLE, ELLIPSE, TRIANGLE, HEX, ERASER, EYEDROPPER
    }

    public ToolBarManager() {
        currentColor = Color.BLACK;
        lineWidth = 2.0;
        colorPicker = new ColorPicker(Color.BLACK);
        lineWidthSlider = new Slider(1, 20, 2);
        lineWidthSlider.setShowTickLabels(true);
        lineWidthSlider.setShowTickMarks(true);
        colorPicker.setOnAction(e -> currentColor = colorPicker.getValue());
        lineWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> lineWidth = newVal.doubleValue());
        toolBar = new HBox(10, new Label("Line Width:"), lineWidthSlider, colorPicker, buildToolBar());
    }
    private ToolBar buildToolBar() {
        ToggleGroup group = new ToggleGroup();

        ToggleButton pencilBtn = makeToolButton("Pencil", Tool.PENCIL, group, true);
        ToggleButton lineBtn = makeToolButton("Line", Tool.LINE, group, false);
        ToggleButton rectBtn = makeToolButton("Rect", Tool.RECT, group, false);
        ToggleButton circleBtn = makeToolButton("Circle", Tool.CIRCLE, group, false);
        ToggleButton ellipseBtn = makeToolButton("Ellipse", Tool.ELLIPSE, group, false);
        ToggleButton triangleBtn = makeToolButton("Triangle", Tool.TRIANGLE, group, false);
        ToggleButton hexBtn = makeToolButton("Hex", Tool.HEX, group, false);
        ToggleButton eraserBtn = makeToolButton("Eraser", Tool.ERASER, group, false);
        ToggleButton dropperBtn = makeToolButton("Eyedropper", Tool.EYEDROPPER, group, false);

        return new ToolBar(
                pencilBtn, lineBtn, rectBtn, circleBtn, ellipseBtn,
                triangleBtn, hexBtn, eraserBtn, dropperBtn
        );
    }

    private ToggleButton makeToolButton(String text, Tool tool, ToggleGroup group, boolean selected) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        if (selected) currentTool = tool;
        btn.setOnAction(e -> currentTool = tool);
        return btn;
    }

}
