/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.jdbc;

/**
 * Utility methods to build SQL queries.
 */
public class SqlUtil {

	/**
	 * Builds an IN() clause.
	 * @param values the values to write into the IN clause
	 * @return the IN clause with the specified values
	 */
	public static String in(int... values) {
		StringBuilder builder = new StringBuilder();
		in(builder, values);
		return builder.toString();
	}

	/**
	 * Builds an IN() clause.
	 * @param builder the string builder to write to
	 * @param values the values to write into the IN clause
	 */
	public static void in(StringBuilder builder, int... values) {
		builder.append(" IN(");
		boolean first = true;
		for (int value : values) {
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(value);
		}
		builder.append(") ");
	}
	
}
