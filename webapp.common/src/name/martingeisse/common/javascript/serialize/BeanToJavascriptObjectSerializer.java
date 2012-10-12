/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.common.javascript.serialize;

import name.martingeisse.common.javascript.JavascriptAssembler;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This class turns Java beans into Javascript objects. It optionally only
 * serializes specific fields, and optionally allows subclasses to add
 * custom generated fields that are not actually present in the object being
 * serialized.
 */
public class BeanToJavascriptObjectSerializer<T> implements IJavascriptSerializer<T> {

	/**
	 * the fieldNames
	 */
	private String[] fieldNames;
	
	/**
	 * Constructor.
	 */
	public BeanToJavascriptObjectSerializer() {
	}
	
	/**
	 * Constructor.
	 * @param fieldNames the fields to serialize
	 */
	public BeanToJavascriptObjectSerializer(String... fieldNames) {
		this.fieldNames = fieldNames;
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
	public final void setFieldNames(String... fieldNames) {
		this.fieldNames = fieldNames;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#serialize(java.lang.Object, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	public void serialize(T bean, JavascriptAssembler assembler) {
		try {
			assembler.beginObject();
			if (fieldNames == null) {
				serializeAllFields(bean, assembler);
			} else {
				serializeSpecificFields(bean, assembler);
			}
			serializeGeneratedFields(bean, assembler);
			assembler.endObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 */
	private void serializeAllFields(T bean, JavascriptAssembler assembler) throws Exception {
		throw new RuntimeException("NOT YET IMPLEMENTED"); // TODO
	}
	
	/**
	 * 
	 */
	private void serializeSpecificFields(T bean, JavascriptAssembler assembler) throws Exception {
		for (String fieldName : fieldNames) {
			Object value = PropertyUtils.getPropertyDescriptor(bean, fieldName).getReadMethod().invoke(bean);
			assembler.prepareObjectProperty(fieldName);
			assembler.appendPrimitive(value);
		}
	}
	
	/**
	 * This method is a stub that allows subclasses to add custom generated fields not
	 * actually present in the object being serialized. The default implementation does nothing.
	 * 
	 * @param bean the bean being serialized
	 * @param assembler the assembler
	 */
	protected void serializeGeneratedFields(T bean, JavascriptAssembler assembler) {
	}
	
}
