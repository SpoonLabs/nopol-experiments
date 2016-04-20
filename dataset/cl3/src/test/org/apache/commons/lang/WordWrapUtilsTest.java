/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for the wrap methods of WordWrapUtils.
 * 
 * @author <a href="mailto:ridesmet@users.sourceforge.net">Ringo De Smet</a>
 * @author Henri Yandell
 * @author Stephen Colebourne
 * @version $Id: WordWrapUtilsTest.java,v 1.3 2003/07/30 22:21:39 scolebourne Exp $
 */
public class WordWrapUtilsTest extends TestCase {

    public WordWrapUtilsTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(WordWrapUtilsTest.class);
        suite.setName("WordWrapperTests");
        return suite;
    }

    //-----------------------------------------------------------------------
    public void testConstructor() {
        assertNotNull(new WordWrapUtils());
        Constructor[] cons = WordWrapUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertEquals(true, Modifier.isPublic(cons[0].getModifiers()));
        assertEquals(true, Modifier.isPublic(WordWrapUtils.class.getModifiers()));
        assertEquals(false, Modifier.isFinal(WordWrapUtils.class.getModifiers()));
    }
    
    //-----------------------------------------------------------------------
    /**
     * Wrap text. This is the most general use.
     */
    public void testWrapText1() {
        String input =
                "Here is one line of text that is going to be wrapped after 20 columns.";
        String expected =
                "Here is one line of\ntext that is going\nto be wrapped after\n20 columns.";
        assertEquals("Text didn't wrap correctly, ", expected, WordWrapUtils.wrapText(input, "\n", 20));
    }

    /**
     * Wrap text with a tab character in the middle of a string.
     */
    public void testWrapText2() {
        String input =
                "Here is\tone line of text that is going to be wrapped after 20 columns.";
        String expected =
                "Here is\tone line of\ntext that is going\nto be wrapped after\n20 columns.";
        assertEquals("Text with tab didn't wrap correctly, ", expected, WordWrapUtils.wrapText(input, "\n", 20));
    }

    /**
     * Wrap text with a tab character located at the wrapping column index.
     */
    public void testWrapText3() {
        String input =
                "Here is one line of\ttext that is going to be wrapped after 20 columns.";
        String expected =
                "Here is one line\nof\ttext that is\ngoing to be wrapped\nafter 20 columns.";
        assertEquals("Text with tab at wrapping index didn't wrap correctly, ", expected, WordWrapUtils.wrapText(input, "\n", 20));
    }

    public void testWrapText4() {
        assertEquals(null, WordWrapUtils.wrapText(null, "\n", 20));
        assertEquals("", WordWrapUtils.wrapText("", "\n", 20));
    }
}
