package ide.impl.compiler.registryControl;

public abstract class Registry {
	abstract public String getName();

	abstract public String getType();

	abstract public boolean isInitialized();

	abstract public boolean isUsed();

	abstract public void setUsed(boolean b);

	abstract public String getScope();
	abstract public boolean isParameter();
	// int getParameterPosition();
	// boolean isVector();
	// boolean isFunction();

	@Override
	public String toString() {
		return "name=" + getName() + " |type=" + getType() + " |initialized=" + isInitialized() + " |used=" + isUsed()
				+ " |scope=" + getScope() + " |param=" + isParameter();
	}
}
