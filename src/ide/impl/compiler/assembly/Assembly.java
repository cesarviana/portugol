package ide.impl.compiler.assembly;

import java.util.ArrayList;
import java.util.List;

public class Assembly {

    private StringBuilder sb;
    private final List<String> data;
    private final List<String> text;
    public Assembly() {
        data = new ArrayList<>();
        text = new ArrayList<>();
    }

    @Override
    public String toString() {
        sb = new StringBuilder();
        add(".data");
        adicionaSessaoData();
        add(".text");
        addText();
        return sb.toString();
    }

    private void adicionaSessaoData() {
        data.forEach(this::add);
    }

    private void addText() {
        text.forEach(this::add);
    }

    private void add(String str) {
        sb.append(str).append("\n");
    }

    public void addData( String data ) {
        this.data.add( data );
    }

    public void addText(String text) {
        this.text.add(text);
    }
}
