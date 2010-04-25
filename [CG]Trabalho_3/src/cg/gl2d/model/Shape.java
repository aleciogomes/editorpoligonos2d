package cg.gl2d.model;

import javax.media.opengl.GL;

public abstract class Shape {
	
	public abstract void draw(GL gl);
	
	public abstract void mouseMoved(EditorPoint eventPoint);
	
	public abstract void finishDrawing();

}
