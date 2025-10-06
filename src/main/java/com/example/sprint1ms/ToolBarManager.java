package com.example.sprint1ms;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    protected LogHelper logHelper;

    protected enum Tool {
        PENCIL, LINE, SQUARE, RECT, CIRCLE, ELLIPSE, TRIANGLE, HEX, ERASER, EYEDROPPER, RTRIANGLE, OCT, POLYGON, TEXT, SELECT, MOVE, PASTE, COPY, STAR
    }
    /*
    * Default Constructor for ToolBarManager, used to initialize the toolbar with drawing buttons and tools,
     */
    public ToolBarManager() {
        logHelper = new LogHelper();
        currentColor = Color.BLACK;
        lineWidth = 2.0;
        colorPicker = new ColorPicker(Color.BLACK);
        lineWidthSlider = new Slider(1, 20, 2);
        lineWidthSlider.setShowTickLabels(true);
        lineWidthSlider.setShowTickMarks(true);
        colorPicker.setOnAction(e -> currentColor = colorPicker.getValue());
        lineWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> lineWidth = newVal.doubleValue());
        CheckBox dashedBox = new CheckBox("Dashed");
        dashedBox.setTooltip(new Tooltip("Shapes drawn are dashed if selected"));
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
        toolBar = new HBox(10, new Label("Line Width:"), lineWidthSlider, colorPicker, buildToolBar(), dashedBox, new Label ("Sides/Points:"), polygonSides,
        new Label ("Text:"), text);//Adding tools to toolbar
    }
    /*
    * Creates a Toolbar with drawing tool buttons attached.
    * @return  the Toolbar created by the buildToolBar() function.
     */
    private ToolBar buildToolBar() {
        ToggleGroup group = new ToggleGroup();//Adding buttons to a toggle group makes one button activatable at a time

        ToggleButton pencilBtn = PaintUtility.makeToolButton("Pen", Tool.PENCIL, group, true, this, "pen-tool.png");
        pencilBtn.setTooltip(new Tooltip("Pencil tool that allows user to draw freely on the canvas"));
        ToggleButton lineBtn = PaintUtility.makeToolButton("Line", Tool.LINE, group, false, this, "line.png");
        lineBtn.setTooltip(new Tooltip("Line tool that allows user to draw  straight lines on the canvas"));
        ToggleButton textBtn = PaintUtility.makeToolButton("Text", Tool.TEXT, group, false, this, "text.png");
        textBtn.setTooltip(new Tooltip("Text tool that allows user to add text on the canvas"));
        ToggleButton rectBtn = PaintUtility.makeToolButton("Rect", Tool.RECT, group, false, this,"square.png" );
        rectBtn.setTooltip(new Tooltip("Rectangle tool that allows user to draw rectangles on the canvas"));
        ToggleButton squareBtn = PaintUtility.makeToolButton("Square",Tool.SQUARE, group, false, this, "square.png");
        squareBtn.setTooltip(new Tooltip("Square tool that allows user to draw squares on the canvas"));
        ToggleButton circleBtn = PaintUtility.makeToolButton("Circle", Tool.CIRCLE, group, false, this, "circle.png");
        circleBtn.setTooltip(new Tooltip("Circle tool that allows user to draw circles on the canvas"));
        ToggleButton ellipseBtn = PaintUtility.makeToolButton("Oval", Tool.ELLIPSE, group, false, this, "ellipse.png");
        ellipseBtn.setTooltip(new Tooltip("Ellipse tool that allows user to draw ellipses on the canvas"));
        ToggleButton triangleBtn = PaintUtility.makeToolButton("Tri", Tool.TRIANGLE, group, false, this, "triangle.png");
        triangleBtn.setTooltip(new Tooltip("Triangle  tool that allows user to draw triangles on the canvas"));
        ToggleButton rtriangleBtn = PaintUtility.makeToolButton("R-Tri", Tool.RTRIANGLE, group, false, this, "right-triangle.png");
        rtriangleBtn.setTooltip(new Tooltip("Right Triangle tool that allows user to draw right triangles on the canvas"));
        /*ToggleButton hexBtn = PaintUtility.makeToolButton("Hex", Tool.HEX, group, false, this);
        hexBtn.setTooltip(new Tooltip("Hexagon tool that allows user to draw hexagons on the canvas"));
        ToggleButton octBtn = PaintUtility.makeToolButton("Oct", Tool.OCT, group, false, this);
        octBtn.setTooltip(new Tooltip("Octagon tool that allows user to draw octagons on the canvas"));*///Polygon tool makes hexagons and octagons
        ToggleButton eraserBtn = PaintUtility.makeToolButton("Eraser", Tool.ERASER, group, false, this, "eraser.png");
        eraserBtn.setTooltip(new Tooltip("Eraser tool that allows user to freely erase the canvas or images if they are loaded"));
        ToggleButton dropperBtn = PaintUtility.makeToolButton("Dropper", Tool.EYEDROPPER, group, false, this, "dropper.png");
        dropperBtn.setTooltip(new Tooltip("Dropper tool that allows user to grab the color from a pixel on the canvas for use in other drawing tools"));
        ToggleButton polygonBtn = PaintUtility.makeToolButton("Poly", Tool.POLYGON, group, false, this, "pentagon.png");
        polygonBtn.setTooltip(new Tooltip("Polygon tool that allows user to draw polygons on the canvas (# of sides set by putting desired number of sides in sides/points field)"));
        ToggleButton starBtn = PaintUtility.makeToolButton("Star", Tool.STAR, group, false, this, "star.png");
        starBtn.setTooltip(new Tooltip("Star tool that allows user to draw stars on the canvas (# of points set by putting desired number of points in sides/points field)"));
        ToggleButton selectBtn =PaintUtility. makeToolButton("Select", Tool.SELECT, group, false, this, "selection.png");
        selectBtn.setTooltip(new Tooltip("Select tool that allows user to select portions of the canvas to be moved using the MOVE tool"));
        ToggleButton copyBtn = PaintUtility.makeToolButton("Copy", Tool.COPY, group, false, this, "copy.png");
        copyBtn.setTooltip(new Tooltip("Copy tool that allows user to copy portions of the canvas that can be pasted using the PASTE tool"));
        ToggleButton moveBtn = PaintUtility.makeToolButton("Move", Tool.MOVE, group, false, this, "selection.png");
        moveBtn.setTooltip(new Tooltip("Move tool that moves selected portion of the image to desired location (selected portion must be reselected after moving)"));
        ToggleButton pasteBtn = PaintUtility.makeToolButton("Paste", Tool.PASTE, group, false, this, "copy.png");
        pasteBtn.setTooltip(new Tooltip("Paste tool that pastes copied portion of the canvas on desired location (copied portion is not lost after pasting)"));


        return new ToolBar(
                pencilBtn, lineBtn, textBtn, rectBtn, squareBtn, circleBtn, ellipseBtn,
                triangleBtn, rtriangleBtn, polygonBtn, starBtn, selectBtn, moveBtn,
                copyBtn, pasteBtn, eraserBtn, dropperBtn
        );
    }

}
