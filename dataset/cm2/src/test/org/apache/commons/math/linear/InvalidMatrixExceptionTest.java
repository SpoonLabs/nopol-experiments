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

package org.apache.commons.math.linear;

import junit.framework.TestCase;

/**
 * @version $Revision: 1.1 $ $Date: 2004/04/27 16:51:52 $
 */
public class InvalidMatrixExceptionTest extends TestCase {
    /**
     * 
     */
    public void testConstructor(){
        InvalidMatrixException ex = new InvalidMatrixException();
        assertNull(ex.getCause());
        assertNull(ex.getMessage());
    }
    
    /**
     * 
     */
    public void testConstructorMessage(){
        String msg = "message";
        InvalidMatrixException ex = new InvalidMatrixException(msg);
        assertNull(ex.getCause());
        assertEquals(msg, ex.getMessage());
    }
    
    /**
     * 
     */
    public void testConstructorMessageCause(){
        String outMsg = "outer message";
        String inMsg = "inner message";
        Exception cause = new Exception(inMsg);
        InvalidMatrixException ex = new InvalidMatrixException(outMsg, cause);
        assertEquals(outMsg, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
    
    /**
     * 
     */
    public void testConstructorCause(){
        String inMsg = "inner message";
        Exception cause = new Exception(inMsg);
        InvalidMatrixException ex = new InvalidMatrixException(cause);
        assertEquals(cause, ex.getCause());
    }
}
