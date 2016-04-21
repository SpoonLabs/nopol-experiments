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
package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;

/**
 * Interface for (univariate real) rootfinding algorithms.
 * <p>
 * Implementations will search for only one zero in the given interval.
 *  
 * @version $Revision: 1.11 $ $Date: 2004/02/21 21:35:14 $
 */
public interface UnivariateRealSolver {

    /**
     * Set the upper limit for the number of iterations.
     * 
     * Usually a high iteration count indicates convergence problems. However,
     * the "reasonable value" varies widely for different solvers.  Users are
     * advised to use the default value supplied by the solver.
     *  
     * An exception will be thrown if the number is exceeded.
     *  
     * @param count maximum number of iterations
     */
    public void setMaximalIterationCount(int count);

    /**
     * Get the upper limit for the number of iterations.
     * 
     * @return the actual upper limit
     */
    public int getMaximalIterationCount();

    /**
     * Reset the upper limit for the number of iterations to the default.
     * 
     * The default value is supplied by the solver implementation.
     * 
     * @see #setMaximalIterationCount(int)
     */
    public void resetMaximalIterationCount();

    /**
     * Set the absolute accuracy.
     * 
     * The default is usually choosen so that roots in the interval
     * -10..-0.1 and +0.1..+10 can be found with a reasonable accuracy. If the
     * expected absolute value of your roots is of much smaller magnitude, set
     * this to a smaller value.
     * 
     * Solvers are advised to do a plausibility check with the relative
     * accuracy, but clients should not rely on this.
     *  
     * @param accuracy the accuracy.
     * @throws MathException if the accuracy can't be achieved by the solver or
     *         is otherwise deemed unreasonable. 
     */
    public void setAbsoluteAccuracy(double accuracy) throws MathException;

    /**
     * Get the actual absolute accuracy.
     * 
     * @return the accuracy
     */
    public double getAbsoluteAccuracy();

    /**
     * Reset the absolute accuracy to the default.
     * 
     * The default value is provided by the solver implementation.
     */
    public void resetAbsoluteAccuracy();

    /**
     * Set the relative accuracy.
     * 
     * This is used to stop iterations if the absolute accuracy can't be
     * achieved due to large values or short mantissa length.
     * 
     * If this should be the primary criterium for convergence rather then a
     * safety measure, set the absolute accuracy to a ridiculously small value,
     * like 1E-1000.
     * 
     * @param accuracy the relative accuracy.
     * @throws MathException if the accuracy can't be achieved by the solver or
     *         is otherwise deemed unreasonable. 
     */
    public void setRelativeAccuracy(double accuracy) throws MathException;

    /**
     * Get the actual relative accuracy.
     * @return the accuracy
     */
    public double getRelativeAccuracy();

    /**
     * Reset the relative accuracy to the default.
     * The default value is provided by the solver implementation.
     */
    public void resetRelativeAccuracy();

    /**
     * Set the function value accuracy.
     * 
     * This is used to determine whan an evaluated function value or some other
     * value which is used as divisor is zero.
     * 
     * This is a safety guard and it shouldn't be necesary to change this in
     * general.
     * 
     * @param accuracy the accuracy.
     * @throws MathException if the accuracy can't be achieved by the solver or
     *         is otherwise deemed unreasonable. 
     */
    public void setFunctionValueAccuracy(double accuracy) throws MathException;

    /**
     * Get the actual function value accuracy.
     * @return the accuracy
     */
    public double getFunctionValueAccuracy();

    /**
     * Reset the actual function accuracy to the default.
     * The default value is provided by the solver implementation.
     */
    public void resetFunctionValueAccuracy();

    /**
     * Solve for a zero root in the given interval.
     * A solver may require that the interval brackets a single zero root.
     * @param min the lower bound for the interval.
     * @param max the upper bound for the interval.
     * @return a value where the function is zero
     * @throws MathException if the iteration count was exceeded or the
     *  solver detects convergence problems otherwise.
     */
    public double solve(double min, double max) throws MathException;

    /**
     * Solve for a zero in the given interval, start at startValue.
     * A solver may require that the interval brackets a single zero root.
     * @param min the lower bound for the interval.
     * @param max the upper bound for the interval.
     * @param startValue the start value to use
     * @return a value where the function is zero
     * @throws MathException if the iteration count was exceeded or the
     *  solver detects convergence problems otherwise.
     */
    public double solve(double min, double max, double startValue)
        throws MathException;

    /**
     * Get the result of the last run of the solver.
     * @return the last result.
     * @throws MathException if there is no result available, either
     * because no result was yet computed or the last attempt failed.
     */
    public double getResult() throws MathException;

    /**
     * Get the number of iterations in the last run of the solver.
     * This is mainly meant for testing purposes. It may occasionally
     * help track down performance problems: if the iteration count
     * is notoriously high, check whether the function is evaluated
     * properly, and whether another solver is more amenable to the
     * problem.
     * @return the last iteration count.
     * @throws MathException if there is no result available, either
     * because no result was yet computed or the last attempt failed.
     */
    public int getIterationCount() throws MathException;
}