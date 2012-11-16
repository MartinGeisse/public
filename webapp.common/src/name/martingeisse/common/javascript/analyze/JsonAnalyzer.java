/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.analyze;

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
	public JsonAnalyzer(Object value) {
		this(value, null, null);
	}

	/**
	 * Constructor.
	 * @param value the value to analyze
	 * @param parent the parent value
	 * @param contextName the name of this field in the parent value
	 */
	private JsonAnalyzer(Object value, JsonAnalyzer parent, String contextName) {
		this.value = value;
		this.parent = parent;
		this.contextName = contextName;
	}
	
	/**
	 * Parses a JSON-encoded string and analyzes the result.
	 * @param json the JSON-encoded string
	 * @return the analyzer
	 */
	public static JsonAnalyzer parse(String json) {
		return new JsonAnalyzer(JSONValue.parse(json));
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
	private void buildContextDescription(StringBuilder builder) {
		if (parent != null) {
			parent.buildContextDescription(builder);
			builder.append('.');
		}
		builder.append(contextName);
	}
	
	/**
	 * Checks if the value is null.
	 * @return true if null, false otherwise
	 */
	public boolean isNull() {
		return (value == null);
	}
	
	/**
	 * Returns a new analyzer that contains either this value or, if
	 * this value is null, the specified fallback value.
	 * @param fallbackValue the fallback value
	 * @return the new analyzer
	 */
	public JsonAnalyzer fallback(Object fallbackValue) {
		return new JsonAnalyzer(value == null ? fallbackValue : value);
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
		throw exception("expected boolean");
	}

	/**
	 * Tries to cast the value to {@link Integer}. Returns the value
	 * if the cast succeeds, null otherwise.
	 * @return the integer value or null
	 */
	public Integer tryInteger() {
		return (value instanceof Integer) ? (Integer)value : null;
	}
	
	/**
	 * Expects the value to be of integer type and returns its value.
	 * Throws a {@link JsonAnalysisException} for non-integer values.
	 * @return the integer value
	 */
	public int expectInteger() {
		if (value instanceof Integer) {
			return (Integer)value;
		}
		throw exception("expected integer");
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
			} catch (NumberFormatException e) {
				throw exception("expected integer");
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
		} catch (Exception e) {
			throw exception("expected integer");
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
		throw exception("expected string");
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
		throw exception("expected list");
	}
	
	/**
	 * Expects the value to be of list type, wraps each element in
	 * a {@link JsonAnalyzer}, and returns the analyzers in a list.
	 * @return the list of analyzers
	 */
	public List<JsonAnalyzer> analyzeList() {
		if (value instanceof List) {
			List<?> list = (List<?>)value;
			List<JsonAnalyzer> result = new ArrayList<JsonAnalyzer>();
			for (Object element : list) {
				result.add(new JsonAnalyzer(element));
			}
			return result;
		}
		throw exception("expected list");
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
		throw exception("expected map");
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
			Map<?, ?> map = (Map<?, ?>)value;
			Map<String, JsonAnalyzer> result = new HashMap<String, JsonAnalyzer>();
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				result.put(entry.getKey().toString(), new JsonAnalyzer(entry.getValue()));
			}
			return result;
		}
		throw exception("expected map");
	}
	
	/**
	 * Expects the value to be of map type, obtains the specified
	 * element, and wraps it in a new instance of this class.
	 * @param key the key to read from
	 * @return the analyzer for the map element
	 */
	public JsonAnalyzer analyzeMapElement(String key) {
		if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>)value;
			return new JsonAnalyzer(map.get(key));
		}
		throw exception("expected map");
	}
	
	/**
	 * Helper method to create {@link JsonAnalysisException}s.
	 */
	private JsonAnalysisException exception(String message) {
		StringBuilder builder = new StringBuilder();
		buildContextDescription(builder);
		builder.append(": ").append(message);
		return new JsonAnalysisException(builder.toString());
	}

}
