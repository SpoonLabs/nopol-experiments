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
 * @version $Revision: 1.9 $ $Date: 2004/02/21 21:35:18 $
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
    public void testTransform() {
        BeanTransformer b = new BeanTransformer("x");
        TestBean target = new TestBean();
		double value = Double.NaN;
		try {
			value = b.transform(target);
		} catch (MathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestUtils.assertEquals(1.0, value, 1.0e-2);
    }
    
    /**
     * 
     */
    public void testTransformInvalidType(){
        BeanTransformer b = new BeanTransformer("y");
        TestBean target = new TestBean();
        try {
            try {
				b.transform(target);
			} catch (MathException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            fail("Expecting ClassCastException");
        } catch(ClassCastException ex){
            // success
        }
    }
}
