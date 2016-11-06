package ide.impl.compiler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "id" })
public class Scope {

	public static final String GLOBAL_SCOPE = "programa";
	private String id;
	private Map<String, Var> vars;
	private Scope parent = Scope.NULL;
	private Map<String, Scope> childs;

	public Scope(String id) {
		this.id = id;
		vars = new LinkedHashMap<>();
		childs = new LinkedHashMap<>();
	}

	public static Scope instance(String id) {
		return new Scope(id);
	}

	public void addVar(Var var) {
		if (vars.containsKey(var.getId()))
			throw new CompilerException("A variável " + var + " já foi declarada no escopo " + this);
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

	public Scope addChild(String childScopeId) {
		childScopeId = changeChildScopeNameWhenRepeated(childScopeId);
		String nameParentPlusChildName = id + "->" + childScopeId;
		Scope newChildScope = Scope.instance(nameParentPlusChildName);
		newChildScope.setParent(this);
		childs.put(childScopeId, newChildScope);
		return newChildScope;
	}

	private String changeChildScopeNameWhenRepeated(String childScope) {
		if("programa".equals(this.getId()))
			return childScope;
		int ocurrency = 0;
		String ineditedChildScopeName = "";
		do {
			ineditedChildScopeName = childScope + ocurrency;
			ocurrency++;
		} while (childs.containsKey(ineditedChildScopeName));
		return ineditedChildScopeName;
	}

	public boolean isGlobalScope() {
		return id.equals(GLOBAL_SCOPE);
	}
	
	public void setParent(Scope parent){
		if(this.id.equals(GLOBAL_SCOPE))
			this.parent = Scope.NULL;
		if(this.id.equals(parent.id))
			throw new RuntimeException("O escopo pai não pode ter mesmo id que o filho. Id: " + id);
		this.parent = parent;
	}
}
