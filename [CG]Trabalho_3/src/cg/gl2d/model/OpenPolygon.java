package cg.gl2d.model;

import javax.media.opengl.GL;

public class OpenPolygon extends Polygon {

	public OpenPolygon() {
		super(GL.GL_LINE_STRIP);
	}

}
