package ide.impl.compiler.assembly.impl;

import java.util.ArrayList;
import java.util.List;

public class Assembly {

    private final List<String> lines;
    private StringBuilder sb;

    public Assembly() {
        this.lines = new ArrayList<>();
        this.sb = new StringBuilder();
    }

    public void addLine(String line){
        lines.add(line);
    }

    private void add(String str) {
        sb.append(str).append("\n");
    }

    public Assembly build(){
        return this;
    }

    @Override
    public String toString() {
        lines.forEach(this::add);
        return removeLastBreak(sb);
    }

    protected List<String> getLines() {
        return lines;
    }

    private String removeLastBreak(StringBuilder text) {
        return text.toString().substring(0, text.length()-1);
    }

    public void addAssembly(Assembly assembly) {
        assembly.getLines().forEach( this::addLine );
    }
}
