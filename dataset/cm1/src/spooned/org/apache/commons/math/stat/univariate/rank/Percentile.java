package org.apache.commons.math.stat.univariate.rank;

import org.apache.commons.math.stat.univariate.AbstractUnivariateStatistic;
import java.util.Arrays;

/** 
 * @version $Revision: 1.6 $ $Date: 2003/10/13 08:10:58 $
 */
public class Percentile extends AbstractUnivariateStatistic {
    /** 
     */
private double percentile = 0.0;

    /** 
     * Constructs a Percentile with a default percentile
     * value of 50.0.
     */
public Percentile() {
        super();
        percentile = 50.0;
    }

    /** 
     * Constructs a Percentile with the specific percentile value.
     * @param p the percentile
     */
public Percentile(final double p) {
        this.percentile = p;
    }

    /** 
     * Evaluates the double[] top the specified percentile.
     * This does not alter the interal percentile state of the
     * statistic.
     * @param values Is a double[] containing the values
     * @param p Is the percentile to evaluate to.
     * @return the result of the evaluation or Double.NaN
     * if the array is empty
     */
public double evaluate(final double[] values, final double p) {
        return evaluate(values, 0, values.length, p);
    }

    /** 
     * @see org.apache.commons.math.stat.univariate.UnivariateStatistic#evaluate(double[], int, int)
     */
public double evaluate(final double[] values, final int start, final int length) {
        return evaluate(values, start, length, percentile);
    }

    /** 
     * Evaluates the double[] top the specified percentile.
     * This does not alter the interal percentile state of the
     * statistic.
     * @param values Is a double[] containing the values
     * @param begin processing at this point in the array
     * @param length processing at this point in the array
     * @param p Is the percentile to evaluate to.*
     * @return the result of the evaluation or Double.NaN
     * if the array is empty
     */
public double evaluate(final double[] values, final int begin, final int length, final double p) {
        test(values, begin, length);
        if ((p > 100) || (p <= 0)) {
            throw new IllegalArgumentException("invalid percentile value");
        } 
        double n = ((double)(length));
        if (n == 0) {
            return Double.NaN;
        } 
        if (n == 1) {
            return values[begin];
        } 
        double pos = (p * (n + 1)) / 100;
        double fpos = java.lang.Math.floor(pos);
        int intPos = ((int)(fpos));
        double dif = pos - fpos;
        double[] sorted = new double[length];
        java.lang.System.arraycopy(values, begin, sorted, 0, length);
        java.util.Arrays.sort(sorted);
        if (pos < 1) {
            return sorted[0];
        } 
        if ((length)<=(fpos)) {
            return sorted[(length - 1)];
        } 
        double lower = sorted[(intPos - 1)];
        double upper = sorted[intPos];
        return lower + (dif * (upper - lower));
    }

    /** 
     * The default internal state of this percentile can be set.
     * This will return that value.
     * @return percentile
     */
public double getPercentile() {
        return percentile;
    }

    /** 
     * The default internal state of this percentile can be set.
     * This will setthat value.
     * @param p a value between 0 <= p <= 100
     */
public void setPercentile(final double p) {
        percentile = p;
    }
}

