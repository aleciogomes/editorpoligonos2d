package cg.gl2d.editor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import cg.gl2d.model.EditorPoint;
import cg.gl2d.model.Line;
import cg.gl2d.model.Shape;
import cg.gl2d.model.Utils;

public class Editor implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	private GLCanvas canvas;
	private GLCapabilities capabilities;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	
	private List<Shape> shapes = new ArrayList<Shape>();
	
	private int editorWidth;
	private int editorHeight;
	
	private double xn = 0.0;
	private double xp = 50.0;
	private double yn = 0.0;
	private double yp = 50.0;
	
	private EditorPoint clicked = null;

	public Editor() {
		/**
		 * Cria um objeto GLCapabilities para especificar o número de bits por
		 * pixel para RGBA
		 */
		capabilities = new GLCapabilities();
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		/**
		 * Cria um objecto GLCanvas e adiciona o Editor como listener dos
		 * eventos do GL, do teclado e do mouse.
		 */
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		
		//temporário, apenas para teste
		shapes.add(new Line());
	}

	public GLCanvas getCanvas() {
		return canvas;
	}

	public void focus() {
		canvas.requestFocus();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = glDrawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		 gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		 gl.glMatrixMode(GL.GL_MODELVIEW);
		 gl.glLoadIdentity();

		 // configurar window
		 glu.gluOrtho2D(0, editorHeight * 0.1, 0, editorWidth * 0.1);
		 
		 for (Shape s : shapes) {
			 s.draw(gl);
		 }

		 gl.glFlush();
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int width, int height) {
		editorWidth = width;
		editorHeight = height;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			glDrawable.display();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	private EditorPoint normalizePoint(int x, int y) {
		EditorPoint p = new EditorPoint();
		p.x = Utils.normalize(0, x, editorWidth, xn, xp);
		p.y = Utils.normalize(0, editorHeight - y, editorHeight, yn, yp);
		return p;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		clicked = normalizePoint(e.getX(), e.getY());
		
		glDrawable.display();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
