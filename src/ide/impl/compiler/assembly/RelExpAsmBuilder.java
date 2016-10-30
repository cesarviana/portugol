package ide.impl.compiler.assembly;


public interface RelExpAsmBuilder {
    String build();

    void addOperand(String operand);

    void setRelationalOperator(String relationalOperator);

    void startWatching();

    void setBranchIfNotEqual(String scope);

    void stopWatching();

    String useBranchIfNotEqual();
}
