/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import name.martingeisse.admin.application.AdminWicketApplication;
import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.application.capabilities.IEntityPresentationContributor;
import name.martingeisse.admin.application.capabilities.IEntityReferenceDetector;
import name.martingeisse.admin.pages.EntityPresentationPage;
import name.martingeisse.admin.pages.EntityTablePage;

/**
 * This class defines the structure but not the content of {@link ApplicationSchema}.
 */
public class AbstractApplicationSchema {

	/**
	 * the databaseDescriptors
	 */
	private final List<DatabaseDescriptor> databaseDescriptors;

	/**
	 * the explicitEntityDescriptors
	 */
	private final List<EntityDescriptor> explicitEntityDescriptors;

	/**
	 * the implicitEntityDescriptors
	 */
	private final List<EntityDescriptor> implicitEntityDescriptors;

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
	public AbstractApplicationSchema() {
		this.databaseDescriptors = new ArrayList<DatabaseDescriptor>();
		this.explicitEntityDescriptors = new ArrayList<EntityDescriptor>();
		this.implicitEntityDescriptors = new ArrayList<EntityDescriptor>();
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
	 * Getter method for the explicitEntityDescriptors.
	 * @return the explicitEntityDescriptors
	 */
	public List<EntityDescriptor> getExplicitEntityDescriptors() {
		return explicitEntityDescriptors;
	}

	/**
	 * Getter method for the implicitEntityDescriptors.
	 * @return the implicitEntityDescriptors
	 */
	public List<EntityDescriptor> getImplicitEntityDescriptors() {
		return implicitEntityDescriptors;
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
	 * Fetches implicit schema components from the database.
	 */
	protected final void initializeImplicitSchema() {
		for (DatabaseDescriptor databaseDescriptor : databaseDescriptors) {
			DiscoverEntitiesAction action = new DiscoverEntitiesAction();
			action.setDatabase(databaseDescriptor);
			for (EntityDescriptor entity : action.execute()) {
				implicitEntityDescriptors.add(entity);
			}
		}
	}
	
	/**
	 * Builds internal data structures from the actual schema built by
	 * the subclass. This method should be invoked after setting custom
	 * schema components but before using the schema.
	 */
	protected final void prepare() {
		mergeEntityDescriptors();
		detectEntityReferences();
		registerEntityPresenters();
		mountPages();
	}
	
	/**
	 * Merges explicit and implicit entity descriptors.
	 */
	private void mergeEntityDescriptors() {
		HashMap<String, EntityDescriptor> knownDescriptors = new HashMap<String, EntityDescriptor>();
		for (EntityDescriptor implicitDescriptor : implicitEntityDescriptors) {
			knownDescriptors.put(implicitDescriptor.getTableName(), implicitDescriptor);
		}
		for (EntityDescriptor explicitDescriptor : explicitEntityDescriptors) {
			String name = explicitDescriptor.getTableName();
			EntityDescriptor implicitDescriptor = knownDescriptors.get(name);
			if (implicitDescriptor == null) {
				knownDescriptors.put(name, explicitDescriptor);
			} else {
				EntityDescriptor resultDescriptor = mergeEntityDescriptors(explicitDescriptor, implicitDescriptor);
				knownDescriptors.put(name, resultDescriptor);
			}
		}
		for (EntityDescriptor descriptor : knownDescriptors.values()) {
			entityDescriptors.add(descriptor);
		}
	}

	/**
	 * Merges an explicit with an implicit entity descriptor.
	 */
	private EntityDescriptor mergeEntityDescriptors(EntityDescriptor explicitDescriptor, EntityDescriptor implicitDescriptor) {
		if (!explicitDescriptor.getTableName().equals(implicitDescriptor.getTableName())) {
			throw new IllegalArgumentException("trying to merge two entity descriptors with different table name: " + 
				explicitDescriptor.getTableName() + " / " + implicitDescriptor.getTableName());
		}
		if (explicitDescriptor.getDatabase() != implicitDescriptor.getDatabase()) {
			throw new IllegalArgumentException("trying to merge two entity descriptors with different database: " + explicitDescriptor.getTableName());
		}
		EntityDescriptor result = new EntityDescriptor();
		result.setDatabase(explicitDescriptor.getDatabase());
		result.setTableName(explicitDescriptor.getTableName());
		return result;
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
