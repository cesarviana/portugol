package ide.impl.compiler.assembly;


public interface ControlStrucuture {
    void build(Assembler assembler);

    void addOperand(String operand);

    void startWatching();

    void setFalseBranch(String scope);

    void stopWatching();

    String useBranch();

    void setOperator(String operator);
}
