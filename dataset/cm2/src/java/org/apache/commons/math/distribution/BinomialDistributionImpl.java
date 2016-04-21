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

import java.io.Serializable;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.apache.commons.math.util.MathUtils;

/**
 * The default implementation of {@link BinomialDistribution}.
 * 
 * @version $Revision: 1.13 $ $Date: 2004/04/27 04:37:58 $
 */
public class BinomialDistributionImpl
    extends AbstractDiscreteDistribution
    implements BinomialDistribution, Serializable {

    /** The number of trials. */
    private int numberOfTrials;

    /** The probability of success. */
    private double probabilityOfSuccess;

    /**
     * Create a binomial distribution with the given number of trials and
     * probability of success.
     * @param trials the number of trials.
     * @param p the probability of success.
     */
    public BinomialDistributionImpl(int trials, double p) {
        super();
        setNumberOfTrials(trials);
        setProbabilityOfSuccess(p);
    }

    /**
     * Access the number of trials for this distribution.
     * @return the number of trials.
     */
    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    /**
     * Access the probability of success for this distribution.
     * @return the probability of success.
     */
    public double getProbabilityOfSuccess() {
        return probabilityOfSuccess;
    }

    /**
     * Change the number of trials for this distribution.
     * @param trials the new number of trials.
     */
    public void setNumberOfTrials(int trials) {
        if (trials < 0) {
            throw new IllegalArgumentException("number of trials must be non-negative.");
        }
        numberOfTrials = trials;
    }

    /**
     * Change the probability of success for this distribution.
     * @param p the new probability of success.
     */
    public void setProbabilityOfSuccess(double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("probability of success must be between 0.0 and 1.0, inclusive.");
        }
        probabilityOfSuccess = p;
    }

    /**
     * Access the domain value lower bound, based on <code>p</code>, used to
     * bracket a PDF root.
     * 
     * @param p the desired probability for the critical value
     * @return domain value lower bound, i.e.
     *         P(X &lt; <i>lower bound</i>) &lt; <code>p</code> 
     */
    protected int getDomainLowerBound(double p) {
        return -1;
    }

    /**
     * Access the domain value upper bound, based on <code>p</code>, used to
     * bracket a PDF root.
     * 
     * @param p the desired probability for the critical value
     * @return domain value upper bound, i.e.
     *         P(X &lt; <i>upper bound</i>) &gt; <code>p</code> 
     */
    protected int getDomainUpperBound(double p) {
        return getNumberOfTrials();
    }

    /**
     * For this disbution, X, this method returns P(X &le; x).
     * @param x the value at which the PDF is evaluated.
     * @return PDF for this distribution. 
     * @exception MathException if the cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     */
    public double cumulativeProbability(int x) throws MathException {
        double ret;
        if (x < 0) {
            ret = 0.0;
        } else if (x >= getNumberOfTrials()) {
            ret = 1.0;
        } else {
            ret =
                1.0 - Beta.regularizedBeta(
                        getProbabilityOfSuccess(),
                        x + 1.0,
                        getNumberOfTrials() - x);
        }
        return ret;
    }

    /**
     * For this disbution, X, this method returns P(X = x).
     * @param x the value at which the PMF is evaluated.
     * @return PMF for this distribution. 
     */
    public double probability(int x) {
        double ret;
        if (x < 0 || x > getNumberOfTrials()) {
            ret = 0.0;
        } else {
            ret = MathUtils.binomialCoefficientDouble(
            		getNumberOfTrials(), x) *
				  Math.pow(getProbabilityOfSuccess(), x) *
				  Math.pow(1.0 - getProbabilityOfSuccess(),
				  		getNumberOfTrials() - x);
        }
        return ret;
    }
}
