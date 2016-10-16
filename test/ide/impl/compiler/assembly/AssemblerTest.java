package ide.impl.compiler.assembly;

import ide.impl.compiler.Scope;
import ide.impl.compiler.SimbolTable;
import ide.impl.compiler.Var;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ALL")
public class AssemblerTest {

    private Assembler assembler;
    private SimbolTable simbolTable;
    private StringBuilder sb;

    @Before
    public void setUp() {
        assembler = Assembler.instance();
        simbolTable = SimbolTable.instance();
        assembler.setSimbolTable(simbolTable);
        sb = new StringBuilder();
    }

    @Test
    public void testEmptyTable() {
        Assembly assembly = assembler.assembly();
        add(".data");
        add(".text");
        add("_PRINCIPAL");
        add("HLT");
        assemblyOk(assembly);
    }

    @Test
    public void testOneInt() {
        Scope programa = Scope.instance("programa");
        programa.addVar(Var.instance("programa", "inteiro", "id"));
        simbolTable.addScope(programa);

        assembler.setSimbolTable(simbolTable);
        Assembly assembly = assembler.assembly();

        add(".data");
        add("programa_a : 0");
        add(".text");
        add("_PRINCIPAL");
        add("HLT");
        assemblyOk(assembly);
    }

    private void assemblyOk(Assembly assembly) {
        assertEquals(sb.toString(), assembly.toString());
    }

    private StringBuilder add(String s) {
        return sb.append(s).append("\n");
    }

}