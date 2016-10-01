package ide.impl.compiler.registryControl;

import ide.impl.compiler.Var;

public class VarRegistry implements Registry {

	private Var var;
	
	public static VarRegistry instance(String scope, String id) {
		return new VarRegistry(Var.instance(scope, "", id, false));
	}

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
		return var.isUsed();
	}

	@Override
	public String toString() {
		return "name="+getName()+" |type="+getType()+" |initialized="+isInitialized()+" |used="+isUsed()+"";
	}

	@Override
	public void setUsed(boolean b) {
		var.use();
	}
}
