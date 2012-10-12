/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.schema.orm.NonColumnGetter;
import name.martingeisse.common.datarow.DataRowMeta;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This class contains meta-data about a specific generated entity
 * class. An instance is generated for each generated class and
 * passed to generic code.
 */
public final class SpecificEntityInstanceMeta {

	/**
	 * the concreteClass
	 */
	private final Class<?> concreteClass;
	
	/**
	 * the propertyDescriptors
	 */
	private final PropertyDescriptor[] propertyDescriptors;
	
	/**
	 * the fieldNames
	 */
	private final String[] fieldNames;
	
	/**
	 * the propertyDescriptorsByFieldName
	 */
	private final Map<String, PropertyDescriptor> propertyDescriptorsByFieldName;
	
	/**
	 * the dataRowMeta
	 */
	private final DataRowMeta dataRowMeta;
	
	/**
	 * the tableName
	 */
	private final String tableName;
	
	/**
	 * Constructor.
	 * @param concreteClass the concrete class of the entity instance
	 */
	public SpecificEntityInstanceMeta(Class<?> concreteClass) {
		this.concreteClass = concreteClass;
		this.propertyDescriptors = PropertyUtils.getPropertyDescriptors(concreteClass);
		this.fieldNames = new String[propertyDescriptors.length];
		this.propertyDescriptorsByFieldName = new HashMap<String, PropertyDescriptor>();
		
		for (int i=0; i<propertyDescriptors.length; i++) {
			final PropertyDescriptor property = propertyDescriptors[i];
			if (property.getName().equals("class") || property.getReadMethod().getAnnotation(NonColumnGetter.class) != null) {
				continue;
			}
			final GeneratedFromColumn generatedFromColumn = property.getReadMethod().getAnnotation(GeneratedFromColumn.class);
			if (generatedFromColumn == null) {
				throw new RuntimeException("class " + concreteClass + " has no GeneratedFromColumn annotation for property " + property.getName());
			}
			final String fieldName = generatedFromColumn.value();
			fieldNames[i] = fieldName;
			propertyDescriptorsByFieldName.put(fieldName, property);
		}
		
		this.dataRowMeta = new DataRowMeta();
		this.dataRowMeta.setNames(fieldNames);
		final GeneratedFromTable generatedFromTable = concreteClass.getAnnotation(GeneratedFromTable.class);
		if (generatedFromTable == null) {
			throw new RuntimeException("class " + concreteClass + " has no GeneratedFromTable annotation");
		}
		this.tableName = generatedFromTable.value();
	}
	
	/**
	 * Getter method for the concreteClass.
	 * @return the concreteClass
	 */
	public Class<?> getConcreteClass() {
		return concreteClass;
	}

	/**
	 * Getter method for the propertyDescriptors.
	 * @return the propertyDescriptors
	 */
	public PropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	/**
	 * Getter method for the fieldNames.
	 * @return the fieldNames
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * Getter method for the propertyDescriptorsByFieldName.
	 * @return the propertyDescriptorsByFieldName
	 */
	public Map<String, PropertyDescriptor> getPropertyDescriptorsByFieldName() {
		return propertyDescriptorsByFieldName;
	}
	
	/**
	 * Getter method for the dataRowMeta.
	 * @return the dataRowMeta
	 */
	public DataRowMeta getDataRowMeta() {
		return dataRowMeta;
	}
	
	/**
	 * Getter method for the tableName.
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * Returns all field values in an array.
	 * @param bean the bean
	 * @return the field values
	 */
	public Object[] getFieldValues(Object bean) {
		try {
			Object[] result = new Object[propertyDescriptors.length];
			for (int i=0; i<result.length; i++) {
				result[i] = propertyDescriptors[i].getReadMethod().invoke(bean);
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns the value of a single field.
	 * @param bean the bean
	 * @param fieldName the field name
	 * @return the field value
	 */
	public Object getFieldValue(Object bean, String fieldName) {
		try {
			return propertyDescriptorsByFieldName.get(fieldName).getReadMethod().invoke(bean);
		} catch (Exception e) {
			throw new RuntimeException("getFieldValue(" + fieldName + ") failed", e);
		}
	}
	
	/**
	 * Sets the value of a single field.
	 * @param bean the bean
	 * @param fieldName the field name
	 * @param value the value to set
	 */
	public void setFieldValue(Object bean, String fieldName, Object value) {
		try {
			propertyDescriptorsByFieldName.get(fieldName).getWriteMethod().invoke(bean, value);
		} catch (Exception e) {
			throw new RuntimeException("setFieldValue(" + fieldName + ", *) failed", e);
		}
	}
	
}
