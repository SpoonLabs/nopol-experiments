package org.apache.commons.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/** 
 * <p>Operates on classes without using reflection.</p>
 * 
 * <p>This class handles invalid <code>null</code> inputs as best it can.
 * Each method documents its behaviour in more detail.</p>
 * 
 * <p>The notion of a <code>canonical name</code> includes the human
 * readable name for the type, for example <code>int[]</code>. The
 * non-canonical method variants work with the JVM names, such as
 * <code>[I</code>. </p>
 * 
 * @author Stephen Colebourne
 * @author Gary Gregory
 * @author Norm Deane
 * @author Alban Peignier
 * @author Tomasz Blachowicz
 * @since 2.0
 * @version $Id$
 */
public class ClassUtils {
    /** 
     * <p>The package separator character: <code>'&#x2e;' == {@value}</code>.</p>
     */
public static final char PACKAGE_SEPARATOR_CHAR = '.';

    /** 
     * <p>The package separator String: <code>"&#x2e;"</code>.</p>
     */
public static final String PACKAGE_SEPARATOR = java.lang.String.valueOf(PACKAGE_SEPARATOR_CHAR);

    /** 
     * <p>The inner class separator character: <code>'$' == {@value}</code>.</p>
     */
public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

    /** 
     * <p>The inner class separator String: <code>"$"</code>.</p>
     */
public static final java.lang.String INNER_CLASS_SEPARATOR = java.lang.String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

    /** 
     * Maps primitive <code>Class</code>es to their corresponding wrapper <code>Class</code>.
     */
private static final Map<java.lang.Class<?>, Class<?>> primitiveWrapperMap = new HashMap<java.lang.Class<?>, Class<?>>();

    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }

    /** 
     * Maps wrapper <code>Class</code>es to their corresponding primitive types.
     */
private static final Map<java.lang.Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<java.lang.Class<?>, Class<?>>();

    static {
        for (Class<?> primitiveClass : primitiveWrapperMap.keySet()) {
            Class<?> wrapperClass = primitiveWrapperMap.get(primitiveClass);
            if (!(primitiveClass.equals(wrapperClass))) {
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            } 
        }
    }

    /** 
     * Maps a primitive class name to its corresponding abbreviation used in array class names.
     */
private static final Map<java.lang.String, java.lang.String> abbreviationMap = new HashMap<java.lang.String, java.lang.String>();

    /** 
     * Maps an abbreviation used in array class names to corresponding primitive class name.
     */
private static final Map<java.lang.String, java.lang.String> reverseAbbreviationMap = new HashMap<java.lang.String, java.lang.String>();

    /** 
     * Add primitive type abbreviation to maps of abbreviations.
     * 
     * @param primitive Canonical name of primitive type
     * @param abbreviation Corresponding abbreviation of primitive type
     */
private static void addAbbreviation(java.lang.String primitive, java.lang.String abbreviation) {
        abbreviationMap.put(primitive, abbreviation);
        reverseAbbreviationMap.put(abbreviation, primitive);
    }

    static {
        org.apache.commons.lang.ClassUtils.addAbbreviation("int", "I");
        org.apache.commons.lang.ClassUtils.addAbbreviation("boolean", "Z");
        org.apache.commons.lang.ClassUtils.addAbbreviation("float", "F");
        org.apache.commons.lang.ClassUtils.addAbbreviation("long", "J");
        org.apache.commons.lang.ClassUtils.addAbbreviation("short", "S");
        org.apache.commons.lang.ClassUtils.addAbbreviation("byte", "B");
        org.apache.commons.lang.ClassUtils.addAbbreviation("double", "D");
        org.apache.commons.lang.ClassUtils.addAbbreviation("char", "C");
    }

    /** 
     * <p>ClassUtils instances should NOT be constructed in standard programming.
     * Instead, the class should be used as
     * <code>ClassUtils.getShortClassName(cls)</code>.</p>
     * 
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
public ClassUtils() {
        super();
    }

    /** 
     * <p>Gets the class name minus the package name for an <code>Object</code>.</p>
     * 
     * @param object  the class to get the short name for, may be null
     * @param valueIfNull  the value to return if null
     * @return the class name of the object without the package name, or the null value
     */
public static java.lang.String getShortClassName(Object object, java.lang.String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        } 
        return org.apache.commons.lang.ClassUtils.getShortClassName(object.getClass());
    }

    /** 
     * <p>Gets the class name minus the package name from a <code>Class</code>.</p>
     * 
     * @param cls  the class to get the short name for.
     * @return the class name without the package name or an empty string
     */
public static java.lang.String getShortClassName(Class<?> cls) {
        if (cls == null) {
            return StringUtils.EMPTY;
        } 
        return org.apache.commons.lang.ClassUtils.getShortClassName(cls.getName());
    }

    /** 
     * <p>Gets the class name minus the package name from a String.</p>
     * 
     * <p>The string passed in is assumed to be a class name - it is not checked.</p>
     * 
     * @param className  the className to get the short name for
     * @return the class name of the class without the package name or an empty string
     */
public static java.lang.String getShortClassName(java.lang.String className) {
        if (className == null) {
            return StringUtils.EMPTY;
        } 
        if ((className.length()) == 0) {
            return StringUtils.EMPTY;
        } 
        StringBuffer arrayPrefix = new StringBuffer();
        if (className.startsWith("[")) {
            while ((className.charAt(0)) == '[') {
                className = className.substring(1);
                arrayPrefix.append("[]");
            }
            if (((className.charAt(0)) == 'L') && ((className.charAt(((className.length()) - 1))) == ';')) {
                className = className.substring(1, ((className.length()) - 1));
            } 
        } 
        if (reverseAbbreviationMap.containsKey(className)) {
            className = reverseAbbreviationMap.get(className);
        } 
        int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR, (lastDotIdx == (-1) ? 0 : lastDotIdx + 1));
        java.lang.String out = className.substring((lastDotIdx + 1));
        if (innerIdx != (-1)) {
            out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
        } 
        return out + arrayPrefix;
    }

    /** 
     * <p>Gets the package name of an <code>Object</code>.</p>
     * 
     * @param object  the class to get the package name for, may be null
     * @param valueIfNull  the value to return if null
     * @return the package name of the object, or the null value
     */
public static java.lang.String getPackageName(Object object, java.lang.String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        } 
        return org.apache.commons.lang.ClassUtils.getPackageName(object.getClass());
    }

    /** 
     * <p>Gets the package name of a <code>Class</code>.</p>
     * 
     * @param cls  the class to get the package name for, may be <code>null</code>.
     * @return the package name or an empty string
     */
public static java.lang.String getPackageName(Class<?> cls) {
        if (cls == null) {
            return StringUtils.EMPTY;
        } 
        return org.apache.commons.lang.ClassUtils.getPackageName(cls.getName());
    }

    /** 
     * <p>Gets the package name from a <code>String</code>.</p>
     * 
     * <p>The string passed in is assumed to be a class name - it is not checked.</p>
     * <p>If the class is unpackaged, return an empty string.</p>
     * 
     * @param className  the className to get the package name for, may be <code>null</code>
     * @return the package name or an empty string
     */
public static java.lang.String getPackageName(java.lang.String className) {
        if (className.length()==0) {
            return StringUtils.EMPTY;
        } 
        while ((className.charAt(0)) == '[') {
            className = className.substring(1);
        }
        if (((className.charAt(0)) == 'L') && ((className.charAt(((className.length()) - 1))) == ';')) {
            className = className.substring(1);
        } 
        int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (i == (-1)) {
            return StringUtils.EMPTY;
        } 
        return className.substring(0, i);
    }

    /** 
     * <p>Gets a <code>List</code> of superclasses for the given class.</p>
     * 
     * @param cls  the class to look up, may be <code>null</code>
     * @return the <code>List</code> of superclasses in order going up from this one
     * <code>null</code> if null input
     */
public static List<java.lang.Class<?>> getAllSuperclasses(Class<?> cls) {
        if (cls == null) {
            return null;
        } 
        List<java.lang.Class<?>> classes = new ArrayList<java.lang.Class<?>>();
        Class<?> superclass = cls.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }

    /** 
     * <p>Gets a <code>List</code> of all interfaces implemented by the given
     * class and its superclasses.</p>
     * 
     * <p>The order is determined by looking through each interface in turn as
     * declared in the source file and following its hierarchy up. Then each
     * superclass is considered in the same way. Later duplicates are ignored,
     * so the order is maintained.</p>
     * 
     * @param cls  the class to look up, may be <code>null</code>
     * @return the <code>List</code> of interfaces in order,
     * <code>null</code> if null input
     */
public static List<java.lang.Class<?>> getAllInterfaces(Class<?> cls) {
        if (cls == null) {
            return null;
        } 
        LinkedHashSet<java.lang.Class<?>> interfacesFound = new LinkedHashSet<java.lang.Class<?>>();
        org.apache.commons.lang.ClassUtils.getAllInterfaces(cls, interfacesFound);
        return new ArrayList<java.lang.Class<?>>(interfacesFound);
    }

    private static void getAllInterfaces(Class<?> cls, HashSet<java.lang.Class<?>> interfacesFound) {
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            for (Class<?> i : interfaces) {
                if (interfacesFound.add(i)) {
                    org.apache.commons.lang.ClassUtils.getAllInterfaces(i, interfacesFound);
                } 
            }
            cls = cls.getSuperclass();
        }
    }

    /** 
     * <p>Given a <code>List</code> of class names, this method converts them into classes.</p>
     * 
     * <p>A new <code>List</code> is returned. If the class name cannot be found, <code>null</code>
     * is stored in the <code>List</code>. If the class name in the <code>List</code> is
     * <code>null</code>, <code>null</code> is stored in the output <code>List</code>.</p>
     * 
     * @param classNames  the classNames to change
     * @return a <code>List</code> of Class objects corresponding to the class names,
     * <code>null</code> if null input
     * @throws ClassCastException if classNames contains a non String entry
     */
public static List<java.lang.Class<?>> convertClassNamesToClasses(List<java.lang.String> classNames) {
        if (classNames == null) {
            return null;
        } 
        List<java.lang.Class<?>> classes = new ArrayList<java.lang.Class<?>>(classNames.size());
        for (java.lang.String className : classNames) {
            try {
                classes.add(java.lang.Class.forName(className));
            } catch (Exception ex) {
                classes.add(null);
            }
        }
        return classes;
    }

    /** 
     * <p>Given a <code>List</code> of <code>Class</code> objects, this method converts
     * them into class names.</p>
     * 
     * <p>A new <code>List</code> is returned. <code>null</code> objects will be copied into
     * the returned list as <code>null</code>.</p>
     * 
     * @param classes  the classes to change
     * @return a <code>List</code> of class names corresponding to the Class objects,
     * <code>null</code> if null input
     * @throws ClassCastException if <code>classes</code> contains a non-<code>Class</code> entry
     */
public static List<java.lang.String> convertClassesToClassNames(List<java.lang.Class<?>> classes) {
        if (classes == null) {
            return null;
        } 
        List<java.lang.String> classNames = new ArrayList<java.lang.String>(classes.size());
        for (java.lang.Class<?> cls : classes) {
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }

    /** 
     * <p>Checks if an array of Classes can be assigned to another array of Classes.</p>
     * 
     * <p>This method calls {@link #isAssignable(Class, Class) isAssignable} for each
     * Class pair in the input arrays. It can be used to check if a set of arguments
     * (the first parameter) are suitably compatible with a set of method parameter types
     * (the second parameter).</p>
     * 
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this
     * method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     * 
     * <p>Primitive widenings allow an int to be assigned to a <code>long</code>,
     * <code>float</code> or <code>double</code>. This method returns the correct
     * result for these cases.</p>
     * 
     * <p><code>Null</code> may be assigned to any reference type. This method will
     * return <code>true</code> if <code>null</code> is passed in and the toClass is
     * non-primitive.</p>
     * 
     * <p>Specifically, this method tests whether the type represented by the
     * specified <code>Class</code> parameter can be converted to the type
     * represented by this <code>Class</code> object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     * 
     * @param classArray  the array of Classes to check, may be <code>null</code>
     * @param toClassArray  the array of Classes to try to assign into, may be <code>null</code>
     * @return <code>true</code> if assignment possible
     */
public static boolean isAssignable(java.lang.Class<?>[] classArray, java.lang.Class<?>[] toClassArray) {
        return org.apache.commons.lang.ClassUtils.isAssignable(classArray, toClassArray, false);
    }

    /** 
     * <p>Checks if an array of Classes can be assigned to another array of Classes.</p>
     * 
     * <p>This method calls {@link #isAssignable(Class, Class) isAssignable} for each
     * Class pair in the input arrays. It can be used to check if a set of arguments
     * (the first parameter) are suitably compatible with a set of method parameter types
     * (the second parameter).</p>
     * 
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this
     * method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     * 
     * <p>Primitive widenings allow an int to be assigned to a <code>long</code>,
     * <code>float</code> or <code>double</code>. This method returns the correct
     * result for these cases.</p>
     * 
     * <p><code>Null</code> may be assigned to any reference type. This method will
     * return <code>true</code> if <code>null</code> is passed in and the toClass is
     * non-primitive.</p>
     * 
     * <p>Specifically, this method tests whether the type represented by the
     * specified <code>Class</code> parameter can be converted to the type
     * represented by this <code>Class</code> object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     * 
     * @param classArray  the array of Classes to check, may be <code>null</code>
     * @param toClassArray  the array of Classes to try to assign into, may be <code>null</code>
     * @param autoboxing  whether to use implicit autoboxing/unboxing between primitives and wrappers
     * @return <code>true</code> if assignment possible
     */
public static boolean isAssignable(java.lang.Class<?>[] classArray, java.lang.Class<?>[] toClassArray, boolean autoboxing) {
        if ((org.apache.commons.lang.ArrayUtils.isSameLength(classArray, toClassArray)) == false) {
            return false;
        } 
        if (classArray == null) {
            classArray = org.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY;
        } 
        if (toClassArray == null) {
            toClassArray = org.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY;
        } 
        for (int i = 0 ; i < (classArray.length) ; i++) {
            if ((org.apache.commons.lang.ClassUtils.isAssignable(classArray[i], toClassArray[i], autoboxing)) == false) {
                return false;
            } 
        }
        return true;
    }

    /** 
     * <p>Checks if one <code>Class</code> can be assigned to a variable of
     * another <code>Class</code>.</p>
     * 
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     * 
     * <p>Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.</p>
     * 
     * <p><code>Null</code> may be assigned to any reference type. This method
     * will return <code>true</code> if <code>null</code> is passed in and the
     * toClass is non-primitive.</p>
     * 
     * <p>Specifically, this method tests whether the type represented by the
     * specified <code>Class</code> parameter can be converted to the type
     * represented by this <code>Class</code> object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     * 
     * @param cls  the Class to check, may be null
     * @param toClass  the Class to try to assign into, returns false if null
     * @return <code>true</code> if assignment possible
     */
public static boolean isAssignable(java.lang.Class<?> cls, java.lang.Class<?> toClass) {
        return org.apache.commons.lang.ClassUtils.isAssignable(cls, toClass, false);
    }

    /** 
     * <p>Checks if one <code>Class</code> can be assigned to a variable of
     * another <code>Class</code>.</p>
     * 
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     * 
     * <p>Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.</p>
     * 
     * <p><code>Null</code> may be assigned to any reference type. This method
     * will return <code>true</code> if <code>null</code> is passed in and the
     * toClass is non-primitive.</p>
     * 
     * <p>Specifically, this method tests whether the type represented by the
     * specified <code>Class</code> parameter can be converted to the type
     * represented by this <code>Class</code> object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     * 
     * @param cls  the Class to check, may be null
     * @param toClass  the Class to try to assign into, returns false if null
     * @param autoboxing  whether to use implicit autoboxing/unboxing between primitives and wrappers
     * @return <code>true</code> if assignment possible
     */
public static boolean isAssignable(java.lang.Class<?> cls, java.lang.Class<?> toClass, boolean autoboxing) {
        if (toClass == null) {
            return false;
        } 
        if (cls == null) {
            return !(toClass.isPrimitive());
        } 
        if (autoboxing) {
            if ((cls.isPrimitive()) && (!(toClass.isPrimitive()))) {
                cls = org.apache.commons.lang.ClassUtils.primitiveToWrapper(cls);
                if (cls == null) {
                    return false;
                } 
            } 
            if ((toClass.isPrimitive()) && (!(cls.isPrimitive()))) {
                cls = org.apache.commons.lang.ClassUtils.wrapperToPrimitive(cls);
                if (cls == null) {
                    return false;
                } 
            } 
        } 
        if (cls.equals(toClass)) {
            return true;
        } 
        if (cls.isPrimitive()) {
            if ((toClass.isPrimitive()) == false) {
                return false;
            } 
            if (Integer.TYPE.equals(cls)) {
                return ((Long.TYPE.equals(toClass)) || (Float.TYPE.equals(toClass))) || (Double.TYPE.equals(toClass));
            } 
            if (Long.TYPE.equals(cls)) {
                return (Float.TYPE.equals(toClass)) || (Double.TYPE.equals(toClass));
            } 
            if (Boolean.TYPE.equals(cls)) {
                return false;
            } 
            if (Double.TYPE.equals(cls)) {
                return false;
            } 
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(toClass);
            } 
            if (Character.TYPE.equals(cls)) {
                return (((Integer.TYPE.equals(toClass)) || (Long.TYPE.equals(toClass))) || (Float.TYPE.equals(toClass))) || (Double.TYPE.equals(toClass));
            } 
            if (Short.TYPE.equals(cls)) {
                return (((Integer.TYPE.equals(toClass)) || (Long.TYPE.equals(toClass))) || (Float.TYPE.equals(toClass))) || (Double.TYPE.equals(toClass));
            } 
            if (Byte.TYPE.equals(cls)) {
                return ((((Short.TYPE.equals(toClass)) || (Integer.TYPE.equals(toClass))) || (Long.TYPE.equals(toClass))) || (Float.TYPE.equals(toClass))) || (Double.TYPE.equals(toClass));
            } 
            return false;
        } 
        return toClass.isAssignableFrom(cls);
    }

    /** 
     * <p>Converts the specified primitive Class object to its corresponding
     * wrapper Class object.</p>
     * 
     * <p>NOTE: From v2.2, this method handles <code>Void.TYPE</code>,
     * returning <code>Void.TYPE</code>.</p>
     * 
     * @param cls  the class to convert, may be null
     * @return the wrapper class for <code>cls</code> or <code>cls</code> if
     * <code>cls</code> is not a primitive. <code>null</code> if null input.
     * @since 2.1
     */
public static java.lang.Class<?> primitiveToWrapper(java.lang.Class<?> cls) {
        java.lang.Class<?> convertedClass = cls;
        if ((cls != null) && (cls.isPrimitive())) {
            convertedClass = primitiveWrapperMap.get(cls);
        } 
        return convertedClass;
    }

    /** 
     * <p>Converts the specified array of primitive Class objects to an array of
     * its corresponding wrapper Class objects.</p>
     * 
     * @param classes  the class array to convert, may be null or empty
     * @return an array which contains for each given class, the wrapper class or
     * the original class if class is not a primitive. <code>null</code> if null input.
     * Empty array if an empty array passed in.
     * @since 2.1
     */
public static java.lang.Class<?>[] primitivesToWrappers(java.lang.Class<?>[] classes) {
        if (classes == null) {
            return null;
        } 
        if ((classes.length) == 0) {
            return classes;
        } 
        java.lang.Class<?>[] convertedClasses = new java.lang.Class[classes.length];
        for (int i = 0 ; i < (classes.length) ; i++) {
            convertedClasses[i] = org.apache.commons.lang.ClassUtils.primitiveToWrapper(classes[i]);
        }
        return convertedClasses;
    }

    /** 
     * <p>Converts the specified wrapper class to its corresponding primitive
     * class.</p>
     * 
     * <p>This method is the counter part of <code>primitiveToWrapper()</code>.
     * If the passed in class is a wrapper class for a primitive type, this
     * primitive type will be returned (e.g. <code>Integer.TYPE</code> for
     * <code>Integer.class</code>). For other classes, or if the parameter is
     * <b>null</b>, the return value is <b>null</b>.</p>
     * 
     * @param cls the class to convert, may be <b>null</b>
     * @return the corresponding primitive type if <code>cls</code> is a
     * wrapper class, <b>null</b> otherwise
     * @see #primitiveToWrapper(Class)
     * @since 2.4
     */
public static java.lang.Class<?> wrapperToPrimitive(java.lang.Class<?> cls) {
        return wrapperPrimitiveMap.get(cls);
    }

    /** 
     * <p>Converts the specified array of wrapper Class objects to an array of
     * its corresponding primitive Class objects.</p>
     * 
     * <p>This method invokes <code>wrapperToPrimitive()</code> for each element
     * of the passed in array.</p>
     * 
     * @param classes  the class array to convert, may be null or empty
     * @return an array which contains for each given class, the primitive class or
     * <b>null</b> if the original class is not a wrapper class. <code>null</code> if null input.
     * Empty array if an empty array passed in.
     * @see #wrapperToPrimitive(Class)
     * @since 2.4
     */
public static java.lang.Class<?>[] wrappersToPrimitives(java.lang.Class<?>[] classes) {
        if (classes == null) {
            return null;
        } 
        if ((classes.length) == 0) {
            return classes;
        } 
        java.lang.Class<?>[] convertedClasses = new java.lang.Class[classes.length];
        for (int i = 0 ; i < (classes.length) ; i++) {
            convertedClasses[i] = org.apache.commons.lang.ClassUtils.wrapperToPrimitive(classes[i]);
        }
        return convertedClasses;
    }

    /** 
     * <p>Is the specified class an inner class or static nested class.</p>
     * 
     * @param cls  the class to check, may be null
     * @return <code>true</code> if the class is an inner or static nested class,
     * false if not or <code>null</code>
     */
public static boolean isInnerClass(java.lang.Class<?> cls) {
        if (cls == null) {
            return false;
        } 
        return (cls.getName().indexOf(INNER_CLASS_SEPARATOR_CHAR)) >= 0;
    }

    /** 
     * Returns the class represented by <code>className</code> using the
     * <code>classLoader</code>.  This implementation supports names like
     * "<code>java.lang.String[]</code>" as well as "<code>[Ljava.lang.String;</code>".
     * 
     * @param classLoader  the class loader to use to load the class
     * @param className  the class name
     * @param initialize  whether the class must be initialized
     * @return the class represented by <code>className</code> using the <code>classLoader</code>
     * @throws ClassNotFoundException if the class is not found
     */
public static java.lang.Class<?> getClass(ClassLoader classLoader, java.lang.String className, boolean initialize) throws ClassNotFoundException {
        java.lang.Class<?> clazz;
        if (abbreviationMap.containsKey(className)) {
            java.lang.String clsName = "[" + (abbreviationMap.get(className));
            clazz = java.lang.Class.forName(clsName, initialize, classLoader).getComponentType();
        } else {
            clazz = java.lang.Class.forName(org.apache.commons.lang.ClassUtils.toCanonicalName(className), initialize, classLoader);
        }
        return clazz;
    }

    /** 
     * Returns the (initialized) class represented by <code>className</code>
     * using the <code>classLoader</code>.  This implementation supports names
     * like "<code>java.lang.String[]</code>" as well as
     * "<code>[Ljava.lang.String;</code>".
     * 
     * @param classLoader  the class loader to use to load the class
     * @param className  the class name
     * @return the class represented by <code>className</code> using the <code>classLoader</code>
     * @throws ClassNotFoundException if the class is not found
     */
public static java.lang.Class<?> getClass(ClassLoader classLoader, java.lang.String className) throws ClassNotFoundException {
        return org.apache.commons.lang.ClassUtils.getClass(classLoader, className, true);
    }

    /** 
     * Returns the (initialized) class represented by <code>className</code>
     * using the current thread's context class loader. This implementation
     * supports names like "<code>java.lang.String[]</code>" as well as
     * "<code>[Ljava.lang.String;</code>".
     * 
     * @param className  the class name
     * @return the class represented by <code>className</code> using the current thread's context class loader
     * @throws ClassNotFoundException if the class is not found
     */
public static java.lang.Class<?> getClass(java.lang.String className) throws ClassNotFoundException {
        return org.apache.commons.lang.ClassUtils.getClass(className, true);
    }

    /** 
     * Returns the class represented by <code>className</code> using the
     * current thread's context class loader. This implementation supports
     * names like "<code>java.lang.String[]</code>" as well as
     * "<code>[Ljava.lang.String;</code>".
     * 
     * @param className  the class name
     * @param initialize  whether the class must be initialized
     * @return the class represented by <code>className</code> using the current thread's context class loader
     * @throws ClassNotFoundException if the class is not found
     */
public static java.lang.Class<?> getClass(java.lang.String className, boolean initialize) throws ClassNotFoundException {
        ClassLoader contextCL = java.lang.Thread.currentThread().getContextClassLoader();
        ClassLoader loader = contextCL == null ? org.apache.commons.lang.ClassUtils.class.getClassLoader() : contextCL;
        return org.apache.commons.lang.ClassUtils.getClass(loader, className, initialize);
    }

    /** 
     * <p>Returns the desired Method much like <code>Class.getMethod</code>, however
     * it ensures that the returned Method is from a public class or interface and not
     * from an anonymous inner class. This means that the Method is invokable and
     * doesn't fall foul of Java bug
     * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4071957">4071957</a>).
     * 
     * <code><pre>Set set = Collections.unmodifiableSet(...);
     * Method method = ClassUtils.getPublicMethod(set.getClass(), "isEmpty",  new Class[0]);
     * Object result = method.invoke(set, new Object[]);</pre></code>
     * </p>
     * 
     * @param cls  the class to check, not null
     * @param methodName  the name of the method
     * @param parameterTypes  the list of parameters
     * @return the method
     * @throws NullPointerException if the class is null
     * @throws SecurityException if a a security violation occured
     * @throws NoSuchMethodException if the method is not found in the given class
     * or if the metothod doen't conform with the requirements
     */
public static Method getPublicMethod(java.lang.Class<?> cls, java.lang.String methodName, java.lang.Class<?>[] parameterTypes) throws NoSuchMethodException, SecurityException {
        Method declaredMethod = cls.getMethod(methodName, parameterTypes);
        if (java.lang.reflect.Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
            return declaredMethod;
        } 
        List<java.lang.Class<?>> candidateClasses = new ArrayList<java.lang.Class<?>>();
        candidateClasses.addAll(org.apache.commons.lang.ClassUtils.getAllInterfaces(cls));
        candidateClasses.addAll(org.apache.commons.lang.ClassUtils.getAllSuperclasses(cls));
        for (java.lang.Class<?> candidateClass : candidateClasses) {
            if (!(java.lang.reflect.Modifier.isPublic(candidateClass.getModifiers()))) {
                continue;
            } 
            Method candidateMethod;
            try {
                candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException ex) {
                continue;
            }
            if (java.lang.reflect.Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
                return candidateMethod;
            } 
        }
        throw new NoSuchMethodException(((("Can\'t find a public method for " + methodName) + " ") + (org.apache.commons.lang.ArrayUtils.toString(parameterTypes))));
    }

    /** 
     * Converts a class name to a JLS style class name.
     * 
     * @param className  the class name
     * @return the converted name
     */
private static java.lang.String toCanonicalName(java.lang.String className) {
        className = org.apache.commons.lang.StringUtils.deleteWhitespace(className);
        if (className == null) {
            throw new NullPointerException("className must not be null.");
        } else if (className.endsWith("[]")) {
            StringBuffer classNameBuffer = new StringBuffer();
            while (className.endsWith("[]")) {
                className = className.substring(0, ((className.length()) - 2));
                classNameBuffer.append("[");
            }
            java.lang.String abbreviation = abbreviationMap.get(className);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            } else {
                classNameBuffer.append("L").append(className).append(";");
            }
            className = classNameBuffer.toString();
        } 
        return className;
    }

    /** 
     * <p>Converts an array of <code>Object</code> in to an array of <code>Class</code> objects.</p>
     * 
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     * 
     * @param array an <code>Object</code> array
     * @return a <code>Class</code> array, <code>null</code> if null array input
     * @since 2.4
     */
public static java.lang.Class<?>[] toClass(Object[] array) {
        if (array == null) {
            return null;
        } else if ((array.length) == 0) {
            return org.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY;
        } 
        java.lang.Class<?>[] classes = new java.lang.Class[array.length];
        for (int i = 0 ; i < (array.length) ; i++) {
            classes[i] = array[i].getClass();
        }
        return classes;
    }

    /** 
     * <p>Gets the canonical name minus the package name for an <code>Object</code>.</p>
     * 
     * @param object  the class to get the short name for, may be null
     * @param valueIfNull  the value to return if null
     * @return the canonical name of the object without the package name, or the null value
     * @since 2.4
     */
public static java.lang.String getShortCanonicalName(Object object, java.lang.String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        } 
        return org.apache.commons.lang.ClassUtils.getShortCanonicalName(object.getClass().getName());
    }

    /** 
     * <p>Gets the canonical name minus the package name from a <code>Class</code>.</p>
     * 
     * @param cls  the class to get the short name for.
     * @return the canonical name without the package name or an empty string
     * @since 2.4
     */
public static java.lang.String getShortCanonicalName(java.lang.Class<?> cls) {
        if (cls == null) {
            return org.apache.commons.lang.StringUtils.EMPTY;
        } 
        return org.apache.commons.lang.ClassUtils.getShortCanonicalName(cls.getName());
    }

    /** 
     * <p>Gets the canonical name minus the package name from a String.</p>
     * 
     * <p>The string passed in is assumed to be a canonical name - it is not checked.</p>
     * 
     * @param canonicalName  the class name to get the short name for
     * @return the canonical name of the class without the package name or an empty string
     * @since 2.4
     */
public static java.lang.String getShortCanonicalName(java.lang.String canonicalName) {
        return org.apache.commons.lang.ClassUtils.getShortClassName(org.apache.commons.lang.ClassUtils.getCanonicalName(canonicalName));
    }

    /** 
     * <p>Gets the package name from the canonical name of an <code>Object</code>.</p>
     * 
     * @param object  the class to get the package name for, may be null
     * @param valueIfNull  the value to return if null
     * @return the package name of the object, or the null value
     * @since 2.4
     */
public static java.lang.String getPackageCanonicalName(Object object, java.lang.String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        } 
        return org.apache.commons.lang.ClassUtils.getPackageCanonicalName(object.getClass().getName());
    }

    /** 
     * <p>Gets the package name from the canonical name of a <code>Class</code>.</p>
     * 
     * @param cls  the class to get the package name for, may be <code>null</code>.
     * @return the package name or an empty string
     * @since 2.4
     */
public static java.lang.String getPackageCanonicalName(java.lang.Class<?> cls) {
        if (cls == null) {
            return org.apache.commons.lang.StringUtils.EMPTY;
        } 
        return org.apache.commons.lang.ClassUtils.getPackageCanonicalName(cls.getName());
    }

    /** 
     * <p>Gets the package name from the canonical name. </p>
     * 
     * <p>The string passed in is assumed to be a canonical name - it is not checked.</p>
     * <p>If the class is unpackaged, return an empty string.</p>
     * 
     * @param canonicalName  the canonical name to get the package name for, may be <code>null</code>
     * @return the package name or an empty string
     * @since 2.4
     */
public static java.lang.String getPackageCanonicalName(java.lang.String canonicalName) {
        return org.apache.commons.lang.ClassUtils.getPackageName(org.apache.commons.lang.ClassUtils.getCanonicalName(canonicalName));
    }

    /** 
     * <p>Converts a given name of class into canonical format.
     * If name of class is not a name of array class it returns
     * unchanged name.</p>
     * <p>Example:
     * <ul>
     * <li><code>getCanonicalName("[I") = "int[]"</code></li>
     * <li><code>getCanonicalName("[Ljava.lang.String;") = "java.lang.String[]"</code></li>
     * <li><code>getCanonicalName("java.lang.String") = "java.lang.String"</code></li>
     * </ul>
     * </p>
     * 
     * @param className the name of class
     * @return canonical form of class name
     * @since 2.4
     */
private static java.lang.String getCanonicalName(java.lang.String className) {
        className = org.apache.commons.lang.StringUtils.deleteWhitespace(className);
        if (className == null) {
            return null;
        } else {
            int dim = 0;
            while (className.startsWith("[")) {
                dim++;
                className = className.substring(1);
            }
            if (dim < 1) {
                return className;
            } else {
                if (className.startsWith("L")) {
                    className = className.substring(1, (className.endsWith(";") ? (className.length()) - 1 : className.length()));
                } else {
                    if ((className.length()) > 0) {
                        className = reverseAbbreviationMap.get(className.substring(0, 1));
                    } 
                }
                StringBuffer canonicalClassNameBuffer = new StringBuffer(className);
                for (int i = 0 ; i < dim ; i++) {
                    canonicalClassNameBuffer.append("[]");
                }
                return canonicalClassNameBuffer.toString();
            }
        }
    }
}

