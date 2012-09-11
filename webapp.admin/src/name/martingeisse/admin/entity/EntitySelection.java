/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import name.martingeisse.admin.entity.component.list.EntityInstanceDataProvider;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.EntityConditions;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceEndpoint;
import name.martingeisse.common.util.ObjectStateUtil;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.model.IModel;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.Wildcard;
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
	 * Constructor that creates an instance from a referrer entity instance and an entity
	 * reference. The resulting entity selection looks up the referred entity instances.
	 * 
	 * NOTE: This method currently unpacks the referrer data on construction. The consequence
	 * of this is that if the reference key in the referrer is changed later, then this selection
	 * will still use the old reference key.
	 * 
	 * @param referrerInstanceModel the referrer model
	 * @param nearReferenceEndpoint the near endpoint of the reference
	 */
	public EntitySelection(final IModel<EntityInstance> referrerInstanceModel, final EntityReferenceEndpoint nearReferenceEndpoint) {
		ParameterUtil.ensureNotNull(referrerInstanceModel, "referrerInstanceModel");
		ParameterUtil.ensureNotNull(nearReferenceEndpoint, "nearReferenceEndpoint");
		final EntityReferenceEndpoint farReferenceEndpoint = nearReferenceEndpoint.getOther();
		
		// obtain the value of the key property in the referrer
		final EntityInstance referrer = ObjectStateUtil.nullMeansMissing(referrerInstanceModel.getObject(), "referrer (entity instance)") ;
		if (referrer.getEntity() != nearReferenceEndpoint.getEntity()) {
			throw new IllegalArgumentException("EntitySelection (from reference): referrer instance is an instance of entity " + referrer.getEntityName() + ", but reference source is " + nearReferenceEndpoint.getEntity().getName());
		}
		final Object referrerKey = referrer.getFieldValue(nearReferenceEndpoint.getPropertyName());
		// TODO: what if referrerKey is null? check if null means "no reference" or "reference by null"? or is that clear in this context?

		// build a condition object for the query
		final EntityConditions conditions = new EntityConditions();
		conditions.addFieldEquals(farReferenceEndpoint.getPropertyName(), referrerKey);

		// initialize this entity selection
		this.entityModel = new EntityDescriptorModel(farReferenceEndpoint.getEntity());
		this.predicate = conditions;

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
	 * Internal implementation of the forUniqueKey() methods.
	 */
	private static EntitySelection forKey(final IModel<EntityDescriptor> entityModel, EntityDescriptor entity, String propertyName, Object propertyValue, boolean nullIsReference) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		ParameterUtil.ensureNotNull(entity, "entity");
		ParameterUtil.ensureNotNull(propertyName, "propertyName");
		if (propertyValue == null && !nullIsReference) {
			return new EntitySelection(entityModel, BooleanTemplate.FALSE);
		} else {
			final EntityConditions conditions = new EntityConditions();
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
	 * and returns a {@link ResultSet} for it.
	 * @return the result set
	 */
	public ResultSet executeQuery() {
		return executeQueryFor(entityModel, predicate);
	}

	/**
	 * Static version of executeQuery().
	 * @param entityModel the model for the entity descriptor
	 * @param predicate the filter predicate
	 * @return the result set
	 */
	public static ResultSet executeQueryFor(IModel<EntityDescriptor> entityModel, Predicate predicate) {
		ParameterUtil.ensureNotNull(entityModel, "entityModel");
		return executeQueryFor(entityModel.getObject(), predicate);
	}

	/**
	 * Static version of executeQuery().
	 * @param entity the entity descriptor
	 * @param predicate the filter predicate
	 * @return the result set
	 */
	public static ResultSet executeQueryFor(EntityDescriptor entity, Predicate predicate) {
		ParameterUtil.ensureNotNull(entity, "entity");
		SQLQuery query = entity.createQuery(EntityDescriptor.ALIAS);
		if (predicate != null) {
			query = query.where(predicate);
		}
		return query.getResults(Wildcard.all);
	}
	
	/**
	 * Executes a query using the entity and (optionally) the predicate from this selection.
	 * 
	 * If no such instance was found in the database, then the "optional" flag determines
	 * what happens: If this flag is set, then this method returns null. Otherwise this
	 * method throws a {@link NoSuchElementException}. 
	 * 
	 * @param optional whether the entity instance is optional
	 * @return the instance
	 */
	public EntityInstance fetchSingleInstance(final boolean optional) {
		return fetchSingleInstanceFor(entityModel, predicate, optional);
	}

	/**
	 * Static version of fetchSingleInstance().
	 * @param entityModel the model for the entity descriptor
	 * @param predicate the filter predicate
	 * @param optional whether the entity instance is optional
	 * @return the instance
	 */
	public static EntityInstance fetchSingleInstanceFor(IModel<EntityDescriptor> entityModel, Predicate predicate, final boolean optional) {
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
	public static EntityInstance fetchSingleInstanceFor(EntityDescriptor entity, Predicate predicate, final boolean optional) {
		ParameterUtil.ensureNotNull(entity, "entity");
		try {
			final ResultSet resultSet = executeQueryFor(entity, predicate);
			entity.checkDataRowMeta(resultSet);
			if (resultSet.next()) {
				return new EntityInstance(entity, resultSet);
			} else if (optional) {
				return null;
			} else {
				throw new NoSuchElementException("no instance of entity '" + entity.getName() + "' with conditions: " + predicate);
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
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
