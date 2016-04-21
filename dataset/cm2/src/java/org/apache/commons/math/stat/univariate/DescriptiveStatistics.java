/*
 * Copyright 2003-2004 The Apache Software Foundation.
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
package org.apache.commons.math.stat.univariate;

import java.io.Serializable;

import org.apache.commons.discovery.tools.DiscoverClass;


/**
 * Abstract factory class for univariate statistical summaries.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/05/03 14:32:25 $
 */
public abstract class DescriptiveStatistics implements Serializable, StatisticalSummary {

	/**
	 * Create an instance of a <code>DescriptiveStatistics</code>
     * @param cls the type of <code>DescriptiveStatistics</code> object to
     *        create. 
	 * @return a new factory. 
     * @exception InstantiationException is thrown if the object can not be
     *            created.
     * @exception IllegalAccessException is thrown if the type's default
     *            constructor is not accessible.
     * @exception ClassNotFoundException if the named
     *            <code>DescriptiveStatistics</code> type can not be found.
	 */
	public static DescriptiveStatistics newInstance(String cls) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return newInstance(Class.forName(cls));
	}
    
	/**
	 * Create an instance of a <code>DescriptiveStatistics</code>
     * @param cls the type of <code>DescriptiveStatistics</code> object to
     *        create. 
	 * @return a new factory. 
     * @exception InstantiationException is thrown if the object can not be
     *            created.
     * @exception IllegalAccessException is thrown if the type's default
     *            constructor is not accessible.
	 */
	public static DescriptiveStatistics newInstance(Class cls) throws InstantiationException, IllegalAccessException {
		return (DescriptiveStatistics)cls.newInstance();
	}
	
	/**
	 * Create an instance of a <code>DescriptiveStatistics</code>
	 * @return a new factory. 
	 */
	public static DescriptiveStatistics newInstance() {
		DescriptiveStatistics factory = null;
		try {
			DiscoverClass dc = new DiscoverClass();
			factory = (DescriptiveStatistics) dc.newInstance(
				DescriptiveStatistics.class,
				"org.apache.commons.math.stat.univariate.DescriptiveStatisticsImpl");
		} catch(Exception ex) {
            ex.printStackTrace();
			// ignore as default implementation will be used.
		}
		return factory;
	}
	
	/**
	 * This constant signals that a Univariate implementation
	 * takes into account the contributions of an infinite number of
	 * elements.  In other words, if getWindow returns this
	 * constant, there is, in effect, no "window".
	 */
	public static final int INFINITE_WINDOW = -1;

	/**
	 * A LEPTOKURTIC set has a positive kurtosis (a high peak) 
	 */
	public static int LEPTOKURTIC = 1;
	/**
	 * A MESOKURTIC set has a kurtosis of 0 - it is a normal distribution
	 */
	public static int MESOKURTIC = 0;
	/**
	 * A PLATYKURTIC set has a negative kurtosis (a flat "peak")
	 */
	public static int PLATYKURTIC = -1;

	/**
	 * Adds the value to the set of numbers
	 * @param v the value to be added 
	 */
	public abstract void addValue(double v);

	/** 
	 * Returns the <a href="http://www.xycoon.com/arithmetic_mean.htm">
	 * arithmetic mean </a> of the available values 
	 * @return The mean or Double.NaN if no values have been added.
	 */
	public abstract double getMean();

	/** 
	 * Returns the <a href="http://www.xycoon.com/geometric_mean.htm">
	 * geometric mean </a> of the available values
	 * @return The geometricMean, Double.NaN if no values have been added, 
	 * or if the productof the available values is less than or equal to 0.
	 */
	public abstract double getGeometricMean();

	/** 
	 * Returns the variance of the available values.
	 * @return The variance, Double.NaN if no values have been added 
	 * or 0.0 for a single value set.  
	 */
	public abstract double getVariance();

	/** 
	 * Returns the standard deviation of the available values.
	 * @return The standard deviation, Double.NaN if no values have been added 
	 * or 0.0 for a single value set. 
	 */
	public abstract double getStandardDeviation();

	/**
	 * Returns the skewness of the available values. Skewness is a 
	 * measure of the assymetry of a given distribution.
	 * @return The skewness, Double.NaN if no values have been added 
	 * or 0.0 for a value set &lt;=2. 
	 */
	public abstract double getSkewness();

	/**
	 * Returns the Kurtosis of the available values. Kurtosis is a 
	 * measure of the "peakedness" of a distribution
	 * @return The kurtosis, Double.NaN if no values have been added, or 0.0 
	 * for a value set &lt;=3. 
	 */
	public abstract double getKurtosis();

	/**
	 * Returns the Kurtosis "classification" a distribution can be 
	 * leptokurtic (high peak), platykurtic (flat peak), 
	 * or mesokurtic (zero kurtosis).  
	 * 
	 * @return A static constant defined in this interface, 
	 *         StoredDeviation.LEPTOKURITC, StoredDeviation.PLATYKURTIC, or 
	 *         StoredDeviation.MESOKURTIC
	 */
	public abstract int getKurtosisClass();
    
	/** 
	 * Returns the maximum of the available values
	 * @return The max or Double.NaN if no values have been added.
	 */
	public abstract double getMax();

	/** 
	* Returns the minimum of the available values
	* @return The min or Double.NaN if no values have been added.
	*/
	public abstract double getMin();

	/** 
	 * Returns the number of available values
	 * @return The number of available values
	 */
	public abstract long getN();

	/**
	 * Returns the sum of the values that have been added to Univariate.
	 * @return The sum or Double.NaN if no values have been added
	 */
	public abstract double getSum();

	/**
	 * Returns the sum of the squares of the available values.
	 * @return The sum of the squares or Double.NaN if no 
	 * values have been added.
	 */
	public abstract double getSumsq();

	/** 
	 * Resets all statistics and storage
	 */
	public abstract void clear();

	/**
	 * Univariate has the ability to return only measures for the
	 * last N elements added to the set of values.
	 * @return The current window size or -1 if its Infinite.
	 */

	public abstract int getWindowSize();

	/**
	 * WindowSize controls the number of values which contribute 
	 * to the values returned by Univariate.  For example, if 
	 * windowSize is set to 3 and the values {1,2,3,4,5} 
	 * have been added <strong> in that order</strong> 
	 * then the <i>available values</i> are {3,4,5} and all
	 * reported statistics will be based on these values
	 * @param windowSize sets the size of the window.
	 */
	public abstract void setWindowSize(int windowSize);
	
    /**
     * Returns the current set of values in an array of double primitives.  
     * The order of addition is preserved.  The returned array is a fresh
     * copy of the underlying data -- i.e., it is not a reference to the
     * stored data.
     * 
     * @return returns the current set of numbers in the order in which they 
     *         were added to this set
     */
	public abstract double[] getValues();

    /**
     * Returns the current set of values in an array of double primitives,  
     * sorted in ascending order.  The returned array is a fresh
     * copy of the underlying data -- i.e., it is not a reference to the
     * stored data.
     * @return returns the current set of 
     * numbers sorted in ascending order        
     */
	public abstract double[] getSortedValues();

    /**
     * Returns the element at the specified index
     * @param index The Index of the element
     * @return return the element at the specified index
     */
	public abstract double getElement(int index);

    /**
     * Returns an estimate for the pth percentile of the stored values. 
     * <p>
     * The implementation provided here follows the first estimation procedure presented
     * <a href="http://www.itl.nist.gov/div898/handbook/prc/section2/prc252.htm">here.</a>
     * <p>
     * <strong>Preconditions</strong>:<ul>
     * <li><code>0 &lt; p &lt; 100</code> (otherwise an 
     * <code>IllegalArgumentException</code> is thrown)</li>
     * <li>at least one value must be stored (returns <code>Double.NaN
     *     </code> otherwise)</li>
     * </ul>
     * 
     * @param p the requested percentile (scaled from 0 - 100)
     * @return An estimate for the pth percentile of the stored data 
     * values
     */
	public abstract double getPercentile(double p);
	
	/**
	 * Apply the given statistic to the data associated with this set of statistics.
	 * @param stat the statistic to apply
	 * @return the computed value of the statistic.
	 */
	public abstract double apply(UnivariateStatistic stat);

}
