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
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");

        add("BNE INICIO_FUNCAOTESTE_SENAO0");

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
    public void testSenaoSeAninhado(){
        String code = "" +
                "programa {" +
                "   funcao funcaoteste(){" +
                "       inteiro x = 5" +
                "       inteiro y = 5" +
                "       se( x == y ){" +
                "           escreva(1)" +
                "       } senao {" +
                "           se(10 < 2) {" +
                "               escreva(3)" +
                "           }" +
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
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");

        add("BNE INICIO_FUNCAOTESTE_SENAO0");

        add("LDI 1");
        add("STO $out_port");
        add("JMP FIM_FUNCAOTESTE_SE0");

        add("INICIO_FUNCAOTESTE_SENAO0:");
            add("LDI 10");
            add("STO 1000");
            add("LDI 2");
            add("STO 1001");
            add("LD 1000");
            add("SUB 1001");
            add("BGE FIM_FUNCAOTESTE_SENAO0_SE0");
                add("LDI 3");
                add("STO $out_port");
            add("FIM_FUNCAOTESTE_SENAO0_SE0:");
        add("FIM_FUNCAOTESTE_SE0:");

        add("HLT 0");

        generateAssemblyAndAssert(code);
    }

    @Test
    public void testSenaoSeAninhadoSenaoAninhado(){
        String code = "" +
                "programa {" +
                "   funcao funcaoteste(){" +
                "       inteiro x = 5" +
                "       inteiro y = 5" +
                "       se( x == y ){" +
                "           escreva(1)" +
                "       } senao {" +
                "           inteiro a = 2 " +
                "           inteiro b = 2 " +
                "           se(a < b) {" +
                "               escreva(3) " +
                "           } senao {" +
                "               escreva(4) " +
                "           }" +
                "       }" +
                "   }" +
                "}";
        add(".data");
        add("funcaoteste_x : 0");
        add("funcaoteste_y : 0");
        add("funcaoteste_senao0_a : 0");
        add("funcaoteste_senao0_b : 0");
        add(".text");
        add("_FUNCAOTESTE:");
        add("LDI 5");
        add("STO funcaoteste_x");
        add("LDI 5");
        add("STO funcaoteste_y");

        add("LD funcaoteste_x");
        add("STO 1000");
        add("LD funcaoteste_y");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");

        add("BNE INICIO_FUNCAOTESTE_SENAO0");

        add("LDI 1");
        add("STO $out_port");
        add("JMP FIM_FUNCAOTESTE_SE0");

        add("INICIO_FUNCAOTESTE_SENAO0:");
        add("LDI 2");
        add("STO funcaoteste_senao0_a");
        add("LDI 2");
        add("STO funcaoteste_senao0_b");
        add("LD funcaoteste_senao0_a");
        add("STO 1000");
        add("LDI funcaoteste_senao0_b");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BGE INICIO_FUNCAOTESTE_SENAO0_SENAO0");
        add("LDI 3");
        add("STO $out_port");
        add("INICIO_FUNCAOTESTE_SENAO0_SENAO0:");
        add("LDI 4");
        add("STO $out_port");
        add("FIM_FUNCAOTESTE_SENAO0_SE0:");
        add("FIM_FUNCAOTESTE_SE0:");

        add("HLT 0");

        generateAssemblyAndAssert(code);
    }

    @Test
    public void testEnquanto(){
        String code = ""+
                "programa {"+
                "   funcao teste(){" +
                "       inteiro a = 0" +
                "       enquanto(a < 10){" +
                "           escreva(a)" +
                "       }" +
                "   }" +
                "}";

        add(".data");
        add("teste_a : 0");
        add(".text");

        add("_TESTE:");
        add("LDI 0");
        add("STO teste_a");

        add("INICIO_TESTE_ENQUANTO0:");
        add("LD teste_a");
        add("STO 1000");
        add("LDI 10");
        add("STO 1001");

        add("LD 1000");
        add("SUB 1001");
        add("BGE FIM_TESTE_ENQUANTO0");

        add("LD teste_a");
        add("STO $out_port");

        add("JMP INICIO_TESTE_ENQUANTO0");
        add("FIM_TESTE_ENQUANTO0:");

        add("HLT 0");

        generateAssemblyAndAssert(code);

    }

    @Test
    public void testFacaEnquanto(){
        String code = ""+
                "programa {"+
                "   funcao teste(){" +
                "       inteiro a = 0" +
                "       faca{" +
                "           escreva(5)" +
                "       }enquanto(a < 10)" +
                "   }" +
                "}";

        add(".data");
        add("teste_a : 0");
        add(".text");

        add("_TESTE:");
        add("LDI 0");
        add("STO teste_a");

        add("INICIO_TESTE_FACA0:");

        add("LDI 5");
        add("STO $out_port");

        add("LD teste_a");
        add("STO 1000");
        add("LDI 10");
        add("STO 1001");

        add("LD 1000");
        add("SUB 1001");
        add("BLT INICIO_TESTE_FACA0");

        add("HLT 0");

        generateAssemblyAndAssert(code);

    }

    @Test
    public void testEnquanto2(){
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
        add("teste_x : 0");
        add(".text");
        add("_TESTE:");
        add("LDI 5");
        add("STO teste_x");
        add("INICIO_TESTE_ENQUANTO0:");
        add("LD teste_x");
        add("STO 1000");
        add("LDI 5");
        add("STO 1001");
        add("LD 1000");
        add("SUB 1001");
        add("BGE FIM_TESTE_ENQUANTO0");
        add("LD teste_x");
        add("ADDI 1");
        add("STO teste_x");
        add("JMP INICIO_TESTE_ENQUANTO0");
        add("FIM_TESTE_ENQUANTO0:");
        add("HLT 0");

        generateAssemblyAndAssert(code);
    }

    @Test
    public void testChamadaFuncao(){
        String code = ""+
                "programa {"+
                "   funcao dobro( inteiro valor ){" +
                "       retorne valor + valor " +
                "   }" +
                "" +
                "   funcao inicio(){" +
                "       inteiro a = 2 " +
                "       escreva( dobro( a ) )" +
                "   }" +
                "}";

        add(".data");
        add("dobro_valor : 0");
        add("inicio_a : 0");
        add(".text");
        add("JMP _PRINCIPAL");
        add("_DOBRO:");
        add("LD dobro_valor");
        add("ADD dobro_valor");
        add("RETURN 0");

        add("_PRINCIPAL:");

        add("LD inicio_a");

        add("STO dobro_valor");

        add("CALL _DOBRO");

        add("STO $out_port");
        add("HLT 0");

        generateAssemblyAndAssert(code);
    }

}

