package ide.impl.compiler;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"id", "scope"})
public class Var {
	private final String type, id;
	private String value;
	private boolean initialized = false;
	private boolean used = false;
	private boolean constant = false;
	private boolean param = false;
	private Scope scope;

	protected Var(String scope, String type, String id, boolean constant) {
		super();
		this.scope = Scope.instance(scope);
		this.type = type;
		this.id = id;
		this.constant = constant;
	}

	public static Var instance(String scope, String type, String id) {
		return instance(scope, type, id, false);
	}
	
	public static Var instance(String scope, String type, String id,
			boolean constant) {
		return new Var(scope, type, id, constant);
	}

	public void initialize() {
		this.initialized = true;
	}	
	
	public void use(){
		this.setUsed(true);
	}

	@Override
	public String toString() {
		return id;
	}

	public static final Var NULL = new Var("", "", "", false) {

		@Override
		public String getId() {
			return "";
		}

		@Override
		public String getValue() {
			return "";
		}

		@Override
		public boolean isInitialized() {
			return false;
		}
		
		@Override
		public boolean isParam() {
			return false;
		}
	};

	public int getParamPosition() {
		if (scope instanceof Function) {
			Function function = (Function) scope;
			return function.getParamPosition(this);
		}
		return 0;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public boolean isVector() {
		return false;
	}

}
