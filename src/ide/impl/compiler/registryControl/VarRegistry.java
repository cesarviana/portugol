package ide.impl.compiler.registryControl;

import ide.impl.compiler.Var;

public class VarRegistry implements SimbolTableRegistry {

	private Var var;

	public VarRegistry(Var var) {
		this.var = var;
	}

	@Override
	public String getName() {
		return var.getId();
	}

	@Override
	public String getType() {
		return var.getType();
	}

	@Override
	public boolean isInitialized() {
		return var.isInitialized();
	}

	@Override
	public boolean isUsed() {
		return false;
	}

	@Override
	public String toString() {
		return "name="+getName()+" |type="+getType()+" |initialized="+isInitialized()+" |used="+isUsed()+"";
	}
}
