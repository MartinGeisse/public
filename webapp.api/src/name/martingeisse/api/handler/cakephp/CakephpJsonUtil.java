/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.cakephp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import name.martingeisse.common.database.QueryUtil;
import name.martingeisse.common.javascript.JavascriptAssembler;
import name.martingeisse.common.javascript.serialize.BeanToJavascriptObjectSerializer;
import name.martingeisse.common.javascript.serialize.IJavascriptSerializable;
import name.martingeisse.common.javascript.serialize.IJavascriptSerializer;
import name.martingeisse.common.javascript.serialize.ResultSetRowToJavascriptObjectSerializer;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Path;

/**
 * Utility methods for JSON generation using a {@link JavascriptAssembler}.
 */
public final class CakephpJsonUtil {

	/**
	 * Prevent instantiation.
	 */
	private CakephpJsonUtil() {
	}

	// --- entry handling methods ---------------------------------------------------------------------

	/**
	 * Assembles a JSON object from a Java object using the specified serializer.
	 * 
	 * If modelName is null, then this method simply applies the serializer.
	 * Otherwise, the result is wrapped in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assemble the inner object.
	 * @param serializer the serializer that generates a Javascript object from the Java object
	 * @param object the object to serialize
	 */
	public static <T> void generateModelEntry(final JavascriptAssembler assembler, final String modelName, final IJavascriptSerializer<T> serializer, final T object) {
		if (modelName == null) {
			serializer.serialize(object, assembler);
		} else {
			assembler.beginObject();
			assembler.prepareObjectProperty(modelName);
			serializer.serialize(object, assembler);
			assembler.endObject();
		}
	}

	/**
	 * Assembles a JSON object from an {@link IJavascriptSerializable}.
	 * 
	 * If modelName is null, then this method simply serializes the object.
	 * Otherwise, the result is wrapped in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assemble the inner object.
	 * @param object the object to serialize
	 */
	public static <T> void generateModelEntry(final JavascriptAssembler assembler, final String modelName, final IJavascriptSerializable object) {
		generateModelEntry(assembler, modelName, IJavascriptSerializable.Serializer.instance, object);
	}
	
	/**
	 * Assembles a JSON object from the current row of the specified result set.
	 * 
	 * If modelName is null, then this method simply assembles the fields of the
	 * current row as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param resultSet the result set to read from
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelEntry(final JavascriptAssembler assembler, final String modelName, final ResultSet resultSet) throws SQLException {
		generateModelEntry(assembler, modelName, ResultSetRowToJavascriptObjectSerializer.instance, resultSet);
	}

	/**
	 * Assembles a JSON object from the first row returned by the specified query,
	 * using the serializer. Throws a {@link NoSuchElementException} if the query
	 * does not return any results.
	 * 
	 * If modelName is null, then this method simply applies the serializer.
	 * Otherwise, the same object is wrapped in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param serializer the serializer that generates a Javascript object from the result set row
	 * @param query the query
	 * @param parentPath the parent path for field paths
	 * @param fieldNames the field names
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelEntry(final JavascriptAssembler assembler, final String modelName, final IJavascriptSerializer<ResultSet> serializer, final SQLQuery query, final Path<?> parentPath, final String... fieldNames) throws SQLException {
		final ResultSet resultSet = QueryUtil.getFieldsResultSet(query, parentPath, fieldNames);
		if (!resultSet.next()) {
			throw new NoSuchElementException();
		}
		generateModelEntry(assembler, modelName, serializer, resultSet);
		resultSet.close();
	}

	/**
	 * Assembles a JSON object from the first row returned by the specified query. Throws a
	 * {@link NoSuchElementException} if the query does not return any results.
	 * 
	 * If modelName is null, then this method simply assembles the fields of the
	 * row as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param query the query
	 * @param parentPath the parent path for field paths
	 * @param fieldNames the field names
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelEntry(final JavascriptAssembler assembler, final String modelName, final SQLQuery query, final Path<?> parentPath, final String... fieldNames) throws SQLException {
		generateModelEntry(assembler, modelName, ResultSetRowToJavascriptObjectSerializer.instance, query, parentPath, fieldNames);
	}

	/**
	 * Assembles a JSON object from the properties of the specified Java bean.
	 * 
	 * If modelName is null, then this method simply assembles the fields of the
	 * bean as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param bean the bean that contains field values
	 * @param fieldNames the field names
	 */
	public static <T> void generateModelEntry(final JavascriptAssembler assembler, final String modelName, final T bean, final String... fieldNames) {
		generateModelEntry(assembler, modelName, new BeanToJavascriptObjectSerializer<T>(fieldNames), bean);
	}

	// --- collection/iterable handling methods ---------------------------------------------------------------------

	/**
	 * Assembles an array of JSON objects from an {@link Iterable} of Java objects using the specified serializer.
	 * 
	 * If modelName is null, then this method simply applies the serializer to each element.
	 * Otherwise, each result is wrapped in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param serializer the serializer that generates a Javascript object from the Java object
	 * @param iterable the iterable of objects to serialize
	 */
	public static <T> void generateModelList(final JavascriptAssembler assembler, final String modelName, final IJavascriptSerializer<T> serializer, final Iterable<? extends T> iterable) {
		assembler.beginList();
		for (T element : iterable) {
			assembler.prepareListElement();
			generateModelEntry(assembler, modelName, serializer, element);
		}
		assembler.endList();
	}

	/**
	 * Assembles an array of JSON objects from an {@link Iterable} of IJavascriptSerializable objects.
	 * 
	 * If modelName is null, then this method simply serializes each element.
	 * Otherwise, each result is wrapped in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param iterable the iterable of objects to serialize
	 */
	public static <T> void generateModelList(final JavascriptAssembler assembler, final String modelName, final Iterable<? extends IJavascriptSerializable> iterable) {
		generateModelList(assembler, modelName, IJavascriptSerializable.Serializer.instance, iterable);
	}
	
	/**
	 * Assembles all remaining rows from the specified resultSet, not including the current
	 * row (if any), using the serializer and wrapped in a JSON array.
	 * 
	 * This method is intended primarily for use with a fresh {@link ResultSet}
	 * from which no rows have been fetched yet.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just return the inner object.
	 * @param serializer the serializer that generates a Javascript object from each result set row
	 * @param resultSet the result set to read from
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelList(final JavascriptAssembler assembler, final String modelName, final IJavascriptSerializer<ResultSet> serializer, final ResultSet resultSet) throws SQLException {
		assembler.beginList();
		while (resultSet.next()) {
			assembler.prepareListElement();
			generateModelEntry(assembler, modelName, serializer, resultSet);
		}
		assembler.endList();
	}
	
	/**
	 * Assembles all remaining rows from the specified resultSet, not including the current
	 * row (if any), wrapped in a JSON array.
	 * 
	 * This method is intended primarily for use with a fresh {@link ResultSet}
	 * from which no rows have been fetched yet.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just return the inner object.
	 * @param resultSet the result set to read from
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelList(final JavascriptAssembler assembler, final String modelName, final ResultSet resultSet) throws SQLException {
		generateModelList(assembler, modelName, ResultSetRowToJavascriptObjectSerializer.instance, resultSet);
	}

	/**
	 * Assembles an array of JSON objects from all rows returned by the specified query
	 * using the serializer.
	 * 
	 * If modelName is null, then this method simply assembles the fields of each
	 * row as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param serializer the serializer that generates a Javascript object from each result set row
	 * @param query the query
	 * @param parentPath the parent path for field paths
	 * @param fieldNames the field names
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelList(final JavascriptAssembler assembler, final String modelName, final IJavascriptSerializer<ResultSet> serializer, final SQLQuery query, final Path<?> parentPath, final String... fieldNames) throws SQLException {
		final ResultSet resultSet = QueryUtil.getFieldsResultSet(query, parentPath, fieldNames);
		generateModelList(assembler, modelName, serializer, resultSet);
		resultSet.close();
	}
	
	/**
	 * Assembles an array of JSON objects from all rows returned by the specified query.
	 * 
	 * If modelName is null, then this method simply assembles the fields of each
	 * row as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param query the query
	 * @param parentPath the parent path for field paths
	 * @param fieldNames the field names
	 * @throws SQLException on SQL errors
	 */
	public static void generateModelList(final JavascriptAssembler assembler, final String modelName, final SQLQuery query, final Path<?> parentPath, final String... fieldNames) throws SQLException {
		generateModelList(assembler, modelName, ResultSetRowToJavascriptObjectSerializer.instance, query, parentPath, fieldNames);
	}

	/**
	 * Assembles an array of JSON objects from all Java beans in the specified {@link Iterable}.
	 * 
	 * If modelName is null, then this method simply assembles the fields of each
	 * bean as a JSON object. Otherwise, the same object is wrapped
	 * in another object using modelName as the key.
	 * 
	 * @param assembler the Javascript assembler
	 * @param modelName to key of the inner object in the outer object, or null
	 * to just assembles the inner object.
	 * @param beans the beans that contain field values
	 * @param fieldNames the field names
	 */
	public static <T> void generateModelList(final JavascriptAssembler assembler, final String modelName, final Iterable<T> beans, final String... fieldNames) {
		generateModelList(assembler, modelName, new BeanToJavascriptObjectSerializer<T>(fieldNames), beans);
	}

}
