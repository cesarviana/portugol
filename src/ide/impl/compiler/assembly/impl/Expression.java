package ide.impl.compiler.assembly.impl;

import lombok.Getter;

public class Expression extends Assembly {

    private String operator = "";
    private String leftOperand, rightOperand;
    @Getter
    private boolean closed;

    private Expression(){
    }

    public String getBranchCommand() {
        switch (operator){
            case "!=":
                return "BNE";
            case "==":
                return "BEQ";
            case ">":
                return "BGT";
            case ">=":
                return "BGE";
            case "<":
                return "BLT";
            case "<=":
                return "BLE";
        }
        return "";
    }

    public String getInvertedBranchCommand() {
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
        if(closed)
            return;
        if (leftOperand == null)
            leftOperand = operand;
        else if(rightOperand == null)
            rightOperand = operand;
    }

    public static Expression instance() {
        return new Expression();
    }

    @Override
    public void build() {
        addLine( GeneralAssembler.createCommand("LD", leftOperand) );
        addLine( "STO 1000" );
        addLine( GeneralAssembler.createCommand("LD", rightOperand) );
        addLine( "STO 1001" );
        addLine( "LD 1000" );
        addLine( "SUB 1001" );
    }

    public void close() {
        this.closed = true;
    }

    public void open() {
        this.closed = false;
    }

}
