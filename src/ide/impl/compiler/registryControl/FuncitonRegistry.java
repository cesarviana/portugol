package ide.impl.compiler.registryControl;

import ide.impl.compiler.Function;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "function", callSuper = false)
public class FuncitonRegistry extends Registry {

	private Function function;

	public FuncitonRegistry(Function Function) {
		setFunction(Function);
	}

	public static FuncitonRegistry instance(String id) {
		return new FuncitonRegistry(Function.instance("", id));
	}

	public static Registry instance(Function Function) {
		return new FuncitonRegistry(Function);
	}

	@Override
	public String getName() {
		return function.getId();
	}

	@Override
	public String getType() {
		return function.getType();
	}

	@Override
	public boolean isInitialized() {
		return false;
	}

	@Override
	public boolean isUsed() {
		return function.isUsed();
	}

	@Override
	public void setUsed(boolean b) {
		// TODO Indicate that the function was used 
	}

	@Override
	public String getScope() {
		return function.getParent().toString();
	}

	@Override
	public boolean isVector() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

}
