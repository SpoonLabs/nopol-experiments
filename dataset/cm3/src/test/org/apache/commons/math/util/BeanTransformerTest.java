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

package org.apache.commons.math.util;

import org.apache.commons.math.MathException;
import org.apache.commons.math.TestUtils;

import junit.framework.TestCase;

/**
 * @version $Revision: 1.12 $ $Date: 2004/06/01 23:21:32 $
 */
public class BeanTransformerTest extends TestCase {
    
    /**
     *
     */
    public void testConstructor(){
        BeanTransformer b = new BeanTransformer();
        assertNull(b.getPropertyName());
    }
    
    /**
     *
     */
    public void testConstructorString(){
        String name = "property";
        BeanTransformer b = new BeanTransformer(name);
        assertEquals(name, b.getPropertyName());
    }
    
    /**
     *
     */
    public void testSetPropertyName(){
        String name = "property";
        BeanTransformer b = new BeanTransformer();
        b.setPropertyName(name);
        assertEquals(name, b.getPropertyName());
    }
    
    /**
     * 
     */
    public void testTransformNoSuchMethod(){
        BeanTransformer b = new BeanTransformer("z");
        TestBean target = new TestBean();
		try {
		    b.transform(target);
			fail("Expecting MathException");
		} catch (MathException e) {
			// expected
		}
    }
    
    /**
     * 
     */
    public void testTransform() throws Exception {
        BeanTransformer b = new BeanTransformer("x");
        TestBean target = new TestBean();
		double value = Double.NaN;
		value = b.transform(target);
		TestUtils.assertEquals(1.0, value, 1.0e-2);
    }
    
    /**
     */
    public void testTransformInvalidType() throws Exception {
        BeanTransformer b = new BeanTransformer("y");
        TestBean target = new TestBean();
        try {
			b.transform(target);
            fail("Expecting ClassCastException");
        } catch(ClassCastException ex){
            // success
        }
    }
}
