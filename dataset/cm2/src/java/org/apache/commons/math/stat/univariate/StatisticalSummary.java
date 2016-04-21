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
package org.apache.commons.math.stat.univariate;

/**
 *  Reporting interface for basic univariate statistics.
 * 
  * @version $Revision: 1.2 $ $Date: 2004/04/27 04:37:59 $
 */
public interface StatisticalSummary {
	/** 
	 * Returns the <a href="http://www.xycoon.com/arithmetic_mean.htm">
	 * arithmetic mean </a> of the available values 
	 * @return The mean or Double.NaN if no values have been added.
	 */
	public abstract double getMean();
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
}