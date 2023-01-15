package script.formula;

import com.sun.istack.internal.NotNull;
import net.openhft.compiler.CompilerUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * maintains all the formulas in the server, only one instance is going to be created
 */
public class FormulaHolder {
    private Map<String, Formula> formulas;
    private int formulaID;
    public FormulaHolder()
    {
        formulas = new HashMap<>();
        formulaID = 0;
    }

    /**
     * @param initArgs code for converting arg[] into a more coder friendly way, in order to prevent bugs, different names
     *                 for different properties are advised, in case of using the same names, please make sure the same
     *                 name are at the same place, also, such code should end with '/n'
     * @param formula code for actual calculation, loaded form scripts, <strong>no security check is done</strong>, you
     *                should make sure the scripts you load is safe yourself, it doesn't need to end with ';', it should
     *                have a value of double
     * @return an instance of Formula can do the calculation, <strong>different instances may use the same formula</strong>,
     *         therefore please make sure what you write in the script is a <strong>pure function</strong>
     */
    public Formula compileFormula(@NotNull String initArgs, @NotNull String formula) throws ClassNotFoundException
    {
        String actual = formula.replaceAll("\\s*","");
        if(formulas.containsKey(actual))
            return formulas.get(actual);
        StringBuilder builder = new StringBuilder("package formulas;\nimport script.formula.Formula;\nimport static java.lang.Math.*;\npublic class FormulaInstance");
        builder.append(formulaID);
        String className = "formulas.FormulaInstance" + formulaID;
        formulaID++;
        builder.append(" implements Formula\n{\npublic double calc(double... arg)\n{\n");
        builder.append(initArgs);
        builder.append("return ");
        builder.append(formula);
        builder.append(";\n}\n}");
        Formula result = null;
        try{
            result = (Formula) CompilerUtils.CACHED_COMPILER.loadFromJava(className, builder.toString()).newInstance();
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
        formulas.put(actual, result);
        return result;
    }
}
