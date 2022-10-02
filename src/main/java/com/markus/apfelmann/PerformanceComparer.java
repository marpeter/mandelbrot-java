package com.markus.apfelmann;

import java.time.Duration;
import java.time.Instant;

import com.markus.math.complex.ComplexNumber;

public class PerformanceComparer {

	static final int REPEATS = 100;
	static final int DEPTH =23;
	static final int MODULO = 8;
	static final int WIDTH = 640;
	static final int HEIGHT = 480;
	static final ComplexNumber LOWER_LEFT = new ComplexNumber(-2.0,-1.25);
	static final ComplexNumber UPPER_RIGHT = new ComplexNumber(1.0,1.25);
	static Instant startTime;
	
	public static void main(String ...args) {
	
		startMeasurement("Running the classic loop algorithm");
		for(int repeat=0; repeat<REPEATS; repeat++) {
			ImageGenerator.generateRawImageClassic(
					LOWER_LEFT, UPPER_RIGHT, DEPTH, WIDTH, HEIGHT, MODULO);
			
		};
		endMeasurement();
		
		startMeasurement("Running the algorithm using IntStreams and forEach");
		for(int repeat=0; repeat<REPEATS; repeat++) {
			ImageGenerator.generateRawImageUsingIntStreamsForEach(
					LOWER_LEFT, UPPER_RIGHT, DEPTH, WIDTH, HEIGHT, MODULO);
			
		};
		endMeasurement();
		
		startMeasurement("Running the algorithm using IntStreams and map");
		for(int repeat=0; repeat<REPEATS; repeat++) {
			ImageGenerator.generateRawImageStreamMapping(
					LOWER_LEFT, UPPER_RIGHT, DEPTH, WIDTH, HEIGHT, MODULO);
			
		};
		endMeasurement();
		
	}

	private static void startMeasurement(String message) {
		System.out.println(message + " " + REPEATS + " times ...");
		startTime = Instant.now();
	}
	
	private static void endMeasurement() {
		Instant endTime = Instant.now();
		System.out.println("Total runtime " + Duration.between(startTime, endTime).toMillis()+"ms");
		System.out.println("Average runtime " + Duration.between(startTime, endTime).toMillis()/REPEATS+"ms");
	}

}
