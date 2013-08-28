/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.analyze;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

/**
 * This class provides basic functionality to dissect a JSON
 * value parsed by JSON-Simple.
 */
public final class JsonAnalyzer {

	/**
	 * the value
	 */
	private final Object value;

	/**
	 * the parent
	 */
	private final JsonAnalyzer parent;

	/**
	 * the contextName
	 */
	private final String contextName;

	/**
	 * Constructor.
	 * @param value the value to analyze
	 */
	public JsonAnalyzer(final Object value) {
		this(value, null, null);
	}

	/**
	 * Constructor.
	 * @param value the value to analyze
	 * @param parent the parent value
	 * @param contextName the name of this field in the parent value
	 */
	private JsonAnalyzer(final Object value, final JsonAnalyzer parent, final String contextName) {
		if ((parent == null) != (contextName == null)) {
			throw new IllegalArgumentException("must either pass both parent and context name or none");
		}
		this.value = value;
		this.parent = parent;
		this.contextName = contextName;
	}

	/**
	 * Parses a JSON-encoded string and analyzes the result.
	 * @param json the JSON-encoded string
	 * @return the analyzer
	 */
	public static JsonAnalyzer parse(final String json) {
		return new JsonAnalyzer(JSONValue.parse(json));
	}
	
	/**
	 * Parses a JSON-encoded string and analyzes the result.
	 * @param jsonReader the reader for the JSON-encoded string
	 * @return the analyzer
	 */
	public static JsonAnalyzer parse(final Reader jsonReader) {
		return new JsonAnalyzer(JSONValue.parse(jsonReader));
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 
	 */
	private void buildContextDescription(final StringBuilder builder) {
		if (parent != null) {
			parent.buildContextDescription(builder);
			if (parent.parent != null) {
				builder.append('.');
			}
			builder.append(contextName);
		}
	}

	/**
	 * Checks if the value is null.
	 * @return true if null, false otherwise
	 */
	public boolean isNull() {
		return (value == null);
	}
	
	/**
	 * Checks if the value is a list.
	 * @return true if list, false otherwise
	 */
	public boolean isList() {
		return (value instanceof List<?>);
	}

	/**
	 * Checks if the value is a map.
	 * @return true if map, false otherwise
	 */
	public boolean isMap() {
		return (value instanceof Map<?, ?>);
	}
	
	/**
	 * Returns a new analyzer that contains either this value or, if
	 * this value is null, the specified fallback value.
	 * @param fallbackValue the fallback value
	 * @return the new analyzer
	 */
	public JsonAnalyzer fallback(final Object fallbackValue) {
		return new JsonAnalyzer(value == null ? fallbackValue : value, parent, contextName);
	}

	/**
	 * Tries to cast the value to {@link Boolean}. Returns the value
	 * if the cast succeeds, null otherwise.
	 * @return the boolean value or null
	 */
	public Boolean tryBoolean() {
		return (value instanceof Boolean) ? (Boolean)value : null;
	}

	/**
	 * Expects the value to be of boolean type and returns its value.
	 * Throws a {@link JsonAnalysisException} for non-boolean values.
	 * @return the boolean value
	 */
	public boolean expectBoolean() {
		if (value instanceof Boolean) {
			return (Boolean)value;
		}
		throw expectedException("boolean");
	}

	/**
	 * Tries to cast the value to {@link Integer}. Returns the value
	 * if the cast succeeds, null otherwise.
	 * @return the integer value or null
	 */
	public Integer tryInteger() {
		return (value instanceof Number) ? ((Number)value).intValue() : null;
	}

	/**
	 * Expects the value to be of integer type and returns its value.
	 * Throws a {@link JsonAnalysisException} for non-integer values.
	 * @return the integer value
	 */
	public int expectInteger() {
		if (value instanceof Number) {
			return ((Number)value).intValue();
		}
		throw expectedException("integer");
	}

	/**
	 * Returns null if the value is null. Otherwise turns the value
	 * into a string using {@link Object#toString()}, then parses
	 * it as an integer. Throws a {@link JsonAnalysisException}
	 * if parsing fails.
	 * @return the parsed integer or null
	 */
	public Integer toIntegerOrNull() {
		if (value == null) {
			return null;
		} else {
			try {
				return new Integer(value.toString());
			} catch (final NumberFormatException e) {
				throw expectedException("integer");
			}
		}
	}

	/**
	 * Turns the value into a string using {@link Object#toString()},
	 * then parses it as an integer. Throws a {@link JsonAnalysisException}
	 * if parsing fails.
	 * @return the parsed integer
	 */
	public int toInt() {
		try {
			return Integer.parseInt(value.toString());
		} catch (final Exception e) {
			throw expectedException("integer");
		}
	}

	/**
	 * Tries to cast the value to {@link Long}. Returns the value
	 * if the cast succeeds, null otherwise.
	 * @return the long value or null
	 */
	public Long tryLong() {
		return (value instanceof Number) ? ((Number)value).longValue() : null;
	}

	/**
	 * Expects the value to be of long type and returns its value.
	 * Throws a {@link JsonAnalysisException} for non-long values.
	 * @return the long value
	 */
	public long expectLong() {
		if (value instanceof Number) {
			return ((Number)value).longValue();
		}
		throw expectedException("long");
	}

	/**
	 * Returns null if the value is null. Otherwise turns the value
	 * into a string using {@link Object#toString()}, then parses
	 * it as an long. Throws a {@link JsonAnalysisException}
	 * if parsing fails.
	 * @return the parsed long or null
	 */
	public Long toLongOrNull() {
		if (value == null) {
			return null;
		} else {
			try {
				return new Long(value.toString());
			} catch (final NumberFormatException e) {
				throw expectedException("long");
			}
		}
	}

	/**
	 * Turns the value into a string using {@link Object#toString()},
	 * then parses it as an long. Throws a {@link JsonAnalysisException}
	 * if parsing fails.
	 * @return the parsed long
	 */
	public long toLong() {
		try {
			return Long.parseLong(value.toString());
		} catch (final Exception e) {
			throw expectedException("long");
		}
	}

	/**
	 * Tries to cast the value to {@link Double}. Returns the value
	 * if the cast succeeds, null otherwise.
	 * @return the double value or null
	 */
	public Double tryDouble() {
		return (value instanceof Number) ? ((Number)value).doubleValue() : null;
	}
	
	/**
	 * Expects the value to be of double type and returns its value.
	 * Throws a {@link JsonAnalysisException} for non-double values.
	 * @return the double value
	 */
	public double expectDouble() {
		if (value instanceof Number) {
			return ((Number)value).doubleValue();
		}
		throw expectedException("double");
	}
	
	/**
	 * Returns null if the value is null. Otherwise turns the value
	 * into a string using {@link Object#toString()}, then parses
	 * it as an double. Throws a {@link JsonAnalysisException}
	 * if parsing fails.
	 * @return the parsed double or null
	 */
	public Double toDoubleOrNull() {
		if (value == null) {
			return null;
		} else {
			try {
				return new Double(value.toString());
			} catch (final NumberFormatException e) {
				throw expectedException("double");
			}
		}
	}
	
	/**
	 * Turns the value into a string using {@link Object#toString()},
	 * then parses it as an double. Throws a {@link JsonAnalysisException}
	 * if parsing fails.
	 * @return the parsed double
	 */
	public double toDouble() {
		try {
			return Double.parseDouble(value.toString());
		} catch (final Exception e) {
			throw expectedException("double");
		}
	}
	
	/**
	 * Tries to cast the value to {@link String}. Returns the value
	 * if the cast succeeds, null otherwise.
	 * @return the string value or null
	 */
	public String tryString() {
		return (value instanceof String) ? (String)value : null;
	}

	/**
	 * Expects the value to be of string type and returns its value.
	 * Throws a {@link JsonAnalysisException} for non-string values.
	 * @return the string value
	 */
	public String expectString() {
		if (value instanceof String) {
			return (String)value;
		}
		throw expectedException("string");
	}

	/**
	 * Returns null if the value is null. Otherwise turns the value
	 * into a string using {@link Object#toString()}.
	 * @return the result of {@link Object#toString()} or null
	 */
	public String toStringOrNull() {
		return (value == null ? null : value.toString());
	}

	/**
	 * Throws a {@link JsonAnalysisException} if the value is null.
	 * Otherwise turns the value into a string using {@link Object#toString()}.
	 * @return the result of {@link Object#toString()}
	 */
	public String toStringNotNull() {
		if (value == null) {
			throw exception("null not allowed here");
		} else {
			return value.toString();
		}
	}

	/**
	 * Tries to cast the value to {@link List}. Returns the value
	 * if the cast succeeds, null otherwise.
	 * @return the list value or null
	 */
	@SuppressWarnings("unchecked")
	public List<Object> tryList() {
		return (value instanceof List) ? (List<Object>)value : null;
	}

	/**
	 * Expects the value to be of list type and returns its value.
	 * Throws a {@link JsonAnalysisException} for non-list values.
	 * @return the list value
	 */
	@SuppressWarnings("unchecked")
	public List<Object> expectList() {
		if (value instanceof List) {
			return (List<Object>)value;
		}
		throw expectedException("list");
	}

	/**
	 * Expects the value to be of list type, wraps each element in
	 * a {@link JsonAnalyzer}, and returns the analyzers in a list.
	 * @return the list of analyzers
	 */
	public List<JsonAnalyzer> analyzeList() {
		if (value instanceof List) {
			final List<?> list = (List<?>)value;
			final List<JsonAnalyzer> result = new ArrayList<JsonAnalyzer>();
			int i = 0;
			for (final Object element : list) {
				result.add(new JsonAnalyzer(element, this, Integer.toString(i)));
				i++;
			}
			return result;
		}
		throw expectedException("list");
	}

	/**
	 * Expects the value to be either of list type, or to represent a
	 * single-element list; wraps each element in a {@link JsonAnalyzer},
	 * and returns the analyzers in a list.
	 * 
	 * @return the list of analyzers
	 */
	public List<JsonAnalyzer> analyzeListOrSingle() {
		if (value instanceof List) {
			return analyzeList();
		} else {
			List<JsonAnalyzer> list = new ArrayList<JsonAnalyzer>();
			list.add(this);
			return list;
		}
	}
	
	/**
	 * Expects the value to be of list type, obtains the specified
	 * element, and wraps it in a new instance of this class. Unlike
	 * {@link #analyzeMapElement(String)}, this method will throw an
	 * exception if the index is out of range.
	 * 
	 * @param index the index to read from
	 * @return the analyzer for the list element
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public JsonAnalyzer analyzeListElement(final int index) throws IndexOutOfBoundsException {
		if (value instanceof List) {
			final List<?> list = (List<?>)value;
			return new JsonAnalyzer(list.get(index), this, Integer.toString(index));
		}
		throw expectedException("list");
	}
	
	/**
	 * Tries to cast the value to {@link Map}. Returns the value
	 * if the cast succeeds, null otherwise.
	 * @return the map value or null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> tryMap() {
		return (value instanceof Map) ? (Map<String, Object>)value : null;
	}

	/**
	 * Expects the value to be of map type and returns its value.
	 * Throws a {@link JsonAnalysisException} for non-map values.
	 * @return the map value
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> expectMap() {
		if (value instanceof Map) {
			return (Map<String, Object>)value;
		}
		throw expectedException("map");
	}

	/**
	 * Expects the value to be of map type, wraps each element in
	 * a {@link JsonAnalyzer}, and returns the analyzers in a map
	 * using the keys from the original map, turned to strings using
	 * {@link Object#toString()}.
	 * 
	 * @return the map of analyzers
	 */
	public Map<String, JsonAnalyzer> analyzeMap() {
		if (value instanceof Map) {
			final Map<?, ?> map = (Map<?, ?>)value;
			final Map<String, JsonAnalyzer> result = new HashMap<String, JsonAnalyzer>();
			for (final Map.Entry<?, ?> entry : map.entrySet()) {
				String key = entry.getKey().toString();
				result.put(key, new JsonAnalyzer(entry.getValue(), this, key));
			}
			return result;
		}
		throw expectedException("map");
	}

	/**
	 * Expects the value to be of map type, obtains the specified
	 * element, and wraps it in a new instance of this class.
	 * @param key the key to read from
	 * @return the analyzer for the map element
	 */
	public JsonAnalyzer analyzeMapElement(final String key) {
		if (value instanceof Map) {
			final Map<?, ?> map = (Map<?, ?>)value;
			return new JsonAnalyzer(map.get(key), this, key);
		}
		throw expectedException("map");
	}

	/**
	 * Helper method to create {@link JsonAnalysisException}s for
	 * unexpected values.
	 * @param what a description of what was expected
	 * @return the exception
	 */
	public JsonAnalysisException expectedException(String what) {
		return exception("expected " + what + ", found " + value + (value == null ? "" : " (" + value.getClass().getSimpleName() + ")"));
	}
	
	/**
	 * Helper method to create {@link JsonAnalysisException}s.
	 */
	private JsonAnalysisException exception(final String message) {
		final StringBuilder builder = new StringBuilder();
		buildContextDescription(builder);
		if (parent != null) {
			builder.append(": ");
		}
		builder.append(message);
		return new JsonAnalysisException(builder.toString());
	}

}
