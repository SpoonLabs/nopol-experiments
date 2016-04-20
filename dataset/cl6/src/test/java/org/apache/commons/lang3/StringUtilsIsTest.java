/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import junit.framework.TestCase;

/**
 * Unit tests {@link org.apache.commons.lang3.StringUtils} - Substring methods
 *
 * @author Apache Software Foundation
 * @author Michael Davey
 * @version $Id$
 */
public class StringUtilsIsTest extends TestCase {

    public StringUtilsIsTest(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------

    public void testIsAlpha_1() {
        assertEquals(false, StringUtils.isAlpha(null));
    }
    public void testIsAlpha_2() {
        assertEquals(false, StringUtils.isAlpha(""));
    }
    public void testIsAlpha_3() {
        assertEquals(false, StringUtils.isAlpha(" "));
    }
    public void testIsAlpha_4() {
        assertEquals(true, StringUtils.isAlpha("a"));
    }
    public void testIsAlpha_5() {
        assertEquals(true, StringUtils.isAlpha("A"));
    }
    public void testIsAlpha_6() {
        assertEquals(true, StringUtils.isAlpha("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
    }
    public void testIsAlpha_7() {
        assertEquals(false, StringUtils.isAlpha("ham kso"));
    }
    public void testIsAlpha_8() {
        assertEquals(false, StringUtils.isAlpha("1"));
    }
    public void testIsAlpha_9() {
        assertEquals(false, StringUtils.isAlpha("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
    }
    public void testIsAlpha_10() {
        assertEquals(false, StringUtils.isAlpha("_"));
    }
    public void testIsAlpha_11() {
        assertEquals(false, StringUtils.isAlpha("hkHKHik*khbkuh"));
    }

    public void testIsAlphanumeric_1() {
        assertEquals(false, StringUtils.isAlphanumeric(null));
    }
    public void testIsAlphanumeric_2() {
        assertEquals(false, StringUtils.isAlphanumeric(""));
    }
    public void testIsAlphanumeric_3() {
        assertEquals(false, StringUtils.isAlphanumeric(" "));
    }
    public void testIsAlphanumeric_4() {
        assertEquals(true, StringUtils.isAlphanumeric("a"));
    }
    public void testIsAlphanumeric_5() {
        assertEquals(true, StringUtils.isAlphanumeric("A"));
    }
    public void testIsAlphanumeric_6() {
        assertEquals(true, StringUtils.isAlphanumeric("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
    }
    public void testIsAlphanumeric_7() {
        assertEquals(false, StringUtils.isAlphanumeric("ham kso"));
    }
    public void testIsAlphanumeric_8() {
        assertEquals(true, StringUtils.isAlphanumeric("1"));
    }
    public void testIsAlphanumeric_9() {
        assertEquals(true, StringUtils.isAlphanumeric("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
    }
    public void testIsAlphanumeric_10() {
        assertEquals(false, StringUtils.isAlphanumeric("_"));
    }
    public void testIsAlphanumeric_11() {
        assertEquals(false, StringUtils.isAlphanumeric("hkHKHik*khbkuh"));
    }

    public void testIsWhitespace_1() {
        assertEquals(false, StringUtils.isWhitespace(null));
    }
    public void testIsWhitespace_2() {
        assertEquals(true, StringUtils.isWhitespace(""));
    }
    public void testIsWhitespace_3() {
        assertEquals(true, StringUtils.isWhitespace(" "));
    }
    public void testIsWhitespace_4() {
        assertEquals(true, StringUtils.isWhitespace("\t \n \t"));
    }
    public void testIsWhitespace_5() {
        assertEquals(false, StringUtils.isWhitespace("\t aa\n \t"));
    }
    public void testIsWhitespace_6() {
        assertEquals(true, StringUtils.isWhitespace(" "));
    }
    public void testIsWhitespace_7() {
        assertEquals(false, StringUtils.isWhitespace(" a "));
    }
    public void testIsWhitespace_8() {
        assertEquals(false, StringUtils.isWhitespace("a  "));
    }
    public void testIsWhitespace_9() {
        assertEquals(false, StringUtils.isWhitespace("  a"));
    }
    public void testIsWhitespace_10() {
        assertEquals(false, StringUtils.isWhitespace("aba"));
    }
    public void testIsWhitespace_11() {
        assertEquals(true, StringUtils.isWhitespace(StringUtilsTest.WHITESPACE));
    }
    public void testIsWhitespace_12() {
        assertEquals(false, StringUtils.isWhitespace(StringUtilsTest.NON_WHITESPACE));
    }

    public void testIsAlphaspace_1() {
        assertEquals(false, StringUtils.isAlphaSpace(null));
    }
    public void testIsAlphaspace_2() {
        assertEquals(true, StringUtils.isAlphaSpace(""));
    }
    public void testIsAlphaspace_3() {
        assertEquals(true, StringUtils.isAlphaSpace(" "));
    }
    public void testIsAlphaspace_4() {
        assertEquals(true, StringUtils.isAlphaSpace("a"));
    }
    public void testIsAlphaspace_5() {
        assertEquals(true, StringUtils.isAlphaSpace("A"));
    }
    public void testIsAlphaspace_6() {
        assertEquals(true, StringUtils.isAlphaSpace("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
    }
    public void testIsAlphaspace_7() {
        assertEquals(true, StringUtils.isAlphaSpace("ham kso"));
    }
    public void testIsAlphaspace_8() {
        assertEquals(false, StringUtils.isAlphaSpace("1"));
    }
    public void testIsAlphaspace_9() {
        assertEquals(false, StringUtils.isAlphaSpace("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
    }
    public void testIsAlphaspace_10() {
        assertEquals(false, StringUtils.isAlphaSpace("_"));
    }
    public void testIsAlphaspace_11() {
        assertEquals(false, StringUtils.isAlphaSpace("hkHKHik*khbkuh"));
    }

    public void testIsAlphanumericSpace_1() {
        assertEquals(false, StringUtils.isAlphanumericSpace(null));
    }
    public void testIsAlphanumericSpace_2() {
        assertEquals(true, StringUtils.isAlphanumericSpace(""));
    }
    public void testIsAlphanumericSpace_3() {
        assertEquals(true, StringUtils.isAlphanumericSpace(" "));
    }
    public void testIsAlphanumericSpace_4() {
        assertEquals(true, StringUtils.isAlphanumericSpace("a"));
    }
    public void testIsAlphanumericSpace_5() {
        assertEquals(true, StringUtils.isAlphanumericSpace("A"));
    }
    public void testIsAlphanumericSpace_6() {
        assertEquals(true, StringUtils.isAlphanumericSpace("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
    }
    public void testIsAlphanumericSpace_7() {
        assertEquals(true, StringUtils.isAlphanumericSpace("ham kso"));
    }
    public void testIsAlphanumericSpace_8() {
        assertEquals(true, StringUtils.isAlphanumericSpace("1"));
    }
    public void testIsAlphanumericSpace_9() {
        assertEquals(true, StringUtils.isAlphanumericSpace("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
    }
    public void testIsAlphanumericSpace_10() {
        assertEquals(false, StringUtils.isAlphanumericSpace("_"));
    }
    public void testIsAlphanumericSpace_11() {
        assertEquals(false, StringUtils.isAlphanumericSpace("hkHKHik*khbkuh"));
    }

    public void testIsAsciiPrintable_String_1() {
        assertEquals(false, StringUtils.isAsciiPrintable(null));
    }
    public void testIsAsciiPrintable_String_2() {
        assertEquals(true, StringUtils.isAsciiPrintable(""));
    }
    public void testIsAsciiPrintable_String_3() {
        assertEquals(true, StringUtils.isAsciiPrintable(" "));
    }
    public void testIsAsciiPrintable_String_4() {
        assertEquals(true, StringUtils.isAsciiPrintable("a"));
    }
    public void testIsAsciiPrintable_String_5() {
        assertEquals(true, StringUtils.isAsciiPrintable("A"));
    }
    public void testIsAsciiPrintable_String_6() {
        assertEquals(true, StringUtils.isAsciiPrintable("1"));
    }
    public void testIsAsciiPrintable_String_7() {
        assertEquals(true, StringUtils.isAsciiPrintable("Ceki"));
    }
    public void testIsAsciiPrintable_String_8() {
        assertEquals(true, StringUtils.isAsciiPrintable("!ab2c~"));
    }
    public void testIsAsciiPrintable_String_9() {
        assertEquals(true, StringUtils.isAsciiPrintable("1000"));
    }
    public void testIsAsciiPrintable_String_10() {
        assertEquals(true, StringUtils.isAsciiPrintable("10 00"));
    }
    public void testIsAsciiPrintable_String_11() {
        assertEquals(false, StringUtils.isAsciiPrintable("10\t00"));
    }
    public void testIsAsciiPrintable_String_12() {
        assertEquals(true, StringUtils.isAsciiPrintable("10.00"));
    }
    public void testIsAsciiPrintable_String_13() {
        assertEquals(true, StringUtils.isAsciiPrintable("10,00"));
    }
    public void testIsAsciiPrintable_String_14() {
        assertEquals(true, StringUtils.isAsciiPrintable("!ab-c~"));
    }
    public void testIsAsciiPrintable_String_15() {
        assertEquals(true, StringUtils.isAsciiPrintable("hkHK=Hik6i?UGH_KJgU7.tUJgKJ*GI87GI,kug"));
    }
    public void testIsAsciiPrintable_String_16() {
        assertEquals(true, StringUtils.isAsciiPrintable("\u0020"));
    }
    public void testIsAsciiPrintable_String_17() {
        assertEquals(true, StringUtils.isAsciiPrintable("\u0021"));
    }
    public void testIsAsciiPrintable_String_18() {
        assertEquals(true, StringUtils.isAsciiPrintable("\u007e"));
    }
    public void testIsAsciiPrintable_String_19() {
        assertEquals(false, StringUtils.isAsciiPrintable("\u007f"));
    }
    public void testIsAsciiPrintable_String_20() {
        assertEquals(true, StringUtils.isAsciiPrintable("G?lc?"));
    }
    public void testIsAsciiPrintable_String_21() {
        assertEquals(true, StringUtils.isAsciiPrintable("=?iso-8859-1?Q?G=FClc=FC?="));
    }
    public void testIsAsciiPrintable_String_22() {
        assertEquals(false, StringUtils.isAsciiPrintable("G\u00fclc\u00fc"));
    }
  
    public void testIsNumeric_1() {
        assertEquals(false, StringUtils.isNumeric(null));
    }
    public void testIsNumeric_2() {
        assertEquals(false, StringUtils.isNumeric(""));
    }
    public void testIsNumeric_3() {
        assertEquals(false, StringUtils.isNumeric(" "));
    }
    public void testIsNumeric_4() {
        assertEquals(false, StringUtils.isNumeric("a"));
    }
    public void testIsNumeric_5() {
        assertEquals(false, StringUtils.isNumeric("A"));
    }
    public void testIsNumeric_6() {
        assertEquals(false, StringUtils.isNumeric("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
    }
    public void testIsNumeric_7() {
        assertEquals(false, StringUtils.isNumeric("ham kso"));
    }
    public void testIsNumeric_8() {
        assertEquals(true, StringUtils.isNumeric("1"));
    }
    public void testIsNumeric_9() {
        assertEquals(true, StringUtils.isNumeric("1000"));
    }
    public void testIsNumeric_10() {
        assertEquals(false, StringUtils.isNumeric("2.3"));
    }
    public void testIsNumeric_11() {
        assertEquals(false, StringUtils.isNumeric("10 00"));
    }
    public void testIsNumeric_12() {
        assertEquals(false, StringUtils.isNumeric("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
    }
    public void testIsNumeric_13() {
        assertEquals(false, StringUtils.isNumeric("_"));
    }
    public void testIsNumeric_14() {
        assertEquals(false, StringUtils.isNumeric("hkHKHik*khbkuh"));
    }

    public void testIsNumericSpace_1() {
        assertEquals(false, StringUtils.isNumericSpace(null));
    }
    public void testIsNumericSpace_2() {
        assertEquals(true, StringUtils.isNumericSpace(""));
    }
    public void testIsNumericSpace_3() {
        assertEquals(true, StringUtils.isNumericSpace(" "));
    }
    public void testIsNumericSpace_4() {
        assertEquals(false, StringUtils.isNumericSpace("a"));
    }
    public void testIsNumericSpace_5() {
        assertEquals(false, StringUtils.isNumericSpace("A"));
    }
    public void testIsNumericSpace_6() {
        assertEquals(false, StringUtils.isNumericSpace("kgKgKgKgkgkGkjkjlJlOKLgHdGdHgl"));
    }
    public void testIsNumericSpace_7() {
        assertEquals(false, StringUtils.isNumericSpace("ham kso"));
    }
    public void testIsNumericSpace_8() {
        assertEquals(true, StringUtils.isNumericSpace("1"));
    }
    public void testIsNumericSpace_9() {
        assertEquals(true, StringUtils.isNumericSpace("1000"));
    }
    public void testIsNumericSpace_10() {
        assertEquals(false, StringUtils.isNumericSpace("2.3"));
    }
    public void testIsNumericSpace_11() {
        assertEquals(true, StringUtils.isNumericSpace("10 00"));
    }
    public void testIsNumericSpace_12() {
        assertEquals(false, StringUtils.isNumericSpace("hkHKHik6iUGHKJgU7tUJgKJGI87GIkug"));
    }
    public void testIsNumericSpace_13() {
        assertEquals(false, StringUtils.isNumericSpace("_"));
    }
    public void testIsNumericSpace_14() {
        assertEquals(false, StringUtils.isNumericSpace("hkHKHik*khbkuh"));
    }

}
