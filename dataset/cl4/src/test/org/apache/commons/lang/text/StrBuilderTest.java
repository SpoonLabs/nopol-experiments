/*
 * Copyright 2005 The Apache Software Foundation.
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

package org.apache.commons.lang.text;

import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.ArrayUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Unit tests for {@link org.apache.commons.lang.text.StrBuilder}.
 * 
 * @author Michael Heuer
 * @version $Id$
 */
public class StrBuilderTest extends TestCase {

    /** Test subclass of Object, with a toString method. */
    private static Object FOO = new Object() {
        public String toString() {
            return "foo";
        }
    };

    /**
     * Main method.
     * 
     * @param args  command line arguments, ignored
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Return a new test suite containing this test case.
     * 
     * @return a new test suite containing this test case
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(StrBuilderTest.class);
        suite.setName("StrBuilder Tests");
        return suite;
    }

    /**
     * Create a new test case with the specified name.
     * 
     * @param name
     *            name
     */
    public StrBuilderTest(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    public void testConstructors() {
        StrBuilder sb0 = new StrBuilder();
        assertEquals(32, sb0.capacity());
        assertEquals(0, sb0.length());
        assertEquals(0, sb0.size());

        StrBuilder sb1 = new StrBuilder(32);
        assertEquals(32, sb1.capacity());
        assertEquals(0, sb1.length());
        assertEquals(0, sb1.size());

        StrBuilder sb2 = new StrBuilder(0);
        assertEquals(32, sb2.capacity());
        assertEquals(0, sb2.length());
        assertEquals(0, sb2.size());

        StrBuilder sb3 = new StrBuilder(-1);
        assertEquals(32, sb3.capacity());
        assertEquals(0, sb3.length());
        assertEquals(0, sb3.size());

        StrBuilder sb4 = new StrBuilder(1);
        assertEquals(1, sb4.capacity());
        assertEquals(0, sb4.length());
        assertEquals(0, sb4.size());

        StrBuilder sb5 = new StrBuilder((String) null);
        assertEquals(32, sb5.capacity());
        assertEquals(0, sb5.length());
        assertEquals(0, sb5.size());

        StrBuilder sb6 = new StrBuilder("");
        assertEquals(32, sb6.capacity());
        assertEquals(0, sb6.length());
        assertEquals(0, sb6.size());

        StrBuilder sb7 = new StrBuilder("foo");
        assertEquals(35, sb7.capacity());
        assertEquals(3, sb7.length());
        assertEquals(3, sb7.size());
    }

    public void testDeleteChar() {
        StrBuilder sb = new StrBuilder("abc");
        sb.delete('X');
        assertEquals("abc",sb.toString()); 
        sb.delete('a');
        assertEquals("bc",sb.toString()); 
        sb.delete('c');
        assertEquals("b",sb.toString()); 
        sb.delete('b');
        assertEquals("",sb.toString()); 
    }
    
    public void testDeleteIntInt() {
        StrBuilder sb = new StrBuilder("abc");
        sb.delete(0, 1);
        assertEquals("bc",sb.toString()); 
        sb.delete(1, 2);
        assertEquals("b",sb.toString());
        sb.delete(0, 1);
        assertEquals("",sb.toString()); 
        sb.delete(0, 1);
        assertEquals("",sb.toString()); 
    }
    
    public void testDeleteString() {
        StrBuilder sb = new StrBuilder("abc");
        sb.delete(null);
        assertEquals("abc",sb.toString()); 
        sb.delete("");
        assertEquals("abc",sb.toString()); 
        sb.delete("X");
        assertEquals("abc",sb.toString()); 
        sb.delete("a");
        assertEquals("bc",sb.toString()); 
        sb.delete("c");
        assertEquals("b",sb.toString()); 
        sb.delete("b");
        assertEquals("",sb.toString()); 
    }
    
    public void testDeleteCharAt() {
        StrBuilder sb = new StrBuilder("abc");
        sb.deleteCharAt(0);
        assertEquals("bc",sb.toString()); 
    }
    
    public void testDeleteCharAtExceptions() {
        StrBuilder sb = new StrBuilder("abc");
        try {
            sb.deleteCharAt(1000);
            fail("Expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
    
    //-----------------------------------------------------------------------
    public void testCapacityAndLength() {
        StrBuilder sb = new StrBuilder();
        assertEquals(32, sb.capacity());
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());

        sb.minimizeCapacity();
        assertEquals(0, sb.capacity());
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());

        sb.ensureCapacity(32);
        assertTrue(sb.capacity() >= 32);
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());

        sb.append("foo");
        assertTrue(sb.capacity() >= 32);
        assertEquals(3, sb.length());
        assertEquals(3, sb.size());
        assertTrue(sb.isEmpty() == false);

        sb.clear();
        assertTrue(sb.capacity() >= 32);
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());

        sb.append("123456789012345678901234567890123");
        assertTrue(sb.capacity() > 32);
        assertEquals(33, sb.length());
        assertEquals(33, sb.size());
        assertTrue(sb.isEmpty() == false);

        sb.ensureCapacity(16);
        assertTrue(sb.capacity() > 16);
        assertEquals(33, sb.length());
        assertEquals(33, sb.size());
        assertTrue(sb.isEmpty() == false);

        sb.minimizeCapacity();
        assertEquals(33, sb.capacity());
        assertEquals(33, sb.length());
        assertEquals(33, sb.size());
        assertTrue(sb.isEmpty() == false);

        try {
            sb.setLength(-1);
            fail("setLength(-1) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.setLength(33);
        assertEquals(33, sb.capacity());
        assertEquals(33, sb.length());
        assertEquals(33, sb.size());
        assertTrue(sb.isEmpty() == false);

        sb.setLength(16);
        assertTrue(sb.capacity() >= 16);
        assertEquals(16, sb.length());
        assertEquals(16, sb.size());
        assertEquals("1234567890123456", sb.toString());
        assertTrue(sb.isEmpty() == false);

        sb.setLength(32);
        assertTrue(sb.capacity() >= 32);
        assertEquals(32, sb.length());
        assertEquals(32, sb.size());
        assertEquals("1234567890123456\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0", sb.toString());
        assertTrue(sb.isEmpty() == false);

        sb.setLength(0);
        assertTrue(sb.capacity() >= 32);
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());
    }

    //-----------------------------------------------------------------------
    public void testLength() {
        StrBuilder sb = new StrBuilder();
        assertEquals(0, sb.length());
        
        sb.append("Hello");
        assertEquals(5, sb.length());
    }

    public void testSetLength() {
        StrBuilder sb = new StrBuilder();
        sb.append("Hello");
        sb.setLength(2);  // shorten
        assertEquals("He", sb.toString());
        sb.setLength(2);  // no change
        assertEquals("He", sb.toString());
        sb.setLength(3);  // lengthen
        assertEquals("He\0", sb.toString());

        try {
            sb.setLength(-1);
            fail("setLength(-1) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    public void testCapacity() {
        StrBuilder sb = new StrBuilder();
        assertEquals(sb.buffer.length, sb.capacity());
        
        sb.append("HelloWorldHelloWorldHelloWorldHelloWorld");
        assertEquals(sb.buffer.length, sb.capacity());
    }

    public void testEnsureCapacity() {
        StrBuilder sb = new StrBuilder();
        sb.ensureCapacity(2);
        assertEquals(true, sb.capacity() >= 2);
        
        sb.ensureCapacity(-1);
        assertEquals(true, sb.capacity() >= 0);
        
        sb.append("HelloWorld");
        sb.ensureCapacity(40);
        assertEquals(true, sb.capacity() >= 40);
    }

    public void testMinimizeCapacity() {
        StrBuilder sb = new StrBuilder();
        sb.minimizeCapacity();
        assertEquals(0, sb.capacity());
        
        sb.append("HelloWorld");
        sb.minimizeCapacity();
        assertEquals(10, sb.capacity());
    }

    //-----------------------------------------------------------------------
    public void testSize() {
        StrBuilder sb = new StrBuilder();
        assertEquals(0, sb.size());
        
        sb.append("Hello");
        assertEquals(5, sb.size());
    }

    public void testIsEmpty() {
        StrBuilder sb = new StrBuilder();
        assertEquals(true, sb.isEmpty());
        
        sb.append("Hello");
        assertEquals(false, sb.isEmpty());
        
        sb.clear();
        assertEquals(true, sb.isEmpty());
    }

    public void testClear() {
        StrBuilder sb = new StrBuilder();
        sb.append("Hello");
        sb.clear();
        assertEquals(0, sb.length());
        assertEquals(true, sb.buffer.length >= 5);
    }

    //-----------------------------------------------------------------------
    public void testCharAt() {
        StrBuilder sb = new StrBuilder();
        try {
            sb.charAt(0);
            fail("charAt(0) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            sb.charAt(-1);
            fail("charAt(-1) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        sb.append("foo");
        assertEquals('f', sb.charAt(0));
        assertEquals('o', sb.charAt(1));
        assertEquals('o', sb.charAt(2));
        try {
            sb.charAt(-1);
            fail("charAt(-1) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            sb.charAt(3);
            fail("charAt(3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     * Tests {@link StrBuilder#replace(char, char)}.
     */
    public void testReplaceCharChar() {
        StrBuilder sb = new StrBuilder("abc");
        sb.replace('a', 'd');
        assertEquals("dbc", sb.toString());
        sb.replace('a', 'd');
        assertEquals("dbc", sb.toString());
        
        sb = new StrBuilder("aabbcc");
        sb.replace('a', 'd');
        assertEquals("ddbbcc", sb.toString());
        sb.replace('a', 'd');
        assertEquals("ddbbcc", sb.toString());
        sb.replace('d', 'd');
        assertEquals("ddbbcc", sb.toString());
    }
    
    /**
     * Tests {@link StrBuilder#replace(String, String)}.
     */
    public void testReplaceStringString() {
        StrBuilder sb = new StrBuilder("abc");
        sb.replace("a", "d");
        assertEquals("dbc", sb.toString());
        sb.replace("a", "d");
        assertEquals("dbc", sb.toString());
        
        sb = new StrBuilder("aabbcc");
        sb.replace("a", "d");
        assertEquals("ddbbcc", sb.toString());
        sb.replace("a", "d");
        assertEquals("ddbbcc", sb.toString());
    }
    
    public void testReplaceIntIntStrBuilder() {
        StrBuilder sb = new StrBuilder("abc");
        sb.replace(0, 1, new StrBuilder ("d"));
        assertEquals("dbc", sb.toString());
        sb.replace(0, 1, new StrBuilder ("aaa"));
        assertEquals("aaabc", sb.toString());
        
        sb = new StrBuilder("aabbcc");
        sb.replace(0, 2, new StrBuilder("d"));
        assertEquals("dbbcc", sb.toString());
    }
    
    public void testSetCharAt() {
        StrBuilder sb = new StrBuilder();
        try {
            sb.setCharAt(0, 'f');
            fail("setCharAt(0,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            sb.setCharAt(-1, 'f');
            fail("setCharAt(-1,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        sb.append("foo");
        sb.setCharAt(0, 'b');
        sb.setCharAt(1, 'a');
        sb.setCharAt(2, 'r');
        try {
            sb.setCharAt(3, '!');
            fail("setCharAt(3,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        assertEquals("bar", sb.toString());
    }

    public void testStartsWith() {
        this.testStartsWith(new StrBuilder());
        this.testStartsWith(new StrBuilder(""));
        this.testStartsWith(new StrBuilder(null));
    }
    
    void testStartsWith(StrBuilder sb ) {
        assertFalse(sb.startsWith("a"));
        assertFalse(sb.startsWith(null));
        assertTrue(sb.startsWith(""));
        sb.append("abc");
        assertTrue(sb.startsWith("a"));
        assertTrue(sb.startsWith("ab"));
        assertTrue(sb.startsWith("abc"));
        assertFalse(sb.startsWith("cba"));
    }
    
    public void testEndsWith() {
        this.testEndsWith(new StrBuilder());
        this.testEndsWith(new StrBuilder(""));
        this.testEndsWith(new StrBuilder(null));
    }
    
    void testEndsWith(StrBuilder sb) {
        assertFalse(sb.endsWith("a"));
        assertFalse(sb.endsWith("c"));
        assertTrue(sb.endsWith(""));
        assertFalse(sb.endsWith(null));
        sb.append("abc");
        assertTrue(sb.endsWith("c"));
        assertTrue(sb.endsWith("bc"));
        assertTrue(sb.endsWith("abc"));
        assertFalse(sb.endsWith("cba"));
        assertFalse(sb.endsWith("abcd"));
        assertFalse(sb.endsWith(" abc"));
        assertFalse(sb.endsWith("abc "));
    }
    
    //-----------------------------------------------------------------------
    public void testNullText() {
        StrBuilder sb = new StrBuilder();
        assertEquals(null, sb.getNullText());

        sb.setNullText("null");
        assertEquals("null", sb.getNullText());

        sb.setNullText("");
        assertEquals(null, sb.getNullText());

        sb.setNullText("NULL");
        assertEquals("NULL", sb.getNullText());

        sb.setNullText((String) null);
        assertEquals(null, sb.getNullText());
    }

    //-----------------------------------------------------------------------
    public void testAppendWithNullText() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL");
        assertEquals("", sb.toString());

        sb.appendNull();
        assertEquals("NULL", sb.toString());

        sb.append((Object) null);
        assertEquals("NULLNULL", sb.toString());

        sb.append(FOO);
        assertEquals("NULLNULLfoo", sb.toString());

        sb.append((String) null);
        assertEquals("NULLNULLfooNULL", sb.toString());

        sb.append("");
        assertEquals("NULLNULLfooNULL", sb.toString());

        sb.append("bar");
        assertEquals("NULLNULLfooNULLbar", sb.toString());

        sb.append((StringBuffer) null);
        assertEquals("NULLNULLfooNULLbarNULL", sb.toString());

        sb.append(new StringBuffer("baz"));
        assertEquals("NULLNULLfooNULLbarNULLbaz", sb.toString());
    }

    //-----------------------------------------------------------------------
    public void testAppend_Object() {
        StrBuilder sb = new StrBuilder();
        sb.appendNull();
        assertEquals("", sb.toString());

        sb.append((Object) null);
        assertEquals("", sb.toString());

        sb.append(FOO);
        assertEquals("foo", sb.toString());

        sb.append((StringBuffer) null);
        assertEquals("foo", sb.toString());

        sb.append(new StringBuffer("baz"));
        assertEquals("foobaz", sb.toString());

        sb.append(new StrBuilder("yes"));
        assertEquals("foobazyes", sb.toString());
    }

    public void testAppend_String() {
        StrBuilder sb = new StrBuilder();

        sb.append("foo");
        assertEquals("foo", sb.toString());

        sb.append((String) null);
        assertEquals("foo", sb.toString());

        sb.append("");
        assertEquals("foo", sb.toString());

        sb.append("bar");
        assertEquals("foobar", sb.toString());
    }

    public void testAppend_String_int_int() {
        StrBuilder sb = new StrBuilder();
        
        sb.append("foo", 0, 3);
        assertEquals("foo", sb.toString());

        sb.append((String) null, 0, 1);
        assertEquals("foo", sb.toString());

        try {
            sb.append("bar", -1, 1);
            fail("append(char[], -1,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", 3, 1);
            fail("append(char[], 3,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", 1, -1);
            fail("append(char[],, -1) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", 1, 3);
            fail("append(char[], 1, 3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", -1, 3);
            fail("append(char[], -1, 3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", 4, 0);
            fail("append(char[], 4, 0) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.append("bar", 3, 0);
        assertEquals("foo", sb.toString());

        sb.append("abcbardef", 3, 3);
        assertEquals("foobar", sb.toString());
    }

    public void testAppend_StringBuffer() {
        StrBuilder sb = new StrBuilder();

        sb.append(new StringBuffer("foo"));
        assertEquals("foo", sb.toString());

        sb.append((StringBuffer) null);
        assertEquals("foo", sb.toString());

        sb.append(new StringBuffer(""));
        assertEquals("foo", sb.toString());

        sb.append(new StringBuffer("bar"));
        assertEquals("foobar", sb.toString());
    }

    public void testAppend_StringBuffer_int_int() {
        StrBuilder sb = new StrBuilder();
        
        sb.append(new StringBuffer("foo"), 0, 3);
        assertEquals("foo", sb.toString());

        sb.append((StringBuffer) null, 0, 1);
        assertEquals("foo", sb.toString());

        try {
            sb.append(new StringBuffer("bar"), -1, 1);
            fail("append(char[], -1,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), 3, 1);
            fail("append(char[], 3,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), 1, -1);
            fail("append(char[],, -1) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), 1, 3);
            fail("append(char[], 1, 3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), -1, 3);
            fail("append(char[], -1, 3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), 4, 0);
            fail("append(char[], 4, 0) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.append(new StringBuffer("bar"), 3, 0);
        assertEquals("foo", sb.toString());

        sb.append(new StringBuffer("abcbardef"), 3, 3);
        assertEquals("foobar", sb.toString());
    }

    public void testAppend_StrBuilder() {
        StrBuilder sb = new StrBuilder();

        sb.append(new StrBuilder("foo"));
        assertEquals("foo", sb.toString());

        sb.append((StrBuilder) null);
        assertEquals("foo", sb.toString());

        sb.append(new StrBuilder(""));
        assertEquals("foo", sb.toString());

        sb.append(new StrBuilder("bar"));
        assertEquals("foobar", sb.toString());
    }

    public void testAppend_StrBuilder_int_int() {
        StrBuilder sb = new StrBuilder();
        
        sb.append(new StrBuilder("foo"), 0, 3);
        assertEquals("foo", sb.toString());

        sb.append((StrBuilder) null, 0, 1);
        assertEquals("foo", sb.toString());

        try {
            sb.append(new StrBuilder("bar"), -1, 1);
            fail("append(char[], -1,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), 3, 1);
            fail("append(char[], 3,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), 1, -1);
            fail("append(char[],, -1) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), 1, 3);
            fail("append(char[], 1, 3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), -1, 3);
            fail("append(char[], -1, 3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), 4, 0);
            fail("append(char[], 4, 0) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.append(new StrBuilder("bar"), 3, 0);
        assertEquals("foo", sb.toString());

        sb.append(new StrBuilder("abcbardef"), 3, 3);
        assertEquals("foobar", sb.toString());
    }

    public void testAppend_CharArray() {
        StrBuilder sb = new StrBuilder();
        
        sb.append((char[]) null);
        assertEquals("", sb.toString());

        sb.append(new char[0]);
        assertEquals("", sb.toString());

        sb.append(new char[]{'f', 'o', 'o'});
        assertEquals("foo", sb.toString());
    }

    public void testAppend_CharArray_int_int() {
        StrBuilder sb = new StrBuilder();
        
        sb.append(new char[]{'f', 'o', 'o'}, 0, 3);
        assertEquals("foo", sb.toString());

        sb.append((char[]) null, 0, 1);
        assertEquals("foo", sb.toString());

        try {
            sb.append(new char[]{'b', 'a', 'r'}, -1, 1);
            fail("append(char[], -1,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, 3, 1);
            fail("append(char[], 3,) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, 1, -1);
            fail("append(char[],, -1) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, 1, 3);
            fail("append(char[], 1, 3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, -1, 3);
            fail("append(char[], -1, 3) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, 4, 0);
            fail("append(char[], 4, 0) expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.append(new char[]{'b', 'a', 'r'}, 3, 0);
        assertEquals("foo", sb.toString());

        sb.append(new char[]{'a', 'b', 'c', 'b', 'a', 'r', 'd', 'e', 'f'}, 3, 3);
        assertEquals("foobar", sb.toString());
    }

    public void testAppend_Primitive() {
        StrBuilder sb = new StrBuilder();
        sb.append(true);
        assertEquals("true", sb.toString());

        sb.append(false);
        assertEquals("truefalse", sb.toString());

        sb.append('!');
        assertEquals("truefalse!", sb.toString());
    }

    public void testAppend_PrimitiveNumber() {
        StrBuilder sb = new StrBuilder();
        sb.append(0);
        assertEquals("0", sb.toString());

        sb.append(1L);
        assertEquals("01", sb.toString());

        sb.append(2.3f);
        assertEquals("012.3", sb.toString());

        sb.append(4.5d);
        assertEquals("012.34.5", sb.toString());
    }

    //-----------------------------------------------------------------------
    public void testAppendPadding() {
        StrBuilder sb = new StrBuilder();
        sb.append("foo");
        assertEquals("foo", sb.toString());

        sb.appendPadding(-1, '-');
        assertEquals("foo", sb.toString());

        sb.appendPadding(0, '-');
        assertEquals("foo", sb.toString());

        sb.appendPadding(1, '-');
        assertEquals("foo-", sb.toString());

        sb.appendPadding(16, '-');
        assertEquals(20, sb.length());
        //            12345678901234567890
        assertEquals("foo-----------------", sb.toString());
    }

    //-----------------------------------------------------------------------
    public void testAppendFixedWidthPadLeft() {
        StrBuilder sb = new StrBuilder();
        sb.appendFixedWidthPadLeft("foo", -1, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 0, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 1, '-');
        assertEquals("o", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 2, '-');
        assertEquals("oo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 3, '-');
        assertEquals("foo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 4, '-');
        assertEquals("-foo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 10, '-');
        assertEquals(10, sb.length());
        //            1234567890
        assertEquals("-------foo", sb.toString());

        sb.clear();
        sb.setNullText("null");
        sb.appendFixedWidthPadLeft(null, 5, '-');
        assertEquals("-null", sb.toString());
    }

    public void testAppendFixedWidthPadLeft_int() {
        StrBuilder sb = new StrBuilder();
        sb.appendFixedWidthPadLeft(123, -1, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 0, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 1, '-');
        assertEquals("3", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 2, '-');
        assertEquals("23", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 3, '-');
        assertEquals("123", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 4, '-');
        assertEquals("-123", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 10, '-');
        assertEquals(10, sb.length());
        //            1234567890
        assertEquals("-------123", sb.toString());
    }

    //-----------------------------------------------------------------------
    public void testAppendFixedWidthPadRight() {
        StrBuilder sb = new StrBuilder();
        sb.appendFixedWidthPadRight("foo", -1, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 0, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 1, '-');
        assertEquals("f", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 2, '-');
        assertEquals("fo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 3, '-');
        assertEquals("foo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 4, '-');
        assertEquals("foo-", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 10, '-');
        assertEquals(10, sb.length());
        //            1234567890
        assertEquals("foo-------", sb.toString());

        sb.clear();
        sb.setNullText("null");
        sb.appendFixedWidthPadRight(null, 5, '-');
        assertEquals("null-", sb.toString());
    }

    public void testAppendFixedWidthPadRight_int() {
        StrBuilder sb = new StrBuilder();
        sb.appendFixedWidthPadRight(123, -1, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 0, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 1, '-');
        assertEquals("1", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 2, '-');
        assertEquals("12", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 3, '-');
        assertEquals("123", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 4, '-');
        assertEquals("123-", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 10, '-');
        assertEquals(10, sb.length());
        //            1234567890
        assertEquals("123-------", sb.toString());
    }

    //-----------------------------------------------------------------------
    public void testAppendWithSeparators_Array() {
        StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators((Object[]) null, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(new Object[0], ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(new Object[]{"foo", "bar", "baz"}, ",");
        assertEquals("foo,bar,baz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(new Object[]{"foo", "bar", "baz"}, null);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(new Object[]{"foo", null, "baz"}, ",");
        assertEquals("foo,,baz", sb.toString());
    }

    public void testAppendWithSeparators_Collection() {
        StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators((Collection) null, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Collections.EMPTY_LIST, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", "bar", "baz"}), ",");
        assertEquals("foo,bar,baz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", "bar", "baz"}), null);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", null, "baz"}), ",");
        assertEquals("foo,,baz", sb.toString());
    }

    public void testAppendWithSeparators_Iterator() {
        StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators((Iterator) null, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Collections.EMPTY_LIST.iterator(), ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", "bar", "baz"}).iterator(), ",");
        assertEquals("foo,bar,baz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", "bar", "baz"}).iterator(), null);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", null, "baz"}).iterator(), ",");
        assertEquals("foo,,baz", sb.toString());
    }

    public void testAppendWithSeparatorsWithNullText() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("null");
        sb.appendWithSeparators(new Object[]{"foo", null, "baz"}, ",");
        assertEquals("foo,null,baz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", null, "baz"}), ",");
        assertEquals("foo,null,baz", sb.toString());
    }

    public void testInsert() {

        StrBuilder sb = new StrBuilder();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, FOO);
            fail("insert(-1, Object) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, FOO);
            fail("insert(7, Object) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (Object) null);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, FOO);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, "foo");
            fail("insert(-1, String) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, "foo");
            fail("insert(7, String) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (String) null);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, "foo");
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, new char[]{'f', 'o', 'o'});
            fail("insert(-1, char[]) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, new char[]{'f', 'o', 'o'});
            fail("insert(7, char[]) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (char[]) null);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, new char[0]);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, new char[]{'f', 'o', 'o'});
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3);
            fail("insert(-1, char[], 3, 3) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3);
            fail("insert(7, char[], 3, 3) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (char[]) null, 0, 0);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, new char[0], 0, 0);
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, -1, 3);
            fail("insert(0, char[], -1, 3) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 10, 3);
            fail("insert(0, char[], 10, 3) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, -1);
            fail("insert(0, char[], 0, -1) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, 10);
            fail("insert(0, char[], 0, 10) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, 0);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, true);
            fail("insert(-1, boolean) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, true);
            fail("insert(7, boolean) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, true);
        assertEquals("truebarbaz", sb.toString());

        sb.insert(0, false);
        assertEquals("falsetruebarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, '!');
            fail("insert(-1, char) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, '!');
            fail("insert(7, char) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, '!');
        assertEquals("!barbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, 0);
            fail("insert(-1, int) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, 0);
            fail("insert(7, int) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, '0');
        assertEquals("0barbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, 1L);
            fail("insert(-1, long) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, 1L);
            fail("insert(7, long) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, 1L);
        assertEquals("1barbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, 2.3F);
            fail("insert(-1, float) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, 2.3F);
            fail("insert(7, float) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, 2.3F);
        assertEquals("2.3barbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, 4.5D);
            fail("insert(-1, double) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, 4.5D);
            fail("insert(7, double) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, 4.5D);
        assertEquals("4.5barbaz", sb.toString());
    }

    public void testInsertWithNullText() {

        StrBuilder sb = new StrBuilder();
        sb.setNullText("null");
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, FOO);
            fail("insert(-1, Object) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, FOO);
            fail("insert(7, Object) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (Object) null);
        assertEquals("nullbarbaz", sb.toString());

        sb.insert(0, FOO);
        assertEquals("foonullbarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        try {
            sb.insert(-1, "foo");
            fail("insert(-1, String) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, "foo");
            fail("insert(7, String) expected StringIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (String) null);
        assertEquals("nullbarbaz", sb.toString());

        sb.insert(0, "foo");
        assertEquals("foonullbarbaz", sb.toString());
    }
    
    public void testToCharArray ( ) {
        
        StrBuilder sb = new StrBuilder();        
        assertEquals(ArrayUtils.EMPTY_CHAR_ARRAY, sb.toCharArray());
        
        char[] a = sb.toCharArray();
        assertNotNull ("toCharArray() result is null", a);
        assertEquals ("toCharArray() result is too large", 0, a.length);
        
        sb.append("junit");
        a = sb.toCharArray();
        assertEquals ("toCharArray() result incorrect length",5, a.length);
        assertTrue ("toCharArray() result does not match",Arrays.equals("junit".toCharArray(), a));
    }
    
    public void testToCharArrayIntInt() {
        StrBuilder sb = new StrBuilder();
        assertEquals(ArrayUtils.EMPTY_CHAR_ARRAY, sb.toCharArray(0, 0));

        sb.append("junit");
        char[] a = sb.toCharArray(0, 20); //too large test
        assertEquals ("toCharArray(int,int) result incorrect length",5, a.length);
        assertTrue ("toCharArray(int,int) result does not match",Arrays.equals("junit".toCharArray(), a));
        
        a = sb.toCharArray(0, 4);
        assertEquals ("toCharArray(int,int) result incorrect length",4, a.length);
        assertTrue ("toCharArray(int,int) result does not match",Arrays.equals("juni".toCharArray(), a));
        
        a = sb.toCharArray(0, 4);
        assertEquals ("toCharArray(int,int) result incorrect length",4, a.length);
        assertTrue ("toCharArray(int,int) result does not match",Arrays.equals("juni".toCharArray(), a));
        
        a = sb.toCharArray(0,1);
        assertNotNull ("toCharArray(int,int) result is null", a);
        
        try {
            sb.toCharArray(-1, 5);
            fail ("no string index out of bound on -1");
        }
        catch (IndexOutOfBoundsException e) {}

        try {
            sb.toCharArray(6, 5);
            fail ("no string index out of bound on -1");
        }
        catch (IndexOutOfBoundsException e) {}
    }
    
    public void testGetChars ( ) {
        
        StrBuilder sb = new StrBuilder();
        
        char[] input = new char[10];
        char[] a = sb.getChars(input);
        assertSame (input, a);
        assertTrue(Arrays.equals(new char[10], a));
        
        sb.append("junit");
        a = sb.getChars(input);
        assertSame(input, a);
        assertTrue(Arrays.equals(new char[] {'j','u','n','i','t',0,0,0,0,0},a));
        
        a = sb.getChars(null);
        assertNotSame(input,a);
        assertEquals(5,a.length);
        assertTrue(Arrays.equals("junit".toCharArray(),a));
        
        input = new char[5];
        a = sb.getChars(input);
        assertSame(input, a);
        
        input = new char[4];
        a = sb.getChars(input);
        assertNotSame(input, a);
    }
    
    public void testGetCharsIntIntCharArrayInt( ) {
        
        StrBuilder sb = new StrBuilder();
               
        sb.append("junit");
        char[] a = new char[5];
        sb.getChars(0,5,a,0);
        assertTrue(Arrays.equals(new char[] {'j','u','n','i','t'},a));
        
        a = new char[5];
        sb.getChars(0,2,a,3);
        assertTrue(Arrays.equals(new char[] {0,0,0,'j','u'},a));
        
        try {
            sb.getChars(-1,0,a,0);
            fail("no exception");
        }
        catch (IndexOutOfBoundsException e) {
        }
        
        try {
            sb.getChars(0,-1,a,0);
            fail("no exception");
        }
        catch (IndexOutOfBoundsException e) {
        }
        
        try {
            sb.getChars(0,20,a,0);
            fail("no exception");
        }
        catch (IndexOutOfBoundsException e) {
        }
        
        try {
            sb.getChars(4,2,a,0);
            fail("no exception");
        }
        catch (IndexOutOfBoundsException e) {
        }
    }
    
    public void testAppendStringBuffer() {
        StrBuilder sb = new StrBuilder();
        
        sb = sb.append(new StringBuffer());
        assertNotNull(sb);
        
        sb = sb.append(new StringBuffer("junit"));
        
        assertEquals ("junit", sb.toString());
    }
    
    public void testAppendStrBuilder() {
        StrBuilder sb = new StrBuilder();
        
        sb = sb.append((StrBuilder)null);
        assertNotNull(sb);
        
        sb = sb.append(new StrBuilder());
        assertNotNull(sb);
        assertEquals("", sb.toString());
    }
    
    public void testStringBuffer() {
        StrBuilder sb = new StrBuilder();
        assertEquals (new StringBuffer().toString(), sb.toStringBuffer().toString());
        
        sb.append("junit");
        assertEquals(new StringBuffer("junit").toString(), sb.toStringBuffer().toString());
    }
    
    public void testReverse() {
        StrBuilder sb = new StrBuilder();
        
        String actual = sb.reverse().toString();
        assertEquals ("", actual);
        
        sb.append(true);
        actual = sb.reverse().toString();
        assertEquals("eurt", actual);
        
        actual = sb.reverse().toString();
        assertEquals("true", actual);
    }
    
    public void testIndexOfChar() {
        StrBuilder sb = new StrBuilder("abab");
        
        assertEquals (0, sb.indexOf('a'));
        //should work like String#indexOf
        assertEquals ("abab".indexOf('a'), sb.indexOf('a'));
        
        assertEquals(1, sb.indexOf('b'));
        assertEquals ("abab".indexOf('b'), sb.indexOf('b'));
        
        assertEquals (-1, sb.indexOf('z'));
    }
    
    public void testLastIndexOfChar() {
        StrBuilder sb = new StrBuilder("abab");
        
        assertEquals (2, sb.lastIndexOf('a'));
        //should work like String#lastIndexOf
        assertEquals ("abab".lastIndexOf('a'), sb.lastIndexOf('a'));
        
        assertEquals(3, sb.lastIndexOf('b'));
        assertEquals ("abab".lastIndexOf('b'), sb.lastIndexOf('b'));
        
        assertEquals (-1, sb.lastIndexOf('z'));
    }
    
    public void testIndexOfCharInt() {
        StrBuilder sb = new StrBuilder("abab");
        
        assertEquals (2, sb.indexOf('a', 1));
        //should work like String#indexOf
        assertEquals ("abab".indexOf('a', 1), sb.indexOf('a', 1));
        
        assertEquals(3, sb.indexOf('b', 2));
        assertEquals ("abab".indexOf('b', 2), sb.indexOf('b', 2));
        
        assertEquals (-1, sb.indexOf('z', 2));
        
        sb = new StrBuilder("xyzabc");
        assertEquals (2, sb.indexOf('z', 0));
        assertEquals (-1, sb.indexOf('z', 3));
    }
    
    public void testLastIndexOfCharInt() {
        StrBuilder sb = new StrBuilder("abab");
        
        assertEquals (0, sb.lastIndexOf('a', 1));
        //should work like String#lastIndexOf
        assertEquals ("abab".lastIndexOf('a', 1), sb.lastIndexOf('a', 1));
        
        assertEquals(1, sb.lastIndexOf('b', 2));
        assertEquals ("abab".lastIndexOf('b', 2), sb.lastIndexOf('b', 2));
        
        assertEquals (-1, sb.lastIndexOf('z', 2));
        
        sb = new StrBuilder("xyzabc");
        assertEquals (2, sb.lastIndexOf('z', sb.length()));
        assertEquals (-1, sb.lastIndexOf('z', 1));
    }
    
    public void testIndexOfString_1() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(0, sb.indexOf("a"));
    }
    public void testIndexOfString_2() {
        StrBuilder sb = new StrBuilder("abab");
        //should work like String#indexOf
        assertEquals("abab".indexOf("a"), sb.indexOf("a"));
    }
    public void testIndexOfString_3() {
        StrBuilder sb = new StrBuilder("abab");        
        assertEquals(0, sb.indexOf("ab"));
    }
    public void testIndexOfString_4() {
        StrBuilder sb = new StrBuilder("abab");
        //should work like String#indexOf
        assertEquals("abab".indexOf("ab"), sb.indexOf("ab"));
    }
    public void testIndexOfString_5() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(1, sb.indexOf("b"));
    }
    public void testIndexOfString_6() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals("abab".indexOf("b"), sb.indexOf("b"));
    }
    public void testIndexOfString_7() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(1, sb.indexOf("ba"));
    }
    public void testIndexOfString_8() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals("abab".indexOf("ba"), sb.indexOf("ba"));
    }
    public void testIndexOfString_9() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(-1, sb.indexOf("z"));
    }
    public void testIndexOfString_10() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(-1, sb.indexOf((String) null));
    }
    
    public void testLastIndexOfString() {
        StrBuilder sb = new StrBuilder("abab");
        
        assertEquals(2, sb.lastIndexOf("a"));
        //should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf("a"), sb.lastIndexOf("a"));
        
        assertEquals(2, sb.lastIndexOf("ab"));
        //should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf("ab"), sb.lastIndexOf("ab"));
        
        assertEquals(3, sb.lastIndexOf("b"));
        assertEquals("abab".lastIndexOf("b"), sb.lastIndexOf("b"));
        
        assertEquals(1, sb.lastIndexOf("ba"));
        assertEquals("abab".lastIndexOf("ba"), sb.lastIndexOf("ba"));
        
        assertEquals(-1, sb.lastIndexOf("z"));
        
        assertEquals(-1, sb.lastIndexOf((String) null));
    }
    
    public void testIndexOfStringInt_1() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(2, sb.indexOf("a", 1));
    }
    public void testIndexOfStringInt_2() {
        StrBuilder sb = new StrBuilder("abab");
        //should work like String#indexOf
        assertEquals ("abab".indexOf("a", 1), sb.indexOf("a", 1));
    }
    public void testIndexOfStringInt_3() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(2, sb.indexOf("ab", 1));
    }
    public void testIndexOfStringInt_4() {
        StrBuilder sb = new StrBuilder("abab");
        //should work like String#indexOf
        assertEquals("abab".indexOf("ab", 1), sb.indexOf("ab", 1));
    }
    public void testIndexOfStringInt_5() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(3, sb.indexOf("b", 2));
    }
    public void testIndexOfStringInt_6() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals("abab".indexOf("b", 2), sb.indexOf("b", 2));
    }
    public void testIndexOfStringInt_7() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(1, sb.indexOf("ba", 1));
    }
    public void testIndexOfStringInt_8() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals("abab".indexOf("ba", 2), sb.indexOf("ba", 2));
    }
    public void testIndexOfStringInt_9() {
        StrBuilder sb = new StrBuilder("abab");
        assertEquals(-1, sb.indexOf("z", 2));
    }
    public void testIndexOfStringInt_10() {
        StrBuilder sb = new StrBuilder("xyzabc");
        assertEquals(2, sb.indexOf("za", 0));
    }
    public void testIndexOfStringInt_11() {
        StrBuilder sb = new StrBuilder("xyzabc");
        assertEquals(-1, sb.indexOf("za", 3));
    }
    public void testIndexOfStringInt_12() {
        StrBuilder sb = new StrBuilder("xyzabc");
        assertEquals(-1, sb.indexOf((String) null, 2));
    }
    
    public void testLastIndexOfStringInt() {
        StrBuilder sb = new StrBuilder("abab");
        
        assertEquals(0, sb.lastIndexOf("a", 1));
        //should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf("a", 1), sb.lastIndexOf("a", 1));
        
        assertEquals(0, sb.lastIndexOf("ab", 1));
        //should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf("ab", 1), sb.lastIndexOf("ab", 1));
        
        assertEquals(1, sb.lastIndexOf("b", 2));
        assertEquals("abab".lastIndexOf("b", 2), sb.lastIndexOf("b", 2));
        
        assertEquals(1, sb.lastIndexOf("ba", 2));
        assertEquals("abab".lastIndexOf("ba", 2), sb.lastIndexOf("ba", 2));
        
        assertEquals(-1, sb.lastIndexOf("z", 2));
        
        sb = new StrBuilder("xyzabc");
        assertEquals(2, sb.lastIndexOf("za", sb.length()));
        assertEquals(-1, sb.lastIndexOf("za", 1));
        
        assertEquals(-1, sb.lastIndexOf((String) null, 2));
    }
    
    public void testContainsChar() {
        StrBuilder sb = new StrBuilder("abcdefghijklmnopqrstuvwxyz");
        assertTrue (sb.contains('a'));
        assertTrue (sb.contains('o'));
        assertTrue (sb.contains('z'));
        assertFalse (sb.contains('1'));
    }
    
    public void testContainsString() {
        StrBuilder sb = new StrBuilder("abcdefghijklmnopqrstuvwxyz");
        assertTrue (sb.contains("a"));
        assertTrue (sb.contains("pq"));
        assertTrue (sb.contains("z"));
        assertFalse (sb.contains("zyx"));
    }
    
    public void testMidString() {
        StrBuilder sb = new StrBuilder("hello goodbye hello");
        assertEquals ("goodbye", sb.midString(6, 7));
        assertEquals ("hello", sb.midString(0, 5));
        assertEquals ("hello", sb.midString(-5, 5));
        assertEquals ("", sb.midString(0, -1));
        assertEquals ("", sb.midString(20, 2));
    }
    
    public void testRightString() {
        StrBuilder sb = new StrBuilder("left right");
        assertEquals ("right", sb.rightString(5));
        assertEquals ("", sb.rightString(0));
        assertEquals ("", sb.rightString(-5));
        assertEquals ("left right", sb.rightString(15));
    }
    
    public void testLeftString() {
        StrBuilder sb = new StrBuilder("left right");
        assertEquals ("left", sb.leftString(4));
        assertEquals ("", sb.leftString(0));
        assertEquals ("", sb.leftString(-5));
        assertEquals ("left right", sb.leftString(15));
    }
    
    public void testSubstringInt() {
        StrBuilder sb = new StrBuilder ("hello goodbye");
        assertEquals ("goodbye", sb.substring(6));
        assertEquals ("hello goodbye".substring(6), sb.substring(6));
        assertEquals ("hello goodbye", sb.substring(0));
        assertEquals ("hello goodbye".substring(0), sb.substring(0));
        try {
            sb.substring(-1);
            fail ();
        } catch (IndexOutOfBoundsException e) {}
        
        try {
            sb.substring(15);
            fail ();
        } catch (IndexOutOfBoundsException e) {}
    
    }
    
    public void testSubstringIntInt() {
        StrBuilder sb = new StrBuilder ("hello goodbye");
        assertEquals ("hello", sb.substring(0, 5));
        assertEquals ("hello goodbye".substring(0, 6), sb.substring(0, 6));
        
        assertEquals ("goodbye", sb.substring(6, 13));
        assertEquals ("hello goodbye".substring(6,13), sb.substring(6, 13));
        
        assertEquals ("goodbye", sb.substring(6, 20));
        
        try {
            sb.substring(-1, 5);
            fail();
        } catch (IndexOutOfBoundsException e) {}
        
        try {
            sb.substring(15, 20);
            fail();
        } catch (IndexOutOfBoundsException e) {}
    }

    //-----------------------------------------------------------------------
    public void testAsReader() throws Exception {
        StrBuilder sb = new StrBuilder ("some text");
        Reader reader = sb.asReader();
        char[] buf = new char[40];
        assertEquals(9, reader.read(buf));
        assertEquals("some text", new String(buf, 0, 9));
        
        buf = new char[40];
        assertEquals(-1, reader.read(buf));
    }

    //-----------------------------------------------------------------------
    /*public void testAsWriter() throws Exception {
        StrBuilder sb = new StrBuilder ("base");
        Writer writer = sb.asWriter();
        
        writer.write('l');
        assertEquals("basel", sb.toString());
        
        writer.write(new char[] {'i', 'n'});
        assertEquals("baselin", sb.toString());
        
        writer.write(new char[] {'n', 'e', 'r'}, 1, 2);
        assertEquals("baseliner", sb.toString());
        
        writer.write(" rout");
        assertEquals("baseliner rout", sb.toString());
        
        writer.write("ping that server", 1, 3);
        assertEquals("baseliner routing", sb.toString());
        
        writer.flush();  // no effect
        assertEquals("baseliner routing", sb.toString());
        
        writer.close();  // no effect
        assertEquals("baseliner routing", sb.toString());
        
        writer.write(" hi");  // works after close
        assertEquals("baseliner routing hi", sb.toString());
        
        sb.setLength(4);  // mix and match
        writer.write('d');
        assertEquals("based", sb.toString());
    }*/

}
