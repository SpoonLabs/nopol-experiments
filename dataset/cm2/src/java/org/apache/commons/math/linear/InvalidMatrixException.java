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

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * Thrown when a system attempts an operation on a matrix, and
 * that matrix does not satisfy the preconditions for the
 * aforementioned operation.
 * @version $Revision: 1.4 $ $Date: 2004/04/27 04:37:58 $
 */
public class InvalidMatrixException extends NestableRuntimeException {
    /**
     * Default constructor.
     */
    public InvalidMatrixException() {
        this(null, null);
    }
    
    /**
     * Construct an exception with the given message.
     * @param message descriptive error message. 
     */
    public InvalidMatrixException(String message) {
        this(message, null);
    }

    /**
     * Construct an exception with the given message and root cause.
     * @param message descriptive error message.
     * @param cause root cause.
     */
    public InvalidMatrixException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create an exception with a given root cause.
     * @param throwable caught exception causing this problem
     */
    public InvalidMatrixException(Throwable throwable) {
        this(null, throwable);
    }
}