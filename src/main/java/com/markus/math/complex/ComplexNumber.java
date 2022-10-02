package com.markus.math.complex;

public class ComplexNumber {
	
	private double real, imaginary;
	
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public ComplexNumber add(ComplexNumber that) {
		return new ComplexNumber(this.real + that.real,
				                 this.imaginary + that.imaginary);
	}
	
	public ComplexNumber negate() {
		return new ComplexNumber(-this.real,-this.imaginary);
	}
	
	public ComplexNumber multiply(ComplexNumber that) {
		return new ComplexNumber(this.real*that.real - this.imaginary*that.imaginary,
				                 this.real*that.imaginary+this.imaginary*that.real);
	}

	public ComplexNumber conjugate() {
		return new ComplexNumber(this.real, -this.imaginary);
	}
	
	public ComplexNumber divide(ComplexNumber that) {
		double numerator = that.real*that.real + that.imaginary*that.imaginary;
		return new ComplexNumber( (this.real*that.real+this.imaginary*that.imaginary)/numerator,
				                  (this.imaginary*that.real-this.real*that.imaginary)/numerator);
	}
	
	public double abs() {
		return Math.sqrt(this.real*this.real + this.imaginary*this.imaginary);
	}
	
	public double real() {
		return real;
	}
	
	public double imaginary() {
		return imaginary;
	}
	
}
