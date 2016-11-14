package ide.impl.compiler.assembly;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ide.impl.compiler.Compiler;
import ide.impl.compiler.SimbolTable;
import util.TestUtil;

@SuppressWarnings("ALL")
public class AssemblerControlStructuresTest extends AssemblerTest {

    @Test
    public void testSe(){

        String code = ""+
                "programa"+
                "{"+
                "	funcao inicio()"+
                "	{"+
                "		inteiro x = 4"+
                "		inteiro y = 4"+
                "		se(x == y){"+
                "			escreva(1)"+
                "		}" +
                "	}"+
                "}";

        add(".data");
        add("inicio_x : 0");
        add("inicio_y : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 4");
        add("STO inicio_x");
        add("LDI 4");
        add("STO inicio_y");

        add("LD inicio_x");
        add("STO 1000");
        add("LD inicio_y");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BNE FIM_INICIO_SE0");

        add("LDI 1");
        add("STO $out_port");

        add("FIM_INICIO_SE0:");
        add("HLT 0");

        generateAssemblyAndAssert(code);

    }

    @Test
    public void testSeComparandoNumeros(){

        String code = ""+
                "programa"+
                "{"+
                "	funcao inicio()"+
                "	{"+
                "		se(4 == 4){"+
                "			escreva(1)"+
                "		}"+
                "	}"+
                "}";

        add(".data");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 4");
        add("STO 1000");
        add("LDI 4");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BNE FIM_INICIO_SE0");

        add("LDI 1");
        add("STO $out_port");

        add("FIM_INICIO_SE0:");
        add("HLT 0");

        generateAssemblyAndAssert(code);

    }

    @Test
    public void testSeComparandoNumeroEVar(){

        String code = ""+
                "programa"+
                "{"+
                "	funcao inicio()"+
                "	{"+
                "       inteiro a = 4"+
                "		se(a == 4){"+
                "			escreva(1)"+
                "		}"+
                "	}"+
                "}";

        add(".data");
        add("inicio_a : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 4");
        add("STO inicio_a");
        add("LD inicio_a");
        add("STO 1000");
        add("LDI 4");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BNE FIM_INICIO_SE0");

        add("LDI 1");
        add("STO $out_port");

        add("FIM_INICIO_SE0:");
        add("HLT 0");

        generateAssemblyAndAssert(code);

    }

    @Test
    public void testSeAninhado(){
        String code = ""+
                "programa" +
                "{" +
                "   funcao inicio() {" +
                "       se(4 == 4) {" +
                "           inteiro a = 2" +
                "           se(5 == 5) {" +
                "               escreva(3)" +
                "           }" +
                "       }" +
                "   }" +
                "}";

        add(".data");
        add("inicio_se0_a : 0");
        add(".text");
        add("_PRINCIPAL:");

        add("LDI 4");
        add("STO 1000");
        add("LDI 4");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");

        add("BNE FIM_INICIO_SE0");

        add("LDI 2");
        add("STO inicio_se0_a");

        add("LDI 5");
        add("STO 1000");
        add("LDI 5");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");

        add("BNE FIM_INICIO_SE0_SE0");

        add("LDI 3");
        add("STO $out_port");

        add("FIM_INICIO_SE0_SE0:");
        add("FIM_INICIO_SE0:");

        add("HLT 0");

        generateAssemblyAndAssert(code);
    }

    @Test
    public void testDiferente(){
        String code = "" +
                "programa {" +
                "   funcao inicio(){" +
                "       inteiro x = 5 " +
                "       inteiro y = 5 " +
                "       se( x != y ){ " +
                "           escreva(1) " +
                "       }" +
                "   }" +
                "}";
        add(".data");
        add("inicio_x : 0");
        add("inicio_y : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO inicio_x");
        add("LDI 5");
        add("STO inicio_y");

        add("LD inicio_x");
        add("STO 1000");
        add("LD inicio_y");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BEQ FIM_INICIO_SE0");  // se igual, então falso, vai pro fim
        add("LDI 1");               // escreva (entra se verdadeiro)
        add("STO $out_port");
        add("FIM_INICIO_SE0:");
        add("HLT 0");
        generateAssemblyAndAssert(code);
    }

    @Test
    public void testSenao(){
        String code = "" +
                "programa {" +
                "   funcao funcaoteste(){" +
                "       inteiro x = 5" +
                "       inteiro y = 5" +
                "       se( x == y ){" +
                "           escreva(1)" +
                "       } senao {" +
                "           escreva(2)" +
                "       }" +
                "   }" +
                "}";
        add(".data");
        add("funcaoteste_x : 0");
        add("funcaoteste_y : 0");
        add(".text");
        add("_FUNCAOTESTE:");
        add("LDI 5");
        add("STO funcaoteste_x");
        add("LDI 5");
        add("STO funcaoteste_y");

        add("LD funcaoteste_x");
        add("STO 1000");
        add("LD funcaoteste_y");
        add("STO 1001");                   // ---|
        add("LD 1000");                    //    |   1ª parte
        add("SUB 1001");                   //    |

        add("BNE INICIO_FUNCAOTESTE_SENAO0");   // ---|

        add("LDI 1");
        add("STO $out_port");
        add("JMP FIM_FUNCAOTESTE_SE0");

        add("INICIO_FUNCAOTESTE_SENAO0:");
        add("LDI 2");
        add("STO $out_port");

        add("FIM_FUNCAOTESTE_SE0:");

        add("HLT 0");

        generateAssemblyAndAssert(code);
    }


    @Test
    public void testEnquanto(){
        String code = "" +
                "programa {" +
                "   funcao teste(){" +
                "       inteiro x = 5" +
                "       enquanto( x < 5 ){" +
                "           x = x + 1" +
                "       }" +
                "   }" +
                "}";
        add(".data");
        add("x : 0");
        add(".text");
        add("_PRINCIPAL:");
        add("LDI 5");
        add("STO x");
        add("INICIO_TESTE_ENQUANTO:");
        add("LD x");
        add("STO 1000");
        add("LDI 5");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BGE FIMFACA1");
        add("LD x");
        add("ADDI 1");
        add("JMP INICIO_TESTE_ENQUANTO");
        add("FIMFACA1");
        add("HLT 0");

        generateAssemblyAndAssert(code);
    }


}

