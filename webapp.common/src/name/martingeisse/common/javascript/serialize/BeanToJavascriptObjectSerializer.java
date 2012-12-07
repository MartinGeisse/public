/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.serialize;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import name.martingeisse.common.javascript.JavascriptAssembler;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This class turns Java beans into Javascript objects. It optionally only
 * serializes specific fields, and optionally allows subclasses to add
 * custom generated fields that are not actually present in the object being
 * serialized. Calling code may also specify a list of requested generated
 * fields which subclasses should honor.
 * 
 * Subclasses may distinguish bean property names (used to read properties)
 * and serialized property names (used in the JSON output). Whenever property
 * names are explicitly specified, the serialized names are expected.
 * 
 * @param <T> the type of values this serializer can handle
 */
public class BeanToJavascriptObjectSerializer<T> extends AbstractJavascriptSerializer<T> {

	/**
	 * the fieldNames
	 */
	private String[] fieldNames;

	/**
	 * the requestedGeneratedFieldNames
	 */
	private String[] requestedGeneratedFieldNames;

	/**
	 * Constructor.
	 */
	public BeanToJavascriptObjectSerializer() {
	}

	/**
	 * Constructor.
	 * @param fieldNames the fields to serialize
	 */
	public BeanToJavascriptObjectSerializer(final String... fieldNames) {
		this.fieldNames = fieldNames;
	}

	/**
	 * Constructor.
	 * @param fieldNames the fields to serialize
	 * @param requestedGeneratedFieldNames the fields to generate
	 */
	public BeanToJavascriptObjectSerializer(String[] fieldNames, String[] requestedGeneratedFieldNames) {
		this.fieldNames = fieldNames;
		this.requestedGeneratedFieldNames = requestedGeneratedFieldNames;
	}

	/**
	 * Getter method for the fieldNames.
	 * @return the fieldNames
	 */
	public final String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * Setter method for the fieldNames.
	 * @param fieldNames the fieldNames to set
	 */
	public final void setFieldNames(final String... fieldNames) {
		this.fieldNames = fieldNames;
	}

	/**
	 * Getter method for the requestedGeneratedFieldNames.
	 * @return the requestedGeneratedFieldNames
	 */
	public String[] getRequestedGeneratedFieldNames() {
		return requestedGeneratedFieldNames;
	}

	/**
	 * Setter method for the requestedGeneratedFieldNames.
	 * @param requestedGeneratedFieldNames the requestedGeneratedFieldNames to set
	 */
	public void setRequestedGeneratedFieldNames(String[] requestedGeneratedFieldNames) {
		this.requestedGeneratedFieldNames = requestedGeneratedFieldNames;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#serialize(java.lang.Object, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	public void serialize(final T bean, final JavascriptAssembler assembler) {
		try {
			assembler.beginObject();
			if (fieldNames == null) {
				serializeAllFields(bean, assembler);
			} else {
				serializeSpecificFields(bean, assembler);
			}
			serializeGeneratedFields(bean, assembler);
			assembler.endObject();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 */
	private void serializeAllFields(final T bean, final JavascriptAssembler assembler) throws Exception {
		for (final PropertyDescriptor property : PropertyUtils.getPropertyDescriptors(bean)) {
			final String beanPropertyName = property.getName();
			if (beanPropertyName.equals("class")) {
				continue;
			}
			final String serializedFieldName = mapPropertyNameToSerializedName(beanPropertyName);
			final Object value = property.getReadMethod().invoke(bean);
			assembler.prepareObjectProperty(serializedFieldName);
			serializeFieldValue(bean, assembler, beanPropertyName, serializedFieldName, value);
		}
	}

	/**
	 * 
	 */
	private void serializeSpecificFields(final T bean, final JavascriptAssembler assembler) throws Exception {
		serializeFields(bean, assembler, fieldNames);
	}

	/**
	 * This method is a stub that allows subclasses to add custom generated fields not
	 * actually present in the object being serialized. The default implementation does nothing.
	 * 
	 * @param bean the bean being serialized
	 * @param assembler the assembler
	 */
	protected void serializeGeneratedFields(final T bean, final JavascriptAssembler assembler) {
	}

	/**
	 * Serializes the specified bean fields.
	 * @param bean the bean to get values from
	 * @param assembler the Javascript assembler to use
	 * @param fieldNames the names of the fields to serialize
	 */
	public void serializeFields(final Object bean, final JavascriptAssembler assembler, final String... fieldNames) {
		try {
			for (final String serializedName : fieldNames) {
				String beanPropertyName = mapSerializedNameToPropertyName(serializedName);
				final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, beanPropertyName);
				if (propertyDescriptor == null) {
					throw new RuntimeException("no such property: " + serializedName + " (bean property name: " + beanPropertyName + ")");
				}
				final Method readMethod = propertyDescriptor.getReadMethod();
				final Object value = readMethod.invoke(bean);
				assembler.prepareObjectProperty(serializedName);
				serializeFieldValue(bean, assembler, beanPropertyName, serializedName, value);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method is used to serialize a single field value. The default implementation
	 * uses {@link JavascriptAssembler#appendPrimitive(Object)}.
	 * 
	 * @param bean the bean
	 * @param assembler the Javascript assembler
	 * @param beanPropertyName the bean property name of the field
	 * @param serializedFieldName the serialized name of the field
	 * @param value the field value
	 */
	protected void serializeFieldValue(final Object bean, final JavascriptAssembler assembler, final String beanPropertyName, final String serializedFieldName, final Object value) {
		assembler.appendPrimitive(value);
	}

	/**
	 * Maps the serialized name of a property to its bean property name.
	 * The default implementation just returns the serialized name.
	 * 
	 * @param serializedName the serialized name
	 * @return the bean property name
	 */
	protected String mapSerializedNameToPropertyName(String serializedName) {
		return serializedName;
	}

	/**
	 * Maps the bean property name of a property to its serialized
	 * property name. The default implementation just returns the bean property name.
	 * 
	 * @param propertyName the bean property name
	 * @return the serialized name
	 */
	protected String mapPropertyNameToSerializedName(String propertyName) {
		return propertyName;
	}
	
}
