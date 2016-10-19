package ide.impl.compiler.assembly;

import java.util.ArrayList;
import java.util.List;

public class Assembly {

    private StringBuilder sb;
    private List<String> data;
    public Assembly() {
        sb = new StringBuilder();
        data = new ArrayList<>();
    }

    @Override
    public String toString() {
        add(".data");
        adicionaSessaoData();
        add(".text");
        return sb.toString();
    }

    private void adicionaSessaoData() {
        data.forEach(this::add);
    }

    private void add(String str) {
        sb.append(str).append("\n");
    }

    public void addData( String data ) {
        this.data.add( data );
    }

}
