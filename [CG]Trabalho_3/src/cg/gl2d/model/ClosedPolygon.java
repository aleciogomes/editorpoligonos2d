package cg.gl2d.model;

import javax.media.opengl.GL;

public class ClosedPolygon extends Polygon {
	
	public ClosedPolygon(EditorColor fc) {
		super(GL.GL_LINE_STRIP, fc);
	}
	
	@Override
	public void finishDrawing() {
		super.setDrawPrimitive(GL.GL_LINE_LOOP);
		super.finishDrawing();
	}

}
