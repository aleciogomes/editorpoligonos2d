package cg.gl2d.model;

import javax.media.opengl.GL;

public class SinglePoint extends Shape {
	
	private EditorPoint point;
	
	public SinglePoint(EditorPoint point) {
		this.point = point;
	}

	@Override
	public void draw(GL gl) {
		 gl.glColor3f(0.0f, 0.0f, 0.0f);
		 gl.glPointSize(6.0f);

		 gl.glBegin(GL.GL_POINTS);
		 	gl.glVertex2d(point.x, point.y);
		 gl.glEnd();
	}
	
	@Override
	public void mouseMoving(EditorPoint eventPoint) { } // no action
	
	@Override
	public void finishDrawing() { }

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
		point.x = point.x + newPoint.x;
		point.y = point.y + newPoint.y;
	}	
}
