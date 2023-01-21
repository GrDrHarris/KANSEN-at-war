package script.formula;

import static java.lang.Math.*;
public class FormulaUtils {
    public static double clamp(double v, double l, double u)
    {
        v = max(v, l);
        return min(v, u);
    }
}
