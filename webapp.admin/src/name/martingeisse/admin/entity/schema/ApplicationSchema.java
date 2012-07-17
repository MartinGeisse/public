/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.GeneralEntityConfiguration;
import name.martingeisse.admin.entity.IEntityNameAware;
import name.martingeisse.admin.entity.IEntityPresentationContributor;
import name.martingeisse.admin.entity.IEntityReferenceDetector;
import name.martingeisse.admin.navigation.DefaultNavigationNodeHandlerMergeStrategy;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.INavigationNodeHandlerMergeStrategy;
import name.martingeisse.admin.navigation.INavigationNodeVisitor;
import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.handler.GlobalEntityListNavigationHandler;
import name.martingeisse.admin.pages.EntityPresentationPage;

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
		ApplicationSchema schema = new ApplicationSchema();
		
		// synchronize on the schema to ensure that the thread's cache is emptied afterwards
		synchronized(schema) {
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
	public EntityDescriptor findEntity(String name) {
		for (final EntityDescriptor entity : entityDescriptors) {
			if (entity.getTableName().equals(name)) {
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
		registerEntityPresenters();
		mountPages();
		createNavigation();
	}
	
	/**
	 * Copies the list of databases from the {@link ApplicationConfigurationOld}.
	 */
	private void copyDatabaseList() {
		for (AbstractDatabaseDescriptor database : ApplicationConfiguration.get().getDatabases()) {
			databaseDescriptors.add(database);
		}
	}
	
	/**
	 * Fetches implicit schema components from the database.
	 */
	private void buildEntityDescriptors() {
		for (AbstractDatabaseDescriptor databaseDescriptor : databaseDescriptors) {
			DiscoverEntitiesAction action = new DiscoverEntitiesAction();
			action.setDatabase(databaseDescriptor);
			for (EntityDescriptor entity : action.execute()) {
				entityDescriptors.add(entity);
			}
		}
	}
	
	/**
	 * Finds entity references and stores them in the entity reference list as well
	 * as the source and destination entities.
	 */
	private void detectEntityReferences() {
		for (EntityDescriptor entity : entityDescriptors) {
			for (EntityPropertyDescriptor property : entity.getProperties().values()) {
				String propertyName = property.getName();
				for (IEntityReferenceDetector detector : EntityConfigurationUtil.getEntityReferenceDetectors()) {
					String destinationName = detector.detectEntityReference(this, entity.getTableName(), propertyName);
					if (destinationName != null) {
						EntityDescriptor destination = findEntity(destinationName);
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
	private void registerEntityReference(EntityDescriptor source, EntityDescriptor destination, String fieldName) {
		EntityReferenceInfo reference = new EntityReferenceInfo();
		reference.setSource(source);
		reference.setDestination(destination);
		reference.setFieldName(fieldName);
		entityReferences.add(reference);
		source.getOutgoingReferences().add(reference);
		destination.getIncomingReferences().add(reference);
	}
	
	/**
	 * Loops through all entities and registers their presenters, which in turn are contributed
	 * by plugins.
	 */
	private void registerEntityPresenters() {
		for (EntityDescriptor entity : entityDescriptors) {
			for (IEntityPresentationContributor contributor : EntityConfigurationUtil.getEntityPresentationContributors()) {
				contributor.contributeEntityPresenters(entity);
			}
			entity.addDefaultListPresenterIfNeeded();
		}
	}
	
	/**
	 * Mounts pages for all entities and presenters.
	 */
	private void mountPages() {
		AdminWicketApplication application = AdminWicketApplication.get();
//		application.mountPage("/${entity}/list/#{presenter}", EntityTablePage.class);
		application.mountPage("/${entity}/show/${id}/#{presenter}", EntityPresentationPage.class);
//		application.mountNonNavigationPage("/${entity}/list/#{presenter}", EntityTablePage.class);
//		application.mountNonNavigationPage("/${entity}/show/${id}/#{presenter}", EntityPresentationPage.class);
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
	 * 
	 * TODO: This uses a fixed global mount point. I'm currently working on local navigation trees;
	 * in the future we need a way to mount whole entity types in the navigation, automatically
	 * mount local trees there, and use a fallback for entity types that have no such place to mount.
	 * However, it's probably better to replace entity list presentation first, because entity
	 * instance mounting is going to depend on that.
	 */
	private void createNavigation() {
		NavigationNode root = NavigationConfigurationUtil.getNavigationTree().getRoot();
		NavigationNode entitiesNode = root.createGlobalNavigationFolderChild("new-entities", "New Entities");
		NavigationNode templateNode = EntityConfigurationUtil.getGeneralEntityConfiguration().getEntityInstanceNavigationTemplate();
		INavigationNodeHandlerMergeStrategy mergeStrategy = new DefaultNavigationNodeHandlerMergeStrategy();
		for (EntityDescriptor entity : entityDescriptors) {
			final String entityName = entity.getTableName();
			NavigationNode entityListNode = entitiesNode.createChild(new GlobalEntityListNavigationHandler(entity, "default"));
			NavigationNode entityInstanceNode = entityListNode.createGlobalNavigationFolderChild("${id}", "Entity Instance");
			entityInstanceNode.merge(templateNode.cloneSubtree(), mergeStrategy);
			entityInstanceNode.acceptVisitor(new INavigationNodeVisitor() {
				@Override
				public void visit(NavigationNode node) {
					INavigationNodeHandler handler = node.getHandler();
					if (handler instanceof IEntityNameAware) {
						IEntityNameAware entityNameAware = (IEntityNameAware)handler;
						entityNameAware.setEntityName(entityName);
					}
				}
			});
		}
	}
	
}
