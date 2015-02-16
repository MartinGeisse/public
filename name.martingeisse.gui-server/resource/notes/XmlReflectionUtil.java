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
	
		// no constructor found, so build an error message
		StringBuilder builder = new StringBuilder();
		builder.append("no suitable constructor found for class: ").append(theClass.getSimpleName());
		builder.append(" and arguments (");
		{
			boolean first = true;
			for (Object argument : arguments) {
				if (first) {
					first = false;
				} else {
					builder.append(", ");
				}
				builder.append(argument);
				if (argument != null) {
					builder.append(": ").append(argument.getClass().getSimpleName());
				}
			}
		}
		builder.append("). found constructors: [");
		{
			boolean firstConstructor = true;
			for (Constructor<?> constructor : theClass.getConstructors()) {
				if (firstConstructor) {
					firstConstructor = false;
				} else {
					builder.append(", ");
				}
				builder.append(theClass.getSimpleName()).append('(');
				boolean firstParameter = true;
				for (Parameter parameter : constructor.getParameters()) {
					if (firstParameter) {
						firstParameter = false;
					} else {
						builder.append(", ");
					}
					builder.append(parameter.getType().getSimpleName());
				}
				builder.append(')');
			}
		}
		builder.append(']');
		throw new RuntimeException(builder.toString());
	}
	
}
