package ide.impl.compiler;

import gals.LexicalError;
import gals.Lexico;
import gals.SemanticError;
import gals.Semantico;
import gals.Sintatico;
import gals.SyntaticError;
import ide.impl.files.PortugolFile;

public class Compiler {

	//private static Compiler instance;
	private final SimbolTable simbolTable;
	
	public static Compiler instance(){
		return instance(SimbolTable.instance());
	}
	
	public static Compiler instance(SimbolTable simbolTable) {
//		if (instance == null)
//			instance = new Compiler(simbolTable);
//		return instance;
		return new Compiler(simbolTable);
	}
	
	private Compiler(SimbolTable simbolTable) {
		this.simbolTable = simbolTable;
	}

	public void compile(PortugolFile selectedFile) {
		Lexico lexico = new Lexico(selectedFile.getText());
		Sintatico sintatico = new Sintatico();
		Semantico semantico = new SemanticoPortugol(simbolTable);
		try {
			sintatico.parse(lexico, semantico);
		} catch (LexicalError e) {
			throw new CompilerException("Erro léxico: " + e.getMessage() + " Posição:" + e.getPosition(), e);
		} catch (SyntaticError e) {
			throw new CompilerException("Erro sintático: " + e.getMessage() + " Posição:" + e.getPosition(), e);
		} catch (SemanticError e) {
			throw new CompilerException("Erro semântico: "+ e.getMessage() + " Posição:" + e.getPosition(), e);
		} catch (Exception e) {
			throw new CompilerException("Erro durante a compilação: " + e.getMessage(), e);
		}
	}

}
