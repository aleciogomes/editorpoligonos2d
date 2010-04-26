package cg.gl2d.model;

import javax.media.opengl.GL;

public class ClosedPolygon extends Polygon {
	
	public ClosedPolygon() {
		super(GL.GL_LINE_LOOP);
	}

}
