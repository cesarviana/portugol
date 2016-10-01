package ide.impl.compiler.registryControl;

public interface SimbolTableRegistry {
	String getName();
	String getType();
	boolean isInitialized();
	boolean isUsed();
	//String getScope();
	//boolean isParameter();
	//int getParameterPosition();
	//boolean isVector();
	//boolean isFunction();
}
