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

		if (selected) {
			drawPoints(gl);
			boundBox.draw(gl);
		}
	}

	public void drawPoints(GL gl) {
		for (EditorPoint p : points) {
			p.draw(gl);
		}
	}

	@Override
	public void mouseMoving(EditorPoint eventPoint) {
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

	@Override
	public boolean isPointInside(EditorPoint p) {

		if (!super.isPointInside(p))
			return false;

		EditorPoint ptMenorDist = null;
		double menorDist = Double.MAX_VALUE;

		for (EditorPoint polygonPoint : points) {
			double dist = Utils.distanceBetweenPoints(polygonPoint, p);

			if (menorDist > dist) {
				menorDist = dist;
				ptMenorDist = polygonPoint;
			}
		}

		ptMenorDist.setSelected(true);

		for (EditorPoint polygonPoint : points) {
			if (polygonPoint != ptMenorDist) {
				polygonPoint.setSelected(false);
			}
		}

		return true;
	}

	@Override
	public void update(EditorPoint newPoint) {
		EditorPoint p = getSelectedPoint();

		if (p != null) {
			if (Math.abs(newPoint.x - p.x) <= 3.0 && Math.abs(newPoint.y - p.y) <= 3.0) {
				p.x = newPoint.x;
				p.y = newPoint.y;

				boundBox.calcular();
			}
		}
	}

	@Override
	public void mover(EditorPoint newPoint) {

		Transform t = new Transform();
		t.setElement(Transform.Lx, newPoint.x);
		t.setElement(Transform.Ly, newPoint.y);
		for (EditorPoint p : getPoints()) {
			EditorPoint p2 = t.transformPoint(p);
			p.x = p2.x;
			p.y = p2.y;
		}

		boundBox.calcular();
	}

	private EditorPoint getSelectedPoint() {
		for (EditorPoint p : points) {
			if (p.isSelected()) {
				return p;
			}
		}

		return null;
	}

	@Override
	public void scale(boolean enlarge) {
		updatePointsByMatrix(internalScale(enlarge));
	}
	
	@Override
	public void rotate(double angle) {
		updatePointsByMatrix(internalRotate(angle));
	}
	
	private void updatePointsByMatrix(Transform matrix){
		
		for (EditorPoint p : getPoints()) {
			EditorPoint p2 = matrix.transformPoint(p);
			p.x = p2.x;
			p.y = p2.y;
		}

		getBoundBox().calcular();
	}
}
