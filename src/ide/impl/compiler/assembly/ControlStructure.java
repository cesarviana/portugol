package ide.impl.compiler.assembly;

import ide.impl.compiler.assembly.impl.AssemblyPart;

public abstract class ControlStructure extends AssemblyPart {
    protected String renameScopeToBranch(String scope) {
        return scope.toUpperCase().replace("->", "_");
    }
}
