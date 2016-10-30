package ide.impl.compiler.assembly;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AssemblerTest2 extends AssemblerTest {

    @Test
    public void testIf(){
        String code = "" +
                "programa {" +
                "   funcao inicio(){" +
                "       inteiro x = 4" +
                "       inteiro y = 4" +
                "       se( x == y ){" +
                "          escreva(1)" +
                "       }" +
                "   }" +
                "}";

        add(".data");
        add("programa_inicio_x : 0");
        add("programa_inicio_y : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 4");
        add("STO programa_inicio_x");
        add("LDI 4");
        add("STO programa_inicio_y");

        add("LD programa_inicio_x");    // begin compare instructions
        add("STO 1000");
        add("LD programa_inicio_y");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001"); // x minus y   // end compare instructions
        add("BNE FIM_PROGRAMA_INICIO_SE_1");
        add("LDI 1");
        add("STO $out_port");
        add("FIM_PROGRAMA_INICIO_SE_1:");
        add("HLT 0");

        generateAssemblyAndAssert(code);
    }


}
