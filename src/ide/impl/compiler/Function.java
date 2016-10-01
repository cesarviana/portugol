package ide.impl.compiler;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Function {

	private String id;

	public static Function instance(String id) {
		return new Function(id);
	}

	private Function(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}

}
