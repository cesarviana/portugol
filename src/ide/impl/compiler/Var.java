package ide.impl.compiler;

import lombok.Data;

@Data
public class Var //implements SimbolTableRegistry 
{
	private final String scope, type, id;
	private String value;
	private boolean initialized = false;
	private boolean constant = false;

	private Var(String scope, String type, String id, boolean constant) {
		super();
		this.scope = scope;
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
	};

}
