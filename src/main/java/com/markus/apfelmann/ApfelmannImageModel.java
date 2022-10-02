package com.markus.apfelmann;

import java.util.HashSet;
import java.util.Set;

import com.markus.math.complex.ComplexNumber;

public class ApfelmannImageModel {

	private static final double INITIAL_XMIN = -2.;
	private static final double INITIAL_YMIN = -1.25;	
	private static final double INITIAL_XMAX = 1.0; //1.2;	
	private static final double INITIAL_YMAX = 1.25;
	private static final int    INITIAL_DEPTH = 23;
	
	private double xmin, xmax, ymin, ymax;
	private int depth, numberOfColors;

	private Set<ApfelmannImageModelObserver> observers = new HashSet<>();
	
	public ApfelmannImageModel(int numberOfColors) {
		this.xmin = INITIAL_XMIN;
		this.xmax = INITIAL_XMAX;
		this.ymin = INITIAL_YMIN;
		this.ymax = INITIAL_YMAX;
	    this.depth = INITIAL_DEPTH;
	    this.numberOfColors = numberOfColors;
	}

	public void addObserver(ApfelmannImageModelObserver observer) {
		observers.add(observer);
	}
	
	public void removeObserver(ApfelmannImageModelObserver observer) {
		observers.remove(observer);
	}
	
	public double getXmin() {
		return xmin;
	}

	public double getXmax() {
		return xmax;
	}

	public double getYmin() {
		return ymin;
	}

	public double getYmax() {
		return ymax;
	}

	public int getDepth() {
		return depth;
	}
	
	public int[][] getRawImage(double width, double height) {
		return ImageGenerator.generateRawImage(
				new ComplexNumber(xmin,ymin), new ComplexNumber(xmax, ymax), depth,
				(int)width, (int)height, numberOfColors);
	}
	
	public void shiftXIntervalBy(int steps, double width) {
		double deltaX = (xmax-xmin)/width;
		xmin += deltaX * steps;
		xmax += deltaX * steps;
		notifyObserversThatModelChanged();
	}
	
	public void shiftYIntervalBy(int steps, double width) {
		double deltaY = (ymax-ymin)/width;
		ymin += deltaY * steps;
		ymax += deltaY * steps;
		notifyObserversThatModelChanged();
	}
	
	
	public void zoomBy(double factor) {
		double xcenter = (xmax+xmin)/2;
		double ycenter = (ymax+ymin)/2;
		xmin = xcenter-(xcenter-xmin)*factor;
		xmax = xcenter+(xmax-xcenter)*factor;
		ymin = ycenter-(ycenter-ymin)*factor;
		ymax = ycenter+(ymax-ycenter)*factor;
		depth = (int)Math.round(depth/factor);
		notifyObserversThatModelChanged();
	}
    
	private void notifyObserversThatModelChanged() {
		for(ApfelmannImageModelObserver observer: observers) {
			observer.onModelChanged();
		}		
	}
}
