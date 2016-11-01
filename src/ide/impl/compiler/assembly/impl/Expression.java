package ide.impl.compiler.assembly.impl;

public class Expression extends AssemblyPart {

    private String operator = "";
    private String leftOperand, rightOperand;

    public String getBranchCommand() {
        switch (operator){
            case "==":
                return "BNE";
            case "!=":
                return "BEQ";
            case "<=":
                return "BGT";
            case "<":
                return "BGE";
            case ">=":
                return "BLT";
            case ">":
                return "BLE";
        }
        return "";
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void addOperand(String operand) {
        if (leftOperand == null)
            leftOperand = operand;
        else if(rightOperand == null)
            rightOperand = operand;
    }
}
