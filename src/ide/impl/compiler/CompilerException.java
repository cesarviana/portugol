package ide.impl.compiler;

public class CompilerException extends RuntimeException {

	public CompilerException(String message, Exception e) {
		super(message,e);
	}
	
	public CompilerException(String message) {
		super(message);
	}

}
