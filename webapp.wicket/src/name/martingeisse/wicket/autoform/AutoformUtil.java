/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriptor;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor;

/**
 * Utility methods to deal with autoform objects.
 */
public class AutoformUtil {

	/**
	 * Prevent instantiation.
	 */
	private AutoformUtil() {
	}

	/**
	 * Collects all properties, for which {@link IAutoformPropertyDescriptor#isReadOnly()} returns the specified value.
	 * @param beanDescriptor the bean descriptor
	 * @param expectedReturnValue the return value from {@link IAutoformPropertyDescriptor#isReadOnly()}
	 * that is expected from properties to be included in the result
	 * @return the writable properties
	 */
	public static List<IAutoformPropertyDescriptor> getPropertiesByReadOnlyFlag(IAutoformBeanDescriptor beanDescriptor, boolean expectedReturnValue) {
		List<IAutoformPropertyDescriptor> result = new ArrayList<IAutoformPropertyDescriptor>();
		for (IAutoformPropertyDescriptor property : beanDescriptor.getPropertyDescriptors()) {
			if (property.isReadOnly() == expectedReturnValue) {
				result.add(property);
			}
		}
		return result;
	}

	/**
	 * Extracts the names of the specified properties and returns them as a list.
	 * @param properties the properties
	 * @return the names
	 */
	public static List<String> getPropertyNames(List<IAutoformPropertyDescriptor> properties) {
		List<String> result = new ArrayList<String>();
		for (IAutoformPropertyDescriptor property : properties) {
			result.add(property.getName());
		}
		return result;
	}
	
	/**
	 * Extracts the names of the specified properties and returns them as an array.
	 * @param properties the properties
	 * @return the names
	 */
	public static String[] getPropertyNamesAsArray(List<IAutoformPropertyDescriptor> properties) {
		String[] result = new String[properties.size()];
		int i=0;
		for (IAutoformPropertyDescriptor property : properties) {
			result[i] = property.getName();
			i++;
		}
		return result;
	}
	
}
