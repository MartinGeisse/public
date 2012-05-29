/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.appmodel;

/**
 * Utility class to deal with application modeling strategy classes and
 * corresponding dependency injection features.
 */
public class AppmodelStrategyUtil {

	/**
	 * Like createStrategyInstance(className, supertype), but uses the
	 * specified default implementation class in case the className
	 * argument is null.
	 * 
	 * @param <T> the expected supertype
	 * @param className the name of the class to instantiate
	 * @param supertype the class object for the expected supertype
	 * @param defaultClass the default class to use if className is null
	 * @return the instance
	 */
	public static <T> T createStrategyInstance(String className, Class<T> supertype, Class<? extends T> defaultClass) {
		checkArgumentNotNull(supertype, "supertype");
		checkArgumentNotNull(defaultClass, "defaultClass");
		if (className == null) {
			return createStrategyInstance(defaultClass, supertype);
		} else {
			return createStrategyInstance(className, supertype);
		}
	}
	
	/**
	 * Creates an instance of the specified class using its no-arg constructor and
	 * casts it to the expected supertype (typically an interface type).
	 * 
	 * @param <T> the expected supertype
	 * @param className the name of the class to instantiate
	 * @param supertype the class object for the expected supertype
	 * @return the instance
	 */
	public static <T> T createStrategyInstance(String className, Class<T> supertype) {
		
		/** error handling **/
		checkArgumentNotNull(className, "className");
		checkArgumentNotNull(supertype, "supertype");

		/** obtain the specified class **/
		Class<?> specifiedClass;
		try {
			specifiedClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("invalid strategy class name: \"" + className + "\" (class not found)", e);
		}
		
		/** common handling **/
		return createStrategyInstance(specifiedClass, supertype);
		
	}

	/**
	 * Like createStrategyInstance(implementationClass, supertype), but uses the
	 * specified default implementation class in case the implementationClass
	 * argument is null.
	 * 
	 * @param <T> the expected supertype
	 * @param implementationClass the class to instantiate
	 * @param supertype the class object for the expected supertype
	 * @param defaultClass the default class to use if implementationClass is null
	 * @return the instance
	 */
	public static <T> T createStrategyInstance(Class<? extends T> implementationClass, Class<T> supertype, Class<? extends T> defaultClass) {
		checkArgumentNotNull(supertype, "supertype");
		checkArgumentNotNull(defaultClass, "defaultClass");
		return createStrategyInstance(implementationClass == null ? defaultClass : implementationClass, supertype);
	}
	
	/**
	 * Creates an instance of the specified class using its no-arg constructor and
	 * casts it to the expected supertype (typically an interface type).
	 * 
	 * @param <T> the expected supertype
	 * @param implementationClass the class to instantiate
	 * @param supertype the class object for the expected supertype
	 * @return the instance
	 */
	public static <T> T createStrategyInstance(Class<?> implementationClass, Class<T> supertype) {
		
		/** error handling **/
		checkArgumentNotNull(implementationClass, "implementationClass");
		checkArgumentNotNull(supertype, "supertype");
		
		/** prepare **/
		String className = implementationClass.getCanonicalName();

		/** make sure it implements the strategy interface before we even create an instance **/
		if (!supertype.isAssignableFrom(implementationClass)) {
			throw new IllegalArgumentException("invalid strategy class: \"" + className + "\" (does not implement " + supertype.getCanonicalName() + ")");
		}

		/** try to create an instance using the default constructor **/
		Object instance;
		try {
			instance = implementationClass.newInstance();
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("invalid strategy class: \"" + className + "\" (cannot access class or default constructor)", e);
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("invalid strategy class: \"" + className + "\" (instantiation exception: abstract class, array class, interface, primitive type, void, or no no-arg constructor)", e);
		}

		/** this cast should always succeed since we checked above, but let's be sure **/
		try {
			return supertype.cast(instance);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("invalid strategy class: \"" + className + "\" (cannot cast to " + supertype.getCanonicalName() + " despite previous check)", e);
		}
		
	}

	/**
	 * Ensures that the specified argument value is not null, and throws an {@link IllegalArgumentException}
	 * if it is.
	 * @param argument the argument to check
	 * @param name the name of the argument to use in the exception message
	 */
	private static void checkArgumentNotNull(Object argument, String name) {
		if (argument == null) {
			throw new IllegalArgumentException("error: " + name + " argument is null");
		}
	}
	
}
