package ide.impl.compiler;


import java.util.stream.Collectors;

public class Function extends Scope{
	
	public static Function instance(String id) {
		return new Function(id);
	}

	private Function(String id) {
		super(id);
	}

	@Override
	public String toString() {
		return getId();
	}

	public int getParamPosition(Var var) {
		return getVars().values().stream().filter(v -> v.isParam())
				.collect(Collectors.toList()).indexOf(var) + 1;
	}

}
