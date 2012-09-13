/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import java.io.Serializable;

import name.martingeisse.common.util.string.StringUtil;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;

/**
 * Utility methods to deal with Wicket type converters.
 */
public class WicketConverterUtil {

	/**
	 * Prevent instantiation.
	 */
	private WicketConverterUtil() {
	}
	
	/**
	 * Converts a value to a string. The type converter and locale are taken
	 * from the specified context component. Null values are converted to the
	 * empty string.
	 * 
	 * @param value the value to convert
	 * @param context the context that provides the type converter and the locale
	 * @return the converted value
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static String convertValueToString(Object value, Component context) {
		if (value == null) {
			return "";
		} else {
			IConverter converter = context.getConverter(value.getClass());
			return converter.convertToString(value, context.getLocale());
		}
	}
	
	/**
	 * See {{@link #convertValueToString(Object, Component)} for a general
	 * description of this method. This version also limits the length of the
	 * output to the specified maximum number of characters.
	 * 
	 * @param value the value to convert
	 * @param context the context that provides the type converter and the locale
	 * @param maxLength the maximum length of the output string. Must be at
	 * least 3 to allow for an ellipsis to be inserted.
	 * @return the converted value
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static String convertValueToString(Object value, Component context, int maxLength) {
		return StringUtil.limitLength(convertValueToString(value, context), maxLength);
	}

	/**
	 * Creates a model to be used with a label. If the value is {@link Serializable},
	 * then it will be returned wrapped in a {@link Model}. Otherwise, a {@link Model}
	 * is created with the value turned to a string using
	 * {{@link #convertValueToString(Object, Component)}. This is also the reason
	 * why this method is only useful with a label (or at least a component that
	 * behaves like a label) -- other components have little use for a string instead
	 * of the original value.
	 * 
	 * Note: there is no version of this method that accepts a maximum length.
	 * Such a method would require to convert *all* values to string in advance, which
	 * would increase session size dramatically. Use a custom label that limits
	 * output length instead.
	 * 
	 * @param value the value
	 * @param context the context component, used in case a string is needed
	 * @return the model
	 */
	public static IModel<?> createLabelModel(Object value, Component context) {
		if (value instanceof Serializable) {
			return Model.of((Serializable)value);
		} else {
			return Model.of(convertValueToString(value, context));
		}
	}
	
}
