/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.GeneralEntityConfiguration;
import name.martingeisse.admin.entity.IEntityNameAware;
import name.martingeisse.admin.entity.component.list.datatable.raw.RawEntityListPanel;
import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceInfo;
import name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.INavigationNodeVisitor;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;

/**
 * This class holds global data generated from plugins / capabilities and modifiers.
 */
public class ApplicationSchema {

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
	private final List<AbstractDatabaseDescriptor> databaseDescriptors;

	/**
	 * the entityDescriptors
	 */
	private final List<EntityDescriptor> entityDescriptors;

	/**
	 * the entityReferences
	 */
	private final List<EntityReferenceInfo> entityReferences;

	/**
	 * Constructor.
	 */
	public ApplicationSchema() {
		this.databaseDescriptors = new ArrayList<AbstractDatabaseDescriptor>();
		this.entityDescriptors = new ArrayList<EntityDescriptor>();
		this.entityReferences = new ArrayList<EntityReferenceInfo>();
	}

	/**
	 * Getter method for the databaseDescriptors.
	 * @return the databaseDescriptors
	 */
	public List<AbstractDatabaseDescriptor> getDatabaseDescriptors() {
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
	 * Getter method for the entityReferences.
	 * @return the entityReferences
	 */
	public List<EntityReferenceInfo> getEntityReferences() {
		return entityReferences;
	}

	/**
	 * Finds and returns an entity by its name.
	 * @param name the entity name
	 * @return the entity descriptor, or null if not found
	 */
	public EntityDescriptor findEntity(final String name) {
		for (final EntityDescriptor entity : entityDescriptors) {
			if (entity.getName().equals(name)) {
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
	}

	/**
	 * Copies the list of databases from the {@link ApplicationConfigurationOld}.
	 */
	private void copyDatabaseList() {
		for (final AbstractDatabaseDescriptor database : ApplicationConfiguration.get().getDatabases()) {
			databaseDescriptors.add(database);
		}
	}

	/**
	 * Fetches implicit schema components from the database.
	 */
	private void buildEntityDescriptors() {
		for (final AbstractDatabaseDescriptor databaseDescriptor : databaseDescriptors) {
			final DiscoverEntitiesAction action = new DiscoverEntitiesAction();
			action.setDatabase(databaseDescriptor);
			for (final EntityDescriptor entity : action.execute()) {
				entity.mapNames();
				entityDescriptors.add(entity);
			}
		}
	}

	/**
	 * Finds entity references and stores them in the entity reference list as well
	 * as the source and destination entities.
	 */
	private void detectEntityReferences() {
		for (final EntityDescriptor entity : entityDescriptors) {
			for (final EntityPropertyDescriptor property : entity.getPropertiesInDatabaseOrder()) {
				final String propertyName = property.getName();
				for (final IEntityReferenceDetector detector : EntityConfigurationUtil.getEntityReferenceDetectors()) {
					final String destinationName = detector.detectEntityReference(this, entity.getName(), entity.getTableName(), propertyName);
					if (destinationName != null) {
						final EntityDescriptor destination = findEntity(destinationName);
						if (destination != null) {
							registerEntityReference(entity, destination, propertyName);
						}
					}
				}
			}
		}
	}

	/**
	 * Registers an entity reference once it is detected.
	 */
	private void registerEntityReference(final EntityDescriptor source, final EntityDescriptor destination, final String fieldName) {
		final EntityReferenceInfo reference = new EntityReferenceInfo();
		reference.setSource(source);
		reference.setDestination(destination);
		reference.setFieldName(fieldName);
		entityReferences.add(reference);
		source.getOutgoingReferences().add(reference);
		destination.getIncomingReferences().add(reference);
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
		NavigationConfigurationUtil.getNavigationTree().acceptVisior(new INavigationNodeVisitor() {
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
		final NavigationNode globalRoot = NavigationConfigurationUtil.getNavigationTree().getRoot();
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
		final Iterable<IEntityNavigationContributor> entityNavigationContributors = EntityConfigurationUtil.getEntityNavigationContributors();
		for (final EntityDescriptor entity : entityDescriptors) {
			final String entityName = entity.getName();
			final NavigationNode canonicalEntityListNode = canonicalEntityListNodes.get(entityName);
			final NavigationNode entityInstanceRootNode = canonicalEntityListNode.getChildFactory().createNavigationFolderChild("${id}", "Entity Instance");
			entity.setInstanceNavigationRootNode(entityInstanceRootNode);
			for (final IEntityNavigationContributor contributor : entityNavigationContributors) {
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

}
