/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math.stat.inference;

import java.io.Serializable;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.DistributionFactory;
import org.apache.commons.math.distribution.ChiSquaredDistribution;

/**
 * Implements Chi-Square test statistics defined in the {@link ChiSquareTest} interface.
 *
 * @version $Revision: 1.1 $ $Date: 2004/05/03 03:02:25 $
 */
public class ChiSquareTestImpl implements ChiSquareTest, Serializable {

    /** Serializable version identifier */
   static final long serialVersionUID = 8125110460369960493L;
    
    public ChiSquareTestImpl() {
        super();
    }

     /**
     * @param observed array of observed frequency counts
     * @param expected array of expected frequency counts
     * @return chi-square test statistic
     * @throws IllegalArgumentException if preconditions are not met
     * or length is less than 2
     */
    public double chiSquare(double[] expected, long[] observed)
        throws IllegalArgumentException {
        double sumSq = 0.0d;
        double dev = 0.0d;
        if ((expected.length < 2) || (expected.length != observed.length)) {
            throw new IllegalArgumentException("observed, expected array lengths incorrect");
        }
        if (!isPositive(expected) || !isNonNegative(observed)) {
            throw new IllegalArgumentException(
                "observed counts must be non-negative and expected counts must be postive");
        }
        for (int i = 0; i < observed.length; i++) {
            dev = ((double) observed[i] - expected[i]);
            sumSq += dev * dev / expected[i];
        }
        return sumSq;
    }

    /**
     * @param observed array of observed frequency counts
     * @param expected array of exptected frequency counts
     * @return p-value
     * @throws IllegalArgumentException if preconditions are not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double chiSquareTest(double[] expected, long[] observed)
        throws IllegalArgumentException, MathException {
        ChiSquaredDistribution chiSquaredDistribution =
            DistributionFactory.newInstance().createChiSquareDistribution((double) expected.length - 1);
        return 1 - chiSquaredDistribution.cumulativeProbability(chiSquare(expected, observed));
    }

    /**
     * @param observed array of observed frequency counts
     * @param expected array of exptected frequency counts
     * @param alpha significance level of the test
     * @return true iff null hypothesis can be rejected with confidence
     * 1 - alpha
     * @throws IllegalArgumentException if preconditions are not met
     * @throws MathException if an error occurs performing the test
     */
    public boolean chiSquareTest(double[] expected, long[] observed, double alpha)
        throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return (chiSquareTest(expected, observed) < alpha);
    }
    
    /**
     * @param observed array of observed frequency counts
     * @param expected array of expected frequency counts
     * @return chi-square test statistic
     * @throws IllegalArgumentException if preconditions are not met
     */
    public double chiSquare(long[][] counts)
    throws IllegalArgumentException {
        
        checkArray(counts);
        int nRows = counts.length;
        int nCols = counts[0].length;
        
        // compute row, column and total sums
        double[] rowSum = new double[nRows];
        double[] colSum = new double[nCols];
        double total = 0.0d;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                rowSum[row] += (double) counts[row][col];
                colSum[col] += (double) counts[row][col];
                total += (double) counts[row][col];
            }
        }
        
        // compute expected counts and chi-square
        double sumSq = 0.0d;
        double expected = 0.0d;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                expected = (rowSum[row] * colSum[col]) / total;
                sumSq += (((double) counts[row][col] - expected) * ((double) counts[row][col] - expected))
                	/ expected; 
            }
        } 
        return sumSq;
    }

    /**
     * @param observed array of observed frequency counts
     * @param expected array of exptected frequency counts
     * @return p-value
     * @throws IllegalArgumentException if preconditions are not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double chiSquareTest(long[][] counts)
    throws IllegalArgumentException, MathException {
        checkArray(counts);
        double df = ((double) counts.length -1) * ((double) counts[0].length - 1);
        ChiSquaredDistribution chiSquaredDistribution =
            DistributionFactory.newInstance().createChiSquareDistribution(df);
        return 1 - chiSquaredDistribution.cumulativeProbability(chiSquare(counts));
    }

    /**
     * @param observed array of observed frequency counts
     * @param expected array of exptected frequency counts
     * @param alpha significance level of the test
     * @return true iff null hypothesis can be rejected with confidence
     * 1 - alpha
     * @throws IllegalArgumentException if preconditions are not met
     * @throws MathException if an error occurs performing the test
     */
    public boolean chiSquareTest(long[][] counts, double alpha)
    throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return (chiSquareTest(counts) < alpha);
    }
    
    /**
     * Checks to make sure that the input long[][] array is rectangular,
     * has at least 2 rows and 2 columns, and has all non-negative entries,
     * throwing IllegalArgumentException if any of these checks fail.
     * 
     * @param in input 2-way table to check
     * @throws IllegalArgumentException
     */
    private void checkArray(long[][] in) throws IllegalArgumentException {
        
        if (in.length < 2) {
            throw new IllegalArgumentException("Input table must have at least two rows");
        }
        
        if (in[0].length < 2) {
            throw new IllegalArgumentException("Input table must have at least two columns");
        }    
        
        if (!isRectangular(in)) {
            throw new IllegalArgumentException("Input table must be rectangular");
        }
        
        if (!isNonNegative(in)) {
            throw new IllegalArgumentException("All entries in input 2-way table must be non-negative");
        }
        
    }
    
    //---------------------  Private array methods -- should find a utility home for these
    
    /**
     * Returns true iff input array is rectangular.
     * Throws NullPointerException if input array is null
     * Throws ArrayIndexOutOfBoundsException if input array is empty
     * 
     * @param in array to be tested
     * @return true if the array is rectangular
     */
    private boolean isRectangular(long[][] in) {
        for (int i = 1; i < in.length; i++) {
            if (in[i].length != in[0].length) {
                return false;
            }
        }  
        return true;
    }
    
    /**
     * Returns true iff all entries of the input array are > 0.
     * Throws NullPointerException if input array is null.
     * Returns true if the array is non-null, but empty
     * 
     * @param in array to be tested
     * @return true if all entries of the array are positive
     */
    private boolean isPositive(double[] in) {
        for (int i = 0; i < in.length; i ++) {
            if (in[i] <= 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns true iff all entries of the input array are >= 0.
     * Throws NullPointerException if input array is null.
     * Returns true if the array is non-null, but empty
     * 
     * @param in array to be tested
     * @return true if all entries of the array are non-negative
     */
    private boolean isNonNegative(double[] in) {
        for (int i = 0; i < in.length; i ++) {
            if (in[i] < 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns true iff all entries of the input array are > 0.
     * Throws NullPointerException if input array is null.
     * Returns true if the array is non-null, but empty
     * 
     * @param in array to be tested
     * @return true if all entries of the array are positive
     */
    private boolean isPositive(long[] in) {
        for (int i = 0; i < in.length; i ++) {
            if (in[i] <= 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns true iff all entries of the input array are >= 0.
     * Throws NullPointerException if input array is null.
     * Returns true if the array is non-null, but empty
     * 
     * @param in array to be tested
     * @return true if all entries of the array are non-negative
     */
    private boolean isNonNegative(long[] in) {
        for (int i = 0; i < in.length; i ++) {
            if (in[i] < 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns true iff all entries of (all subarrays of) the input array are > 0.
     * Throws NullPointerException if input array is null.
     * Returns true if the array is non-null, but empty
     * 
     * @param in array to be tested
     * @return true if all entries of the array are positive
     */
    private boolean isPositive(long[][] in) {
        for (int i = 0; i < in.length; i ++) {
            for (int j = 0; j < in[i].length; j++) {
                if (in[i][j] <= 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Returns true iff all entries of (all subarrays of) the input array are >= 0.
     * Throws NullPointerException if input array is null.
     * Returns true if the array is non-null, but empty
     * 
     * @param in array to be tested
     * @return true if all entries of the array are non-negative
     */
    private boolean isNonNegative(long[][] in) {
        for (int i = 0; i < in.length; i ++) {
            for (int j = 0; j < in[i].length; j++) {
                if (in[i][j] <= 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
}
