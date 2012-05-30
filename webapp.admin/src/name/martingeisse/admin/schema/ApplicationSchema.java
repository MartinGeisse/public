/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.application.AdminWicketApplication;
import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.capabilities.IEntityPresentationContributor;
import name.martingeisse.admin.application.capabilities.IEntityReferenceDetector;
import name.martingeisse.admin.pages.EntityPresentationPage;
import name.martingeisse.admin.pages.EntityTablePage;

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
	private final List<DatabaseDescriptor> databaseDescriptors;

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
		this.databaseDescriptors = new ArrayList<DatabaseDescriptor>();
		this.entityDescriptors = new ArrayList<EntityDescriptor>();
		this.entityReferences = new ArrayList<EntityReferenceInfo>();
	}

	/**
	 * Getter method for the databaseDescriptors.
	 * @return the databaseDescriptors
	 */
	public List<DatabaseDescriptor> getDatabaseDescriptors() {
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
	}
	
	/**
	 * Copies the list of databases from the {@link ApplicationConfiguration}.
	 */
	private void copyDatabaseList() {
		for (DatabaseDescriptor database : ApplicationConfiguration.getDatabases()) {
			databaseDescriptors.add(database);
		}
	}
	
	/**
	 * Fetches implicit schema components from the database.
	 */
	private void buildEntityDescriptors() {
		for (DatabaseDescriptor databaseDescriptor : databaseDescriptors) {
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
			for (EntityPropertyDescriptor property : entity.getProperties()) {
				String propertyName = property.getName();
				for (IEntityReferenceDetector detector : ApplicationConfiguration.getCapabilities().getEntityReferenceDetectors()) {
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
			for (IEntityPresentationContributor contributor : ApplicationConfiguration.getCapabilities().getEntityPresentationContributors()) {
				contributor.contributeEntityPresenters(entity);
			}
		}
	}
	
	/**
	 * Mounts pages for all entities and presenters.
	 */
	private void mountPages() {
		AdminWicketApplication application = AdminWicketApplication.get();
		application.mountPage("/${entity}/list/#{presenter}", EntityTablePage.class);
		application.mountPage("/${entity}/show/${id}/#{presenter}", EntityPresentationPage.class);
	}
	
}
