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
 * Interface representing a univariate real interpolating function.
 *  
 * @version $Revision: 1.10 $ $Date: 2004/02/21 21:35:14 $
 */
public interface UnivariateRealInterpolator {

    /**
     * Computes an interpolating function for the data set.
     * @param xval the arguments for the interpolation points
     * @param yval the values for the interpolation points
     * @return a function which interpolates the data set
     * @throws MathException if arguments violate assumptions made by the
     *         interpolationg algorithm
     */
    public UnivariateRealFunction interpolate(double xval[], double yval[])
        throws MathException;
}
