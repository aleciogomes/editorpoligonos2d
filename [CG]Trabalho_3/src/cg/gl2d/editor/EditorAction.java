package cg.gl2d.editor;

/**
 * Ações que indicam à classe Editor como reagir aos eventos do mouse e teclado
 * select: modo de seleção 
 * move: modo de movimentação de polígonos no plano 
 * closedPolygon: modo de desenho de polígonos fechados 
 * openPolygon: modo de desenho de polígonos abertos 
 * circle: modo de desenho de círculos 
 * spline: modo de desenho de splines 
 * zoomIn: modo de zoom in 
 * zoomOut: modo de zoom out
 */
public enum EditorAction {
	
	select, move, closedPolygon, openPolygon, circle, spline, zoomIn, zoomOut

}
