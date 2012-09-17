/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.entity.EntityCapabilities;
import name.martingeisse.admin.entity.GeneralEntityConfiguration;
import name.martingeisse.admin.entity.IEntityNameAware;
import name.martingeisse.admin.entity.UnknownEntityException;
import name.martingeisse.admin.entity.component.list.datatable.raw.RawEntityListPanel;
import name.martingeisse.admin.entity.property.IRawEntityListPropertyDisplayFilter;
import name.martingeisse.admin.entity.schema.annotation.DefaultEntityAnnotationResolver;
import name.martingeisse.admin.entity.schema.annotation.IEntityAnnotationResolver;
import name.martingeisse.admin.entity.schema.lowlevel.ILowlevelDatabaseStructure;
import name.martingeisse.admin.entity.schema.lowlevel.JdbcColumnStructure;
import name.martingeisse.admin.entity.schema.lowlevel.JdbcSchemaStructure;
import name.martingeisse.admin.entity.schema.lowlevel.JdbcTableStructure;
import name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector;
import name.martingeisse.admin.entity.schema.type.IEntityIdTypeInfo;
import name.martingeisse.admin.entity.schema.type.ISqlTypeInfo;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.INavigationNodeVisitor;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationParameters;
import name.martingeisse.common.database.IDatabaseDescriptor;
import name.martingeisse.common.datarow.DataRowMeta;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.log4j.Logger;

/**
 * This class holds global data generated from plugins / capabilities and modifiers.
 */
public class ApplicationSchema {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ApplicationSchema.class);
	
	/**
	 * the instance
	 */
	public static volatile ApplicationSchema instance;

	/**
	 * Initializes the application schema.
	 */
	public static void initialize() {
		final ApplicationSchema schema = new ApplicationSchema();

		// synchronize on the schema to ensure that the thread's cache is emptied afterwards
		synchronized (schema) {
			schema.prepare();
		}

		// store the instance -- the 'instance' variable is volatile to avoid caching
		instance = schema;

	}

	/**
	 * the databaseDescriptors
	 */
	private final List<IDatabaseDescriptor> databaseDescriptors;

	/**
	 * the databaseStructures
	 */
	private final Map<IDatabaseDescriptor, JdbcSchemaStructure> databaseStructures;
	
	/**
	 * the entityDescriptors
	 */
	private final List<EntityDescriptor> entityDescriptors;

	/**
	 * Constructor.
	 */
	public ApplicationSchema() {
		this.databaseDescriptors = new ArrayList<IDatabaseDescriptor>();
		this.databaseStructures = new HashMap<IDatabaseDescriptor, JdbcSchemaStructure>();
		this.entityDescriptors = new ArrayList<EntityDescriptor>();
	}

	/**
	 * Getter method for the databaseDescriptors.
	 * @return the databaseDescriptors
	 */
	public List<IDatabaseDescriptor> getDatabaseDescriptors() {
		return databaseDescriptors;
	}

	/**
	 * Getter method for the entityDescriptors.
	 * @return the entityDescriptors
	 */
	public List<EntityDescriptor> getEntityDescriptors() {
		return entityDescriptors;
	}

	/**
	 * Finds and returns an entity by its name.
	 * @param entityName the entity name
	 * @return the entity descriptor, or null if not found
	 */
	public EntityDescriptor findOptionalEntity(final String entityName) {
		ParameterUtil.ensureNotNull(entityName, "entityName");
		for (final EntityDescriptor entity : entityDescriptors) {
			if (entity.getName().equals(entityName)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * Finds and returns an entity by its name. Throws an {@link UnknownEntityException}
	 * if no such entity exists.
	 * @param entityName the entity name
	 * @return the entity descriptor
	 */
	public EntityDescriptor findRequiredEntity(final String entityName) {
		EntityDescriptor entity = findOptionalEntity(entityName);
		if (entity == null) {
			throw new UnknownEntityException(entityName);
		}
		return entity;
	}

	/**
	 * Finds and returns an entity by its database table name.
	 * @param tableName the table name
	 * @return the entity descriptor, or null if not found
	 */
	public EntityDescriptor findEntityByTableName(final String tableName) {
		for (final EntityDescriptor entity : entityDescriptors) {
			if (entity.getTableName().equals(tableName)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * Builds internal data structures from the actual schema built by
	 * the subclass. This method should be invoked after setting custom
	 * schema components but before using the schema.
	 */
	protected final void prepare() {
		copyDatabaseList();
		buildEntityDescriptors();
		detectEntityReferences();
		createNavigation();
		initializeSearchStrategies();
		initializeAutoformMetadata();
	}

	/**
	 * Copies the list of databases from the {@link ApplicationConfiguration}.
	 */
	private void copyDatabaseList() {
		for (final IDatabaseDescriptor database : ApplicationConfiguration.get().getDatabases()) {
			databaseDescriptors.add(database);
		}
	}

	/**
	 * Fetches implicit schema components from the database.
	 */
	private void buildEntityDescriptors() {
		for (final IDatabaseDescriptor databaseDescriptor : databaseDescriptors) {
			try {
				Connection connection = databaseDescriptor.createJdbcConnection();
				
				// determine database structure
				JdbcSchemaStructure structure = new JdbcSchemaStructure(connection);
				databaseStructures.put(databaseDescriptor, structure);

				// discover entities
				List<EntityDescriptor> currentDatabaseEntities = new ArrayList<EntityDescriptor>();
				for (JdbcTableStructure table : structure.getTables()) {
					logger.debug("discovered entity table: " + table.getSelector());
					
					// initialize the entity descriptor
					final EntityDescriptor entityDescriptor = new EntityDescriptor();
					entityDescriptor.setDatabase(databaseDescriptor);
					entityDescriptor.setTableName(table.getSelector().getTable());
					
					// initialize properties
					final List<EntityPropertyDescriptor> properties = new ArrayList<EntityPropertyDescriptor>();
					for (JdbcColumnStructure column : table.getColumns()) {
						logger.debug("discovered entity property: " + column.getSelector());
						final EntityPropertyDescriptor propertyDescriptor = new EntityPropertyDescriptor();
						propertyDescriptor.setName(column.getSelector().getColumn());
						propertyDescriptor.setType(column.determineHighlevelType(databaseDescriptor.getDefaultTimeZone() != null));
						propertyDescriptor.setVisibleInRawEntityList(true);
						propertyDescriptor.setVisibleInRawEntityList(isPropertyVisibleInRawEntityList(entityDescriptor, propertyDescriptor));
						propertyDescriptor.addAnnotationsFromType();
						properties.add(propertyDescriptor);
						logger.debug("-> type: " + propertyDescriptor.getType());
					}
					entityDescriptor.initializeProperties(properties);
					
					// initialize ID property information
					// note: we cannot handle entities without a primary key or with a multi-column primary key yet
					if (table.getPrimaryKeyElements().size() != 1) {
						continue;
					}
					String idColumnName = table.getPrimaryKeyElements().get(0).getSelector().getColumn();
					final EntityPropertyDescriptor idPropertyDescriptor = entityDescriptor.getPropertiesByName().get(idColumnName);
					if (idPropertyDescriptor == null) {
						throw new IllegalStateException("table meta-data of table " + entityDescriptor.getTableName() + " specified column " + idColumnName + " as its ID column, but no such column exists in the property descriptors");
					}
					final ISqlTypeInfo idColumnType = idPropertyDescriptor.getType();
					if (!(idColumnType instanceof IEntityIdTypeInfo)) {
						throw new IllegalStateException("type of the ID column " + entityDescriptor.getTableName() + "." + idColumnName + " is not supported as an entity ID type");
					}
					entityDescriptor.setIdColumnName(idColumnName);
					entityDescriptor.setIdColumnType((IEntityIdTypeInfo)idColumnType);
					
					// add the entity to the schema
					entityDescriptors.add(entityDescriptor);
					currentDatabaseEntities.add(entityDescriptor);
					
				}
				
				// Fetch the data row meta-data for each table. Unlike the properties/columns fetched
				// above this directly detects the format of the result set when fetching the entity.
				{
					final Statement statement = connection.createStatement();
					for (final EntityDescriptor entityDescriptor : currentDatabaseEntities) {
						final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + entityDescriptor.getTableName() + " LIMIT 1");
						entityDescriptor.setDataRowMeta(new DataRowMeta(resultSet.getMetaData()));
						entityDescriptor.initializeDataRowTypes();
						resultSet.close();
					}
				}
				
				// map the table names to entity names and display names
				for (final EntityDescriptor entity : currentDatabaseEntities) {
					entity.mapNames();
				}
				
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 
	 */
	private boolean isPropertyVisibleInRawEntityList(final EntityDescriptor entity, final EntityPropertyDescriptor propertyDescriptor) {
		int score = Integer.MIN_VALUE;
		boolean visible = true;
		for (final IRawEntityListPropertyDisplayFilter filter : EntityCapabilities.rawEntityListPropertyDisplayFilterCapability) {
			final int filterScore = filter.getScore(entity, propertyDescriptor);
			if (filterScore >= score) {
				final Boolean filterResult = filter.isPropertyVisible(entity, propertyDescriptor);
				if (filterResult != null) {
					score = filterScore;
					visible = filterResult;
				}
			}
		}
		return visible;
	}
	
	/**
	 * Finds entity references and stores them in the entity reference list as well
	 * as the source and destination entities.
	 */
	private void detectEntityReferences() {
		for (final EntityDescriptor entity : entityDescriptors) {
			ILowlevelDatabaseStructure lowlevelStructure = databaseStructures.get(entity.getDatabase());
			for (final EntityPropertyDescriptor property : entity.getPropertiesInDatabaseOrder()) {
				final String propertyName = property.getName();
				for (final IEntityReferenceDetector detector : EntityCapabilities.entityReferenceDetectorCapability) {
					detector.detectEntityReference(this, lowlevelStructure, entity, propertyName);
				}
			}
		}
	}

	/**
	 * Creates navigation nodes in the global navigation tree for each entity, based on the
	 * globally defined template (in {@link GeneralEntityConfiguration}) and local navigation
	 * trees defined in the {@link EntityDescriptor}s.
	 * 
	 * This method also looks for any navigation nodes in the local navigation tree that
	 * implement {@link IEntityNameAware} and sets the entity name for them. This is a
	 * central feature for building entity-instance navigation tree templates, since a
	 * general template cannot be bound to a single entity type by definition.
	 */
	private void createNavigation() {

		// determine the (declared) canonical entity list nodes
		final Map<String, NavigationNode> canonicalEntityListNodes = new HashMap<String, NavigationNode>();
		NavigationParameters.navigationTreeParameter.get().acceptVisior(new INavigationNodeVisitor() {
			@Override
			public void visit(final NavigationNode node) {
				final String entityName = node.getHandler().getEntityNameForCanonicalEntityListNode();
				if (entityName != null) {
					final NavigationNode old = canonicalEntityListNodes.put(entityName, node);
					if (old != null) {
						throw new IllegalStateException("found two 'canonical' entity list nodes for entity " + entityName + ": " + node.getPath() + " and " + old.getPath());
					}
				}
			}
		});

		// create ad-hoc canonical list nodes for entities with no declared canonical list node
		final NavigationNode globalRoot = NavigationParameters.navigationTreeParameter.get().getRoot();
		final NavigationNode allEntitiesNode = globalRoot.getChildFactory().createNavigationFolderChild("all-entities", "All Entities");
		for (final EntityDescriptor entity : entityDescriptors) {

			// create a child node of "All Entities" for this entity
			final NavigationNode adHocListNode = allEntitiesNode.getChildFactory().createEntityListPanelChild(entity.getName(), entity.getDisplayName(), RawEntityListPanel.class, entity.getName());

			// use the declared canonical list node if any, or the ad-hoc node as a fallback
			final String entityName = entity.getName();
			final NavigationNode declaredCanonicalListNode = canonicalEntityListNodes.get(entityName);
			if (declaredCanonicalListNode == null) {
				canonicalEntityListNodes.put(entityName, adHocListNode);
				entity.setCanonicalListNavigationNode(adHocListNode);
			} else {
				entity.setCanonicalListNavigationNode(declaredCanonicalListNode);
			}
		}

		// create and mount the local entity instance navigation tree for each entity
		for (final EntityDescriptor entity : entityDescriptors) {
			final String entityName = entity.getName();
			final NavigationNode canonicalEntityListNode = canonicalEntityListNodes.get(entityName);
			final NavigationNode entityInstanceRootNode = canonicalEntityListNode.getChildFactory().createNavigationFolderChild("${id}", "Entity Instance");
			entity.setInstanceNavigationRootNode(entityInstanceRootNode);
			for (final IEntityNavigationContributor contributor : EntityCapabilities.entityNavigationContributorCapability) {
				contributor.contributeNavigationNodes(entity, entityInstanceRootNode);
			}
			entityInstanceRootNode.acceptVisitor(new INavigationNodeVisitor() {
				@Override
				public void visit(final NavigationNode node) {
					final INavigationNodeHandler handler = node.getHandler();
					if (handler instanceof IEntityNameAware) {
						final IEntityNameAware entityNameAware = (IEntityNameAware)handler;
						entityNameAware.setEntityName(entityName);
					}
				}
			});
		}

	}

	/**
	 * Initializes the search strategies for all entities.
	 */
	private void initializeSearchStrategies() {
		for (final EntityDescriptor entity : entityDescriptors) {
			entity.initializeSearchStrategy();
		}
	}

	/**
	 * Initializes meta-data used to build autoforms.
	 */
	private void initializeAutoformMetadata() {
		IEntityAnnotationResolver resolver = new DefaultEntityAnnotationResolver();
		resolver.resolveEntityAnnotations(this);
	}
	
}
