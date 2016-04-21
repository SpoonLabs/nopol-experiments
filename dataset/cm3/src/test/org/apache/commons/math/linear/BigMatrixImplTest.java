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
package org.apache.commons.math.linear;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.math.BigDecimal;

/**
 * Test cases for the {@link BigMatrixImpl} class.
 *
 * @version $Revision: 1.3 $ $Date: 2004/09/05 01:19:23 $
 */

public final class BigMatrixImplTest extends TestCase {
    
    private double[][] testData = { {1d,2d,3d}, {2d,5d,3d}, {1d,0d,8d} };
    private String[][] testDataString = { {"1","2","3"}, {"2","5","3"}, {"1","0","8"} };
    private double[][] testDataLU = {{2d, 5d, 3d}, {.5d, -2.5d, 6.5d}, {0.5d, 0.2d, .2d}};
    private double[][] testDataPlus2 = { {3d,4d,5d}, {4d,7d,5d}, {3d,2d,10d} };
    private double[][] testDataMinus = { {-1d,-2d,-3d}, {-2d,-5d,-3d}, 
       {-1d,0d,-8d} };
    private double[] testDataRow1 = {1d,2d,3d};
    private double[] testDataCol3 = {3d,3d,8d};
    private double[][] testDataInv = 
        { {-40d,16d,9d}, {13d,-5d,-3d}, {5d,-2d,-1d} };
    private double[] preMultTest = {8,12,33};
    private double[][] testData2 ={ {1d,2d,3d}, {2d,5d,3d}};
    private double[][] testData2T = { {1d,2d}, {2d,5d}, {3d,3d}};
    private double[][] testDataPlusInv = 
        { {-39d,18d,12d}, {15d,0d,0d}, {6d,-2d,7d} };
    private double[][] id = { {1d,0d,0d}, {0d,1d,0d}, {0d,0d,1d} };
    private double[][] luData = { {2d,3d,3d}, {0d,5d,7d}, {6d,9d,8d} };
    private double[][] luDataLUDecomposition = { {6d,9d,8d}, {0d,5d,7d}, {0.33333333333333,0d,0.33333333333333} };
    private double[][] singular = { {2d,3d}, {2d,3d} };
    private double[][] bigSingular = {{1d,2d,3d,4d}, {2d,5d,3d,4d},
        {7d,3d,256d,1930d}, {3d,7d,6d,8d}}; // 4th row = 1st + 2nd
    private double[][] detData = { {1d,2d,3d}, {4d,5d,6d}, {7d,8d,10d} };
    private double[][] detData2 = { {1d, 3d}, {2d, 4d}};
    private double[] testVector = {1,2,3};
    private double[] testVector2 = {1,2,3,4};
    private double entryTolerance = 10E-16;
    private double normTolerance = 10E-14;
    
    public BigMatrixImplTest(String name) {
        super(name);
    }
    
    public void setUp() {
        
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(BigMatrixImplTest.class);
        suite.setName("BigMatrixImpl Tests");
        return suite;
    }

    public static final double[] asDouble(BigDecimal[] data) {
        double d[] = new double[data.length];
        for (int i=0;i<d.length;i++) {
            d[i] = data[i].doubleValue();
        }
        return d;
    }

    public static final double[][] asDouble(BigDecimal[][] data) {
        double d[][] = new double[data.length][data[0].length];
        for (int i=0;i<d.length;i++) {
            for (int j=0;j<d[i].length;j++)
            d[i][j] = data[i][j].doubleValue();
        }
        return d;
    }

    public static final BigDecimal[] asBigDecimal(double [] data) {
        BigDecimal d[] = new BigDecimal[data.length];
        for (int i=0;i<d.length;i++) {
            d[i] = new BigDecimal(data[i]);
        }
        return d;
    }

    public static final BigDecimal[][] asBigDecimal(double [][] data) {
        BigDecimal d[][] = new BigDecimal[data.length][data[0].length];
        for (int i=0;i<d.length;i++) {
            for (int j=0;j<data[i].length;j++) {
                d[i][j] = new BigDecimal(data[i][j]);
            }
        }
        return d;
    }

    /** test dimensions */
    public void testDimensions() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrixImpl m2 = new BigMatrixImpl(testData2);
        assertEquals("testData row dimension",3,m.getRowDimension());
        assertEquals("testData column dimension",3,m.getColumnDimension());
        assertTrue("testData is square",m.isSquare());
        assertEquals("testData2 row dimension",m2.getRowDimension(),2);
        assertEquals("testData2 column dimension",m2.getColumnDimension(),3);
        assertTrue("testData2 is not square",!m2.isSquare());
        BigMatrixImpl m3 = new BigMatrixImpl();
        m3.setData(testData);
    } 
    
    /** test copy functions */
    public void testCopyFunctions() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrixImpl m2 = new BigMatrixImpl(testData2);
        m2.setData(m.getData());
        assertClose("getData",m2,m,entryTolerance);
        // no dangling reference...
        m2.setEntry(1,1,2000d);
        BigMatrixImpl m3 = new BigMatrixImpl(testData);
        assertClose("no getData side effect",m,m3,entryTolerance);
        m3 = (BigMatrixImpl) m.copy();
        double[][] stompMe = {{1d,2d,3d}};
        m3.setDataRef(asBigDecimal(stompMe));
        assertClose("no copy side effect",m,new BigMatrixImpl(testData),
            entryTolerance);
    }
    
    /** test constructors */
    public void testConstructors() {
        BigMatrix m1 = new BigMatrixImpl(testData);
        BigMatrix m2 = new BigMatrixImpl(testDataString);
        BigMatrix m3 = new BigMatrixImpl(asBigDecimal(testData));
        assertClose("double, string", m1, m2, Double.MIN_VALUE);
        assertClose("double, BigDecimal", m1, m3, Double.MIN_VALUE);
        assertClose("string, BigDecimal", m2, m3, Double.MIN_VALUE);
        try {
            BigMatrix m4 = new BigMatrixImpl(new String[][] {{"0", "hello", "1"}});
            fail("Expecting NumberFormatException");
        } catch (NumberFormatException ex) {
            // expected
        }
    }
    
    /** test add */
    public void testAdd() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrixImpl mInv = new BigMatrixImpl(testDataInv);
        BigMatrixImpl mPlusMInv = (BigMatrixImpl)m.add(mInv);
        double[][] sumEntries = asDouble(mPlusMInv.getData());
        for (int row = 0; row < m.getRowDimension(); row++) {
            for (int col = 0; col < m.getColumnDimension(); col++) {
                assertEquals("sum entry entry",
                    testDataPlusInv[row][col],sumEntries[row][col],
                        entryTolerance);
            }
        }    
    }
    
    /** test add failure */
    public void testAddFail() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrixImpl m2 = new BigMatrixImpl(testData2);
        try {
            BigMatrixImpl mPlusMInv = (BigMatrixImpl)m.add(m2);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            ;
        }
    }
    
    /** test norm */
    public void testNorm() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrixImpl m2 = new BigMatrixImpl(testData2);
        assertEquals("testData norm",14d,m.getNorm().doubleValue(),entryTolerance);
        assertEquals("testData2 norm",7d,m2.getNorm().doubleValue(),entryTolerance);
    }
    
     /** test m-n = m + -n */
    public void testPlusMinus() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrixImpl m2 = new BigMatrixImpl(testDataInv);
        assertClose("m-n = m + -n",m.subtract(m2),
            m2.scalarMultiply(new BigDecimal(-1d)).add(m),entryTolerance);
        try {
            BigMatrix a = m.subtract(new BigMatrixImpl(testData2));
            fail("Expecting illegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }      
    }
   
    /** test multiply */
     public void testMultiply() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrixImpl mInv = new BigMatrixImpl(testDataInv);
        BigMatrixImpl identity = new BigMatrixImpl(id);
        BigMatrixImpl m2 = new BigMatrixImpl(testData2);
        assertClose("inverse multiply",m.multiply(mInv),
            identity,entryTolerance);
        assertClose("inverse multiply",mInv.multiply(m),
            identity,entryTolerance);
        assertClose("identity multiply",m.multiply(identity),
            m,entryTolerance);
        assertClose("identity multiply",identity.multiply(mInv),
            mInv,entryTolerance);
        assertClose("identity multiply",m2.multiply(identity),
            m2,entryTolerance); 
        try {
            BigMatrix a = m.multiply(new BigMatrixImpl(bigSingular));
            fail("Expecting illegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }      
    }   
    
    //Additional Test for BigMatrixImplTest.testMultiply

    private double[][] d3 = new double[][] {{1,2,3,4},{5,6,7,8}};
    private double[][] d4 = new double[][] {{1},{2},{3},{4}};
    private double[][] d5 = new double[][] {{30},{70}};
     
    public void testMultiply2() { 
       BigMatrix m3 = new BigMatrixImpl(d3);
       BigMatrix m4 = new BigMatrixImpl(d4);
       BigMatrix m5 = new BigMatrixImpl(d5);
       assertClose("m3*m4=m5", m3.multiply(m4), m5, entryTolerance);
   }  
        
    /** test isSingular */
    public void testIsSingular() {
        BigMatrixImpl m = new BigMatrixImpl(singular);
        assertTrue("singular",m.isSingular());
        m = new BigMatrixImpl(bigSingular);
        assertTrue("big singular",m.isSingular());
        m = new BigMatrixImpl(id);
        assertTrue("identity nonsingular",!m.isSingular());
        m = new BigMatrixImpl(testData);
        assertTrue("testData nonsingular",!m.isSingular());
    }
        
    /** test inverse */
    public void testInverse() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrix mInv = new BigMatrixImpl(testDataInv);
        assertClose("inverse",mInv,m.inverse(),normTolerance);
        assertClose("inverse^2",m,m.inverse().inverse(),10E-12);
        
        // Not square
        m = new BigMatrixImpl(testData2);
        try {
            m.inverse();
            fail("Expecting InvalidMatrixException");
        } catch (InvalidMatrixException ex) {
            // expected
        }
        
        // Singular
        m = new BigMatrixImpl(singular);
        try {
            m.inverse();
            fail("Expecting InvalidMatrixException");
        } catch (InvalidMatrixException ex) {
            // expected
        }
    }
    
    /** test solve */
    public void testSolve() {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrix mInv = new BigMatrixImpl(testDataInv);
        // being a bit slothful here -- actually testing that X = A^-1 * B
        assertClose("inverse-operate",
                    asDouble(mInv.operate(asBigDecimal(testVector))),
                    asDouble(m.solve(asBigDecimal(testVector))),
                    normTolerance);
        try {
            double[] x = asDouble(m.solve(asBigDecimal(testVector2)));
            fail("expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }       
        BigMatrix bs = new BigMatrixImpl(bigSingular);
        try {
            BigMatrix a = bs.solve(bs);
            fail("Expecting InvalidMatrixException");
        } catch (InvalidMatrixException ex) {
            ;
        }
        try {
            BigMatrix a = m.solve(bs);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }
        try {
            BigMatrix a = (new BigMatrixImpl(testData2)).solve(bs);
            fail("Expecting illegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        } 
        try {
            (new BigMatrixImpl(testData2)).luDecompose();
            fail("Expecting InvalidMatrixException");
        } catch (InvalidMatrixException ex) {
            ;
        }  
    }
    
    /** test determinant */
    public void testDeterminant() {       
        BigMatrix m = new BigMatrixImpl(bigSingular);
        assertEquals("singular determinant",0,m.getDeterminant().doubleValue(),0);
        m = new BigMatrixImpl(detData);
        assertEquals("nonsingular test",-3d,m.getDeterminant().doubleValue(),normTolerance);
        
        // Examples verified against R (version 1.8.1, Red Hat Linux 9)
        m = new BigMatrixImpl(detData2);
        assertEquals("nonsingular R test 1",-2d,m.getDeterminant().doubleValue(),normTolerance);
        m = new BigMatrixImpl(testData);
        assertEquals("nonsingular  R test 2",-1d,m.getDeterminant().doubleValue(),normTolerance);

        try {
            double a = new BigMatrixImpl(testData2).getDeterminant().doubleValue();
            fail("Expecting InvalidMatrixException");
        } catch (InvalidMatrixException ex) {
            ;
        }      
    }
    
    /** test trace */
    public void testTrace() {
        BigMatrix m = new BigMatrixImpl(id);
        assertEquals("identity trace",3d,m.getTrace().doubleValue(),entryTolerance);
        m = new BigMatrixImpl(testData2);
        try {
            double x = m.getTrace().doubleValue();
            fail("Expecting illegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }      
    }
    
    /** test sclarAdd */
    public void testScalarAdd() {
        BigMatrix m = new BigMatrixImpl(testData);
        assertClose("scalar add",new BigMatrixImpl(testDataPlus2),
            m.scalarAdd(new BigDecimal(2d)),entryTolerance);
    }
                    
    /** test operate */
    public void testOperate() {
        BigMatrix m = new BigMatrixImpl(id);
        double[] x = asDouble(m.operate(asBigDecimal(testVector)));
        assertClose("identity operate",testVector,x,entryTolerance);
        m = new BigMatrixImpl(bigSingular);
        try {
            x = asDouble(m.operate(asBigDecimal(testVector)));
            fail("Expecting illegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }      
    }
    
    /** test transpose */
    public void testTranspose() {
        BigMatrix m = new BigMatrixImpl(testData);
        assertClose("inverse-transpose",m.inverse().transpose(),
            m.transpose().inverse(),normTolerance);
        m = new BigMatrixImpl(testData2);
        BigMatrix mt = new BigMatrixImpl(testData2T);
        assertClose("transpose",mt,m.transpose(),normTolerance);
    }
    
    /** test preMultiply by vector */
    public void testPremultiplyVector() {
        BigMatrix m = new BigMatrixImpl(testData);
        assertClose("premultiply",asDouble(m.preMultiply(asBigDecimal(testVector))),preMultTest,normTolerance);
        m = new BigMatrixImpl(bigSingular);
        try {
            m.preMultiply(asBigDecimal(testVector));
            fail("expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }
    }
    
    public void testPremultiply() {
        BigMatrix m3 = new BigMatrixImpl(d3);
        BigMatrix m4 = new BigMatrixImpl(d4);
        BigMatrix m5 = new BigMatrixImpl(d5);
        assertClose("m3*m4=m5", m4.preMultiply(m3), m5, entryTolerance);
        
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrixImpl mInv = new BigMatrixImpl(testDataInv);
        BigMatrixImpl identity = new BigMatrixImpl(id);
        BigMatrixImpl m2 = new BigMatrixImpl(testData2);
        assertClose("inverse multiply",m.preMultiply(mInv),
                identity,entryTolerance);
        assertClose("inverse multiply",mInv.preMultiply(m),
                identity,entryTolerance);
        assertClose("identity multiply",m.preMultiply(identity),
                m,entryTolerance);
        assertClose("identity multiply",identity.preMultiply(mInv),
                mInv,entryTolerance);
        try {
            BigMatrix a = m.preMultiply(new BigMatrixImpl(bigSingular));
            fail("Expecting illegalArgumentException");
        } catch (IllegalArgumentException ex) {
            ;
        }      
    }
    
    public void testGetVectors() {
        BigMatrix m = new BigMatrixImpl(testData);
        assertClose("get row",m.getRowAsDoubleArray(0),testDataRow1,entryTolerance);
        assertClose("get col",m.getColumnAsDoubleArray(2),testDataCol3,entryTolerance);
        try {
            double[] x = m.getRowAsDoubleArray(10);
            fail("expecting MatrixIndexException");
        } catch (MatrixIndexException ex) {
            ;
        }
        try {
            double[] x = m.getColumnAsDoubleArray(-1);
            fail("expecting MatrixIndexException");
        } catch (MatrixIndexException ex) {
            ;
        }
    }
    
    public void testEntryMutators() {
        BigMatrix m = new BigMatrixImpl(testData);
        assertEquals("get entry",m.getEntry(0,1).doubleValue(),2d,entryTolerance);
        m.setEntry(0,1,100d);
        assertEquals("get entry",m.getEntry(0,1).doubleValue(),100d,entryTolerance);
        try {
            double x = m.getEntry(-1,2).doubleValue();
            fail("expecting MatrixIndexException");
        } catch (MatrixIndexException ex) {
            ;
        }
        try {
            m.setEntry(1,3,200d);
            fail("expecting MatrixIndexException");
        } catch (MatrixIndexException ex) {
            ;
        }
        m.setEntry(1, 2, "0.1");
        m.setEntry(1, 1, 0.1d);
        assertFalse(m.getEntry(1, 2).equals(m.getEntry(1, 1)));
        assertTrue(m.getEntry(1, 2).equals(new BigDecimal("0.1")));
        try {
            m.setEntry(1, 2, "not a number");
            fail("Expecting NumberFormatException");
        } catch (NumberFormatException ex) {
            ;
        }     
    }
        
    public void testLUDecomposition() throws Exception {
        BigMatrixImpl m = new BigMatrixImpl(testData);
        BigMatrix lu = m.getLUMatrix();
        assertClose("LU decomposition", lu, (BigMatrix) new BigMatrixImpl(testDataLU), normTolerance);
        verifyDecomposition(m, lu);
        m = new BigMatrixImpl(luData);
        lu = m.getLUMatrix();
        assertClose("LU decomposition", lu, (BigMatrix) new BigMatrixImpl(luDataLUDecomposition), normTolerance);
        verifyDecomposition(m, lu);
        m = new BigMatrixImpl(testDataMinus);
        lu = m.getLUMatrix();
        verifyDecomposition(m, lu);
        m = new BigMatrixImpl(id);
        lu = m.getLUMatrix();
        verifyDecomposition(m, lu);
        try {
            m = new BigMatrixImpl(bigSingular); // singular
            lu = m.getLUMatrix();
            fail("Expecting InvalidMatrixException");
        } catch (InvalidMatrixException ex) {
            // expected
        }
        try {
            m = new BigMatrixImpl(testData2);  // not square
            lu = m.getLUMatrix();
            fail("Expecting InvalidMatrixException");
        } catch (InvalidMatrixException ex) {
            // expected
        }
    }
    
    //--------------- -----------------Protected methods
        
    /** verifies that two matrices are close (1-norm) */              
    protected void assertClose(String msg, BigMatrix m, BigMatrix n,
        double tolerance) {
        assertTrue(msg,m.subtract(n).getNorm().doubleValue() < tolerance);
    }
    
    /** verifies that two vectors are close (sup norm) */
    protected void assertClose(String msg, double[] m, double[] n,
        double tolerance) {
        if (m.length != n.length) {
            fail("vectors not same length");
        }
        for (int i = 0; i < m.length; i++) {
            assertEquals(msg + " " +  i + " elements differ", 
                m[i],n[i],tolerance);
        }
    }
    
    /** extracts the l  and u matrices from compact lu representation */
    protected void splitLU(BigMatrix lu, BigMatrix lower, BigMatrix upper) throws InvalidMatrixException {
        if (!lu.isSquare() || !lower.isSquare() || !upper.isSquare() ||
                lower.getRowDimension() != upper.getRowDimension() 
                || lower.getRowDimension() != lu.getRowDimension()) {
            throw new InvalidMatrixException("incorrect dimensions");
        }    
        int n = lu.getRowDimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j < i) {
                    lower.setEntry(i, j, lu.getEntry(i, j));
                    upper.setEntry(i, j, 0d);
                } else if (i == j) {
                    lower.setEntry(i, j, 1d);
                    upper.setEntry(i, j, lu.getEntry(i, j));
                } else {
                    lower.setEntry(i, j, 0d);
                    upper.setEntry(i, j, lu.getEntry(i, j));
                }   
            }
        }
    }
    
    /** Returns the result of applying the given row permutation to the matrix */
    protected BigMatrix permuteRows(BigMatrix matrix, int[] permutation) {
        if (!matrix.isSquare() || matrix.getRowDimension() != permutation.length) {
            throw new IllegalArgumentException("dimension mismatch");
        }
        int n = matrix.getRowDimension();
        BigMatrix out = new BigMatrixImpl(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.setEntry(i, j, matrix.getEntry(permutation[i], j));
            }
        }
        return out;
    }
    
    /** Extracts l and u matrices from lu and verifies that matrix = l times u modulo permutation */
    protected void verifyDecomposition(BigMatrix matrix, BigMatrix lu) throws Exception{
        int n = matrix.getRowDimension();
        BigMatrix lower = new BigMatrixImpl(n, n);
        BigMatrix upper = new BigMatrixImpl(n, n);
        splitLU(lu, lower, upper);
        int[] permutation = ((BigMatrixImpl) matrix).getPermutation();
        BigMatrix permuted = permuteRows(matrix, permutation);
        assertClose("lu decomposition does not work", permuted, lower.multiply(upper), normTolerance);
    }
      
    
    /** Useful for debugging */
    private void dumpMatrix(BigMatrix m) {
          for (int i = 0; i < m.getRowDimension(); i++) {
              String os = "";
              for (int j = 0; j < m.getColumnDimension(); j++) {
                  os += m.getEntry(i, j) + " ";
              }
              System.out.println(os);
          }
    }
        
}
