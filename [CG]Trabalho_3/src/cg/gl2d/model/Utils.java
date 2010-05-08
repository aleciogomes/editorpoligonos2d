package cg.gl2d.model;

import java.util.List;

public class Utils {
	
	public static double normalizeE(int a, int b, int c, double d, double f) {
		return ((b - a) * ((f - d) / (double)(c - a))) + d;
	}
	
	public static int normalizeB(int a, int c, double d, double e, double f) {
		return (int)((e - d) * ((double)(c - a) / (f - d))) + a;
	}
	
	public static double distanceBetweenPoints(EditorPoint a, EditorPoint b){
		double x = (b.x - a.x);
		double y = (b.y - a.y);

		return Math.sqrt((x * x) + (y * y));
	}
	
	public static EditorPoint nextPointInList(List<EditorPoint> points, int index){
		if(index + 1 >= points.size()){
			return points.get(0);
		}
		
		return points.get(index + 1);
	}

}
