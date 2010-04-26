package cg.gl2d.model;

import javax.media.opengl.GL;

public abstract class Shape {

	protected BoundBox boundBox;
	protected boolean selected;
	
	public Shape(){
		boundBox = new BoundBox(this);
		selected = false;
	}
	
	public abstract void draw(GL gl);
	
	public abstract void mouseMoved(EditorPoint eventPoint);
	
	public abstract void finishDrawing();
	
	public boolean isPointInside(EditorPoint p){
		if (p.x >= boundBox.getMin().x && p.y >= boundBox.getMin().y) {
			if (p.x <= boundBox.getMax().x && p.y <= boundBox.getMax().y) {
				return true;
			}
		}
		return false;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
