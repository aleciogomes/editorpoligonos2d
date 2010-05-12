package cg.gl2d.editor;

/**
 * A��es que indicam � classe Editor como reagir aos eventos do mouse e teclado
 * select: modo de sele��o 
 * move: modo de movimenta��o de pol�gonos no plano 
 * closedPolygon: modo de desenho de pol�gonos fechados 
 * openPolygon: modo de desenho de pol�gonos abertos 
 * circle: modo de desenho de c�rculos 
 * spline: modo de desenho de splines 
 * zoomIn: modo de zoom in 
 * zoomOut: modo de zoom out
 */
public enum EditorAction {
	
	select, move, closedPolygon, openPolygon, circle, spline, zoomIn, zoomOut

}
