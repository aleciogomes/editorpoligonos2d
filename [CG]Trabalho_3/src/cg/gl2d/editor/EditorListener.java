package cg.gl2d.editor;

import cg.gl2d.model.Shape;

public interface EditorListener {
	
	/**
	 * M�todo disparado pelo Editor quando a a��o de edi��o � alterada
	 * @param action Nova a��o
	 */
	public void actionChanged(EditorAction action);
	
	/**
	 * M�todo disparado pelo Editor quando o zoom � habilitado ou desabilitado
	 * @param zoomIn "True" quando Zoom In est� habilitado
	 * @param zoomOut "True" quando Zoom Out est� habilitado
	 */
	public void zoomEnable(boolean zoomIn, boolean zoomOut);
	
	/**
	 * M�todo disparado pelo Editor quando um pol�gono � selecionado
	 * ou descelecionado.
	 * @param selected Pol�gono selecionado. Null indica que n�o h� 
	 * pol�gono selecionado.
	 */
	public void selectedChanged(Shape selected);

}
