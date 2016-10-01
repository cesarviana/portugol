package ide.impl.compiler.registryControl;

public interface Registry {
	String getName();
	String getType();
	boolean isInitialized();
	boolean isUsed();
	void setUsed(boolean b);	
}
