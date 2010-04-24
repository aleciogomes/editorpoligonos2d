package cg.gl2d.model;

public class Utils {
	
	public static double normalize(int a, int b, int c, double d, double f) {
		return ((b - a) * ((f - d) / (double)(c - a))) + d;
	}

}
