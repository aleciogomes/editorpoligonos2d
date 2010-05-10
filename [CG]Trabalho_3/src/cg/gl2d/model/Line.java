package cg.gl2d.model;

import javax.media.opengl.GL;

public class Line extends Shape {

	private EditorPoint p1;

	private EditorPoint p2;

	public Line(EditorPoint p1, EditorPoint p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public void draw(GL gl, EditorColor foreColor, EditorColor backColor) {
		gl.glColor3f(foreColor.red, foreColor.green, foreColor.blue);

		gl.glBegin(GL.GL_LINES);
			gl.glVertex2d(p1.x, p1.y);
			gl.glVertex2d(p2.x, p2.y);
		gl.glEnd();
	}

	@Override
	public void mouseMoving(EditorPoint eventPoint) { 
	}
	
	@Override
	public void finishDrawing() {
	}

	@Override
	public void update(EditorPoint newPoint) {
	}
	
	@Override
	public void scale(boolean enlarge) {	
	}
	
	@Override
	public void rotate(double angle) {	
	}

	@Override
	public void mover(EditorPoint newPoint) {
		p1.x = p1.x + newPoint.x;
		p1.y = p1.y + newPoint.y;
		
		p2.x = p2.x + newPoint.x;
		p2.y = p2.y + newPoint.y;
	}
	
	@Override
	public boolean isMoveable() {
		return false;
	}
	
	@Override
	public void removeSelectedPoint() {
	}
	
}
