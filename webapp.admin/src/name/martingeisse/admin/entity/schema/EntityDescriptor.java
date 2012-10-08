/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.lang.annotation.Annotation;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import name.martingeisse.admin.entity.EntitySelection;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.orm.EntitySpecificCodeMapping;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceEndpoint;
import name.martingeisse.admin.entity.schema.search.EntitySearcher;
import name.martingeisse.admin.entity.schema.type.IEntityIdTypeInfo;
import name.martingeisse.admin.entity.schema.type.ISqlTypeInfo;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.IDatabaseDescriptor;
import name.martingeisse.common.database.IEntityDatabaseConnection;
import name.martingeisse.common.datarow.AbstractDataRowMetaHolder;
import name.martingeisse.common.datarow.DataRowMeta;
import name.martingeisse.common.util.ClassKeyedContainer;
import name.martingeisse.common.util.ParameterUtil;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * This class captures a descriptor for a database entity (table).
 * 
 * Instances are created during initialization of the {@link ApplicationSchema}
 * and controlled by various strategies. User code should not create instances
 * of this class directly. 
 * 
 * ID handling: This descriptor stores information about the entity ID (primary
 * key). Currently only single-column IDs are supported.
 */
public class EntityDescriptor {

	/**
	 * The default alias used for the entity in database queries.
	 * Filter predicates should use this to access fields of the entity being filtered.
	 */
	public static final String ALIAS = "e";

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
	private IEntityIdTypeInfo idColumnType;

	/**
	 * the properties
	 */
	private EntityProperties properties;

	/**
	 * the referenceEndpoints
	 */
	private List<EntityReferenceEndpoint> referenceEndpoints;

	/**
	 * the canonicalListNavigationNode
	 */
	private NavigationNode canonicalListNavigationNode;

	/**
	 * the navigation
	 */
	private EntityNavigation navigation;

	/**
	 * the dataRowMeta
	 */
	private DataRowMeta dataRowMeta;

	/**
	 * the dataRowTypes
	 */
	private ISqlTypeInfo[] dataRowTypes;

	/**
	 * the searcher
	 */
	private EntitySearcher searcher;

	/**
	 * the annotations
	 */
	private final ClassKeyedContainer<Annotation> annotations = new ClassKeyedContainer<Annotation>();

	/**
	 * the specificCodeMapping
	 */
	private EntitySpecificCodeMapping specificCodeMapping;

	/**
	 * Constructor.
	 */
	EntityDescriptor() {
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter method for the displayName.
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Setter method for the displayName.
	 * @param displayName the displayName to set
	 */
	void setDisplayName(final String displayName) {
		this.displayName = displayName;
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
	void setDatabase(final IDatabaseDescriptor database) {
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
	void setTableName(final String tableName) {
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
	void setIdColumnName(final String idColumnName) {
		this.idColumnName = idColumnName;
	}

	/**
	 * Getter method for the idColumnType.
	 * @return the idColumnType
	 */
	public IEntityIdTypeInfo getIdColumnType() {
		return idColumnType;
	}

	/**
	 * Setter method for the idColumnType.
	 * @param idColumnType the idColumnType to set
	 */
	void setIdColumnType(final IEntityIdTypeInfo idColumnType) {
		this.idColumnType = idColumnType;
	}

	/**
	 * Getter method for the properties.
	 * @return the properties
	 */
	public EntityProperties getProperties() {
		return properties;
	}

	/**
	 * Setter method for the properties.
	 * @param properties the properties to set
	 */
	void setProperties(EntityProperties properties) {
		this.properties = properties;
	}
	
	/**
	 * Getter method for the referenceEndpoints.
	 * @return the referenceEndpoints
	 */
	public List<EntityReferenceEndpoint> getReferenceEndpoints() {
		return referenceEndpoints;
	}

	/**
	 * Setter method for the referenceEndpoints.
	 * @param referenceEndpoints the referenceEndpoints to set
	 */
	void setReferenceEndpoints(final List<EntityReferenceEndpoint> referenceEndpoints) {
		this.referenceEndpoints = referenceEndpoints;
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
	void setCanonicalListNavigationNode(final NavigationNode canonicalListNavigationNode) {
		this.canonicalListNavigationNode = canonicalListNavigationNode;
	}

	/**
	 * Getter method for the navigation.
	 * @return the navigation
	 */
	public EntityNavigation getNavigation() {
		return navigation;
	}

	/**
	 * Setter method for the navigation.
	 * @param navigation the navigation to set
	 */
	void setNavigation(final EntityNavigation navigation) {
		this.navigation = navigation;
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
	void setDataRowMeta(final DataRowMeta dataRowMeta) {
		this.dataRowMeta = dataRowMeta;
	}

	/**
	 * Getter method for the dataRowTypes.
	 * @return the dataRowTypes
	 */
	public ISqlTypeInfo[] getDataRowTypes() {
		return dataRowTypes;
	}

	/**
	 * Setter method for the dataRowTypes.
	 * @param dataRowTypes the dataRowTypes to set
	 */
	void setDataRowTypes(final ISqlTypeInfo[] dataRowTypes) {
		this.dataRowTypes = dataRowTypes;
	}

	/**
	 * Getter method for the searcher.
	 * @return the searcher
	 */
	public EntitySearcher getSearcher() {
		return searcher;
	}

	/**
	 * Setter method for the searcher.
	 * @param searcher the searcher to set
	 */
	void setSearcher(final EntitySearcher searcher) {
		this.searcher = searcher;
	}

	/**
	 * Getter method for the annotations.
	 * @return the annotations
	 */
	public ClassKeyedContainer<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * Getter method for the specificCodeMapping.
	 * @return the specificCodeMapping
	 */
	public EntitySpecificCodeMapping getSpecificCodeMapping() {
		return specificCodeMapping;
	}

	/**
	 * Setter method for the specificCodeMapping.
	 * @param specificCodeMapping the specificCodeMapping to set
	 */
	void setSpecificCodeMapping(final EntitySpecificCodeMapping specificCodeMapping) {
		this.specificCodeMapping = specificCodeMapping;
	}

	// ----------------------------------------
	// TODO: clean up the code below this line

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
		return EntitySelection.forId(this, id).fetchSingleInstance(optional);
	}

	/**
	 * Looks for a reference with the specified field name.
	 * @param fieldName the field name
	 * @return the reference, or null if none was found
	 */
	public EntityReferenceEndpoint findReference(final String fieldName) {
		for (final EntityReferenceEndpoint endpoint : referenceEndpoints) {
			if (endpoint.getPropertyName().equals(fieldName)) {
				return endpoint;
			}
		}
		return null;
	}

	/**
	 * Creates and returns a {@link RelationalPath} to fetch this entity.
	 * @param alias the alias to use
	 * @return the relational path
	 */
	public RelationalPath<Object> createRelationalPath(final String alias) {
		ParameterUtil.ensureNotNull(alias, "alias");
		return new RelationalPathBase<Object>(Object.class, alias, null, tableName);
	}

	/**
	 * Queries this entity using the specified connection and alias.
	 * @param alias the alias for this entity
	 * @return the query
	 */
	public SQLQuery createQuery(final String alias) {
		ParameterUtil.ensureNotNull(alias, "alias");
		return getConnection().createQuery().from(createRelationalPath(alias));
	}

	/**
	 * Queries for the number of instances of this entity, or the number of
	 * instances accepted by the specified filter predicate.
	 * @param filterPredicate the filter predicate
	 * @return the number of instances
	 */
	public long count(final Predicate filterPredicate) {
		SQLQuery countQuery = createQuery(ALIAS);
		if (filterPredicate != null) {
			countQuery = countQuery.where(filterPredicate);
		}
		return countQuery.count();
	}

	/**
	 * Creates an {@link SQLInsertClause} for this entity.
	 * 
	 * @param alias the alias for this entity
	 * @return the insert clause
	 */
	public SQLInsertClause createInsert(final String alias) {
		ParameterUtil.ensureNotNull(alias, "alias");
		return getConnection().createInsert(createRelationalPath(alias));
	}

	/**
	 * Creates an {@link SQLInsertClause} for this entity using the specified
	 * columns. This is a convenience method to allow specifying the column
	 * names as strings instead of {@link Path}s.
	 * 
	 * @param alias the alias for this entity
	 * @param columns the column names
	 * @return the insert clause
	 */
	public SQLInsertClause createInsert(final String alias, final String... columns) {
		ParameterUtil.ensureNotNull(alias, "alias");
		ParameterUtil.ensureNotNull(columns, "columns");
		ParameterUtil.ensureNoNullElement(columns, "columns");
		final Path<?>[] paths = new Path<?>[columns.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = Expressions.path(Object.class, columns[i]);
		}
		return createInsert(alias).columns(paths);
	}

	/**
	 * Creates an {@link SQLUpdateClause} for this entity.
	 * 
	 * @param alias the alias for this entity
	 * @return the update clause
	 */
	public SQLUpdateClause createUpdate(final String alias) {
		ParameterUtil.ensureNotNull(alias, "alias");
		return getConnection().createUpdate(createRelationalPath(alias));
	}

	/**
	 * Creates an {@link SQLDeleteClause} for this entity.
	 * 
	 * @param alias the alias for this entity
	 * @return the delete clause
	 */
	public SQLDeleteClause createDelete(final String alias) {
		ParameterUtil.ensureNotNull(alias, "alias");
		return getConnection().createDelete(createRelationalPath(alias));
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
	 * @return the database connection for the database that contains this entity.
	 */
	public IEntityDatabaseConnection getConnection() {
		return EntityConnectionManager.getConnection(getDatabase());
	}

}
