/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.admin.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.multi.RawGlobalEntityListPresenter;
import name.martingeisse.admin.single.EntityInstance;
import name.martingeisse.admin.single.FetchEntityInstanceAction;
import name.martingeisse.admin.single.ISingleEntityPresenter;

/**
 * This class captures a descriptor for a database entity (table).
 * Instances are created either explicitly from code or implicitly
 * by fetching information from the database. Explicit and implicit
 * descriptors are then merged to build the final application schema.
 * 
 * TODO: this class should not be serializable. Instead, models should be able
 * to detach and re-attach from/to instances of this class.
 * 
 * TODO: rename "tableName" to "name".
 */
public class EntityDescriptor implements Serializable {

	/**
	 * the database
	 */
	private DatabaseDescriptor database;

	/**
	 * the tableName
	 */
	private String tableName;

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
	 * the singlePresenters
	 */
	private List<ISingleEntityPresenter> singlePresenters;

	/**
	 * the globalListPresenters
	 */
	private List<IGlobalEntityListPresenter> globalListPresenters;

	/**
	 * Constructor.
	 */
	public EntityDescriptor() {
		this.properties = new HashMap<String, EntityPropertyDescriptor>();
		this.incomingReferences = new ArrayList<EntityReferenceInfo>();
		this.outgoingReferences = new ArrayList<EntityReferenceInfo>();
		this.singlePresenters = new ArrayList<ISingleEntityPresenter>();
		this.globalListPresenters = new ArrayList<IGlobalEntityListPresenter>();
		globalListPresenters.add(new RawGlobalEntityListPresenter()); // TODO: remove
	}

	/**
	 * Getter method for the database.
	 * @return the database
	 */
	public DatabaseDescriptor getDatabase() {
		return database;
	}

	/**
	 * Setter method for the database.
	 * @param database the database to set
	 */
	public void setDatabase(final DatabaseDescriptor database) {
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
	 * Getter method for the singlePresenters.
	 * @return the singlePresenters
	 */
	public List<ISingleEntityPresenter> getSinglePresenters() {
		return singlePresenters;
	}

	/**
	 * Setter method for the singlePresenters.
	 * @param singlePresenters the singlePresenters to set
	 */
	public void setSinglePresenters(final List<ISingleEntityPresenter> singlePresenters) {
		this.singlePresenters = singlePresenters;
	}

	/**
	 * Getter method for the globalListPresenters.
	 * @return the globalListPresenters
	 */
	public List<IGlobalEntityListPresenter> getGlobalListPresenters() {
		return globalListPresenters;
	}

	/**
	 * Setter method for the globalListPresenters.
	 * @param globalListPresenters the globalListPresenters to set
	 */
	public void setGlobalListPresenters(final List<IGlobalEntityListPresenter> globalListPresenters) {
		this.globalListPresenters = globalListPresenters;
	}

	/**
	 * Returns the single-instance presenter with the specified URL ID.
	 * @param urlId the URL ID
	 * @return the presenter, or null if not found
	 */
	public ISingleEntityPresenter getSinglePresenter(final String urlId) {
		for (final ISingleEntityPresenter presenter : getSinglePresenters()) {
			if (presenter.getUrlId().equals(urlId)) {
				return presenter;
			}
		}
		return null;
	}

	/**
	 * Returns the global list presenter with the specified URL ID.
	 * @param urlId the URL ID
	 * @return the presenter, or null if not found
	 */
	public IGlobalEntityListPresenter getGlobalListPresenter(final String urlId) {
		for (final IGlobalEntityListPresenter presenter : getGlobalListPresenters()) {
			if (presenter.getUrlId().equals(urlId)) {
				return presenter;
			}
		}
		return null;
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

}
