package cg.gl2d.editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;

public class MainWindow extends JFrame implements EditorListener, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel toolBar;
	private JToggleButton selectButton;
	private JToggleButton openPolygonButton;
	private JToggleButton closedPolygonButton;
	private JToggleButton circleButton;
	private JToggleButton splineButton;
	
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
	}
	
	private JPanel createToolBar() {
		
		selectButton = new JToggleButton(getImage("selectButton"));
		selectButton.setToolTipText("Modo de seleção");
		selectButton.setBounds(10, 5, 25, 25);
		selectButton.addActionListener(this);
		
		openPolygonButton = new JToggleButton(getImage("openPolygonButton"));
		openPolygonButton.setToolTipText("Desenhar polígono aberto");
		openPolygonButton.setBounds(35, 5, 25, 25);
		openPolygonButton.addActionListener(this);
		
		closedPolygonButton = new JToggleButton(getImage("closedPolygonButton"));
		closedPolygonButton.setToolTipText("Desenhar polígono fechado");
		closedPolygonButton.setBounds(60, 5, 25, 25);
		closedPolygonButton.addActionListener(this);
		
		circleButton = new JToggleButton(getImage("circleButton"));
		circleButton.setToolTipText("Desenhar círculo");
		circleButton.setBounds(85, 5, 25, 25);
		circleButton.addActionListener(this);
		
		splineButton = new JToggleButton(getImage("splineButton"));
		splineButton.setToolTipText("Desenhar spline");
		splineButton.setBounds(110, 5, 25, 25);
		splineButton.addActionListener(this);
		
		toolBar = new JPanel();
		toolBar.setLayout(null);
		toolBar.setPreferredSize(new Dimension(getWidth(), 35));
		toolBar.add(selectButton);
		toolBar.add(openPolygonButton);
		toolBar.add(closedPolygonButton);
		toolBar.add(circleButton);
		toolBar.add(splineButton);
		return toolBar;
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
		selectButton.setBorderPainted(selectButton.isSelected());
		openPolygonButton.setSelected(action == EditorAction.openPolygon);
		openPolygonButton.setBorderPainted(openPolygonButton.isSelected());
		closedPolygonButton.setSelected(action == EditorAction.closedPolygon);
		closedPolygonButton.setBorderPainted(closedPolygonButton.isSelected());
		circleButton.setSelected(action == EditorAction.circle);
		circleButton.setBorderPainted(circleButton.isSelected());
		splineButton.setSelected(action == EditorAction.spline);
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
