/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * This class takes a template string that refers to positional
 * parameters as well as a sequence of argument values and
 * returns a string in which the parameter references have been
 * replaced by actual values.
 * 
 * The parameter references are 0-based and written as decimal numbers
 * enclosed by curly braces.
 * 
 * An {@link IllegalArgumentException} occurs if the template
 * string contains a negative reference or a reference outside the
 * bounds of the argument array. This exception type also occurs
 * on lower-level syntax errors, e.g. if the parameter reference
 * is not a number or if braces are not properly matched.
 * 
 * The arguments array reference must not be null even if there is
 * not a single parameter reference.
 */
public class StringParameterReplaceUtil {

	/**
	 * Prevent instantiation.
	 */
	private StringParameterReplaceUtil() {
	}
	
	/**
	 * Replaces parameter references in the specified template string and returns the result.
	 * @param template the template string
	 * @param arguments the argument sequence
	 * @return the result
	 */
	public static String replace(String template, String... arguments) {
		
		/** check for a null argument array **/
		if (arguments == null) {
			throw new IllegalArgumentException("arguments array referece is null");
		}
		
		/** build the result string **/
		StringBuilder resultBuilder = new StringBuilder();
		int index = 0;
		while (true) {
			int openingBraceIndex = template.indexOf('{', index);
			int closingBraceIndex = template.indexOf('}', index);
			
			/** case: no more parameter references **/
			if (openingBraceIndex == -1 && closingBraceIndex == -1) {
				resultBuilder.append(template, index, template.length());
				break;
			}
			
			/** case: mismatched braces (either only opening or only closing brace found) **/
			if (openingBraceIndex == -1 || closingBraceIndex == -1) {
				throw new IllegalArgumentException("invalid template string: [" + template + "]");
			}
			
			/** case: mismatched braces (they appear in the wrong order) **/
			if (openingBraceIndex > closingBraceIndex) {
				throw new IllegalArgumentException("invalid template string: [" + template + "]");
			}
			
			/** copy characters up to the opening brace **/
			resultBuilder.append(template, index, openingBraceIndex);
			
			/** parse the parameter index (may fail if not a number) **/
			String parameterReferenceText = template.substring(openingBraceIndex + 1, closingBraceIndex);
			int parameterIndex;
			try {
				parameterIndex = Integer.parseInt(parameterReferenceText);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("invalid parameter reference [" + parameterReferenceText + "] in template string: [" + template + "]");
			}
			
			/** make sure the index is within the bounds of the argument array **/
			if (parameterIndex < 0 || parameterIndex >= arguments.length) {
				throw new IllegalArgumentException("parameter index [" + parameterIndex + "] out of bounds; number of arguments: [" + arguments.length + "]");
			}
			
			/** the reference is valid, so copy the argument and skip the reference **/
			resultBuilder.append(arguments[parameterIndex]);
			index = closingBraceIndex + 1;
			
		}
		return resultBuilder.toString();
	}
	
}
