package cg.gl2d.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.LinkedList;
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

import cg.gl2d.control.Scanline;
import cg.gl2d.control.Utils;
import cg.gl2d.model.Circle;
import cg.gl2d.model.ClosedPolygon;
import cg.gl2d.model.EditorColor;
import cg.gl2d.model.EditorPoint;
import cg.gl2d.model.OpenPolygon;
import cg.gl2d.model.Polygon;
import cg.gl2d.model.Shape;
import cg.gl2d.model.Spline;

public class Editor extends JPanel implements GLEventListener, KeyListener,
		MouseListener, MouseMotionListener, MouseWheelListener,
		AdjustmentListener {

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
	private int verticalScroll = 1000;
	private int horizontalScroll = 1000;
	private double zoom = 0.1;
	private boolean adjustingZoom = false;
	private boolean enableZoomIn = true;
	private boolean enableZoomOut = true;

	private boolean initializing = true;
	private double xn;
	private double xp;
	private double yn;
	private double yp;
	
	private EditorColor foregndColor = new EditorColor(Color.black);
	private EditorColor backgndColor = new EditorColor(Color.green);

	private Shape shapeAtual;
	private Shape selectedShape;

	private EditorPoint pontoAntes;

	double yi = 0.0;

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
		canvas.addMouseWheelListener(this);
		canvas.addMouseMotionListener(this);
		/*
		 * Cria as barras de rolagem vertical e horizontal
		 */
		verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
		verticalScrollBar.setMaximum(verticalScroll * 2);
		verticalScrollBar.setValue(verticalScroll);
		verticalScrollBar.addAdjustmentListener(this);
		horizontalScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
		horizontalScrollBar.setMaximum(horizontalScroll * 2);
		horizontalScrollBar.setValue(horizontalScroll);
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

		pontoAntes = new EditorPoint();
	}

	public void focus() {
		canvas.requestFocus();
	}

	public void zoomIn() {
		if (enableZoomIn)
			doZoom(-0.01);
	}

	public void zoomOut() {
		if (enableZoomOut)
			doZoom(0.01);
	}

	private void doZoom(double value) {
		adjustingZoom = true;
		try {
			Point p1 = new Point(editorWidth / 2, editorHeight / 2);
			EditorPoint e = normalizePoint(p1.x, p1.y);

			zoom += value;
			enableZoomIn = zoom > 0.01;
			enableZoomOut = zoom < 0.99;
			listener.zoomEnable(enableZoomIn, enableZoomOut);
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

		if (this.action != EditorAction.move
				&& this.action != EditorAction.select) {
			// limpa a seleção
			for (Shape s : shapes) {
				s.setSelected(false);
			}

			// atualiza a tela se for possível
			if (glDrawable != null)
				glDrawable.display();
		}
	}

	public EditorAction getAction() {
		return action;
	}
	
	public Color getForegroundColor() {
		return foregndColor.toNativeColor();
	}
	
	public void setForegroundColor(Color color) {
		foregndColor = new EditorColor(color);
		
		if (selectedShape != null)
			selectedShape.setForegroundColor(foregndColor);
		
		glDrawable.display();
	}
	
	public Color getBackgroundColor() {
		return backgndColor.toNativeColor();
	}
	
	public void setBackgroundColor(Color color) {
		backgndColor = new EditorColor(color);
		
		if (selectedShape != null)
			selectedShape.setBackgroundColor(backgndColor);
		
		glDrawable.display();
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

		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(-40.0, 0.0);
		gl.glVertex2d(40.0, 0.0);

		gl.glVertex2d(0.0, -40.0);
		gl.glVertex2d(0.0, 40.0);
		gl.glEnd();

		if (yi != 0.0) {
			gl.glColor3f(0.5f, 0.0f, 0.5f);
			gl.glBegin(GL.GL_LINES);
			gl.glVertex2d(xn, yi);
			gl.glVertex2d(xp, yi);
			gl.glEnd();
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

	private void initializeOrthoSize() {
		xp = (editorWidth * zoom) / 2;
		xn = -xp;
		yp = (editorHeight * zoom) / 2;
		yn = -yp;
		initializing = false;
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int width,
			int height) {
		editorWidth = width;
		editorHeight = height;

		if (initializing)
			initializeOrthoSize();
		else
			adjustOrthoSize();
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
		} else {
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
		case KeyEvent.VK_COMMA: // ,
		case KeyEvent.VK_PERIOD: // .
			if (selectedShape != null) {
				selectedShape.scale(e.getKeyCode() == KeyEvent.VK_PERIOD);
				glDrawable.display();
			}
			break;
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
			if (selectedShape != null) {
				shapes.remove(selectedShape);
				glDrawable.display();
			}
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
		
		if (e.getButton() == MouseEvent.BUTTON3 && action == EditorAction.select && selectedShape != null) {
			selectedShape.removeSelectedPoint();
			glDrawable.display();
			return;
		}

		if (e.getClickCount() == 2 && shapeAtual != null) {
			shapeAtual.finishDrawing();
			shapeAtual = null;
			glDrawable.display();
			return;
		}

		if (shapeAtual == null) {
			switch (action) {
			case openPolygon: {
				shapeAtual = new OpenPolygon(foregndColor);
				shapes.add(shapeAtual);
				((Polygon) shapeAtual).addPoint(clicked);
				break;
			}
			case closedPolygon: {
				shapeAtual = new ClosedPolygon(foregndColor);
				shapes.add(shapeAtual);
				((Polygon) shapeAtual).addPoint(clicked);
				break;
			}
			case spline: {
				shapeAtual = new Spline(foregndColor, backgndColor);
				shapes.add(shapeAtual);
				((Polygon) shapeAtual).addPoint(clicked);
				break;
			}
			case circle: {
				shapeAtual = new Circle(foregndColor);
				((Circle) shapeAtual).setCenter(clicked);
				shapes.add(shapeAtual);
				break;
			}
			case select: {
				selectedShape = null;

				LinkedList<Shape> selShapes = new LinkedList<Shape>();

				// faz uma pre-selecao
				for (Shape s : shapes) {
					s.setSelected(false);

					if (s.isPointInsideBBox(clicked)) {
						selShapes.add(s);
					}
				}

				// scanline test
				yi = clicked.y;
				Scanline scl = new Scanline();
				scl.scan(selShapes, clicked);

				selectedShape = scl.getSelectedShape();

				if (selectedShape != null) {
					selectedShape.setSelected(true);
				}

				break;
			}
			}
		}

		if (action == EditorAction.openPolygon
				|| action == EditorAction.closedPolygon
				|| action == EditorAction.spline) {
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
		pontoAntes.x = pontoAntes.y = 0.0;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (glDrawable == null)
			return;

		if (selectedShape != null) {

			EditorPoint clicked = normalizePoint(e.getX(), e.getY());

			if (action == EditorAction.select && selectedShape.isMoveable()) {
				// primeira vez
				if (pontoAntes.x == 0 && pontoAntes.y == 0) {
					pontoAntes.x = clicked.x;
					pontoAntes.y = clicked.y;
				}

				EditorPoint ptMove = new EditorPoint(clicked.x - pontoAntes.x,
						clicked.y - pontoAntes.y);

				pontoAntes.x = clicked.x;
				pontoAntes.y = clicked.y;

				selectedShape.mover(ptMove);
			} else if (action == EditorAction.select) {
				selectedShape.update(clicked);
			}
			glDrawable.display();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			if (selectedShape != null) {
				selectedShape.rotate((double) e.getWheelRotation());
				glDrawable.display();
			} else {
				if (e.getWheelRotation() > 0.0) {
					zoomIn();
				} else {
					zoomOut();
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (glDrawable == null)
			return;

		if (shapeAtual != null) {
			shapeAtual.mouseMoving(normalizePoint(e.getX(), e.getY()));
			glDrawable.display();
		}
	}

}
