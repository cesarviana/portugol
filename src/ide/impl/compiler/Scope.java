package ide.impl.compiler;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of={"id"})
public class Scope {

	private String id;
	private Map<String, Var> vars;
	
	public Scope(String id) {
		this.id = id;
		vars = new LinkedHashMap<>();
	}
	
	public void addVar(Var var) {
		if(vars.containsKey(var.getId()))
			throw new CompilerException("A variável " + var + " já foi declarada na função " + this);
		vars.put(var.getId(), var);
		var.setScope(this);
	}

	public Var getVar(String varId) {
		return vars.getOrDefault(varId, Var.NULL);
	}

	public static final Scope NULL = new Scope("") {
		@Override
		public String getId() {
			return "";
		}
		@Override
		public Var getVar(String varId) {
			return Var.NULL;
		}
	};
	
}
