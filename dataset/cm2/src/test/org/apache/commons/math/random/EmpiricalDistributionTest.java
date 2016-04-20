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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.math.RetryTestCase;
import org.apache.commons.math.stat.univariate.SummaryStatistics;

/**
 * Test cases for the EmpiricalDistribution class
 *
 * @version $Revision: 1.16 $ $Date: 2004/04/12 02:27:49 $
 */

public final class EmpiricalDistributionTest extends RetryTestCase {

    protected EmpiricalDistribution empiricalDistribution = null;
    protected EmpiricalDistribution empiricalDistribution2 = null;
    protected File file = null;
    protected URL url = null; 
    protected double[] dataArray = null;
    
    public EmpiricalDistributionTest(String name) {
        super(name);
    }

    public void setUp() throws IOException {
        empiricalDistribution = new EmpiricalDistributionImpl(100);
        url = getClass().getResource("testData.txt");
        
        empiricalDistribution2 = new EmpiricalDistributionImpl(100);
        BufferedReader in = 
                new BufferedReader(new InputStreamReader(
                        url.openStream()));
        String str = null;
        ArrayList list = new ArrayList();
        while ((str = in.readLine()) != null) {
            list.add(Double.valueOf(str));
        }
        in.close();
        in = null;
        
        dataArray = new double[list.size()];
        int i = 0;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            dataArray[i] = ((Double)iter.next()).doubleValue();
            i++;
        }                 
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EmpiricalDistributionTest.class);
        suite.setName("EmpiricalDistribution Tests");
        return suite;
    }

    /**
     * Test EmpiricalDistrbution.load() using sample data file.<br> 
     * Check that the sampleCount, mu and sigma match data in 
     * the sample data file.
     */
    public void testLoad() throws Exception {
        empiricalDistribution.load(url);   
        // testData File has 10000 values, with mean ~ 5.0, std dev ~ 1
        // Make sure that loaded distribution matches this
        assertEquals(empiricalDistribution.getSampleStats().getN(),1000,10E-7);
        //TODO: replace with statistical tests
        assertEquals
            (empiricalDistribution.getSampleStats().getMean(),
                5.069831575018909,10E-7);
        assertEquals
          (empiricalDistribution.getSampleStats().getStandardDeviation(),
                1.0173699343977738,10E-7);
    }

    /**
     * Test EmpiricalDistrbution.load(double[]) using data taken from
     * sample data file.<br> 
     * Check that the sampleCount, mu and sigma match data in 
     * the sample data file.
     */
    public void testDoubleLoad() throws Exception {
        empiricalDistribution2.load(dataArray);   
        // testData File has 10000 values, with mean ~ 5.0, std dev ~ 1
        // Make sure that loaded distribution matches this
        assertEquals(empiricalDistribution2.getSampleStats().getN(),1000,10E-7);
        //TODO: replace with statistical tests
        assertEquals
            (empiricalDistribution2.getSampleStats().getMean(),
                5.069831575018909,10E-7);
        assertEquals
          (empiricalDistribution2.getSampleStats().getStandardDeviation(),
                1.0173699343977738,10E-7);
    }
   
    /** 
      * Generate 1000 random values and make sure they look OK.<br>
      * Note that there is a non-zero (but very small) probability that
      * these tests will fail even if the code is working as designed.
      */
    public void testNext() throws Exception {
        tstGen(0.1);
        tstDoubleGen(0.1);
    }
    
    /**
      * Make sure exception thrown if digest getNext is attempted
      * before loading empiricalDistribution.
     */
    public void testNexFail() {
        try {
            empiricalDistribution.getNextValue();
            empiricalDistribution2.getNextValue();
            fail("Expecting IllegalStateException");
        } catch (IllegalStateException ex) {;}
    }
    
    /**
     * Make sure we can handle a grid size that is too fine
     */
    public void testGridTooFine() throws Exception {
        empiricalDistribution = new EmpiricalDistributionImpl(1001);
        tstGen(0.1);    
        empiricalDistribution2 = new EmpiricalDistributionImpl(1001);           
        tstDoubleGen(0.1);
    }
    
    /**
     * How about too fat?
     */
    public void testGridTooFat() throws Exception {
        empiricalDistribution = new EmpiricalDistributionImpl(1);
        tstGen(5); // ridiculous tolerance; but ridiculous grid size
                   // really just checking to make sure we do not bomb
        empiricalDistribution2 = new EmpiricalDistributionImpl(1);           
        tstDoubleGen(5);           
    }
    
    private void tstGen(double tolerance)throws Exception {
        empiricalDistribution.load(url);   
        SummaryStatistics stats = SummaryStatistics.newInstance();
        for (int i = 1; i < 1000; i++) {
            stats.addValue(empiricalDistribution.getNextValue());
        }
        assertEquals("mean", stats.getMean(),5.069831575018909,tolerance);
        assertEquals
         ("std dev", stats.getStandardDeviation(),1.0173699343977738,tolerance);
    }

    private void tstDoubleGen(double tolerance)throws Exception {
        empiricalDistribution2.load(dataArray);   
        SummaryStatistics stats = SummaryStatistics.newInstance();
        for (int i = 1; i < 1000; i++) {
            stats.addValue(empiricalDistribution2.getNextValue());
        }
        assertEquals("mean", stats.getMean(),5.069831575018909,tolerance);
        assertEquals
         ("std dev", stats.getStandardDeviation(),1.0173699343977738,tolerance);
    }
}
