package ide.impl.compiler;

public class VarVector extends Var {

	protected VarVector(String scope, String type, String id, boolean constant) {
		super(scope,type,id,constant);
	}
	public static VarVector instance(String scope, String type, String id) {
		return instance(scope, type, id, false);
	}
	
	public static VarVector instance(String scope, String type, String id,
			boolean constant) {
		return new VarVector(scope, type, id, constant);
	}
}
