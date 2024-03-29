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

import java.util.List;

import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.univariate.UnivariateStatistic;
import org.apache.commons.math.stat.univariate.AbstractDescriptiveStatistics;
import org.apache.commons.math.util.DefaultTransformer;
import org.apache.commons.math.util.NumberTransformer;

/**
 * @version $Revision: 1.2 $ $Date: 2004/04/24 21:41:02 $
 */
public class ListUnivariateImpl extends AbstractDescriptiveStatistics {

    /**
     * Holds a reference to a list - GENERICs are going to make
     * out lives easier here as we could only accept List<Number>
     */
    protected List list;

    /** Number Transformer maps Objects to Number for us. */
    protected NumberTransformer transformer;
    
    /** hold the window size **/
    protected int windowSize = DescriptiveStatistics.INFINITE_WINDOW;

    /**
     * Construct a ListUnivariate with a specific List.
     * @param list The list that will back this DescriptiveStatistics
     */
    public ListUnivariateImpl(List list) {
        this(list, new DefaultTransformer());
    }
    
    /**
     * Construct a ListUnivariate with a specific List.
     * @param list The list that will back this DescriptiveStatistics
     * @param transformer the number transformer used to convert the list items.
     */
    public ListUnivariateImpl(List list, NumberTransformer transformer) {
        super();
        this.list = list;
        this.transformer = transformer;
    }

    /**
     * @see org.apache.commons.math.stat.univariate.DescriptiveStatistics#getValues()
     */
    public double[] getValues() {

        int length = list.size();

        // If the window size is not INFINITE_WINDOW AND
        // the current list is larger that the window size, we need to
        // take into account only the last n elements of the list
        // as definied by windowSize

        if (windowSize != DescriptiveStatistics.INFINITE_WINDOW &&
            windowSize < list.size())
        {
            length = list.size() - Math.max(0, list.size() - windowSize);
        }

        // Create an array to hold all values
        double[] copiedArray = new double[length];

        for (int i = 0; i < copiedArray.length; i++) {
            copiedArray[i] = getElement(i);
        }
        return copiedArray;
    }

    /**
     * @see org.apache.commons.math.stat.univariate.DescriptiveStatistics#getElement(int)
     */
    public double getElement(int index) {

        double value = Double.NaN;

        int calcIndex = index;

        if (windowSize != DescriptiveStatistics.INFINITE_WINDOW &&
            windowSize < list.size())
        {
            calcIndex = (list.size() - windowSize) + index;
        }

        
        try {
			value = transformer.transform(list.get(calcIndex));
		} catch (MathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return value;
    }

    /**
     * @see org.apache.commons.math.stat.univariate.DescriptiveStatistics#getN()
     */
    public long getN() {
        int n = 0;

        if (windowSize != DescriptiveStatistics.INFINITE_WINDOW) {
            if (list.size() > windowSize) {
                n = windowSize;
            } else {
                n = list.size();
            }
        } else {
            n = list.size();
        }
        return n;
    }

    /**
     * @see org.apache.commons.math.stat.univariate.DescriptiveStatistics#addValue(double)
     */
    public void addValue(double v) {
        list.add(new Double(v));
    }
    
    /**
     * Adds an object to this list. 
     * @param o Object to add to the list
     */
    public void addObject(Object o) {
        list.add(o);
    }

    /**
     * Clears all statistics.
     * <p>
     * <strong>N.B.: </strong> This method has the side effect of clearing the underlying list.
     */
    public void clear() {
        list.clear();
    }
    
    /**
     * Apply the given statistic to this univariate collection.
     * @param stat the statistic to apply
     * @return the computed value of the statistic.
     */
    public double apply(UnivariateStatistic stat) {
        double[] v = this.getValues();

        if (v != null) {
            return stat.evaluate(v, 0, v.length);
        }
        return Double.NaN;
    }
    
    /**
     * Access the number transformer.
     * @return the number transformer.
     */
    public NumberTransformer getTransformer() {
        return transformer;
    }

    /**
     * Modify the number transformer.
     * @param transformer the new number transformer.
     */
    public void setTransformer(NumberTransformer transformer) {
        this.transformer = transformer;
    }
    
    /**
     * @see org.apache.commons.math.stat.Univariate#setWindowSize(int)
     */
    public synchronized void setWindowSize(int windowSize) {
    	this.windowSize = windowSize;
    	//Discard elements from the front of the list if the windowSize is less than 
    	// the size of the list.
    	int extra = list.size() - windowSize;
    	for (int i = 0; i < extra; i++) {
    		list.remove(0);
    	}
    }
    	
    	public int getWindowSize() {
    		return windowSize;
    	}

}