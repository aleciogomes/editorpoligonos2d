package cg.gl2d.model;

import javax.media.opengl.GL;

public abstract class Shape {

	protected BoundBox boundBox;
	protected boolean selected;
	
	public Shape(){
		boundBox = new BoundBox(this);
		selected = false;
	}
	
	public abstract void draw(GL gl, EditorColor foreColor, EditorColor backColor);
	
	public abstract void mouseMoving(EditorPoint eventPoint);
	
	public abstract void finishDrawing();
	
	public abstract void update(EditorPoint newPoint);	
	public abstract void mover(EditorPoint newPoint);
	public abstract void scale(boolean enlarge);
	public abstract void rotate(double angle);
	
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
	
	public BoundBox getBoundBox(){
		return boundBox;
	}
	
	protected Transform internalScale(boolean enlarge) {
		
		EditorPoint center = getBoundBox().getCenter();
		
		// move para origem
		Transform matrix = new Transform();
		matrix = matrix.transformMatrix(translateInverse(center));
		
		// aplica a escala
		Transform scale = new Transform();		
		scale.setElement(Transform.Sx, enlarge ? 2.0 : 0.5);
		scale.setElement(Transform.Sy, enlarge ? 2.0 : 0.5);
		matrix = matrix.transformMatrix(scale);
		
		// volta para o local anterior
		return matrix.transformMatrix(translate(center));
	}
	
	protected Transform internalRotate(double angle) {
		
		EditorPoint center = getBoundBox().getCenter();
		
		// move pra origem
		Transform matrix = new Transform();
		matrix = matrix.transformMatrix(translateInverse(center));
		
		// aplica a rotação
		Transform rotation = new Transform();
		rotation.setAngle(angle);
		matrix = matrix.transformMatrix(rotation);
		
		// volta para o local anterior
		return matrix.transformMatrix(translate(center));
	}

	private Transform translate(EditorPoint center) {
		Transform translate = new Transform();
		translate.setElement(Transform.Lx, center.x * -1);
		translate.setElement(Transform.Ly, center.y * -1);
		return translate;
	}

	private Transform translateInverse(EditorPoint center) {
		Transform translateInverse = new Transform();		
		translateInverse.setElement(Transform.Lx, center.x);
		translateInverse.setElement(Transform.Ly, center.y);
		return translateInverse;
	}

}
