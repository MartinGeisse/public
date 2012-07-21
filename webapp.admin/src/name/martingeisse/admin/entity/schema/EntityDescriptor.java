/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.instance.FetchEntityInstanceAction;
import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceInfo;
import name.martingeisse.admin.navigation.NavigationNode;

/**
 * This class captures a descriptor for a database entity (table).
 * Instances are created either explicitly from code or implicitly
 * by fetching information from the database. Explicit and implicit
 * descriptors are then merged to build the final application schema.
 * 
 * List Presenters: Each entity keeps a set of named list presenters that are
 * able to create a user interface for lists of this entity. If no presenter
 * is registered, the default presenter from the {@link ApplicationConfiguration}
 * is used instead.
 * 
 * TODO: this class should not be serializable. Instead, models should be able
 * to detach and re-attach from/to instances of this class.
 * 
 * TODO: rename "tableName" to "name".
 * 
 * ID handling: This descriptor stores information about the entity ID (primary
 * key). Currently only single-column IDs are supported.
 */
public class EntityDescriptor implements Serializable {

	/**
	 * the database
	 */
	private AbstractDatabaseDescriptor database;

	/**
	 * the tableName
	 */
	private String tableName;

	/**
	 * the idColumnName
	 */
	private String idColumnName;

	/**
	 * the properties
	 */
	private Map<String, EntityPropertyDescriptor> properties;

	/**
	 * the incomingReferences
	 */
	private List<EntityReferenceInfo> incomingReferences;

	/**
	 * the outgoingReferences
	 */
	private List<EntityReferenceInfo> outgoingReferences;

	/**
	 * the canonicalListNavigationNode
	 */
	private NavigationNode canonicalListNavigationNode;

	/**
	 * the instanceNavigationRootNode
	 */
	private NavigationNode instanceNavigationRootNode;

	/**
	 * Constructor.
	 */
	public EntityDescriptor() {
		this.properties = new HashMap<String, EntityPropertyDescriptor>();
		this.incomingReferences = new ArrayList<EntityReferenceInfo>();
		this.outgoingReferences = new ArrayList<EntityReferenceInfo>();
	}

	/**
	 * Getter method for the database.
	 * @return the database
	 */
	public AbstractDatabaseDescriptor getDatabase() {
		return database;
	}

	/**
	 * Setter method for the database.
	 * @param database the database to set
	 */
	public void setDatabase(final AbstractDatabaseDescriptor database) {
		this.database = database;
	}

	/**
	 * Getter method for the tableName.
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Setter method for the tableName.
	 * @param tableName the tableName to set
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Getter method for the idColumnName.
	 * @return the idColumnName
	 */
	public String getIdColumnName() {
		return idColumnName;
	}

	/**
	 * Setter method for the idColumnName.
	 * @param idColumnName the idColumnName to set
	 */
	public void setIdColumnName(final String idColumnName) {
		this.idColumnName = idColumnName;
	}

	/**
	 * Getter method for the properties.
	 * @return the properties
	 */
	public Map<String, EntityPropertyDescriptor> getProperties() {
		return properties;
	}

	/**
	 * Setter method for the properties.
	 * @param properties the properties to set
	 */
	public void setProperties(final Map<String, EntityPropertyDescriptor> properties) {
		this.properties = properties;
	}

	/**
	 * Getter method for the incomingReferences.
	 * @return the incomingReferences
	 */
	public List<EntityReferenceInfo> getIncomingReferences() {
		return incomingReferences;
	}

	/**
	 * Setter method for the incomingReferences.
	 * @param incomingReferences the incomingReferences to set
	 */
	public void setIncomingReferences(final List<EntityReferenceInfo> incomingReferences) {
		this.incomingReferences = incomingReferences;
	}

	/**
	 * Getter method for the outgoingReferences.
	 * @return the outgoingReferences
	 */
	public List<EntityReferenceInfo> getOutgoingReferences() {
		return outgoingReferences;
	}

	/**
	 * Setter method for the outgoingReferences.
	 * @param outgoingReferences the outgoingReferences to set
	 */
	public void setOutgoingReferences(final List<EntityReferenceInfo> outgoingReferences) {
		this.outgoingReferences = outgoingReferences;
	}

	/**
	 * Getter method for the canonicalListNavigationNode.
	 * @return the canonicalListNavigationNode
	 */
	public NavigationNode getCanonicalListNavigationNode() {
		return canonicalListNavigationNode;
	}

	/**
	 * Setter method for the canonicalListNavigationNode.
	 * @param canonicalListNavigationNode the canonicalListNavigationNode to set
	 */
	public void setCanonicalListNavigationNode(final NavigationNode canonicalListNavigationNode) {
		this.canonicalListNavigationNode = canonicalListNavigationNode;
	}

	/**
	 * Getter method for the instanceNavigationRootNode.
	 * @return the instanceNavigationRootNode
	 */
	public NavigationNode getInstanceNavigationRootNode() {
		return instanceNavigationRootNode;
	}

	/**
	 * Setter method for the instanceNavigationRootNode.
	 * @param instanceNavigationRootNode the instanceNavigationRootNode to set
	 */
	public void setInstanceNavigationRootNode(final NavigationNode instanceNavigationRootNode) {
		this.instanceNavigationRootNode = instanceNavigationRootNode;
	}

	/**
	 * Fetches a single instance of this entity.
	 * @param id the id of the instance
	 * @return the fields
	 */
	public EntityInstance fetchSingleInstance(final int id) {
		final FetchEntityInstanceAction action = new FetchEntityInstanceAction();
		action.setEntity(this);
		action.setId(id);
		return action.execute();
	}

	/**
	 * Looks for an outgoing reference with the specified field name.
	 * @param fieldName the field name
	 * @return the reference, or null if none was found
	 */
	public EntityReferenceInfo findOutgoingReference(final String fieldName) {
		for (final EntityReferenceInfo reference : outgoingReferences) {
			if (reference.getFieldName().equals(fieldName)) {
				return reference;
			}
		}
		return null;
	}

	/**
	 * @return the names of the fields for raw lists of this entity type, in the order
	 * they shall be displayed.
	 */
	public String[] getRawEntityListFieldOrder() {

		// determine the list of visible fields
		final List<EntityPropertyDescriptor> fieldOrder = new ArrayList<EntityPropertyDescriptor>();
		for (final EntityPropertyDescriptor property : properties.values()) {
			if (property.isVisibleInRawEntityList()) {
				fieldOrder.add(property);
			}
		}

		// determine their order
		final Comparator<EntityPropertyDescriptor> fieldComparator = EntityConfigurationUtil.getGeneralEntityConfiguration().getEntityListFieldOrder();
		if (fieldComparator != null) {
			Collections.sort(fieldOrder, fieldComparator);
		}

		// build an array of the field names
		final String[] fieldOrderArray = new String[fieldOrder.size()];
		int position = 0;
		for (final EntityPropertyDescriptor property : fieldOrder) {
			fieldOrderArray[position] = property.getName();
			position++;
		}

		return fieldOrderArray;
	}

}
