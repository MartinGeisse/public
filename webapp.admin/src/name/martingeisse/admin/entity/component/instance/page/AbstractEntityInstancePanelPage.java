/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.instance.page;

import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.property.type.IEntityIdType;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class allows to display an entity instance using an existing panel
 * class that takes a model of type {@link EntityDescriptor}. The
 * concrete subclass must determine the panel class to use, a model for
 * the entity instances, and strategies for the panel.
 */
public abstract class AbstractEntityInstancePanelPage extends AbstractAdminPage {

	/**
	 * the instance
	 */
	private transient EntityInstance instance;
	
	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractEntityInstancePanelPage(final PageParameters parameters) {
		super(parameters);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		Class<? extends Panel> panelClass = determinePanelClass();
		IModel<EntityInstance> instanceModel = new PropertyModel<EntityInstance>(this, "instance");
		try {
			getMainContainer().add(panelClass.getConstructor(String.class, IModel.class).newInstance("panel", instanceModel));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("entity instance panel " + panelClass.getCanonicalName() + " has no constructor(String, IModel).");
		} catch (Exception e) {
			throw new RuntimeException("exception while invoking entity instance panel constructor of panel class " + panelClass.getCanonicalName(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Page#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		
		// actually fetch the entity instance to show
		EntityDescriptor entity = determineEntityType();
		instance = entity.fetchSingleInstance(determineId(), false);
		
		// base class behavior
		super.onBeforeRender();
		
	}
	
	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public final EntityInstance getInstance() {
		return instance;
	}
	
	/**
	 * Determines the entity ID. The default implementation uses the "id" page parameter.
	 * @return the entity id
	 */
	private Object determineId() {
		EntityDescriptor entity = determineEntityType();
		IEntityIdType idType = entity.getIdColumnType();
		if (idType == null) {
			throw new IllegalStateException("table " + entity.getTableName() + " has no primary key and thus cannot be viewed");
		}
		return idType.convertFromPageParameter(getPageParameters().get("id"));
	}

	/**
	 * Determines the entity type to use.
	 * @return the entity type.
	 */
	protected abstract EntityDescriptor determineEntityType();
	
	/**
	 * Determines the class of the panel to use
	 * @return the panel class
	 */
	protected abstract Class<? extends Panel> determinePanelClass();

}
