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
package org.apache.commons.math.distribution;

/**
 * The Binomial Distribution.
 *
 * Instances of BinomialDistribution objects should be created using
 * {@link DistributionFactory#createBinomialDistribution(int, double)}.
 * 
 * <p>
 * References:
 * <ul>
 * <li><a href="http://mathworld.wolfram.com/BinomialDistribution.html">
 * Binomial Distribution</a></li>
 * </ul>
 * </p>
 * 
 * @version $Revision: 1.10 $ $Date: 2004/04/26 19:15:48 $
 */
public interface BinomialDistribution extends DiscreteDistribution {
    /**
     * Access the number of trials for this distribution.
     * @return the number of trials.
     */
    int getNumberOfTrials();
    
    /**
     * Access the probability of success for this distribution.
     * @return the probability of success.
     */
    double getProbabilityOfSuccess();
    
    /**
     * Change the number of trials for this distribution.
     * @param trials the new number of trials.
     */
    void setNumberOfTrials(int trials);
    
    /**
     * Change the probability of success for this distribution.
     * @param p the new probability of success.
     */
    void setProbabilityOfSuccess(double p);
}