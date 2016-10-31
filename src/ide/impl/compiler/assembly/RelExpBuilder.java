package ide.impl.compiler.assembly;


public interface RelExpBuilder {
    void build(Assembler assembler);

    void addOperand(String operand);

    void startWatching();

    void setBranchIfNotEqual(String scope);

    void stopWatching();

    String useBranchIfNotEqual();

    void setRelationalOperation(String relationalOperation);
}
