package cg.gl2d.model;

import java.util.List;

import javax.media.opengl.GL;

public class Spline extends Polygon {

	public Spline() {
		super(GL.GL_LINE_STRIP);
	}

	@Override
	public void draw(GL gl) {

		float[] controlPoints = pointsToFloatArray();
		gl.glMap1f(GL.GL_MAP1_VERTEX_3, 0.0f, 1.0f, 3, getPointsCount(), controlPoints, 0);
		gl.glEnable(GL.GL_MAP1_VERTEX_3);

		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINE_STRIP);
		for (float i = 0; i <= 30; i++) {
			gl.glEvalCoord1f(i / (float) 30.0);
		}
		gl.glEnd();

		gl.glPointSize(5.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINE_STRIP);
		for (EditorPoint p : getPoints()) {
			gl.glVertex2d(p.x, p.y);
		}
		gl.glEnd();

		boundBox.draw(gl);
	}

	private float[] pointsToFloatArray() {
		List<EditorPoint> points = getPoints();
		float[] controlPoints = new float[points.size() * 3];

		int controle = 0;
		int pointDaVez = 0;
		for (int i = 0; i < controlPoints.length; i++) {

			EditorPoint p = points.get(pointDaVez);

			if (controle == 0)
				controlPoints[i] = (float) p.x;
			else if (controle == 1)
				controlPoints[i] = (float) p.y;
			else {
				controlPoints[i] = 0.0f;
			}

			controle++;

			if (controle == 3) {
				controle = 0;
				pointDaVez++;
			}

		}

		return controlPoints;
	}

}
