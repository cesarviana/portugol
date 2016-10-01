package ide.impl.compiler;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of={"id", "scope"})
public class Var {
	private final String scope, type, id;
	private String value;
	private boolean initialized = false;
	private boolean used = false;
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

	public static Var instance(String scope, String type, String id, boolean constant) {
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
