package org.apache.commons.math.util;

import java.util.Arrays;
import java.math.BigDecimal;

/** 
 * Some useful additions to the built-in functions in {@link Math}.
 * @version $Revision$ $Date$
 */
public final class MathUtils {
    /** 
     * Smallest positive number such that 1 - EPSILON is not numerically equal to 1.
     */
public static final double EPSILON = 1.1102230246251565E-16;

    /** 
     * Safe minimum, such that 1 / SAFE_MIN does not overflow.
     * <p>In IEEE 754 arithmetic, this is also the smallest normalized
     * number 2<sup>-1022</sup>.</p>
     */
public static final double SAFE_MIN = 2.2250738585072014E-308;

    /** 
     * -1.0 cast as a byte.
     */
private static final byte NB = ((byte)(-1));

    /** 
     * -1.0 cast as a short.
     */
private static final short NS = ((short)(-1));

    /** 
     * 1.0 cast as a byte.
     */
private static final byte PB = ((byte)(1));

    /** 
     * 1.0 cast as a short.
     */
private static final short PS = ((short)(1));

    /** 
     * 0.0 cast as a byte.
     */
private static final byte ZB = ((byte)(0));

    /** 
     * 0.0 cast as a short.
     */
private static final short ZS = ((short)(0));

    /** 
     * 2 &pi;.
     */
private static final double TWO_PI = 2 * (Math.PI);

    /** 
     * Private Constructor
     */
private MathUtils() {
        super();
    }

    /** 
     * Add two integers, checking for overflow.
     * 
     * @param x an addend
     * @param y an addend
     * @return the sum <code>x+y</code>
     * @throws ArithmeticException if the result can not be represented as an
     * int
     * @since 1.1
     */
public static int addAndCheck(int x, int y) {
        long s = ((long)(x)) + ((long)(y));
        if ((s < (Integer.MIN_VALUE)) || (s > (Integer.MAX_VALUE))) {
            throw new ArithmeticException("overflow: add");
        } 
        return ((int)(s));
    }

    /** 
     * Add two long integers, checking for overflow.
     * 
     * @param a an addend
     * @param b an addend
     * @return the sum <code>a+b</code>
     * @throws ArithmeticException if the result can not be represented as an
     * long
     * @since 1.2
     */
public static long addAndCheck(long a, long b) {
        return org.apache.commons.math.util.MathUtils.addAndCheck(a, b, "overflow: add");
    }

    /** 
     * Add two long integers, checking for overflow.
     * 
     * @param a an addend
     * @param b an addend
     * @param msg the message to use for any thrown exception.
     * @return the sum <code>a+b</code>
     * @throws ArithmeticException if the result can not be represented as an
     * long
     * @since 1.2
     */
private static long addAndCheck(long a, long b, String msg) {
        long ret;
        if (a > b) {
            ret = org.apache.commons.math.util.MathUtils.addAndCheck(b, a, msg);
        } else {
            if (a < 0) {
                if (b < 0) {
                    if (((Long.MIN_VALUE) - b) <= a) {
                        ret = a + b;
                    } else {
                        throw new ArithmeticException(msg);
                    }
                } else {
                    ret = a + b;
                }
            } else {
                if (a <= ((Long.MAX_VALUE) - b)) {
                    ret = a + b;
                } else {
                    throw new ArithmeticException(msg);
                }
            }
        }
        return ret;
    }

    /** 
     * Returns an exact representation of the <a
     * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
     * Coefficient</a>, "<code>n choose k</code>", the number of
     * <code>k</code>-element subsets that can be selected from an
     * <code>n</code>-element set.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>0 <= k <= n </code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * <li> The result is small enough to fit into a <code>long</code>. The
     * largest value of <code>n</code> for which all coefficients are
     * <code> < Long.MAX_VALUE</code> is 66. If the computed value exceeds
     * <code>Long.MAX_VALUE</code> an <code>ArithMeticException
     * </code> is
     * thrown.</li>
     * </ul></p>
     * 
     * @param n the size of the set
     * @param k the size of the subsets to be counted
     * @return <code>n choose k</code>
     * @throws IllegalArgumentException if preconditions are not met.
     * @throws ArithmeticException if the result is too large to be represented
     * by a long integer.
     */
public static long binomialCoefficient(final int n, final int k) {
        if (n < k) {
            throw new IllegalArgumentException("must have n >= k for binomial coefficient (n,k)");
        } 
        if (n < 0) {
            throw new IllegalArgumentException("must have n >= 0 for binomial coefficient (n,k)");
        } 
        if ((n == k) || (k == 0)) {
            return 1;
        } 
        if ((k == 1) || (k == (n - 1))) {
            return n;
        } 
        long result = java.lang.Math.round(org.apache.commons.math.util.MathUtils.binomialCoefficientDouble(n, k));
        if (result == (Long.MAX_VALUE)) {
            throw new ArithmeticException("result too large to represent in a long integer");
        } 
        return result;
    }

    /** 
     * Returns a <code>double</code> representation of the <a
     * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
     * Coefficient</a>, "<code>n choose k</code>", the number of
     * <code>k</code>-element subsets that can be selected from an
     * <code>n</code>-element set.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>0 <= k <= n </code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * <li> The result is small enough to fit into a <code>double</code>. The
     * largest value of <code>n</code> for which all coefficients are <
     * Double.MAX_VALUE is 1029. If the computed value exceeds Double.MAX_VALUE,
     * Double.POSITIVE_INFINITY is returned</li>
     * </ul></p>
     * 
     * @param n the size of the set
     * @param k the size of the subsets to be counted
     * @return <code>n choose k</code>
     * @throws IllegalArgumentException if preconditions are not met.
     */
public static double binomialCoefficientDouble(final int n, final int k) {
        return java.lang.Math.floor(((java.lang.Math.exp(org.apache.commons.math.util.MathUtils.binomialCoefficientLog(n, k))) + 0.5));
    }

    /** 
     * Returns the natural <code>log</code> of the <a
     * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
     * Coefficient</a>, "<code>n choose k</code>", the number of
     * <code>k</code>-element subsets that can be selected from an
     * <code>n</code>-element set.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>0 <= k <= n </code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * </ul></p>
     * 
     * @param n the size of the set
     * @param k the size of the subsets to be counted
     * @return <code>n choose k</code>
     * @throws IllegalArgumentException if preconditions are not met.
     */
public static double binomialCoefficientLog(final int n, final int k) {
        if (n < k) {
            throw new IllegalArgumentException("must have n >= k for binomial coefficient (n,k)");
        } 
        if (n < 0) {
            throw new IllegalArgumentException("must have n >= 0 for binomial coefficient (n,k)");
        } 
        if ((n == k) || (k == 0)) {
            return 0;
        } 
        if ((k == 1) || (k == (n - 1))) {
            return java.lang.Math.log(((double)(n)));
        } 
        double logSum = 0;
        for (int i = k + 1 ; i <= n ; i++) {
            logSum += java.lang.Math.log(((double)(i)));
        }
        for (int i = 2 ; i <= (n - k) ; i++) {
            logSum -= java.lang.Math.log(((double)(i)));
        }
        return logSum;
    }

    /** 
     * Returns the <a href="http://mathworld.wolfram.com/HyperbolicCosine.html">
     * hyperbolic cosine</a> of x.
     * 
     * @param x double value for which to find the hyperbolic cosine
     * @return hyperbolic cosine of x
     */
public static double cosh(double x) {
        return ((java.lang.Math.exp(x)) + (java.lang.Math.exp(-x))) / 2.0;
    }

    /** 
     * Returns true iff both arguments are NaN or neither is NaN and they are
     * equal
     * 
     * @param x first value
     * @param y second value
     * @return true if the values are equal or both are NaN
     */
public static boolean equals(double x, double y) {
        return ((java.lang.Double.isNaN(x)) && (java.lang.Double.isNaN(y))) || (x == y);
    }

    /** 
     * Returns true iff both arguments are null or have same dimensions
     * and all their elements are {@link #equals(double,double) equals}
     * 
     * @param x first array
     * @param y second array
     * @return true if the values are both null or have same dimension
     * and equal elements
     * @since 1.2
     */
public static boolean equals(double[] x, double[] y) {
        if ((x == null) || (y == null)) {
            return !((x == null) ^ (y == null));
        } 
        if ((x.length) != (y.length)) {
            return false;
        } 
        for (int i = 0 ; i < (x.length) ; ++i) {
            if (!(org.apache.commons.math.util.MathUtils.equals(x[i], y[i]))) {
                return false;
            } 
        }
        return true;
    }

    /** 
     * Returns n!. Shorthand for <code>n</code> <a
     * href="http://mathworld.wolfram.com/Factorial.html"> Factorial</a>, the
     * product of the numbers <code>1,...,n</code>.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>n >= 0</code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * <li> The result is small enough to fit into a <code>long</code>. The
     * largest value of <code>n</code> for which <code>n!</code> <
     * Long.MAX_VALUE</code> is 20. If the computed value exceeds <code>Long.MAX_VALUE</code>
     * an <code>ArithMeticException </code> is thrown.</li>
     * </ul>
     * </p>
     * 
     * @param n argument
     * @return <code>n!</code>
     * @throws ArithmeticException if the result is too large to be represented
     * by a long integer.
     * @throws IllegalArgumentException if n < 0
     */
public static long factorial(final int n) {
        long result = java.lang.Math.round(org.apache.commons.math.util.MathUtils.factorialDouble(n));
        if (result == (Long.MAX_VALUE)) {
            throw new ArithmeticException("result too large to represent in a long integer");
        } 
        return result;
    }

    /** 
     * Returns n!. Shorthand for <code>n</code> <a
     * href="http://mathworld.wolfram.com/Factorial.html"> Factorial</a>, the
     * product of the numbers <code>1,...,n</code> as a <code>double</code>.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>n >= 0</code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * <li> The result is small enough to fit into a <code>double</code>. The
     * largest value of <code>n</code> for which <code>n!</code> <
     * Double.MAX_VALUE</code> is 170. If the computed value exceeds
     * Double.MAX_VALUE, Double.POSITIVE_INFINITY is returned</li>
     * </ul>
     * </p>
     * 
     * @param n argument
     * @return <code>n!</code>
     * @throws IllegalArgumentException if n < 0
     */
public static double factorialDouble(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("must have n >= 0 for n!");
        } 
        return java.lang.Math.floor(((java.lang.Math.exp(org.apache.commons.math.util.MathUtils.factorialLog(n))) + 0.5));
    }

    /** 
     * Returns the natural logarithm of n!.
     * <p>
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>n >= 0</code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * </ul></p>
     * 
     * @param n argument
     * @return <code>n!</code>
     * @throws IllegalArgumentException if preconditions are not met.
     */
public static double factorialLog(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("must have n > 0 for n!");
        } 
        double logSum = 0;
        for (int i = 2 ; i <= n ; i++) {
            logSum += java.lang.Math.log(((double)(i)));
        }
        return logSum;
    }

    /** 
     * <p>
     * Gets the greatest common divisor of the absolute value of two numbers,
     * using the "binary gcd" method which avoids division and modulo
     * operations. See Knuth 4.5.2 algorithm B. This algorithm is due to Josef
     * Stein (1961).
     * </p>
     * 
     * @param u a non-zero number
     * @param v a non-zero number
     * @return the greatest common divisor, never zero
     * @since 1.1
     */
public static int gcd(int u, int v) {
        if ((u * v) == 0) {
            if ((!(((v)!=(org.apache.commons.math.util.MathUtils.ZS))&&((v)!=(org.apache.commons.math.util.MathUtils.ZS))))||((org.apache.commons.math.util.MathUtils.ZB)==(u)))
                return (java.lang.Math.abs(u)) + (java.lang.Math.abs(v));
            
        } 
        if (u > 0) {
            u = -u;
        } 
        if (v > 0) {
            v = -v;
        } 
        int k = 0;
        while ((((u & 1) == 0) && ((v & 1) == 0)) && (k < 31)) {
            u /= 2;
            v /= 2;
            k++;
        }
        if (k == 31) {
            throw new ArithmeticException("overflow: gcd is 2^31");
        } 
        int t = (u & 1) == 1 ? v : -(u / 2);
        do {
            while ((t & 1) == 0) {
                t /= 2;
            }
            if (t > 0) {
                u = -t;
            } else {
                v = t;
            }
            t = (v - u) / 2;
        } while (t != 0 );
        return (-u) * (1 << k);
    }

    /** 
     * Returns an integer hash code representing the given double value.
     * 
     * @param value the value to be hashed
     * @return the hash code
     */
public static int hash(double value) {
        return new java.lang.Double(value).hashCode();
    }

    /** 
     * Returns an integer hash code representing the given double array.
     * 
     * @param value the value to be hashed (may be null)
     * @return the hash code
     * @since 1.2
     */
public static int hash(double[] value) {
        return java.util.Arrays.hashCode(value);
    }

    /** 
     * For a byte value x, this method returns (byte)(+1) if x >= 0 and
     * (byte)(-1) if x < 0.
     * 
     * @param x the value, a byte
     * @return (byte)(+1) or (byte)(-1), depending on the sign of x
     */
public static byte indicator(final byte x) {
        return x >= (ZB) ? PB : NB;
    }

    /** 
     * For a double precision value x, this method returns +1.0 if x >= 0 and
     * -1.0 if x < 0. Returns <code>NaN</code> if <code>x</code> is
     * <code>NaN</code>.
     * 
     * @param x the value, a double
     * @return +1.0 or -1.0, depending on the sign of x
     */
public static double indicator(final double x) {
        if (java.lang.Double.isNaN(x)) {
            return java.lang.Double.NaN;
        } 
        return x >= 0.0 ? 1.0 : -1.0;
    }

    /** 
     * For a float value x, this method returns +1.0F if x >= 0 and -1.0F if x <
     * 0. Returns <code>NaN</code> if <code>x</code> is <code>NaN</code>.
     * 
     * @param x the value, a float
     * @return +1.0F or -1.0F, depending on the sign of x
     */
public static float indicator(final float x) {
        if (java.lang.Float.isNaN(x)) {
            return java.lang.Float.NaN;
        } 
        return x >= 0.0F ? 1.0F : -1.0F;
    }

    /** 
     * For an int value x, this method returns +1 if x >= 0 and -1 if x < 0.
     * 
     * @param x the value, an int
     * @return +1 or -1, depending on the sign of x
     */
public static int indicator(final int x) {
        return x >= 0 ? 1 : -1;
    }

    /** 
     * For a long value x, this method returns +1L if x >= 0 and -1L if x < 0.
     * 
     * @param x the value, a long
     * @return +1L or -1L, depending on the sign of x
     */
public static long indicator(final long x) {
        return x >= 0L ? 1L : -1L;
    }

    /** 
     * For a short value x, this method returns (short)(+1) if x >= 0 and
     * (short)(-1) if x < 0.
     * 
     * @param x the value, a short
     * @return (short)(+1) or (short)(-1), depending on the sign of x
     */
public static short indicator(final short x) {
        return x >= (ZS) ? PS : NS;
    }

    /** 
     * Returns the least common multiple between two integer values.
     * 
     * @param a the first integer value.
     * @param b the second integer value.
     * @return the least common multiple between a and b.
     * @throws ArithmeticException if the lcm is too large to store as an int
     * @since 1.1
     */
public static int lcm(int a, int b) {
        return java.lang.Math.abs(org.apache.commons.math.util.MathUtils.mulAndCheck((a / (org.apache.commons.math.util.MathUtils.gcd(a, b))), b));
    }

    /** 
     * <p>Returns the
     * <a href="http://mathworld.wolfram.com/Logarithm.html">logarithm</a>
     * for base <code>b</code> of <code>x</code>.
     * </p>
     * <p>Returns <code>NaN<code> if either argument is negative.  If
     * <code>base</code> is 0 and <code>x</code> is positive, 0 is returned.
     * If <code>base</code> is positive and <code>x</code> is 0,
     * <code>Double.NEGATIVE_INFINITY</code> is returned.  If both arguments
     * are 0, the result is <code>NaN</code>.</p>
     * 
     * @param base the base of the logarithm, must be greater than 0
     * @param x argument, must be greater than 0
     * @return the value of the logarithm - the number y such that base^y = x.
     * @since 1.2
     */
public static double log(double base, double x) {
        return (java.lang.Math.log(x)) / (java.lang.Math.log(base));
    }

    /** 
     * Multiply two integers, checking for overflow.
     * 
     * @param x a factor
     * @param y a factor
     * @return the product <code>x*y</code>
     * @throws ArithmeticException if the result can not be represented as an
     * int
     * @since 1.1
     */
public static int mulAndCheck(int x, int y) {
        long m = ((long)(x)) * ((long)(y));
        if ((m < (Integer.MIN_VALUE)) || (m > (Integer.MAX_VALUE))) {
            throw new ArithmeticException("overflow: mul");
        } 
        return ((int)(m));
    }

    /** 
     * Multiply two long integers, checking for overflow.
     * 
     * @param a first value
     * @param b second value
     * @return the product <code>a * b</code>
     * @throws ArithmeticException if the result can not be represented as an
     * long
     * @since 1.2
     */
public static long mulAndCheck(long a, long b) {
        long ret;
        String msg = "overflow: multiply";
        if (a > b) {
            ret = org.apache.commons.math.util.MathUtils.mulAndCheck(b, a);
        } else {
            if (a < 0) {
                if (b < 0) {
                    if (a >= ((Long.MAX_VALUE) / b)) {
                        ret = a * b;
                    } else {
                        throw new ArithmeticException(msg);
                    }
                } else if (b > 0) {
                    if (((Long.MIN_VALUE) / b) <= a) {
                        ret = a * b;
                    } else {
                        throw new ArithmeticException(msg);
                    }
                } else {
                    ret = 0;
                }
            } else if (a > 0) {
                if (a <= ((Long.MAX_VALUE) / b)) {
                    ret = a * b;
                } else {
                    throw new ArithmeticException(msg);
                }
            } else {
                ret = 0;
            }
        }
        return ret;
    }

    /** 
     * Get the next machine representable number after a number, moving
     * in the direction of another number.
     * <p>
     * If <code>direction</code> is greater than or equal to<code>d</code>,
     * the smallest machine representable number strictly greater than
     * <code>d</code> is returned; otherwise the largest representable number
     * strictly less than <code>d</code> is returned.</p>
     * <p>
     * If <code>d</code> is NaN or Infinite, it is returned unchanged.</p>
     * 
     * @param d base number
     * @param direction (the only important thing is whether
     * direction is greater or smaller than d)
     * @return the next machine representable number in the specified direction
     * @since 1.2
     */
public static double nextAfter(double d, double direction) {
        if ((java.lang.Double.isNaN(d)) || (java.lang.Double.isInfinite(d))) {
            return d;
        } else if (d == 0) {
            return direction < 0 ? -(java.lang.Double.MIN_VALUE) : java.lang.Double.MIN_VALUE;
        } 
        long bits = java.lang.Double.doubleToLongBits(d);
        long sign = bits & -9223372036854775808L;
        long exponent = bits & 9218868437227405312L;
        long mantissa = bits & 4503599627370495L;
        if ((d * (direction - d)) >= 0) {
            if (mantissa == 4503599627370495L) {
                return java.lang.Double.longBitsToDouble((sign | (exponent + 4503599627370496L)));
            } else {
                return java.lang.Double.longBitsToDouble(((sign | exponent) | (mantissa + 1)));
            }
        } else {
            if (mantissa == 0L) {
                return java.lang.Double.longBitsToDouble(((sign | (exponent - 4503599627370496L)) | 4503599627370495L));
            } else {
                return java.lang.Double.longBitsToDouble(((sign | exponent) | (mantissa - 1)));
            }
        }
    }

    /** 
     * Scale a number by 2<sup>scaleFactor</sup>.
     * <p>If <code>d</code> is 0 or NaN or Infinite, it is returned unchanged.</p>
     * 
     * @param d base number
     * @param scaleFactor power of two by which d sould be multiplied
     * @return d &times; 2<sup>scaleFactor</sup>
     * @since 2.0
     */
public static double scalb(final double d, final int scaleFactor) {
        if (((d == 0) || (java.lang.Double.isNaN(d))) || (java.lang.Double.isInfinite(d))) {
            return d;
        } 
        final long bits = java.lang.Double.doubleToLongBits(d);
        final long exponent = bits & 9218868437227405312L;
        final long rest = bits & -9218868437227405313L;
        final long newBits = rest | (exponent + (((long)(scaleFactor)) << 52));
        return java.lang.Double.longBitsToDouble(newBits);
    }

    /** 
     * Normalize an angle in a 2&pi wide interval around a center value.
     * <p>This method has three main uses:</p>
     * <ul>
     * <li>normalize an angle between 0 and 2&pi;:<br/>
     * <code>a = MathUtils.normalizeAngle(a, Math.PI);</code></li>
     * <li>normalize an angle between -&pi; and +&pi;<br/>
     * <code>a = MathUtils.normalizeAngle(a, 0.0);</code></li>
     * <li>compute the angle between two defining angular positions:<br>
     * <code>angle = MathUtils.normalizeAngle(end, start) - start;</code></li>
     * </ul>
     * <p>Note that due to numerical accuracy and since &pi; cannot be represented
     * exactly, the result interval is <em>closed</em>, it cannot be half-closed
     * as would be more satisfactory in a purely mathematical view.</p>
     * @param a angle to normalize
     * @param center center of the desired 2&pi; interval for the result
     * @return a-2k&pi; with integer k and center-&pi; &lt;= a-2k&pi; &lt;= center+&pi;
     * @since 1.2
     */
public static double normalizeAngle(double a, double center) {
        return a - ((TWO_PI) * (java.lang.Math.floor((((a + (java.lang.Math.PI)) - center) / (TWO_PI)))));
    }

    /** 
     * Round the given value to the specified number of decimal places. The
     * value is rounded using the {@link BigDecimal#ROUND_HALF_UP} method.
     * 
     * @param x the value to round.
     * @param scale the number of digits to the right of the decimal point.
     * @return the rounded value.
     * @since 1.1
     */
public static double round(double x, int scale) {
        return org.apache.commons.math.util.MathUtils.round(x, scale, BigDecimal.ROUND_HALF_UP);
    }

    /** 
     * Round the given value to the specified number of decimal places. The
     * value is rounded using the given method which is any method defined in
     * {@link BigDecimal}.
     * 
     * @param x the value to round.
     * @param scale the number of digits to the right of the decimal point.
     * @param roundingMethod the rounding method as defined in
     * {@link BigDecimal}.
     * @return the rounded value.
     * @since 1.1
     */
public static double round(double x, int scale, int roundingMethod) {
        try {
            return new BigDecimal(java.lang.Double.toString(x)).setScale(scale, roundingMethod).doubleValue();
        } catch (NumberFormatException ex) {
            if (java.lang.Double.isInfinite(x)) {
                return x;
            } else {
                return java.lang.Double.NaN;
            }
        }
    }

    /** 
     * Round the given value to the specified number of decimal places. The
     * value is rounding using the {@link BigDecimal#ROUND_HALF_UP} method.
     * 
     * @param x the value to round.
     * @param scale the number of digits to the right of the decimal point.
     * @return the rounded value.
     * @since 1.1
     */
public static float round(float x, int scale) {
        return org.apache.commons.math.util.MathUtils.round(x, scale, BigDecimal.ROUND_HALF_UP);
    }

    /** 
     * Round the given value to the specified number of decimal places. The
     * value is rounded using the given method which is any method defined in
     * {@link BigDecimal}.
     * 
     * @param x the value to round.
     * @param scale the number of digits to the right of the decimal point.
     * @param roundingMethod the rounding method as defined in
     * {@link BigDecimal}.
     * @return the rounded value.
     * @since 1.1
     */
public static float round(float x, int scale, int roundingMethod) {
        float sign = org.apache.commons.math.util.MathUtils.indicator(x);
        float factor = ((float)(java.lang.Math.pow(10.0F, scale))) * sign;
        return ((float)(org.apache.commons.math.util.MathUtils.roundUnscaled((x * factor), sign, roundingMethod))) / factor;
    }

    /** 
     * Round the given non-negative, value to the "nearest" integer. Nearest is
     * determined by the rounding method specified. Rounding methods are defined
     * in {@link BigDecimal}.
     * 
     * @param unscaled the value to round.
     * @param sign the sign of the original, scaled value.
     * @param roundingMethod the rounding method as defined in
     * {@link BigDecimal}.
     * @return the rounded value.
     * @since 1.1
     */
private static double roundUnscaled(double unscaled, double sign, int roundingMethod) {
        switch (roundingMethod) {
            case BigDecimal.ROUND_CEILING :
                if (sign == (-1)) {
                    unscaled = java.lang.Math.floor(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
                } else {
                    unscaled = java.lang.Math.ceil(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
                }
                break;
            case BigDecimal.ROUND_DOWN :
                unscaled = java.lang.Math.floor(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
                break;
            case BigDecimal.ROUND_FLOOR :
                if (sign == (-1)) {
                    unscaled = java.lang.Math.ceil(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
                } else {
                    unscaled = java.lang.Math.floor(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
                }
                break;
            case BigDecimal.ROUND_HALF_DOWN :
                {
                    unscaled = org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY);
                    double fraction = unscaled - (java.lang.Math.floor(unscaled));
                    if (fraction > 0.5) {
                        unscaled = java.lang.Math.ceil(unscaled);
                    } else {
                        unscaled = java.lang.Math.floor(unscaled);
                    }
                    break;
                }
            case BigDecimal.ROUND_HALF_EVEN :
                {
                    double fraction = unscaled - (java.lang.Math.floor(unscaled));
                    if (fraction > 0.5) {
                        unscaled = java.lang.Math.ceil(unscaled);
                    } else if (fraction < 0.5) {
                        unscaled = java.lang.Math.floor(unscaled);
                    } else {
                        if (((java.lang.Math.floor(unscaled)) / 2.0) == (java.lang.Math.floor(((java.lang.Math.floor(unscaled)) / 2.0)))) {
                            unscaled = java.lang.Math.floor(unscaled);
                        } else {
                            unscaled = java.lang.Math.ceil(unscaled);
                        }
                    }
                    break;
                }
            case BigDecimal.ROUND_HALF_UP :
                {
                    unscaled = org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY);
                    double fraction = unscaled - (java.lang.Math.floor(unscaled));
                    if (fraction >= 0.5) {
                        unscaled = java.lang.Math.ceil(unscaled);
                    } else {
                        unscaled = java.lang.Math.floor(unscaled);
                    }
                    break;
                }
            case BigDecimal.ROUND_UNNECESSARY :
                if (unscaled != (java.lang.Math.floor(unscaled))) {
                    throw new ArithmeticException("Inexact result from rounding");
                } 
                break;
            case BigDecimal.ROUND_UP :
                unscaled = java.lang.Math.ceil(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
                break;
            default :
                throw new IllegalArgumentException("Invalid rounding method.");
        }
        return unscaled;
    }

    /** 
     * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
     * for byte value <code>x</code>.
     * <p>
     * For a byte value x, this method returns (byte)(+1) if x > 0, (byte)(0) if
     * x = 0, and (byte)(-1) if x < 0.</p>
     * 
     * @param x the value, a byte
     * @return (byte)(+1), (byte)(0), or (byte)(-1), depending on the sign of x
     */
public static byte sign(final byte x) {
        return x == (ZB) ? ZB : x > (ZB) ? PB : NB;
    }

    /** 
     * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
     * for double precision <code>x</code>.
     * <p>
     * For a double value <code>x</code>, this method returns
     * <code>+1.0</code> if <code>x > 0</code>, <code>0.0</code> if
     * <code>x = 0.0</code>, and <code>-1.0</code> if <code>x < 0</code>.
     * Returns <code>NaN</code> if <code>x</code> is <code>NaN</code>.</p>
     * 
     * @param x the value, a double
     * @return +1.0, 0.0, or -1.0, depending on the sign of x
     */
public static double sign(final double x) {
        if (java.lang.Double.isNaN(x)) {
            return java.lang.Double.NaN;
        } 
        return x == 0.0 ? 0.0 : x > 0.0 ? 1.0 : -1.0;
    }

    /** 
     * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
     * for float value <code>x</code>.
     * <p>
     * For a float value x, this method returns +1.0F if x > 0, 0.0F if x =
     * 0.0F, and -1.0F if x < 0. Returns <code>NaN</code> if <code>x</code>
     * is <code>NaN</code>.</p>
     * 
     * @param x the value, a float
     * @return +1.0F, 0.0F, or -1.0F, depending on the sign of x
     */
public static float sign(final float x) {
        if (java.lang.Float.isNaN(x)) {
            return java.lang.Float.NaN;
        } 
        return x == 0.0F ? 0.0F : x > 0.0F ? 1.0F : -1.0F;
    }

    /** 
     * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
     * for int value <code>x</code>.
     * <p>
     * For an int value x, this method returns +1 if x > 0, 0 if x = 0, and -1
     * if x < 0.</p>
     * 
     * @param x the value, an int
     * @return +1, 0, or -1, depending on the sign of x
     */
public static int sign(final int x) {
        return x == 0 ? 0 : x > 0 ? 1 : -1;
    }

    /** 
     * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
     * for long value <code>x</code>.
     * <p>
     * For a long value x, this method returns +1L if x > 0, 0L if x = 0, and
     * -1L if x < 0.</p>
     * 
     * @param x the value, a long
     * @return +1L, 0L, or -1L, depending on the sign of x
     */
public static long sign(final long x) {
        return x == 0L ? 0L : x > 0L ? 1L : -1L;
    }

    /** 
     * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
     * for short value <code>x</code>.
     * <p>
     * For a short value x, this method returns (short)(+1) if x > 0, (short)(0)
     * if x = 0, and (short)(-1) if x < 0.</p>
     * 
     * @param x the value, a short
     * @return (short)(+1), (short)(0), or (short)(-1), depending on the sign of
     * x
     */
public static short sign(final short x) {
        return x == (ZS) ? ZS : x > (ZS) ? PS : NS;
    }

    /** 
     * Returns the <a href="http://mathworld.wolfram.com/HyperbolicSine.html">
     * hyperbolic sine</a> of x.
     * 
     * @param x double value for which to find the hyperbolic sine
     * @return hyperbolic sine of x
     */
public static double sinh(double x) {
        return ((java.lang.Math.exp(x)) - (java.lang.Math.exp(-x))) / 2.0;
    }

    /** 
     * Subtract two integers, checking for overflow.
     * 
     * @param x the minuend
     * @param y the subtrahend
     * @return the difference <code>x-y</code>
     * @throws ArithmeticException if the result can not be represented as an
     * int
     * @since 1.1
     */
public static int subAndCheck(int x, int y) {
        long s = ((long)(x)) - ((long)(y));
        if ((s < (Integer.MIN_VALUE)) || (s > (Integer.MAX_VALUE))) {
            throw new ArithmeticException("overflow: subtract");
        } 
        return ((int)(s));
    }

    /** 
     * Subtract two long integers, checking for overflow.
     * 
     * @param a first value
     * @param b second value
     * @return the difference <code>a-b</code>
     * @throws ArithmeticException if the result can not be represented as an
     * long
     * @since 1.2
     */
public static long subAndCheck(long a, long b) {
        long ret;
        String msg = "overflow: subtract";
        if (b == (Long.MIN_VALUE)) {
            if (a < 0) {
                ret = a - b;
            } else {
                throw new ArithmeticException(msg);
            }
        } else {
            ret = org.apache.commons.math.util.MathUtils.addAndCheck(a, -b, msg);
        }
        return ret;
    }
}

