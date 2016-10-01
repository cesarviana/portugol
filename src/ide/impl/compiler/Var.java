package ide.impl.compiler;

public class Var {

	private final String scopeStr, type, id;
	private String value;
	private boolean initilized = false;
	private boolean constant = false;
	private boolean param = false;
	private Scope scope;

	private Var(String scope, String type, String id, boolean constant) {
		super();
		this.scopeStr = scope;
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

	public String getScope() {
		return scopeStr;
	}

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isConstant() {
		return constant;
	}

	public void setParam(boolean param) {
		this.param = param;
	}
	
	public boolean isParam() {
		return param;
	}
	
	public void initialize() {
		this.initilized = true;
	}
	
	public boolean isInitialized() {
		return initilized;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((scopeStr == null) ? 0 : scopeStr.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Var other = (Var) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (scopeStr == null) {
			if (other.scopeStr != null)
				return false;
		} else if (!scopeStr.equals(other.scopeStr))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id;
	}

	public static final Var NULL = new Var("", "", "",false) {
		@Override
		public String getScope() {
			return "";
		}

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

}
