package cg.gl2d.editor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private Editor editor;
	
	public MainWindow() {
		super("Editor de polígonos 2D");   
		setBounds(300, 200, 500, 500); 
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		editor = new Editor();
		add(editor.getCanvas(), BorderLayout.CENTER);
	}		
	
	public void requestEditorFocus() {
		editor.getCanvas().requestFocus();
	}
	
	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		mainWindow.requestEditorFocus();
	}

	
}
