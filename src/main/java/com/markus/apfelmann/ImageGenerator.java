package com.markus.apfelmann;

import java.util.stream.IntStream;

import com.markus.math.complex.ComplexNumber;

/*
 * All methods are static so that the generator works like a "Transformer",
 * in other words more in the style of functional programming and supporting piping
 * 
 * Drawback: blows up the signature of the methods somewhat ...
 */

public class ImageGenerator {
	
	public static int[][] generateRawImage(
			ComplexNumber lowerLeft, ComplexNumber upperRight, int depth,
			int xSteps, int ySteps, int modulo) {
        return generateRawImageUsingIntStreamsForEach(lowerLeft, upperRight, depth, xSteps, ySteps, modulo);
	}
	
	public static int [][] generateRawImageUsingIntStreamsForEach(
			ComplexNumber lowerLeft, ComplexNumber upperRight, int depth,
			int xSteps, int ySteps, int modulo) {
		
		double deltaX = (upperRight.real() - lowerLeft.real())/xSteps;
		double deltaY = (upperRight.imaginary() - lowerLeft.imaginary())/ySteps;
 
		int[][] result = new int[xSteps][ySteps];
        IntStream.range(0, xSteps)
        	.parallel()
        	.forEach( (column) -> {
        		IntStream.range(0, ySteps)
        			.forEach( (row) -> {
                        ComplexNumber currentPoint = lowerLeft.add(new ComplexNumber(column*deltaX,row*deltaY));
        				result[column][row] = generatePoint(currentPoint, depth)%modulo;
        			});
        	});
        return result;
	}
	
	public static int[][] generateRawImageStreamMapping(
			ComplexNumber lowerLeft, ComplexNumber upperRight, int depth,
			int xSteps, int ySteps, int modulo) {
		
		double deltaX = (upperRight.real() - lowerLeft.real())/xSteps;
		double deltaY = (upperRight.imaginary() - lowerLeft.imaginary())/ySteps;
        
        return IntStream.range(0, xSteps)
        	.parallel()
        	.mapToObj(
        		(column) ->
        			IntStream.range(0,ySteps)
        			.map(
        				(row) -> {
                            ComplexNumber currentPoint = lowerLeft.add(new ComplexNumber(column*deltaX,row*deltaY));
            				return generatePoint(currentPoint, depth)%modulo;
        				}).toArray()
        			)
        	.toArray( int[][]::new );
	}
	
	public static int[][] generateRawImageClassic(
			ComplexNumber lowerLeft, ComplexNumber upperRight, int depth,
			int xSteps, int ySteps, int modulo) {
		
        int[][] result = new int[xSteps][ySteps];
		double deltaX = (upperRight.real() - lowerLeft.real())/xSteps;
		double deltaY = (upperRight.imaginary() - lowerLeft.imaginary())/ySteps;
		
		for(int column=0; column<xSteps; column++) {
			for(int row=0; row<ySteps; row++) {
                ComplexNumber currentPoint = lowerLeft.add(new ComplexNumber(column*deltaX,row*deltaY));
				result[column][row] = generatePoint(currentPoint, depth)%modulo;
			}
		}
		return result;
	}

	public static int generatePoint(ComplexNumber point, int depth) {
		int counter = depth;
		ComplexNumber c = point;
		ComplexNumber z = c;
		while( (counter>0) && z.abs()<=2.0 ) {
			z = z.multiply(z).add(c);
			counter --;
		}
 		return counter;
	}
}
