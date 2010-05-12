package cg.gl2d.editor;

import cg.gl2d.model.Shape;

public interface EditorListener {
	
	/**
	 * Método disparado pelo Editor quando a ação de edição é alterada
	 * @param action Nova ação
	 */
	public void actionChanged(EditorAction action);
	
	/**
	 * Método disparado pelo Editor quando o zoom é habilitado ou desabilitado
	 * @param zoomIn "True" quando Zoom In está habilitado
	 * @param zoomOut "True" quando Zoom Out está habilitado
	 */
	public void zoomEnable(boolean zoomIn, boolean zoomOut);
	
	/**
	 * Método disparado pelo Editor quando um polígono é selecionado
	 * ou descelecionado.
	 * @param selected Polígono selecionado. Null indica que não há 
	 * polígono selecionado.
	 */
	public void selectedChanged(Shape selected);

}
