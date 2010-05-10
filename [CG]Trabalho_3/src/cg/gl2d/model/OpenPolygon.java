package cg.gl2d.model;

import javax.media.opengl.GL;

public class OpenPolygon extends Polygon {

	public OpenPolygon(EditorColor fc) {
		super(GL.GL_LINE_STRIP, fc);
	}

}
