/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.validation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import name.martingeisse.common.util.StringParameterReplaceUtil;

import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * An error that occurred while validating some object. A validation error
 * may or may not be fatal. Typically, however, it is related to erroneous
 * user input and should cause a user-visible notification to allow the
 * user to correct the error.
 * 
 * A validation error contains a location key that specifies where it occurred. Although
 * this class does not impose any restrictions on the semantics of such
 * location keys, they are typically property paths as used by commons-beanutils.
 * 
 * The message associated with a validation error consists of a message key and a
 * sequence of string argument values, and is subject to two processing steps to yield
 * an actual user-readable message:
 * 
 * - the key is resolved to a template string using an error message resolver.
 *   This can be an arbitrary string-to-string mapping mechanism as long as the
 *   resulting template string is valid according to {@link StringParameterReplaceUtil}
 *   and the supplied argument sequence. This step is typically used for i18n.
 *
 * - parameter references in the template string are matched against the supplied
 *   argument list and replaced by the corresponding values, as defined by
 *   {@link StringParameterReplaceUtil}. This step is typically used to make
 *   fixed i18n error message values configurable for the context in which they
 *   appear. For example, an error message for exceeding maximum input length and
 *   with the locale set to english might use the template string "Please enter at
 *   most {0} digits." Thus, the string must be translated only once and can still be
 *   applied to arbitrary maximum input lengths.
 */
public class ValidationError implements Serializable {

	/**
	 * the locationKey
	 */
	private final String locationKey;
	
	/**
	 * the messageKey
	 */
	private final String messageKey;
	
	/**
	 * the arguments
	 */
	private final String[] arguments;
	
	/**
	 * Constructor.
	 * @param locationKey the location key
	 * @param messageKey the message key
	 * @param arguments the string arguments to be inserted into the template string
	 */
	public ValidationError(String locationKey, String messageKey, String... arguments) {
		
		/** sanity checks **/
		if (locationKey == null) {
			throw new IllegalArgumentException("locationKey argument is null");
		}
		if (messageKey == null) {
			throw new IllegalArgumentException("messageKey argument is null");
		}
		if (arguments == null) {
			throw new IllegalArgumentException("arguments argument is null");
		}
		
		/** Store arguments. Note that the arguments array is copied to make this class immutable. **/
		this.locationKey = locationKey;
		this.messageKey = messageKey;
		this.arguments = Arrays.copyOf(arguments, arguments.length);
		
	}

	/**
	 * Getter method for locationKey.
	 * @return the locationKey
	 */
	public String getLocationKey() {
		return locationKey;
	}

	/**
	 * Getter method for messageKey.
	 * @return the messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * Getter method for the number of arguments to be inserted into the template string.
	 * @return the number of arguments
	 */
	public int getArgumentCount() {
		return arguments.length;
	}
	
	/**
	 * Getter method for a specific argument to be inserted into the template string.
	 * @param index the argument index
	 * @return the argument at the specified index.
	 */
	public String getArgument(int index) {
		if (index < 0 || index > arguments.length) {
			throw new IllegalArgumentException("argument index out of bounds: [" + index + "], number of arguments: [" + arguments.length + "]");
		}
		return arguments[index];
	}

	/**
	 * Returns the arguments to be inserted into the template string as an {@link Iterable}.
	 * @return the arguments.
	 */
	public Iterable<String> getArguments() {
		return new Iterable<String>() {
			@Override
			@SuppressWarnings("unchecked")
			public Iterator<String> iterator() {
				return new ArrayIterator(arguments);
			}
		};
	}

	/**
	 * Generates the validation error message for this validation error.
	 * @param messageResolver the message resolver used to turn the message key into a message template.
	 * @return the message
	 */
	public String generateErrorMessage(IValidationErrorMessageResolver messageResolver) {
		return StringParameterReplaceUtil.replace(messageResolver.resolveValidationErrorMessage(messageKey), arguments);
	}
	
}
