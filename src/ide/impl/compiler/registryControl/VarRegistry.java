package ide.impl.compiler.registryControl;

import ide.impl.compiler.Var;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "var", callSuper = false)
public class VarRegistry extends Registry {

	private Var var;

	public static VarRegistry instance(String id, String scope) {
		return new VarRegistry(Var.instance(scope, "", id, false));
	}

	public static VarRegistry instance(Var var) {
		return new VarRegistry(var);
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
	public String getScope() {
		return var.getScope().getId();
	}

	@Override
	public void setUsed(boolean b) {
		var.use();
	}

	@Override
	public boolean isParameter() {
		return var.isParam();
	}

	@Override
<<<<<<< HEAD
	public boolean isVector() {
		return var.isVector();
	}

	
	@Override
	public int getParameterPosition() {
		return var.getParamPosition();
=======
	boolean isFunction() {
		return false;
>>>>>>> 293b4b233026435042e6006111a289155de03d42
	}
}
