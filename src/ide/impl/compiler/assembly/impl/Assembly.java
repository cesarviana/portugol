package ide.impl.compiler.assembly.impl;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Assembly {

    private final List<String> lines;
    private StringBuilder sb;

    @Getter @Setter
    private String assemblyScope;

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

    public void build(){
    }

    public void addAssembly(Assembly assembly) {
        List<String> otherAssemblyLines = assembly.getLines();
        Iterator<String> i = otherAssemblyLines.iterator();
        while (i.hasNext()){
            addLine( i.next() );
        }
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
        if(text.length() > 1)
            return text.toString().substring(0, text.length()-1);
        return "";
    }

    public String getAssemblyScope(){
        return this.assemblyScope.toUpperCase().replace("->","_");
    }

}
