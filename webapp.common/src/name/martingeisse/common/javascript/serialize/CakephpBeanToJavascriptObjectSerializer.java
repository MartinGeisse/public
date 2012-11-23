/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.common.javascript.serialize;

import name.martingeisse.common.util.string.StringUtil;

/**
 * This serializer can handle different names for accessor methods and
 * serialized field names: It accepts field names in underscore-style
 * field_name notation but invokes accessor methods with camel-case
 * getFieldName() notation.
 *
 * @param <T> the bean type
 */
public class CakephpBeanToJavascriptObjectSerializer<T> extends BeanToJavascriptObjectSerializer<T> {

	/**
	 * Constructor.
	 */
	public CakephpBeanToJavascriptObjectSerializer() {
		super();
	}

	/**
	 * Constructor.
	 * @param fieldNames the fields to serialize
	 */
	public CakephpBeanToJavascriptObjectSerializer(String... fieldNames) {
		super(fieldNames);

	}

	/**
	 * Constructor.
	 * @param fieldNames the fields to serialize
	 * @param requestedGeneratedFieldNames the fields to generate
	 */
	public CakephpBeanToJavascriptObjectSerializer(String[] fieldNames, String[] requestedGeneratedFieldNames) {
		super(fieldNames, requestedGeneratedFieldNames);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.BeanToJavascriptObjectSerializer#mapPropertyNameToSerializedName(java.lang.String)
	 */
	@Override
	protected String mapPropertyNameToSerializedName(String propertyName) {
		return StringUtil.convertCamelCaseToLowercaseUnderscores(propertyName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.BeanToJavascriptObjectSerializer#mapSerializedNameToPropertyName(java.lang.String)
	 */
	@Override
	protected String mapSerializedNameToPropertyName(String serializedName) {
		return StringUtil.convertUnderscoresToLowerCamelCase(serializedName);
	}

}
