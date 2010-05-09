package cg.gl2d.model;

import javax.media.opengl.GL;

public class Circle extends Shape {

	private double radius;
	private EditorPoint center;

	public Circle() {
		radius = 0;
	}
	
	public void setCenter(EditorPoint center) {
		this.center = center;
	}

	public EditorPoint getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public void draw(GL gl, EditorColor color) {
		gl.glColor3f(color.red, color.green, color.blue);

		final int qtdPontos = 36;
		final double angulo = 2 * Math.PI / (double) qtdPontos;

		gl.glBegin(GL.GL_LINE_LOOP);
		for (int i = 1; i <= qtdPontos; i++) {
			gl.glVertex2d(Math.cos(i * angulo) * radius + center.x, Math.sin(i * angulo) * radius + center.y);
		}
		gl.glEnd();

		if (selected) {
			boundBox.draw(gl);
		}
	}

	@Override
	public void mouseMoving(EditorPoint eventPoint) {
		radius = Utils.distanceBetweenPoints(center, eventPoint);
	}

	@Override
	public void finishDrawing() {
		boundBox.calcular();
	}

	@Override
	public void update(EditorPoint newPoint) {
		boundBox.calcular();
	}

	@Override
	public void mover(EditorPoint newPoint) {		
		Transform t = new Transform();
		t.setElement(Transform.Lx, newPoint.x);
		t.setElement(Transform.Ly, newPoint.y);
		
		center = t.transformPoint(center);

		boundBox.calcular();
	}
	
	@Override
	public void scale(boolean enlarge) {
	}
	
	@Override
	public void rotate(double angle) {
	}
	
}
