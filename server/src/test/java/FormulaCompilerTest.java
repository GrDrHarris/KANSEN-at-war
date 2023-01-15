import script.formula.Formula;
import script.formula.FormulaHolder;

public class FormulaCompilerTest {
    public static void main(String[] args) throws Exception
    {
        FormulaHolder holder = new FormulaHolder();
        String genx = "double x = arg[0];\n";
        String squarex = "ceil(x*x)";
        Formula f = holder.compileFormula(genx, squarex);
        System.out.println(f.calc(10));
        System.out.println(f.calc(1.2));
    }
}
