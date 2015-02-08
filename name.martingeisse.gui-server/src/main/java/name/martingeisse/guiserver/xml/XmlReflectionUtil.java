/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import com.google.common.primitives.Primitives;

/**
 * Reflection utilities used by the XML parser.
 */
public final class XmlReflectionUtil {

	/**
	 * Finds and invokes a constructor that is suitable for the specified arguments.
	 * 
	 * @param theClass the class to create an instance of
	 * @param arguments the constructor arguments
	 * @return the newly created instance
	 */
	public static <T> T invokeSuitableConstructor(Class<T> theClass, Object[] arguments) {
		constructorLoop: for (Constructor<?> constructor : theClass.getConstructors()) {
			Parameter[] parameters = constructor.getParameters();
			if (parameters.length != arguments.length) {
				continue;
			}
			for (int i=0; i<arguments.length; i++) {
				Parameter parameter = parameters[i];
				Object argument = arguments[i];
				if (parameter.getType().isPrimitive()) {
					if (argument == null) {
						// passing null for a primitive type
						continue constructorLoop;
					} else if (Primitives.wrap(parameter.getType()) != argument.getClass()) {
						// passing some unrelated value for a primitive type
						continue constructorLoop;
					} // else: passing a value of the boxed type for the primitive type is ok
				} else if (argument == null) {
					// passing null is ok for non-primitive types
				} else {
					if (!parameter.getType().isAssignableFrom(argument.getClass())) {
						// passing a value of the wrong type for this constructor
						continue constructorLoop;
					} // else: passing a value of the exact type or subtype is ok
				}
			}
			
			// this constructor will work for us
			try {
				return theClass.cast(constructor.newInstance(arguments));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
		throw new RuntimeException("no viable constructor found for class: " + theClass);
	}
	
}
