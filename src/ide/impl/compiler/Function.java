package ide.impl.compiler;

import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Function extends Scope {

	private String type;
	private boolean used;
	
	public static Function instance(String id) {
		return new Function("", id);
	}

	public static Function instance(String type, String id) {
		return new Function(type, id);
	}

	private Function(String type, String id) {
		super(id);
		setType(type);
	}

	@Override
	public String toString() {
		return getId() + ":" + getType();
	}

	public int getParamPosition(Var var) {
		return getVars().values().stream().filter(v -> v.isParam()).collect(Collectors.toList()).indexOf(var) + 1;
	}

	public void use() {
		this.used = true;
	}

}
