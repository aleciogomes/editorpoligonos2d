package cg.gl2d.model;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class Polygon extends Shape {

	private int drawPrimitive;
	private List<EditorPoint> points;

	public Polygon(int drawPrimitive) {
		this.drawPrimitive = drawPrimitive;
		this.points = new ArrayList<EditorPoint>();
	}

	public void addPoint(EditorPoint point) {
		points.add(point);
	}

	public void removeLastPoint() {
		points.remove(points.size() - 1);
	}

	public int getPointsCount() {
		return points.size();
	}

	public List<EditorPoint> getPoints() {
		return points;
	}

	@Override
	public void draw(GL gl) {
		gl.glColor3f(0.0f, 0.0f, 0.0f);

		gl.glBegin(drawPrimitive);
		for (EditorPoint p : points) {
			gl.glVertex2d(p.x, p.y);
		}
		gl.glEnd();

		//if (selected) {
			boundBox.draw(gl);
		//}
	}

	@Override
	public void mouseMoved(EditorPoint eventPoint) {
		if (getPointsCount() > 1) {
			removeLastPoint();
			addPoint(eventPoint);
		}
	}

	@Override
	public void finishDrawing() {
		removeLastPoint();
		boundBox.calcular();
	}
}
