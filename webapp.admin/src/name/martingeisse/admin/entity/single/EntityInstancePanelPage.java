/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.single;

import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class allows to display an entity instance using an existing panel
 * class that takes a model of type {@link EntityInstance}. This class
 * is primarily used in combination with {@link EntityInstancePanelHandler}
 * to mount entity instance panels in the navigation.
 */
public class EntityInstancePanelPage extends AbstractAdminPage {

	/**
	 * the instance
	 */
	private transient EntityInstance instance;
	
	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public EntityInstancePanelPage(final PageParameters parameters) {
		super(parameters);
	}
	
	/**
	 * @return the entity id
	 */
	private int determineId() {
		return getRequiredStringParameter(getPageParameters(), "id", true).toInt(); // TODO error handling, take ID type from entity descriptor
	}
	
	/**
	 * @return the panel class
	 */
	private Class<? extends Panel> determinePanelClass() {
		String className = getRequiredStringParameter(getPageParameters(), "panelClass", true).toString();
		try {
			return Class.forName(className).asSubclass(Panel.class);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("no such panel class: " + className);
		}
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
	
	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public EntityInstance getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Page#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		
		// actually fetch the entity instance to show
		EntityDescriptor entity = determineEntity(getPageParameters());
		instance = entity.fetchSingleInstance(determineId());
		
		// base class behavior
		super.onBeforeRender();
		
	}

}
