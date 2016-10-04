package ide.impl.compiler;

public class VarVector extends Var {

	protected VarVector(String scope, String type, String id, boolean constant) {
		super(scope,type,id,constant);
	}
	
	public static Var instance(Var var) {
		return new VarVector(var.getScopeStr(), var.getType(), var.getId(), var.isConstant());
	}
}
