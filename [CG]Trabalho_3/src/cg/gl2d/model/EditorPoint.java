package cg.gl2d.model;

import javax.media.opengl.GL;

public class EditorPoint {
	
	public double x;
	
	public double y;

	private boolean selected;
	
	public EditorPoint(double x, double y) {
		this.x = x;
		this.y = y;
		this.selected = false;
	}
	
	public EditorPoint() {
		this(0.0, 0.0);
	}
	
	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(GL gl){
		gl.glPointSize(5.0f);
		
		if( selected ){
			gl.glColor3f(1.0f, 0.0f, 0.0f);
		}
		else{
			gl.glColor3f(0.0f, 0.0f, 0.0f);
		}
		
//		gl.glBegin(GL.GL_POINTS);
//			gl.glVertex2d(this.x, this.y);
//		gl.glEnd();
		
		final int qtdPontos = 36;
		final double angulo = 2 * Math.PI / (double) qtdPontos;
		final double radius = 0.3;

		gl.glBegin(GL.GL_POLYGON);
		for (int i = 1; i <= qtdPontos; i++) {
			gl.glVertex2d(Math.cos(i * angulo) * radius + this.x, Math.sin(i * angulo) * radius + this.y);
		}
		gl.glEnd();
	}
	
	@Override
	public String toString() {
		return "EditorPoint(x = "+ x +"; y = "+ y +")";
	}

	public void setSelected(boolean selected) {
		this.selected = selected;		
	}

	public boolean isSelected() {
		return this.selected;
	}

}
