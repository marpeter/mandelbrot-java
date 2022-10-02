package com.markus.apfelmann;


import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Apfelmann extends Application
					implements ApfelmannImageModelObserver {
	
	private static final int SHIFT_STEPS = 20;
	private static final double ZOOM_FACTOR = 1.2;
	private static final Color[] COLORS = {
			Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,
            Color.PINK, Color.RED, Color.ORANGE, Color.YELLOW};

	private Canvas imageCanvas;
	private ApfelmannImageModel imageModel;

	private Label xRange;
	private Label yRange;
	private Label depth;
    
	@Override
    public void start(Stage stage) {
		
		imageModel = new ApfelmannImageModel(COLORS.length);
		imageModel.addObserver(this);

        stage.setTitle("Mandelbrot Apfelmännchen");
        stage.setScene(createScene());
        draw();
        stage.show();
    }

	@Override
	public void onModelChanged() {
		xRange.setText(getXRange());
		yRange.setText(getYRange());
		depth.setText(getDepth());
		draw();
	}
	
	private Scene createScene() {        
        imageCanvas = new Canvas();
        imageCanvas.setWidth(800);
        imageCanvas.setHeight(480);
        
        // setting the max width/height to Double.MAX_VALUE makes the buttons adjust their width/height to the available space        
        Button shiftLeftButton = new Button("<");
        shiftLeftButton.setMaxHeight(Double.MAX_VALUE);
        shiftLeftButton.setOnAction(evt -> imageModel.shiftXIntervalBy(-SHIFT_STEPS,imageCanvas.getWidth()));
        
        Button shiftRightButton = new Button(">");
        shiftRightButton.setMaxHeight(Double.MAX_VALUE);
        shiftRightButton.setOnAction(evt -> imageModel.shiftXIntervalBy(SHIFT_STEPS,imageCanvas.getWidth()));
        
        Button shiftUpButton = new Button("^");
        shiftUpButton.setMaxWidth(Double.MAX_VALUE);
        shiftUpButton.setOnAction(evt -> imageModel.shiftYIntervalBy(-SHIFT_STEPS, imageCanvas.getHeight()));
        
        Button shiftDownButton = new Button("v");
        shiftDownButton.setMaxWidth(Double.MAX_VALUE);
        shiftDownButton.setOnAction(evt -> imageModel.shiftYIntervalBy(SHIFT_STEPS, imageCanvas.getHeight()));

        Button zoomInButton = new Button("Zoom in");
        zoomInButton.setMaxWidth(Double.MAX_VALUE);
        zoomInButton.setOnAction(evt -> imageModel.zoomBy(1.0/ZOOM_FACTOR));

        Button zoomOutButton = new Button("Zoom out");
        zoomOutButton.setMaxWidth(Double.MAX_VALUE);
        zoomOutButton.setOnAction(evt -> imageModel.zoomBy(ZOOM_FACTOR));
 
        BorderPane imagePane = new BorderPane(imageCanvas,
        		shiftUpButton, shiftRightButton, shiftDownButton, shiftLeftButton);
        
        xRange = new Label(getXRange());
        yRange = new Label(getYRange());
        depth = new Label(getDepth());             
        VBox rangeBar = new VBox(5,xRange,yRange,depth);
        rangeBar.setAlignment(Pos.CENTER);
        rangeBar.setPadding(new Insets(5.0,5.0,5.0,5.0));
        
        VBox zoomBar = new VBox(5,zoomInButton,zoomOutButton);
        
        HBox.setHgrow(rangeBar, Priority.ALWAYS);
        HBox.setHgrow(zoomBar, Priority.ALWAYS);
        HBox infoBar = new HBox(rangeBar,zoomBar);

        VBox outerBox = new VBox(5,imagePane, infoBar);
        
		return new Scene(outerBox);
	}

	private String getXRange() {
		return "" + imageModel.getXmin() + " < x < " + imageModel.getXmax();
	}
	
	private String getYRange() {
		return "" + imageModel.getYmin() + " < y < " + imageModel.getYmax();
	}
	
	private String getDepth() {
		return "Depth: " + imageModel.getDepth();
	}

	private void draw() {
        double width  = imageCanvas.getWidth();
        double height = imageCanvas.getHeight();

        GraphicsContext graphicsContext2D = imageCanvas.getGraphicsContext2D();
        graphicsContext2D.clearRect(0, 0, width, height);
		
        Instant startTime = Instant.now(); // to measure time need to recalculate and redraw
        
		int[][] colorIndex = imageModel.getRawImage(width, height);
		
		Instant endTime = Instant.now(); // to measure time need to recalculate and redraw
		System.out.println("Generating the color array took " + Duration.between(startTime, endTime).toMillis()+"ms");
  	
		IntStream.range(0, (int)height)
			.forEach(
				(row) -> IntStream.range(0, (int)width)
					.forEach(
						(column) -> {
							Color color = COLORS[colorIndex[column][row]];
							graphicsContext2D.setFill(color);
							graphicsContext2D.fillRect(column,row,1.0,1.0);					
					} ));
		
		endTime = Instant.now(); // to measure time need to recalculate and redraw
		System.out.println("Drawing took " + Duration.between(startTime, endTime).toMillis()+"ms");
    }		
}
