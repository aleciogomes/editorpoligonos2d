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

/**
 * Janela principal do editor de pol�gonos 2D
 */
public class MainWindow extends JFrame implements EditorListener, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel toolBar;
	private JToggleButton selectButton;
	private JToggleButton openPolygonButton;
	private JToggleButton closedPolygonButton;
	private JToggleButton circleButton;
	private JToggleButton splineButton;
	private JToggleButton zoomInButton;
	private JToggleButton zoomOutButton;
	private JButton foregndColorButton;
	private JButton backgndColorButton;
	private int buttonOffset = 0;
	
	private Editor editor;
	
	/**
	 * Cria a janela com a barra de ferramentas e a �rea de edi��o
	 */
	public MainWindow() {
		super("Editor de pol�gonos 2D");   
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initComponents();
	}	
	
	/**
	 * Inicializa os componentes da tela
	 */
	private void initComponents() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(createToolBar(), BorderLayout.NORTH);
		contentPane.add(createEditor(), BorderLayout.CENTER);
		foregndColorButton.setBackground(editor.getForegroundColor());
		foregndColorButton.setForeground(foregndColorButton.getBackground());
		backgndColorButton.setBackground(editor.getBackgroundColor());
		backgndColorButton.setForeground(backgndColorButton.getBackground());
	}
	
	/**
	 * Cria a barra de ferramentas
	 * @return Panel com os bot�es
	 */
	private JPanel createToolBar() {
		toolBar = new JPanel();
		toolBar.setLayout(null);
		toolBar.setPreferredSize(new Dimension(getWidth(), 35));
		createSeparator();
		selectButton 		= createToggleButton("Modo de sele��o (ESC)"	, "selectButton");
		openPolygonButton	= createToggleButton("Desenhar pol�gono aberto"	, "openPolygonButton");
		closedPolygonButton = createToggleButton("Desenhar pol�gono fechado", "closedPolygonButton");
		circleButton 		= createToggleButton("Desenhar c�rculo"			, "circleButton");
		splineButton 		= createToggleButton("Desenhar spline"			, "splineButton");
		createSeparator();
		zoomInButton		= createToggleButton("Zoom in (+)"	, "zoomInButton");
		zoomOutButton		= createToggleButton("Zoom out (-)"	, "zoomOutButton");
		createSeparator();
		foregndColorButton	= createButton("Selecionar cor principal", null);
		backgndColorButton	= createButton("Selecionar cor secund�ria", null);
		return toolBar;
	}
	
	/**
	 * Cria um bot�o que o usu�rio clica e fica pressionado
	 * @param text Hint do bot�o
	 * @param imageName Nome da imagem que ser� pintada no bot�o
	 * @return Bot�o
	 */
	private JToggleButton createToggleButton(String text, String imageName) {
		JToggleButton b = new JToggleButton(getImage(imageName));
		b.setToolTipText(text);
		b.setBounds(buttonOffset, 5, 25, 25);
		b.addActionListener(this);
		toolBar.add(b);
		buttonOffset += 25;
		return b;
	}

	/**
	 * Cria um bot�o padr�o
	 * @param text Hint do bot�o
	 * @param imageName Nome da imagem que ser� pintada no bot�o
	 * @return Bot�o
	 */
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
	
	/**
	 * Cria um separador para a agrupar bot�es na barra de ferramentas
	 * @return Separador
	 */
	private JSeparator createSeparator() {
		JSeparator s = new JSeparator(SwingConstants.VERTICAL);
		s.setBounds(buttonOffset + 4, 5, 5, 25);
		buttonOffset += 10;
		toolBar.add(s);
		return s;
	}
	
	/**
	 * Cria o renderizador que ser� usado como �rea de edi��o pelo usu�rio, 
	 * adicionando a pr�pria janela como listener de eventos de notifica��o.
	 * @return Renderizador
	 */
	private JPanel createEditor() {
		editor = new Editor(this);
		return editor;
	}
	
	/**
	 * Chama o m�todo de foco do renderizador
	 */
	public void requestEditorFocus() {
		editor.focus();
	}
	
	/**
	 * M�todo de notifica��o disparado pelo renderizador quando a a��o de edi��o muda.
	 * � usado para alterar o estado dos bot�es da barra de ferramentas.
	 */
	public void actionChanged(EditorAction action) {
		selectButton.setSelected(action == EditorAction.select);
		openPolygonButton.setSelected(action == EditorAction.openPolygon);
		closedPolygonButton.setSelected(action == EditorAction.closedPolygon);
		circleButton.setSelected(action == EditorAction.circle);
		splineButton.setSelected(action == EditorAction.spline);
		zoomInButton.setSelected(action == EditorAction.zoomIn);
		zoomOutButton.setSelected(action == EditorAction.zoomOut);
		openPolygonButton.setBorderPainted(openPolygonButton.isSelected());
		closedPolygonButton.setBorderPainted(closedPolygonButton.isSelected());
		circleButton.setBorderPainted(circleButton.isSelected());
		selectButton.setBorderPainted(selectButton.isSelected());
		splineButton.setBorderPainted(splineButton.isSelected());
	}

	/**
	 * M�todo disparado pelos bot�es da barra de ferramentas quando clicados.
	 */
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
			editor.setAction(EditorAction.zoomIn);
		else if (e.getSource() == zoomOutButton)
			editor.setAction(EditorAction.zoomOut);
		else if (e.getSource() == foregndColorButton) 
			chooseForegroundColor();
		else if (e.getSource() == backgndColorButton) 
			chooseBackgroundColor();
	}
	
	/**
	 * Evento disparado pelo renderizador quando o zoom � habilitado/desabilitado.
	 * Utilizado para habilitar/desabilitar os bot�es de zoom.
	 */
	public void zoomEnable(boolean zoomIn, boolean zoomOut) {
		zoomInButton.setEnabled(zoomIn);
		zoomOutButton.setEnabled(zoomOut);
	}
	
	/**
	 * Evento disparado pelo renderizador quando o usu�rio seleciona os desceleciona um pol�gono.
	 * Utilizado para alterar os bot�es de sele��o de cor conforme cores do pol�gono selecionado.
	 * Obs.: Se "selected == null", � porque n�o tem nenhum objeto selecionado, ent�o usa a cor 
	 * padr�o atribu�da ao renderizador. 
	 */
	public void selectedChanged(Shape selected) {
		if (selected == null) {
			foregndColorButton.setBackground(editor.getForegroundColor());
			backgndColorButton.setEnabled(true);
			backgndColorButton.setBackground(editor.getBackgroundColor());
		}
		else {
			foregndColorButton.setBackground(selected.getForegroundColor().toNativeColor());
			backgndColorButton.setEnabled(selected.getBackgroundColor() != null);
			
			if (selected.getBackgroundColor() == null)
				backgndColorButton.setBackground(null);
			else 
				backgndColorButton.setBackground(selected.getBackgroundColor().toNativeColor());
		}
		foregndColorButton.setForeground(foregndColorButton.getBackground());
		backgndColorButton.setForeground(backgndColorButton.getBackground());
	}
	
	/**
	 * Abre a caixa de sele��o de cor e aplica a cor selecionada � cor principal dos
	 * pol�gonos do renderizador.
	 */
	private void chooseForegroundColor() {
		Color color = JColorChooser.showDialog(this, "Selecione a cor", editor.getForegroundColor());
		
		if (color != null) {
			editor.setForegroundColor(color);
			foregndColorButton.setBackground(color);
			foregndColorButton.setForeground(color);
		}
	}
	
	/**
	 * Abre a caixa de sele��o de cor e aplica a cor selecionada � cor secund�ria dos
	 * pol�gonos do renderizador.
	 */
	private void chooseBackgroundColor() {
		Color color = JColorChooser.showDialog(this, "Selecione a cor", editor.getBackgroundColor());
		
		if (color != null) {
			editor.setBackgroundColor(color);
			backgndColorButton.setBackground(color);
			backgndColorButton.setForeground(color);
		}
	}
	
	/**
	 * Retorna um PNG do pacote "cg.gl2d.editor.img" 
	 * @param imageName Nome da imagem desejada
	 * @return A imagem de acordo com o nome
	 */
	private ImageIcon getImage(String imageName) {
		return new ImageIcon(MainWindow.class.getResource("img/"+ imageName +".png"));
	}
	
	/**
	 * M�todo de inicializa��o do editor
	 * @param args Argumentos de inicializa��o passados pela JVM
	 */
	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		mainWindow.requestEditorFocus();
	}
	
}
