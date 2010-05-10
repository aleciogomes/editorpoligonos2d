package cg.gl2d.editor;

import cg.gl2d.model.Shape;

public interface EditorListener {
	
	public void actionChanged(EditorAction action);
	
	public void zoomEnable(boolean zoomIn, boolean zoomOut);
	
	public void selectedChanged(Shape selected);

}
