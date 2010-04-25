package cg.gl2d.model;

import javax.media.opengl.GL;

public class Circle extends Shape {
	
	private double radius;
	private EditorPoint center;
	
	public Circle(){
		radius = 0;
	}

	@Override
	public void draw(GL gl) {
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		final int qtdPontos = 36;
	    final double angulo = 2 * Math.PI / (double)qtdPontos; 
	    
	    System.out.println("C x: " + center.x +  " y: " + center.y);

	    gl.glBegin(GL.GL_LINE_LOOP);
	    for(int i = 1; i <= qtdPontos; i++)
	    {
	        gl.glVertex2d( Math.cos(i * angulo) * radius + center.x, Math.sin(i * angulo) * radius + center.y);
	    }
	    gl.glEnd();
	}

	@Override
	public void mouseMoved(EditorPoint eventPoint) {
		double x = (eventPoint.x - center.x);
		double y = (eventPoint.y - center.y);
		
		radius = Math.sqrt( (x * x) + (y * y) );
		System.out.println("Novo raio: " + radius);
	}
	
	@Override
	public void finishDrawing() { }

	public void setCenter(EditorPoint center) {
		this.center = center;
	}
}
