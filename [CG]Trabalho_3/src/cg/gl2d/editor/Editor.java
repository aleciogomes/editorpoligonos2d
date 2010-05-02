package cg.gl2d.editor;

import java.awt.BorderLayout;
import java.awt.Point;
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
	private double zoom = 0.1;
	private boolean adjustingZoom = false;

	private double xn = 0.0;
	private double xp = 0.0;
	private double yn = 0.0;
	private double yp = 0.0;

	private Shape shapeAtual;

	private Shape selectedShape;

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

		selectedShape = null;

		setAction(EditorAction.select);
	}

	public void focus() {
		canvas.requestFocus();
	}
	
	public void zoomIn() {
		doZoom(-0.01);
		listener.zoomEnable(zoom > 0.01, true);
	}
	
	public void zoomOut() {
		doZoom(0.01);
		listener.zoomEnable(true, zoom < 0.99);
	}
	
	private void doZoom(double value) {
		adjustingZoom = true;
		try {
			Point p1 = new Point(editorWidth / 2, editorHeight / 2);
			EditorPoint e = normalizePoint(p1.x, p1.y);
			
			zoom += value;
			adjustOrthoSize();
			
			Point p2 = normalizeEditorPoint(e.x, e.y);
			verticalScrollBar.setValue(verticalScroll + (p2.y - p1.y));
			horizontalScrollBar.setValue(horizontalScroll + (p2.x - p1.x));
			adjustOrthoSize();
			
			glDrawable.display();
		} finally {
			adjustingZoom = false;
		}
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
	}
	
	private void adjustOrthoSize() {
		xp = editorWidth * zoom + xn;
		yn = yp - editorHeight * zoom;
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int width, int height) {
		editorWidth = width;
		editorHeight = height;

		if (xp == xn && yp == yn) {
			xp = width * zoom;
			yp = height * zoom;
		}
		else {
			adjustOrthoSize();
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (e.getSource() == verticalScrollBar) {
			/*
			 * Scroll vertical
			 */
			int v = e.getValue() - verticalScroll;
			verticalScroll = e.getValue();
			yp -= v * zoom;
		}
		else {
			/*
			 * Scroll horizontal
			 */
			int v = e.getValue() - horizontalScroll;
			horizontalScroll = e.getValue();
			xn += v * zoom;
		}
		if (!adjustingZoom) {
			adjustOrthoSize();
			glDrawable.display();	
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			glDrawable.display();
			break;
		case KeyEvent.VK_ESCAPE:
			setAction(EditorAction.select);
			break;
		case KeyEvent.VK_MINUS:
			zoomOut();
			break;
		case KeyEvent.VK_EQUALS:
		case KeyEvent.VK_PLUS:
			zoomIn();
			break;
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
		p.x = Utils.normalizeE(0, x, editorWidth, xn, xp);
		p.y = Utils.normalizeE(0, editorHeight - y, editorHeight, yn, yp);
		return p;
	}
	
	private Point normalizeEditorPoint(double x, double y) {
		Point p = new Point();
		p.x = Utils.normalizeB(0, editorWidth, xn, x, xp);
		p.y = editorHeight - Utils.normalizeB(0, editorHeight, yn, y, yp);
		return p;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		EditorPoint clicked = normalizePoint(e.getX(), e.getY());

		if (e.getClickCount() == 2 && shapeAtual != null) {
			shapeAtual.finishDrawing();
			shapeAtual = null;
			glDrawable.display();
			return;
		}

		if (shapeAtual == null) {
			switch (action) {
			case openPolygon: {
				shapeAtual = new OpenPolygon();
				shapes.add(shapeAtual);
				((Polygon) shapeAtual).addPoint(clicked);
				break;
			}
			case closedPolygon: {
				shapeAtual = new ClosedPolygon();
				shapes.add(shapeAtual);
				((Polygon) shapeAtual).addPoint(clicked);
				break;
			}
			case spline: {
				shapeAtual = new Spline();
				shapes.add(shapeAtual);
				((Polygon) shapeAtual).addPoint(clicked);
				break;
			}
			case circle: {
				shapeAtual = new Circle();
				((Circle) shapeAtual).setCenter(clicked);
				shapes.add(shapeAtual);
				break;
			}
			case select: {
				selectedShape = null;
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
				break;
			}
			}
		}

		if (action == EditorAction.openPolygon || action == EditorAction.closedPolygon || action == EditorAction.spline) {
			((Polygon) shapeAtual).addPoint(clicked);
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
		if (glDrawable == null)
			return;

		if (selectedShape != null) {

			selectedShape.update(normalizePoint(e.getX(), e.getY()));
			glDrawable.display();
		}
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
