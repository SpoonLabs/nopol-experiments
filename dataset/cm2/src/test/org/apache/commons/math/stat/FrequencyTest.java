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
package org.apache.commons.math.stat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test cases for the {@link Frequency} class.
 *
 * @version $Revision: 1.12 $ $Date: 2004/03/07 00:57:11 $
 */

public final class FrequencyTest extends TestCase {
    private long oneL = 1;
    private long twoL = 2;
    private long threeL = 3;
    private int oneI = 1;
    private int twoI = 2;
    private int threeI=3;
    private String oneS = "1";
    private String twoS = "2";
    private double tolerance = 10E-15;
    private Frequency f = null;
    
    public FrequencyTest(String name) {
        super(name);
    }
    
    public void setUp() {  
    	f = new Frequency();
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(FrequencyTest.class);
        suite.setName("Frequency Tests");
        return suite;
    }
    
    /** test freq counts */
    public void testCounts() {
        assertEquals("total count",0,f.getSumFreq());
        f.addValue(oneL);
        f.addValue(twoL);
        f.addValue(1);
        f.addValue(oneI);
        assertEquals("one frequency count",3,f.getCount(1));
        assertEquals("two frequency count",1,f.getCount(2));
        assertEquals("three frequency count",0,f.getCount(3));
        assertEquals("total count",4,f.getSumFreq());
        assertEquals("zero cumulative frequency", 0, f.getCumFreq(0));
        assertEquals("one cumulative frequency", 3,  f.getCumFreq(1));
        assertEquals("two cumulative frequency", 4,  f.getCumFreq(2));
        assertEquals("five cumulative frequency", 4,  f.getCumFreq(5));
        assertEquals("foo cumulative frequency", 0,  f.getCumFreq("foo"));
        
        f.clear();
        assertEquals("total count",0,f.getSumFreq());
        
        // userguide examples -------------------------------------------------------------------
        f.addValue("one");
        f.addValue("One");
        f.addValue("oNe");
        f.addValue("Z");
        assertEquals("one cumulative frequency", 1 ,  f.getCount("one"));
        assertEquals("Z cumulative pct", 0.5,  f.getCumPct("Z"), tolerance);
        assertEquals("z cumulative pct", 1.0,  f.getCumPct("z"), tolerance);
        assertEquals("Ot cumulative pct", 0.25,  f.getCumPct("Ot"), tolerance);
        f.clear();
        
        f = null;
        f = new Frequency(String.CASE_INSENSITIVE_ORDER);
        f.addValue("one");
        f.addValue("One");
        f.addValue("oNe");
        f.addValue("Z");
        assertEquals("one count", 3 ,  f.getCount("one"));
        assertEquals("Z cumulative pct -- case insensitive", 1 ,  f.getCumPct("Z"), tolerance);
        assertEquals("z cumulative pct -- case insensitive", 1 ,  f.getCumPct("z"), tolerance);
    }     
    
    /** test pcts */
    public void testPcts() {
        f.addValue(oneL);
        f.addValue(twoL);
        f.addValue(oneI);
        f.addValue(twoI);
        f.addValue(threeL);
        f.addValue(threeL);
        f.addValue(3);
        f.addValue(threeI);
        assertEquals("one pct",0.25,f.getPct(1),tolerance);
        assertEquals("two pct",0.25,f.getPct(new Long(2)),tolerance);
        assertEquals("three pct",0.5,f.getPct(threeL),tolerance);
        assertEquals("five pct",0,f.getPct(5),tolerance);
        assertEquals("foo pct",0,f.getPct("foo"),tolerance);
        assertEquals("one cum pct",0.25,f.getCumPct(1),tolerance);
        assertEquals("two cum pct",0.50,f.getCumPct(new Long(2)),tolerance);
        assertEquals("three cum pct",1.0,f.getCumPct(threeL),tolerance);
        assertEquals("five cum pct",1.0,f.getCumPct(5),tolerance);
        assertEquals("zero cum pct",0.0,f.getCumPct(0),tolerance);
        assertEquals("foo cum pct",0,f.getCumPct("foo"),tolerance);
    }
    
    /** test adding incomparable values */
    public void testAdd() {
    	char aChar = 'a';
    	char bChar = 'b';
    	String aString = "a";
    	f.addValue(aChar);
    	f.addValue(bChar);
    	try {
    		f.addValue(aString); 	
    		fail("Expecting IllegalArgumentException");
    	} catch (IllegalArgumentException ex) {
    		// expected
    	}
    	assertEquals("a pct",0.5,f.getPct(aChar),tolerance);
    	assertEquals("b cum pct",1.0,f.getCumPct(bChar),tolerance);
    	assertEquals("a string pct",0.0,f.getPct(aString),tolerance);
    	assertEquals("a string cum pct",0.0,f.getCumPct(aString),tolerance);
    }
    
    /**
     * Tests toString() 
     */
    public void testToString(){
        f.addValue(oneL);
        f.addValue(twoL);
        f.addValue(oneI);
        f.addValue(twoI);
        
        String s = f.toString();
        //System.out.println(s);
        assertNotNull(s);
        BufferedReader reader = new BufferedReader(new StringReader(s));
        try {
            String line = reader.readLine(); // header line
            assertNotNull(line);
            
            line = reader.readLine(); // one's or two's line
            assertNotNull(line);
                        
            line = reader.readLine(); // one's or two's line
            assertNotNull(line);

            line = reader.readLine(); // no more elements
            assertNull(line);
        } catch(IOException ex){
            fail(ex.getMessage());
        }        
    }
}

