package cg.gl2d.model;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class OpenPoligon extends Shape {

	private List<EditorPoint> points;

	public OpenPoligon() {
		points = new ArrayList<EditorPoint>();
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

	@Override
	public void draw(GL gl) {
		gl.glColor3f(0.0f, 0.0f, 0.0f);

		gl.glBegin(GL.GL_LINE_STRIP);
		for (EditorPoint p : points) {
			System.out.println("x: " + p.x + " y: " + p.y);
			gl.glVertex2d(p.x, p.y);
		}
		gl.glEnd();
	}

}
