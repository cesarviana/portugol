package ide.impl.compiler;

import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {

	private String id;
	private Map<String, Var> vars;
	
	public Scope(String id) {
		this.id = id;
		vars = new LinkedHashMap<>();
	}
	
	public String getId() {
		return id;
	}
	
	public Map<String, Var> getVars() {
		return vars;
	}
	
	public void addVar(Var var) {
		if(vars.containsKey(var.getId()))
			throw new CompilerException("A variável " + var + " já foi declarada na função " + this);
		vars.put(var.getId(), var);
		var.setScope(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scope other = (Scope) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
