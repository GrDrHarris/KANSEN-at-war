package script.formula;

import static java.lang.Math.*;
import org.jetbrains.annotations.Contract;
public class FormulaUtils {
    /**
     * bounds v using upper bound u and lower bound l, please make sure l <= u
     * @return if v > u, return v, else if v < l, return l, else return v
     */
    @Contract(pure=true)
    public static double clamp(double v, double l, double u)
    {
        v = max(v, l);
        return min(v, u);
    }

    /**
     * calculate a polynomial
     * @return a[0]+a[1]*x+a[2]*x*x+...
     */
    @Contract(pure=true)
    public static double polynomial(double x, double... a)
    {
        double p = 1, r = 0;
        for(int i = 0; i < a.length; i++)
        {
            r += p * a[i];
            p = p * x;
        }
        return r;
    }

}
