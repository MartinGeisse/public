/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.util.List;
import java.util.NoSuchElementException;

import name.martingeisse.admin.entity.component.list.EntityInstanceDataProvider;
import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.list.EntityConditions;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceEndpoint;
import name.martingeisse.common.util.ObjectStateUtil;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.model.IModel;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.template.BooleanTemplate;

/**
 * This class groups an entity descriptor and filtering predicate in
 * a single object.
 */
public final class EntitySelection {

	/**
	 * the entityModel
	 */
	private final IModel<EntityDescriptor> entityModel;

	/**
	 * the predicate
	 */
	private final Predicate predicate;

	/**
	 * Constructor.
	 * @param entityModel the model for the entity descriptor
	 */
	public EntitySelection(final IModel<EntityDescriptor> entityModel) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		this.entityModel = entityModel;
		this.predicate = null;
	}

	/**
	 * Constructor.
	 * @param entityModel the model for the entity descriptor
	 * @param predicate the fetch predicate
	 */
	public EntitySelection(final IModel<EntityDescriptor> entityModel, final Predicate predicate) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		this.entityModel = entityModel;
		this.predicate = predicate;
	}

	/**
	 * Returns an instance of this class that selects an instance of the specified entity by id.
	 * @param entityModel the model for the entity descriptor
	 * @param id the id of the entity instance to select
	 * @return the {@link EntitySelection} instance
	 */
	public static EntitySelection forId(final IModel<EntityDescriptor> entityModel, Object id) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		ParameterUtil.ensureNotNull(id, "id");
		return forId(entityModel, entityModel.getObject(), id);
	}
	
	/**
	 * Returns an instance of this class that selects an instance of the specified entity by id.
	 * @param entity the entity descriptor
	 * @param id the id of the entity instance to select
	 * @return the {@link EntitySelection} instance
	 */
	public static EntitySelection forId(EntityDescriptor entity, Object id) {
		ParameterUtil.ensureNotNull(entity, "entity");
		ParameterUtil.ensureNotNull(id, "id");
		return forId(new EntityDescriptorModel(entity), entity, id);
	}
	
	/**
	 * Returns an instance of this class that selects an instance of the specified entity by id.
	 * @param entityName the entity name
	 * @param id the id of the entity instance to select
	 * @return the {@link EntitySelection} instance
	 */
	public static EntitySelection forId(String entityName, Object id) {
		ParameterUtil.ensureNotNull(entityName, "entityName");
		ParameterUtil.ensureNotNull(id, "id");
		return forId(new EntityDescriptorModel(entityName), id);
	}

	/**
	 * Internal implementation of the forId() methods.
	 */
	private static EntitySelection forId(final IModel<EntityDescriptor> entityModel, EntityDescriptor entity, Object id) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		ParameterUtil.ensureNotNull(entity, "entity");
		ParameterUtil.ensureNotNull(id, "id");
		return forKey(entityModel, entity, entity.getIdColumnName(), id, false);
	}

	/**
	 * Returns an instance of this class that selects instances of the specified entity by comparing a key property with a fixed value.
	 * @param entityModel the model for the entity descriptor
	 * @param propertyName the name of the key property that is used to select instances
	 * @param propertyValue the value of the key property that is used to select instances
	 * @param nullIsReference whether null is a valid value of the key property (true) or indicates an empty selection implicitly (false)
	 * @return the {@link EntitySelection} instance
	 */
	public static EntitySelection forKey(final IModel<EntityDescriptor> entityModel, String propertyName, Object propertyValue, boolean nullIsReference) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		ParameterUtil.ensureNotNull(propertyName, "propertyName");
		return forKey(entityModel, entityModel.getObject(), propertyName, propertyValue, nullIsReference);
	}
	
	/**
	 * Returns an instance of this class that selects instances of the specified entity by comparing a key property with a fixed value.
	 * @param entity the entity descriptor
	 * @param propertyName the name of the key property that is used to select instances
	 * @param propertyValue the value of the key property that is used to select instances
	 * @param nullIsReference whether null is a valid value of the key property (true) or indicates an empty selection implicitly (false)
	 * @return the {@link EntitySelection} instance
	 */
	public static EntitySelection forKey(EntityDescriptor entity, String propertyName, Object propertyValue, boolean nullIsReference) {
		ParameterUtil.ensureNotNull(entity, "entity");
		ParameterUtil.ensureNotNull(propertyName, "propertyName");
		return forKey(new EntityDescriptorModel(entity), entity, propertyName, propertyValue, nullIsReference);
	}
	
	/**
	 * Returns an instance of this class that selects instances of the specified entity by comparing a key property with a fixed value.
	 * @param entityName the entity name
	 * @param propertyName the name of the key property that is used to select instances
	 * @param propertyValue the value of the key property that is used to select instances
	 * @param nullIsReference whether null is a valid value of the key property (true) or indicates an empty selection implicitly (false)
	 * @return the {@link EntitySelection} instance
	 */
	public static EntitySelection forKey(String entityName, String propertyName, Object propertyValue, boolean nullIsReference) {
		ParameterUtil.ensureNotNull(entityName, "entityName");
		ParameterUtil.ensureNotNull(propertyName, "propertyName");
		return forKey(new EntityDescriptorModel(entityName), propertyName, propertyValue, nullIsReference);
	}

	/**
	 * Internal implementation of the forKey() methods.
	 */
	private static EntitySelection forKey(final IModel<EntityDescriptor> entityModel, EntityDescriptor entity, String propertyName, Object propertyValue, boolean nullIsReference) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		ParameterUtil.ensureNotNull(entity, "entity");
		ParameterUtil.ensureNotNull(propertyName, "propertyName");
		if (propertyValue == null && !nullIsReference) {
			return new EntitySelection(entityModel, BooleanTemplate.FALSE);
		} else {
			final EntityConditions conditions = new EntityConditions(entity);
			conditions.addFieldEquals(propertyName, propertyValue);
			return new EntitySelection(entityModel, conditions);
		}
	}
	
	/**
	 * Getter method for the entityModel.
	 * @return the entityModel
	 */
	public IModel<EntityDescriptor> getEntityModel() {
		return entityModel;
	}

	/**
	 * Getter method for the predicate.
	 * @return the predicate
	 */
	public Predicate getPredicate() {
		return predicate;
	}
	
	/**
	 * Executes a query using the entity and (optionally) the predicate from this selection
	 * to fetch a single instance.
	 * 
	 * If no such instance was found in the database, then the "optional" flag determines
	 * what happens: If this flag is set, then this method returns null. Otherwise this
	 * method throws a {@link NoSuchElementException}. 
	 * 
	 * @param optional whether the entity instance is optional
	 * @return the instance
	 */
	public IEntityInstance fetchSingleInstance(final boolean optional) {
		return fetchSingleInstanceFor(entityModel, predicate, optional);
	}

	/**
	 * Static version of fetchSingleInstance().
	 * @param entityModel the model for the entity descriptor
	 * @param predicate the filter predicate
	 * @param optional whether the entity instance is optional
	 * @return the instance
	 */
	public static IEntityInstance fetchSingleInstanceFor(IModel<EntityDescriptor> entityModel, Predicate predicate, final boolean optional) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		return fetchSingleInstanceFor(entityModel.getObject(), predicate, optional);
	}

	/**
	 * Static version of fetchSingleInstance().
	 * @param entity the entity descriptor
	 * @param predicate the filter predicate
	 * @param optional whether the entity instance is optional
	 * @return the instance
	 */
	public static IEntityInstance fetchSingleInstanceFor(EntityDescriptor entity, Predicate predicate, final boolean optional) {
		ParameterUtil.ensureNotNull(entity, "entity");
		
		// build the query
		SQLQuery query = entity.getQueryBuilder().createQuery();
		if (predicate != null) {
			query = query.where(predicate);
		}
		query = query.limit(1);
		
		// fetch the row
		IEntityInstance result = entity.getQueryBuilder().getSingle(query);
		if (result == null && !optional) {
			throw new NoSuchElementException("no instance of entity '" + entity.getName() + "' with conditions: " + predicate);
		}
		return result;
		
	}
	
	/**
	 * Executes a query using the entity and (optionally) the predicate from this selection
	 * to fetch all instances.
	 * 
	 * @return the instance
	 */
	public List<IEntityInstance> fetchAllInstances() {
		return fetchAllInstancesFor(entityModel, predicate);
	}

	/**
	 * Static version of fetchAllInstances().
	 * @param entityModel the model for the entity descriptor
	 * @param predicate the filter predicate
	 * @return the instance
	 */
	public static List<IEntityInstance> fetchAllInstancesFor(IModel<EntityDescriptor> entityModel, Predicate predicate) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		return fetchAllInstancesFor(entityModel.getObject(), predicate);
	}
	
	/**
	 * Static version of fetchAllInstances().
	 * @param entity the entity descriptor
	 * @param predicate the filter predicate
	 * @return the instance
	 */
	public static List<IEntityInstance> fetchAllInstancesFor(EntityDescriptor entity, Predicate predicate) {
		ParameterUtil.ensureNotNull(entity, "entity");
		
		// build the query
		SQLQuery query = entity.getQueryBuilder().createQuery();
		if (predicate != null) {
			query = query.where(predicate);
		}
		
		// fetch the rows
		return entity.getQueryBuilder().getAll(query);
		
	}	
	
	/**
	 * Creates a Wicket {@link IModel} that has the same effect as fetchSingleInstance(optional).
	 * @param optional whether the entity instance is optional
	 * @return the model
	 */
	public EntityInstanceModel createSingleInstanceModel(boolean optional) {
		if (optional) {
			return new EntityInstanceModel.Optional(this);
		} else {
			return new EntityInstanceModel.Required(this);
		}
	}

	/**
	 * Creates an {@link EntityInstanceDataProvider} from this selection.
	 * @return the data provider
	 */
	public EntityInstanceDataProvider createDataProvider() {
		return new EntityInstanceDataProvider(entityModel, predicate, null);
	}

}
