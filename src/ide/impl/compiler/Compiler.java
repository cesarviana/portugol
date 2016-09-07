package ide.impl.compiler;

import gals.LexicalError;
import gals.Lexico;
import gals.SemanticError;
import gals.Semantico;
import gals.Sintatico;
import gals.SyntaticError;
import ide.impl.files.PortugolFile;

public class Compiler {

	private static Compiler instance;

	private final Semantico semantico = new SemanticoPortugol();

	public static Compiler instance() {

		if (instance == null)
			instance = new Compiler();
		return instance;
	}

	public void compile(PortugolFile selectedFile) {
		Lexico lexico = new Lexico(selectedFile.getText());
		Sintatico sintatico = new Sintatico();
		try {
			sintatico.parse(lexico, semantico);
		} catch (LexicalError e) {
			throw new CompilerException("Erro léxico: " + e.getMessage(), e);
		} catch (SyntaticError e) {
			throw new CompilerException("Erro sintático: " + e.getMessage(), e);
		} catch (SemanticError e) {
			throw new CompilerException("Erro semantico: "+ e.getMessage(), e);
		} catch (Exception e) {
			throw new CompilerException("Erro durante a compilação: " + e.getMessage(), e);
		}
	}

}
