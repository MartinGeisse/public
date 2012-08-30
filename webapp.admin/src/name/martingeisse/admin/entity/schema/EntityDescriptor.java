/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.admin.database.EntityConnectionManager;
import name.martingeisse.admin.database.IDatabaseDescriptor;
import name.martingeisse.admin.database.IEntityDatabaseConnection;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.instance.FetchEntityInstanceAction;
import name.martingeisse.admin.entity.list.EntityExpressionUtil;
import name.martingeisse.admin.entity.property.type.IEntityIdType;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceInfo;
import name.martingeisse.admin.entity.schema.search.IEntitySearchContributor;
import name.martingeisse.admin.entity.schema.search.IEntitySearchStrategy;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.common.datarow.AbstractDataRowMetaHolder;
import name.martingeisse.common.datarow.DataRowMeta;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Predicate;

/**
 * This class captures a descriptor for a database entity (table).
 * 
 * ID handling: This descriptor stores information about the entity ID (primary
 * key). Currently only single-column IDs are supported.
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
	private IDatabaseDescriptor database;

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
	 * the propertiesInDatabaseOrder
	 */
	private List<EntityPropertyDescriptor> propertiesInDatabaseOrder;

	/**
	 * the propertiesByName
	 */
	private Map<String, EntityPropertyDescriptor> propertiesByName;

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
	 * the searchStrategy
	 */
	private IEntitySearchStrategy searchStrategy;

	/**
	 * Constructor.
	 */
	public EntityDescriptor() {
		this.propertiesByName = new HashMap<String, EntityPropertyDescriptor>();
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
	public IDatabaseDescriptor getDatabase() {
		return database;
	}

	/**
	 * Setter method for the database.
	 * @param database the database to set
	 */
	public void setDatabase(final IDatabaseDescriptor database) {
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
	 * Getter method for the propertiesInDatabaseOrder.
	 * @return the propertiesInDatabaseOrder
	 */
	public List<EntityPropertyDescriptor> getPropertiesInDatabaseOrder() {
		return propertiesInDatabaseOrder;
	}

	/**
	 * Getter method for the propertiesByName.
	 * @return the propertiesByName
	 */
	public Map<String, EntityPropertyDescriptor> getPropertiesByName() {
		return propertiesByName;
	}

	/**
	 * Initializes the properties (both in database order and by-name mapping).
	 */
	void initializeProperties(final List<EntityPropertyDescriptor> propertiesInDatabaseOrder) {
		this.propertiesInDatabaseOrder = propertiesInDatabaseOrder;
		this.propertiesByName = new HashMap<String, EntityPropertyDescriptor>();
		for (final EntityPropertyDescriptor propertyDescriptor : propertiesInDatabaseOrder) {
			propertiesByName.put(propertyDescriptor.getName(), propertyDescriptor);
		}
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
	 * Returns one of the navigation nodes associated with instances of this entity.
	 * @param subpathSegments the subpath segments to walk from the entity instance navigation root
	 * to reach the node to link. The specified node must exist, otherwise this method throws an
	 * {@link IllegalArgumentException}.
	 * @return the navigation node
	 */
	public NavigationNode getInstanceNavigationNode(final String... subpathSegments) {
		NavigationNode node = instanceNavigationRootNode;
		for (final String segment : subpathSegments) {
			final NavigationNode child = node.findChildById(segment);
			if (child == null) {
				throw new IllegalArgumentException("subpath segment '" + segment + "' not found in node " + node.getPath());
			}
			node = child;
		}
		return node;
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
		for (final EntityPropertyDescriptor property : propertiesInDatabaseOrder) {
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
	 * @param alias the alias for this entity
	 * @return the query
	 */
	public SQLQuery query(final String alias) {
		return getConnection().createQuery().from(createRelationalPath(alias));
	}
	
	/**
	 * Queries for the number of instances of this entity, or the number of
	 * instances accepted by the specified filter predicate.
	 * @param filterPredicate the filter predicate
	 * @return the number of instances
	 */
	public long count(Predicate filterPredicate) {
		SQLQuery countQuery = query(EntityExpressionUtil.ALIAS);
		if (filterPredicate != null) {
			countQuery = countQuery.where(filterPredicate);
		}
		return countQuery.count();
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

	/**
	 * Checks whether a search strategy is installed for this entity.
	 * @return true if searching is supported, false if not
	 */
	public boolean isSearchSupported() {
		return (searchStrategy != null);
	}

	/**
	 * Creates an entity list filter for this entity and for the specified search term,
	 * or null if no useful filter can be found for the search term.
	 * @param searchTerm the search term
	 * @return the filter
	 */
	public Predicate createSearchFilter(final String searchTerm) {
		return (searchStrategy == null ? null : searchStrategy.createFilter(this, searchTerm));
	}

	/**
	 * Initializes the search strategy for this entity from the application configuration.
	 */
	public void initializeSearchStrategy() {
		int maxScore = Integer.MIN_VALUE;
		IEntitySearchContributor maxScoreContributor = null;
		for (final IEntitySearchContributor contributor : EntityConfigurationUtil.getEntitySearchContributors()) {
			final int score = contributor.getScore(this);
			if (score > maxScore) {
				maxScoreContributor = contributor;
				maxScore = score;
			}
		}
		if (maxScoreContributor != null) {
			this.searchStrategy = maxScoreContributor.getSearchStrategy(this);
			if (this.searchStrategy == null) {
				throw new RuntimeException("winning IEntitySearchContributor (" + maxScoreContributor + ") for entity " + getName() + " returned null");
			}
		}
	}

	/**
	 * @return the database connection for the database that contains this entity.
	 */
	public IEntityDatabaseConnection getConnection() {
		return EntityConnectionManager.getConnection(getDatabase());
	}
	
}
