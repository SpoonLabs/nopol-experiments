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

package org.apache.commons.math.random;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.math.stat.univariate.SummaryStatistics;

/**
 * Represents an <a href="http://random.mat.sbg.ac.at/~ste/dipl/node11.html">
 * empirical probability distribution</a> -- a probability distribution derived
 * from observed data without making any assumptions about the functional form
 * of the population distribution that the data come from.<p>
 * Implementations of this interface maintain data structures, called 
 * <i>distribution digests</i>, that describe empirical distributions and 
 * support the following operations: <ul>
 * <li>loading the distribution from a file of observed data values</li>
 * <li>saving and re-loading distribution digests to/from "digest files" </li>
 * <li>dividing the input data into "bin ranges" and reporting bin frequency
 *     counts (data for histogram)</li>
 * <li>reporting univariate statistics describing the full set of data values
 *     as well as the observations within each bin</li>
 * <li>generating random values from the distribution</li>
 * </ul>
 * Applications can use <code>EmpiricalDistribution</code> implementations to 
 * build grouped frequnecy histograms representing the input data or to
 * generate random values "like" those in the input file -- i.e., the values
 * generated will follow the distribution of the values in the file.
 * @version $Revision: 1.17 $ $Date: 2004/04/12 02:27:49 $
 */
public interface EmpiricalDistribution {
 
    /**
     * Computes the empirical distribution from the provided
     * array of numbers.
     * @param dataArray the data array
     */
    void load(double[] dataArray); 
        
    /**
     * Computes the empirical distribution from the input file.
     * @param filePath fully qualified name of a file in the local file system
     * @throws IOException if an IO error occurs
     */
    void load(String filePath) throws IOException; 
    
    /**
     * Computes the empirical distribution from the input file.
     * @param file the input file
     * @throws IOException if an IO error occurs
     */
    void load(File file) throws IOException;
    
    /**
     * Computes the empirical distribution using data read from a URL.
     * @param url url of the input file
     * @throws IOException if an IO error occurs
     */
    void load(URL url) throws IOException;
    
    /** 
     * Generates a random value from this distribution.
     * <strong>Preconditions:</strong><ul>
     * <li>the distribution must be loaded before invoking this method</li></ul>
     * @return the random value.
     * @throws IllegalStateException if the distribution has not been loaded
     */
    double getNextValue() throws IllegalStateException;  
    
     
    /** 
     * Returns a DescriptiveStatistics describing this distribution.
     * <strong>Preconditions:</strong><ul>
     * <li>the distribution must be loaded before invoking this method</li></ul>
     * @return the sample statistics
     * @throws IllegalStateException if the distribution has not been loaded
     */
    SummaryStatistics getSampleStats() throws IllegalStateException;
    
    /** 
     * Loads a saved distribution from a file.
     * @param file File reference for a file containing a digested distribution
     * @throws IOException if an error occurs reading the file
     */
    void loadDistribution(File file) throws IOException;  
    
    /** 
     * Loads a saved distribution from a file.
     * @param filePath fully qualified file path for a file 
     * containing a digested distribution 
     * @throws IOException if an error occurs reading the file
     */
    void loadDistribution(String filePath) throws IOException; 
    
    /** 
     * Saves distribution to a file. Overwrites the file if it exists.
     * <strong>Preconditions:</strong><ul>
     * <li>the distribution must be loaded before invoking this method</li></ul>
     * @param filePath fully qualified file path for the file to be written
     * @throws IOException if an error occurs reading the file
     * @throws IllegalStateException if the distribution has not been loaded
     */
    void saveDistribution(String filePath) throws 
        IOException,IllegalStateException;
    
    /** 
     * Saves distribution to a file. Overwrites the file if it exists.
     * <strong>Preconditions:</strong><ul>
     * <li>the distribution must be loaded before invoking this method</li></ul>
     * @param file File reference for the file to be written
     * @throws IOException if an error occurs reading the file
     * @throws IllegalStateException if the distribution has not been loaded
     */
    void saveDistribution(File file) throws IOException,IllegalStateException;
    
    /**
     * property indicating whether or not the distribution has been loaded
     * @return true if the distribution has been loaded
     */
    boolean isLoaded();  
    
     /** 
     * Returns the number of bins
     * @return the number of bins.
     */
    int getBinCount();
    
    /** 
     * Returns a list of Univariates containing statistics describing the
     * values in each of the bins.  The ArrayList is indexed on the bin number.
     * @return ArrayList of bin statistics.
     */
    ArrayList getBinStats();
    
    /** 
     * Returns the array of upper bounds for the bins.  Bins are: <br/>
     * [min,upperBounds[0]],(upperBounds[0],upperBounds[1]],...,
     *  (upperBounds[binCount-1],max]
     * @return array of bin upper bounds
     */
    double[] getUpperBounds();
    
}
