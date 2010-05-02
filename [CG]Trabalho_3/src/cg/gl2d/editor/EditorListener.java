package cg.gl2d.editor;

public interface EditorListener {
	
	public void actionChanged(EditorAction action);
	
	public void zoomEnable(boolean zoomIn, boolean zoomOut);

}
