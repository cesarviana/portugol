package ide.impl.compiler.assembly;

public class Assembly {

    private StringBuilder sb;

    public Assembly() {
        sb = new StringBuilder();
    }

    @Override
    public String toString() {
        add(".data");
        add(".text");
        add("_PRINCIPAL");
        add("HLT");
        return sb.toString();
    }

    private void add(String str) {
        sb.append(str).append("\n");
    }
}
