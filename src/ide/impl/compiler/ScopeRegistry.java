package ide.impl.compiler;

import ide.impl.compiler.registryControl.Registry;

public class ScopeRegistry extends Registry {

	private Scope scope;

	public ScopeRegistry(Scope scope) {
		this.scope =scope;
	}

	@Override
	public String getName() {
		return scope.getId();
	}

	@Override
	public String getType() {
		return "";
	}

	@Override
	public boolean isInitialized() {
		return false;
	}

	@Override
	public boolean isUsed() {
		return false;
	}

	@Override
	public void setUsed(boolean b) {
		// TODO
	}

	@Override
	public String getScope() {
		return scope.getParent().toString();
	}

	public static Registry instance(Scope scope) {
		return new ScopeRegistry(scope);
	}

}
