package cg.gl2d.editor;

import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
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
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import cg.gl2d.model.Circle;
import cg.gl2d.model.ClosedPolygon;
import cg.gl2d.model.EditorPoint;
import cg.gl2d.model.Line;
import cg.gl2d.model.OpenPolygon;
import cg.gl2d.model.Polygon;
import cg.gl2d.model.Shape;
import cg.gl2d.model.Spline;
import cg.gl2d.model.Utils;

public class Editor extends JPanel implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, AdjustmentListener {

	private static final long serialVersionUID = 1L;

	private JScrollBar verticalScrollBar;
	private JScrollBar horizontalScrollBar;

	private GLCanvas canvas;
	private GLCapabilities capabilities;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;

	private List<Shape> shapes = new ArrayList<Shape>();
	
	private EditorAction action;
	
	private EditorListener listener;

	private int editorWidth;
	private int editorHeight;
	private int verticalScroll = 0;
	private int horizontalScroll = 0;

	private double xn = 0.0;
	private double xp = 0.0;
	private double yn = 0.0;
	private double yp = 0.0;

	private Shape shapeAtual;

	private char shapeEscolhido = 'A';

	private boolean desenho = true;

	public Editor(EditorListener listener) {
		/*
		 * Armazena o listener de callback do editor
		 */
		this.listener = listener;
		/*
		 * Cria um objeto GLCapabilities para especificar o número de bits por
		 * pixel para RGBA
		 */
		capabilities = new GLCapabilities();
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		/*
		 * Cria um objecto GLCanvas e adiciona o Editor como listener dos
		 * eventos do GL, do teclado e do mouse.
		 */
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		/*
		 * Cria as barras de rolagem vertical e horizontal
		 */
		verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
		verticalScrollBar.setMaximum(2000);
		verticalScrollBar.addAdjustmentListener(this);
		horizontalScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
		horizontalScrollBar.setMaximum(2000);
		horizontalScrollBar.addAdjustmentListener(this);
		/*
		 * Monta o layout do editor
		 */
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		add(verticalScrollBar, BorderLayout.EAST);
		add(horizontalScrollBar, BorderLayout.SOUTH);
		
		setAction(EditorAction.select);
	}

	public void focus() {
		canvas.requestFocus();
	}
	
	public void setAction(EditorAction action) {
		this.action = action;
		listener.actionChanged(action);
	}
	
	public EditorAction getAction() {
		return action;
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

		glu.gluOrtho2D(xn, xp, yn, yp);

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
		double w = width * 0.1;
		double h = height * 0.1;

		if (xp == xn && yp == yn) {
			xp = w;
			yp = h;
		}
		else {
			xp = w + xn;
			yn = yp - h;
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (e.getSource() == verticalScrollBar) {
			/*
			 * Scroll vertival
			 */
			int v = e.getValue() - verticalScroll;
			verticalScroll = e.getValue();
			yp -= v * 0.1;
			yn = yp - editorHeight * 0.1;
		}
		else {
			/*
			 * Scroll horizontal
			 */
			int v = e.getValue() - horizontalScroll;
			horizontalScroll = e.getValue();
			xn += v * 0.1;
			xp = editorWidth * 0.1 + xn;
		}
		glDrawable.display();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			glDrawable.display();

		if (e.getKeyChar() == 'A' || e.getKeyChar() == 'a' || e.getKeyChar() == 'F' || e.getKeyChar() == 'f' || e.getKeyChar() == 'C' || e.getKeyChar() == 'c' || e.getKeyChar() == 'S' || e.getKeyChar() == 's') {
			shapeEscolhido = e.getKeyChar();
		}

		if (e.getKeyChar() == 'L' || e.getKeyChar() == 'l') {
			desenho = !desenho;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private EditorPoint normalizePoint(int x, int y) {
		EditorPoint p = new EditorPoint();
		p.x = Utils.normalize(0, x, editorWidth, xn, xp);
		p.y = Utils.normalize(0, editorHeight - y, editorHeight, yn, yp);
		return p;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		EditorPoint clicked = normalizePoint(e.getX(), e.getY());

		if (desenho) {
			if (e.getClickCount() == 2 && shapeAtual != null) {
				shapeAtual.finishDrawing();
				shapeAtual = null;
				glDrawable.display();
				return;
			}

			if (shapeAtual == null) {
				switch (shapeEscolhido) {
				case 'A': {
					shapeAtual = new OpenPolygon();
					shapes.add(shapeAtual);
					((Polygon) shapeAtual).addPoint(clicked);
					break;
				}
				case 'F': {
					shapeAtual = new ClosedPolygon();
					shapes.add(shapeAtual);
					((Polygon) shapeAtual).addPoint(clicked);
					break;
				}
				case 'S': {
					shapeAtual = new Spline();
					shapes.add(shapeAtual);
					((Polygon) shapeAtual).addPoint(clicked);
					break;
				}
				case 'C': {
					shapeAtual = new Circle();
					((Circle) shapeAtual).setCenter(clicked);
					shapes.add(shapeAtual);
					System.out.println("Circulo criado");
					break;
				}
				}
			}

			if (shapeEscolhido == 'A' || shapeEscolhido == 'F' || shapeEscolhido == 'S') {
				((Polygon) shapeAtual).addPoint(clicked);
			}
		}
		else {
			Shape selectedShape = null;
			for (Shape s : shapes) {
				if (selectedShape != null) {
					break;
				}

				if (s.isPointInside(clicked)) {
					s.setSelected(true);
					selectedShape = s;
					break;
				}
			}

			if (selectedShape == null) {
				for (Shape s : shapes) {
					s.setSelected(false);
				}
			}
		}

		glDrawable.display();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (glDrawable == null)
			return;

		if (shapeAtual != null) {
			shapeAtual.mouseMoved(normalizePoint(e.getX(), e.getY()));
			glDrawable.display();
		}
	}

}
