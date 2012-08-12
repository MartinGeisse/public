/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.IEntityNameMappingStrategy;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.instance.FetchEntityInstanceAction;
import name.martingeisse.admin.entity.property.type.IEntityIdType;
import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceInfo;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.common.datarow.AbstractDataRowMetaHolder;
import name.martingeisse.common.datarow.DataRowMeta;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;

/**
 * This class captures a descriptor for a database entity (table).
 * 
 * ID handling: This descriptor stores information about the entity ID (primary
 * key). Currently only single-column IDs are supported.
 * 
 * TODO: Raw table uses different order for names, values
 * Values do not use configured raw table field order; ResultSetReader did that.
 * Make order configurable at all? Database Order is enough for a quick preview,
 * anything else uses a custom view anyway. OTOH EntityInstance/DataRow should
 * support getting a field by name, so using a configurable order should be simple,
 * even if not useful.
 */
public class EntityDescriptor {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the displayName
	 */
	private String displayName;

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
	 * the idColumnType
	 */
	private IEntityIdType idColumnType;

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
	 * the dataRowMeta
	 */
	private DataRowMeta dataRowMeta;

	/**
	 * Constructor.
	 */
	public EntityDescriptor() {
		this.properties = new HashMap<String, EntityPropertyDescriptor>();
		this.incomingReferences = new ArrayList<EntityReferenceInfo>();
		this.outgoingReferences = new ArrayList<EntityReferenceInfo>();
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the displayName.
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Maps the table name to name and display name using the application's
	 * entity name mapping.
	 */
	void mapNames() {
		final IEntityNameMappingStrategy mapping = EntityConfigurationUtil.getGeneralEntityConfiguration().getEntityNameMappingStrategy();
		this.name = mapping.determineEntityName(this);
		this.displayName = mapping.determineEntityDisplayName(this);
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
	 * Getter method for the idColumnType.
	 * @return the idColumnType
	 */
	public IEntityIdType getIdColumnType() {
		return idColumnType;
	}

	/**
	 * Setter method for the idColumnType.
	 * @param idColumnType the idColumnType to set
	 */
	public void setIdColumnType(final IEntityIdType idColumnType) {
		this.idColumnType = idColumnType;
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
	 * Getter method for the dataRowMeta.
	 * @return the dataRowMeta
	 */
	public DataRowMeta getDataRowMeta() {
		return dataRowMeta;
	}

	/**
	 * Setter method for the dataRowMeta.
	 * @param dataRowMeta the dataRowMeta to set
	 */
	public void setDataRowMeta(final DataRowMeta dataRowMeta) {
		this.dataRowMeta = dataRowMeta;
	}

	/**
	 * Fetches a single instance of this entity.
	 * 
	 * The 'optional' flag specifies what happens if no instance can be found.
	 * If the flag is true, then the instance is optional, and this method
	 * returns null. Otherwise, the instance is mandatory, and this method
	 * throws an exception.
	 * 
	 * @param id the id of the instance
	 * @param optional whether the existence of the instance is optional
	 * @return the fields
	 */
	public EntityInstance fetchSingleInstance(final Object id, final boolean optional) {
		final FetchEntityInstanceAction action = new FetchEntityInstanceAction();
		action.setEntity(this);
		action.setId(id);
		action.setOptional(optional);
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

	/**
	 * Creates and returns a {@link RelationalPath} to fetch this entity.
	 * @param alias the alias to use
	 * @return the relational path
	 */
	public RelationalPath<Object> createRelationalPath(final String alias) {
		return new RelationalPathBase<Object>(Object.class, alias, null, tableName);
	}

	/**
	 * Queries this entity using the specified connection and alias.
	 * @param connection the JDBC connection
	 * @param alias the alias for this entity
	 * @return the query
	 */
	public SQLQuery query(final Connection connection, final String alias) {
		return new SQLQueryImpl(connection, new MySQLTemplates()).from(createRelationalPath(alias));
	}

	/**
	 * This method delegates to checkDataRowMeta(resultSet.getMetaData()).
	 * @param resultSet the result set
	 * @return the meta-data
	 * @throws SQLException on SQL errors
	 */
	public DataRowMeta checkDataRowMeta(final ResultSet resultSet) throws SQLException {
		return checkDataRowMeta(resultSet.getMetaData());
	}

	/**
	 * Ensures that the data row meta-data for this entity is equal to the one
	 * for the specified result set meta-data. Throws an {@link IllegalStateException}
	 * if that is not the case. This indicates that the table schema is no longer
	 * the same as when this {@link EntityDescriptor} was created, i.e. that the
	 * table schema has been changed while the admin application was running.
	 * Otherwise returns the shared row meta-data object.
	 * 
	 * @param resultSetMetaData the meta-data to check
	 * @return the shared meta-data
	 * @throws SQLException on SQL errors
	 */
	public DataRowMeta checkDataRowMeta(final ResultSetMetaData resultSetMetaData) throws SQLException {
		if (!dataRowMeta.equals(new DataRowMeta(resultSetMetaData))) {
			throw new IllegalStateException("data row schema for entity " + getName() + " does not match");
		}
		return dataRowMeta;
	}

	/**
	 * This method delegates to checkDataRowMeta(metaHolder.getMeta()).
	 * @param metaHolder the meta-data holder
	 * @return the meta-data
	 */
	public DataRowMeta checkDataRowMeta(final AbstractDataRowMetaHolder metaHolder) {
		return checkDataRowMeta(metaHolder.getMeta());
	}

	/**
	 * Ensures that the data row meta-data for this entity is equal to the specified one.
	 * Throws an {@link IllegalStateException} if that is not the case. This indicates
	 * that the table schema is no longer the same as when this {@link EntityDescriptor}
	 * was created, i.e. that the table schema has been changed while the admin
	 * application was running. Otherwise returns the shared row meta-data object.
	 * 
	 * @param meta the meta-data to check
	 * @return the shared meta-data
	 */
	public DataRowMeta checkDataRowMeta(final DataRowMeta meta) {
		if (!dataRowMeta.equals(meta)) {
			throw new IllegalStateException("data row schema for entity " + getName() + " does not match");
		}
		return dataRowMeta;
	}

}
