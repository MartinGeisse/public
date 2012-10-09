/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.instance;

import java.util.List;

import name.martingeisse.admin.entity.EntityConfiguration;
import name.martingeisse.admin.entity.instance.RawEntityInstance;
import name.martingeisse.admin.entity.list.EntityExpressionUtil;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.autoform.EntityAutoformDescriber;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;
import name.martingeisse.wicket.autoform.AutoformPanel;
import name.martingeisse.wicket.autoform.AutoformUtil;
import name.martingeisse.wicket.autoform.componentfactory.IAutoformPropertyComponentFactory;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriber;
import name.martingeisse.wicket.autoform.describe.IAutoformBeanDescriptor;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor;

import org.apache.wicket.model.IModel;

import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Path;

/**
 * This autoform allows editing an entity instance.
 * 
 * You can, for example, mount this panel within an entity instance navigation
 * tree using {@link EntityInstancePanelHandler}.
 * 
 * TODO: Entity-instance-mounted panels inherit the capability to fetch the
 * entity instance on-demand. It could be saved in a similar way: Load on-demand
 * on each request, apply changes, save. Currently, however, the autoform logic
 * wants it different: It fetches the entity once, then serializes it as part
 * of the autoform state. That means that the autoform always works on a copy
 * that was made when the autoform page was created. This copy is obtained
 * by calling {{@link #getBeanDescriptor()}} and then
 * {@link IAutoformBeanDescriptor#getBean()} and is used by the saveEntity()
 * method.
 */
public class EditEntityAutoformPanel extends AutoformPanel {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the entity instance model
	 */
	public EditEntityAutoformPanel(final String id, final IModel<RawEntityInstance> model) {
		this(id, ParameterUtil.ensureNotNull(model, "model").getObject(), EntityAutoformDescriber.instance, EntityConfiguration.parameterKey.get().getAutoformPropertyComponentFactory());
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param bean the bean to show in the autoform
	 * @param beanDescriber the bean describer that extracts meta-data from the bean
	 * @param propertyComponentFactory the factory used to create components for the bean properties
	 */
	public EditEntityAutoformPanel(String id, RawEntityInstance bean, IAutoformBeanDescriber beanDescriber, IAutoformPropertyComponentFactory propertyComponentFactory) {
		super(id, ParameterUtil.ensureNotNull(bean, "bean"), ParameterUtil.ensureNotNull(beanDescriber, "beanDescriber"), ParameterUtil.ensureNotNull(propertyComponentFactory, "propertyComponentFactory"));
	}
	
	/**
	 * Getter method for the model.
	 * @return the model
	 */
	public final IModel<RawEntityInstance> getModel() {
		return GenericTypeUtil.unsafeCast(ReturnValueUtil.nullMeansMissing(getDefaultModel(), "model"));
	}

	/**
	 * Setter method for the model.
	 * 
	 * TODO: See class comment. Setting the default model won't currently
	 * work as expected.
	 *  
	 * @param model the model to set
	 */
	public final void setModel(final IModel<RawEntityInstance> model) {
		// setDefaultModel(model);
		throw new UnsupportedOperationException();
	}

	/**
	 * Getter method for the entityInstance.
	 * @return the entityInstance
	 */
	public final RawEntityInstance getEntityInstance() {
		return ReturnValueUtil.nullMeansMissing(getModel().getObject(), "entity instance");
	}
	
	/**
	 * Returns the entity descriptor of the entity being displayed by this panel.
	 * @return the entity descriptor
	 */
	public final EntityDescriptor getEntityDescriptor() {
		return getEntityInstance().getEntity();
	}
	

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.AutoformPanel#onSuccessfulSubmit()
	 */
	@Override
	protected void onSuccessfulSubmit() {
		super.onSuccessfulSubmit();
		saveEntity();
	}

	/**
	 * 
	 */
	protected void saveEntity() {

		// collect some objects we will need
		EntityDescriptor entity = getEntityDescriptor();
		IAutoformBeanDescriptor beanDescriptor = getBeanDescriptor();
		RawEntityInstance bean = (RawEntityInstance)beanDescriptor.getBean();
		
		// prepare an "empty" update clause for the entity instance being edited
		SQLUpdateClause update = entity.createUpdate(entity.getTableName());
		update.where(EntityExpressionUtil.fieldEquals(entity.getTableName(), entity.getIdColumnName(), bean.getEntityId()));

		// add "set" clauses for each property that is writable in this autoform
		List<IAutoformPropertyDescriptor> writableProperties = AutoformUtil.getPropertiesByReadOnlyFlag(beanDescriptor, false);
		Path<Object> entityPath = EntityExpressionUtil.entityPath();
		for (IAutoformPropertyDescriptor property : writableProperties) {
			Object workObject = property.getModel().getObject();
			Object databaseObject = entity.getProperties().get(property.getName()).getType().convertForSave(workObject, entity.getDatabase());
			update.set(Expressions.path(Object.class, entityPath, property.getName()), databaseObject);
		}
		
		// execute the update
		update.execute();
		
	}

}
