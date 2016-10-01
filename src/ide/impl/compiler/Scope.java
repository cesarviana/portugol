package ide.impl.compiler;

import java.util.HashMap;
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
		vars = new HashMap<>();
	}
	
	public static Scope instance(String id){
		return new Scope(id);
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
	
	@Override
	public String toString() {
		return id;
	}
	
}
