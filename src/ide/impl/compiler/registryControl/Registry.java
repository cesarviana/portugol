package ide.impl.compiler.registryControl;

public abstract class Registry {
	public static final Registry NULL = new Registry() {

		@Override
		public void setUsed(boolean b) {
		}

		@Override
		public boolean isUsed() {
			return false;
		}

		@Override
		public boolean isParameter() {
			return false;
		}

		@Override
		public boolean isInitialized() {
			return false;
		}

		@Override
		public String getType() {
			return null;
		}

		@Override
		public String getScope() {
			return "no-scope";
		}

		@Override
		public String getName() {
			return "no-name";
		}

		@Override
		public int getParameterPosition() {
			return -1;
		}

		@Override
		public boolean isVector() {
			return false;
		}

		@Override
		public boolean isFunction() {
			return false;
		}
	};

	abstract public String getName();

	abstract public String getType();

	abstract public boolean isInitialized();

	abstract public boolean isUsed();

	abstract public void setUsed(boolean b);

	abstract public String getScope();

	public boolean isParameter(){
		return false;
	}

	public int getParameterPosition(){
		return -1;
	}

	public boolean isVector(){
		return false;
	}

	public boolean isFunction(){
		return false;
	}

	@Override
	public String toString() {
		return "name=" + getName() + " |type=" + getType() + " |initialized="
				+ isInitialized() + " |used=" + isUsed() + " |scope="
				+ getScope() + " |param=" + isParameter();
	}
}
