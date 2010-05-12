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
 * Janela principal do editor de polígonos 2D
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
	 * Cria a janela com a barra de ferramentas e a área de edição
	 */
	public MainWindow() {
		super("Editor de polígonos 2D");   
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
	 * @return Panel com os botões
	 */
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
		zoomInButton		= createToggleButton("Zoom in (+)"	, "zoomInButton");
		zoomOutButton		= createToggleButton("Zoom out (-)"	, "zoomOutButton");
		createSeparator();
		foregndColorButton	= createButton("Selecionar cor principal", null);
		backgndColorButton	= createButton("Selecionar cor secundária", null);
		return toolBar;
	}
	
	/**
	 * Cria um botão que o usuário clica e fica pressionado
	 * @param text Hint do botão
	 * @param imageName Nome da imagem que será pintada no botão
	 * @return Botão
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
	 * Cria um botão padrão
	 * @param text Hint do botão
	 * @param imageName Nome da imagem que será pintada no botão
	 * @return Botão
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
	 * Cria um separador para a agrupar botões na barra de ferramentas
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
	 * Cria o renderizador que será usado como área de edição pelo usuário, 
	 * adicionando a própria janela como listener de eventos de notificação.
	 * @return Renderizador
	 */
	private JPanel createEditor() {
		editor = new Editor(this);
		return editor;
	}
	
	/**
	 * Chama o método de foco do renderizador
	 */
	public void requestEditorFocus() {
		editor.focus();
	}
	
	/**
	 * Método de notificação disparado pelo renderizador quando a ação de edição muda.
	 * É usado para alterar o estado dos botões da barra de ferramentas.
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
	 * Método disparado pelos botões da barra de ferramentas quando clicados.
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
	 * Evento disparado pelo renderizador quando o zoom é habilitado/desabilitado.
	 * Utilizado para habilitar/desabilitar os botões de zoom.
	 */
	public void zoomEnable(boolean zoomIn, boolean zoomOut) {
		zoomInButton.setEnabled(zoomIn);
		zoomOutButton.setEnabled(zoomOut);
	}
	
	/**
	 * Evento disparado pelo renderizador quando o usuário seleciona os desceleciona um polígono.
	 * Utilizado para alterar os botões de seleção de cor conforme cores do polígono selecionado.
	 * Obs.: Se "selected == null", é porque não tem nenhum objeto selecionado, então usa a cor 
	 * padrão atribuída ao renderizador. 
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
	 * Abre a caixa de seleção de cor e aplica a cor selecionada à cor principal dos
	 * polígonos do renderizador.
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
	 * Abre a caixa de seleção de cor e aplica a cor selecionada à cor secundária dos
	 * polígonos do renderizador.
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
	 * Método de inicialização do editor
	 * @param args Argumentos de inicialização passados pela JVM
	 */
	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		mainWindow.requestEditorFocus();
	}
	
}
