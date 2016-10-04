package view.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import ide.impl.compiler.CompilerException;
import ide.impl.files.PortugolFile;
import view.View;
import view.ViewListener;

public class ViewImpl extends JFrame implements View {

	private static final String DEFAULT_ERROR_MESSAGE = "Erro";

	private final class ChangeCodeListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			notifyViewListeners();
		}

		private void notifyViewListeners() {
			listeners.forEach(list -> list.codeChanged(txtCode.getText()));
		}
	}

	private JPanel contentPane;
	private static View instance;
	private JButton btnCarregarArquivo;
	private RSyntaxTextArea txtCode;
	private JButton btnSalvar;
	private Collection<ViewListener> listeners;
	private JButton btnCompilar;
	private JTextArea textArea;
	private JPanel panelTabelaSimbolos;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewImpl frame = new ViewImpl();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static View getInstance() {
		if (instance == null)
			instance = new ViewImpl();
		return instance;
	}

	/**
	 * Create the frame.
	 */
	public ViewImpl() {
		this.listeners = new ArrayList<>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1123, 449);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelTop = new JPanel();
		contentPane.add(panelTop, BorderLayout.NORTH);

		btnCarregarArquivo = new JButton("CarregarArquivo");
		panelTop.add(btnCarregarArquivo);

		btnSalvar = new JButton("Salvar");
		panelTop.add(btnSalvar);

		btnCompilar = new JButton("Compilar");
		panelTop.add(btnCompilar);

		JPanel panelFooter = new JPanel();
		contentPane.add(panelFooter, BorderLayout.SOUTH);
		panelFooter.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		textArea.setEditable(false);
		panelFooter.add(textArea);

		JSplitPane splitCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		int location = (int) (getWidth() * 0.7); 
		splitCenter.setDividerLocation(location);
		contentPane.add(splitCenter, BorderLayout.CENTER);
		
		txtCode = new RSyntaxTextArea();
		txtCode.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		txtCode.setAutoIndentEnabled(true);
		txtCode.setColumns(10);
		
		txtCode.addKeyListener(new ChangeCodeListener());
		RTextScrollPane panelCode = new RTextScrollPane(txtCode);
		splitCenter.setLeftComponent(panelCode);
		
		panelTabelaSimbolos = new JPanel(new BorderLayout());
		panelTabelaSimbolos.setPreferredSize(new Dimension(400,600));
		splitCenter.setRightComponent(panelTabelaSimbolos);
		
		table = new JTable();
		panelTabelaSimbolos.add(new JScrollPane(table), BorderLayout.CENTER);
		
	}

	@Override
	public void showView() {
		this.setVisible(true);
	}

	@Override
	public void setLoadFileAction(Action loadFileAction) {
		btnCarregarArquivo.setAction(loadFileAction);
	}

	@Override
	public void setSaveFileAction(Action saveFileAction) {
		btnSalvar.setAction(saveFileAction);
	}

	@Override
	public void setCompileAction(Action compileAction) {
		btnCompilar.setAction(compileAction);
	}

	@Override
	public void error(Throwable e) {
		String errMessage = createErrorMessage(e);
		showErrorMessage(e, errMessage);
	}
	
	@Override
	public void message(String string) {
		textArea.setText(string);
	}

	private String createErrorMessage(Throwable e) {
		String errMessage = e.getMessage();
		boolean noMessage = errMessage == null || errMessage.isEmpty();
		errMessage = noMessage ? DEFAULT_ERROR_MESSAGE : errMessage;
		return errMessage;
	}

	private void showErrorMessage(Throwable e, String errMessage) {
		if (e instanceof CompilerException) {
			message(errMessage);
		} else {
			JOptionPane.showMessageDialog(this, errMessage, DEFAULT_ERROR_MESSAGE,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void setSimbolTable(TableModel tableModel) {
		table.setModel(tableModel);
	}
	
	@Override
	public void showFile(PortugolFile pf) {
		txtCode.setText(pf.getText());
	}

	@Override
	public JComponent getComponent() {
		return contentPane;
	}

	@Override
	public void addListener(ViewListener viewListener) {
		this.listeners.add(viewListener);
	}
}
