package cg.gl2d.model;

public class EditorPoint {
	
	public double x;
	
	public double y;
	
	public EditorPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public EditorPoint() {
		this(0.0, 0.0);
	}
	
	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}

}
