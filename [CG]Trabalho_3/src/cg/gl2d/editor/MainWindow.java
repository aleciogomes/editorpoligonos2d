package cg.gl2d.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import cg.gl2d.model.Shape;

public class MainWindow extends JFrame implements EditorListener, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel toolBar;
	private JToggleButton selectButton;
	private JToggleButton openPolygonButton;
	private JToggleButton closedPolygonButton;
	private JToggleButton circleButton;
	private JToggleButton splineButton;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton foregndColorButton;
	private JButton backgndColorButton;
	private int buttonOffset = 0;
	
	private Editor editor;
	
	public MainWindow() {
		super("Editor de polígonos 2D");   
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initComponents();
	}	
	
	private void initComponents() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(createToolBar(), BorderLayout.NORTH);
		contentPane.add(createEditor(), BorderLayout.CENTER);
		foregndColorButton.setBackground(editor.getForegroundColor());
		backgndColorButton.setBackground(editor.getBackgroundColor());
	}
	
	private JPanel createToolBar() {
		toolBar = new JPanel();
		toolBar.setLayout(null);
		toolBar.setPreferredSize(new Dimension(getWidth(), 35));
		createSeparator();
		selectButton 		= createToggleButton("Modo de seleção (ESC)"	, "selectButton");
		openPolygonButton	= createToggleButton("Desenhar polígono aberto"	, "openPolygonButton");
		closedPolygonButton = createToggleButton("Desenhar polígono fechado", "closedPolygonButton");
		circleButton 		= createToggleButton("Desenhar círculo"			, "circleButton");
		splineButton 		= createToggleButton("Desenhar spline"			, "splineButton");
		createSeparator();
		zoomInButton		= createButton("Zoom in (+)"	, "zoomInButton");
		zoomOutButton		= createButton("Zoom out (-)"	, "zoomOutButton");
		createSeparator();
		foregndColorButton	= createButton("Selecionar cor principal", null);
		backgndColorButton	= createButton("Selecionar cor secundária", null);
		return toolBar;
	}
	
	private JToggleButton createToggleButton(String text, String imageName) {
		JToggleButton b = new JToggleButton(getImage(imageName));
		b.setToolTipText(text);
		b.setBounds(buttonOffset, 5, 25, 25);
		b.addActionListener(this);
		toolBar.add(b);
		buttonOffset += 25;
		return b;
	}
	
	private JButton createButton(String text, String imageName) {
		JButton b = new JButton();
		
		if (imageName != null) {
			b.setIcon(getImage(imageName));	
		}
		b.setToolTipText(text);
		b.setBounds(buttonOffset, 5, 25, 25);
		b.setBorderPainted(true);
		b.addActionListener(this);
		toolBar.add(b);
		buttonOffset += 25;
		return b;
	}
	
	private JSeparator createSeparator() {
		JSeparator s = new JSeparator(SwingConstants.VERTICAL);
		s.setBounds(buttonOffset + 4, 5, 5, 25);
		buttonOffset += 10;
		toolBar.add(s);
		return s;
	}
	
	private JPanel createEditor() {
		editor = new Editor(this);
		return editor;
	}
	
	public void requestEditorFocus() {
		editor.focus();
	}
	
	public void actionChanged(EditorAction action) {
		selectButton.setSelected(action == EditorAction.select);
		openPolygonButton.setSelected(action == EditorAction.openPolygon);
		closedPolygonButton.setSelected(action == EditorAction.closedPolygon);
		circleButton.setSelected(action == EditorAction.circle);
		splineButton.setSelected(action == EditorAction.spline);
		openPolygonButton.setBorderPainted(openPolygonButton.isSelected());
		closedPolygonButton.setBorderPainted(closedPolygonButton.isSelected());
		circleButton.setBorderPainted(circleButton.isSelected());
		selectButton.setBorderPainted(selectButton.isSelected());
		splineButton.setBorderPainted(splineButton.isSelected());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectButton)
			editor.setAction(EditorAction.select);
		else if (e.getSource() == openPolygonButton)
			editor.setAction(EditorAction.openPolygon);
		else if (e.getSource() == closedPolygonButton)
			editor.setAction(EditorAction.closedPolygon);
		else if (e.getSource() == circleButton)
			editor.setAction(EditorAction.circle);
		else if (e.getSource() == splineButton)
			editor.setAction(EditorAction.spline);
		else if (e.getSource() == zoomInButton)
			editor.zoomIn();
		else if (e.getSource() == zoomOutButton)
			editor.zoomOut();
		else if (e.getSource() == foregndColorButton) 
			chooseForegroundColor();
		else if (e.getSource() == backgndColorButton) 
			chooseBackgroundColor();
	}
	
	public void zoomEnable(boolean zoomIn, boolean zoomOut) {
		zoomInButton.setEnabled(zoomIn);
		zoomOutButton.setEnabled(zoomOut);
	}
	
	public void selectedChanged(Shape selected) {
		if (selected == null) {
			foregndColorButton.setBackground(editor.getForegroundColor());
			backgndColorButton.setEnabled(true);
			backgndColorButton.setBackground(editor.getBackgroundColor());
		}
		else {
			foregndColorButton.setBackground(selected.getForegroundColor().toNativeColor());
			
			if (selected.getBackgroundColor() == null)
				backgndColorButton.setEnabled(false);
			else {
				backgndColorButton.setEnabled(true);
				backgndColorButton.setBackground(selected.getBackgroundColor().toNativeColor());
			}
		}
	}
	
	private void chooseForegroundColor() {
		Color color = JColorChooser.showDialog(this, "Selecione a cor", editor.getForegroundColor());
		
		if (color != null) {
			editor.setForegroundColor(color);
			foregndColorButton.setBackground(color);
		}
	}
	
	private void chooseBackgroundColor() {
		Color color = JColorChooser.showDialog(this, "Selecione a cor", editor.getBackgroundColor());
		
		if (color != null) {
			editor.setBackgroundColor(color);
			backgndColorButton.setBackground(color);
		}
	}
	
	private ImageIcon getImage(String imageName) {
		return new ImageIcon(MainWindow.class.getResource("img/"+ imageName +".png"));
	}
	
	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		mainWindow.requestEditorFocus();
	}
	
}
