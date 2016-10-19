package ide.impl.compiler;

public class VarVector extends Var {

	protected VarVector(String scope, String type, String id, boolean constant) {
		super(scope,type,id,constant);
	}
	private int size = 0;

	public static VarVector instance(Var var) {
		return new VarVector(var.getScope().getId(), var.getType(), var.getId(), var.isConstant());
	}
	
	@Override
	public boolean isVector() {
		return true;
	}

    public void setSize(int size) {
        this.size = size;
    }

	public int getSize() {
		return size;
	}
}
