package cg.gl2d.model;

import javax.media.opengl.GL;

public class Line extends Shape {

	@Override
	public void draw(GL gl) {
		 gl.glColor3f(0.0f, 0.0f, 0.0f);

		 gl.glBegin(GL.GL_LINES);
		 	gl.glVertex2d(-20.0, 25.0);
		 	gl.glVertex2d(20.0, 25.0);

		 	gl.glVertex2d(25.0, 20.0);
		 	gl.glVertex2d(25.0, -20.0);
		 gl.glEnd();
	}

}
