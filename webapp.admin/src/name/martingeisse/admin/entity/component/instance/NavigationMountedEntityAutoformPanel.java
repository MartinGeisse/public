/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.instance;

import name.martingeisse.admin.entity.EntityConfiguration;
import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.schema.autoform.EntityAutoformDescriber;
import name.martingeisse.admin.navigation.NavigationUtil;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.model.IModel;

/**
 * Navigation-mounted autoform panel for entity instances. Mount this panel within
 * an entity instance navigation tree using {@link EntityInstancePanelHandler}.
 *
 */
public class NavigationMountedEntityAutoformPanel extends EditEntityAutoformPanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the entity instance model
	 */
	public NavigationMountedEntityAutoformPanel(final String id, final IModel<IEntityInstance> model) {
		super(id, ParameterUtil.ensureNotNull(model.getObject(), "model"), EntityAutoformDescriber.instance, EntityConfiguration.parameterKey.get().getAutoformPropertyComponentFactory());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.instance.EditEntityAutoformPanel#onSuccessfulSubmit()
	 */
	@Override
	protected void onSuccessfulSubmit() {
		super.onSuccessfulSubmit();
		throw NavigationUtil.getNavigationNodeForComponent(this).getParent().createReplaceHandlerException(this);
	}
	
}
