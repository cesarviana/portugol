package ide.impl.compiler.assembly.impl;

import ide.impl.compiler.assembly.Assembler;

import java.util.ArrayList;
import java.util.List;

public class AssemblyPart {

    private final List<String> lines;
    private StringBuilder sb;

    public AssemblyPart() {
        this.lines = new ArrayList<>();
        this.sb = new StringBuilder();
    }

    public void addText(String line){
        lines.add(line);
    }

    private void add(String str) {
        sb.append(str).append("\n");
    }

    public AssemblyPart build(){
        return this;
    }

    @Override
    public String toString() {
        lines.forEach(this::add);
        return removeLastBreak(sb);
    }

    private String removeLastBreak(StringBuilder text) {
        return text.toString().substring(0, text.length()-1);
    }
}
