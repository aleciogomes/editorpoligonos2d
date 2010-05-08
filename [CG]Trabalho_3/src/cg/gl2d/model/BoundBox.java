package cg.gl2d.model;

import java.util.List;

import javax.media.opengl.GL;

public class BoundBox {

	private EditorPoint min;
	private EditorPoint max;
	private EditorPoint center;
	private Shape shape;

	public BoundBox(Shape shape) {
		min = new EditorPoint(0, 0);
		max = new EditorPoint(0, 0);
		center = new EditorPoint(0, 0);
		this.shape = shape;
	}

	public EditorPoint getMin() {
		return min;
	}

	public EditorPoint getMax() {
		return max;
	}
	
	public EditorPoint getCenter(){
		return center;
	}

	public void draw(GL gl) {
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(min.x, min.y);
		gl.glVertex2d(max.x, min.y);
		gl.glVertex2d(max.x, max.y);
		gl.glVertex2d(min.x, max.y);
		gl.glEnd();
		
//		gl.glPointSize(4.0f);
//		gl.glBegin(GL.GL_POINTS);
//		gl.glVertex2d(center.x, center.y);
//		gl.glEnd();
	}

	public void calcular() {
		if (shape.getClass() == Circle.class) {
			Circle circle = (Circle) shape;
			min.x = circle.getCenter().x - circle.getRadius();
			min.y = circle.getCenter().y - circle.getRadius();
			max.x = circle.getCenter().x + circle.getRadius();
			max.y = circle.getCenter().y + circle.getRadius();
			updateCenter();
		}
		else if (Polygon.class.isAssignableFrom(shape.getClass())) {
			Polygon polygon = (Polygon) shape;
			List<EditorPoint> points = polygon.getPoints();

			if (points.size() == 0)
				return;

			min.x = points.get(0).x;
			max.x = points.get(0).x;
			min.y = points.get(0).y;
			max.y = points.get(0).y;

			for (int i = 0; i < points.size(); i++) {
				EditorPoint p = points.get(i);
				if (p.x < min.x)
					min.x = p.x;
				if (p.y < min.y)
					min.y = p.y;
				if (p.x > max.x)
					max.x = p.x;
				if (p.y > max.y)
					max.y = p.y;
			}
			updateCenter();
		}
	}
	
	private void updateCenter(){
		center.x = ((max.x - min.x) / 2) + min.x;
		center.y = ((max.y - min.y) / 2) + min.y;
	}
}
