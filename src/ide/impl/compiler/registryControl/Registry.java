package ide.impl.compiler.registryControl;

import lombok.Data;

public interface Registry {
	String getName();
	String getType();
	boolean isInitialized();
	boolean isUsed();
	void setUsed(boolean b);
	//String getScope();
	//boolean isParameter();
	//int getParameterPosition();
	//boolean isVector();
	//boolean isFunction();

	
}
