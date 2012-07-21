/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.action.IEntityInstanceAction;
import name.martingeisse.admin.entity.action.IEntityInstanceActionContributor;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.single.EntityInstance;

/**
 * Test for entity instance actions.
 */
public class PrintNameAction implements IEntityInstanceAction, IEntityInstanceActionContributor, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute() {
		EntityConfigurationUtil.addEntityInstanceActionContributor(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityInstanceActionContributor#contributeGlobalActions(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public IEntityInstanceAction[] contributeGlobalActions(EntityDescriptor entityDescriptor) {
		if (entityDescriptor.getProperties().get("name") != null) {
			return new IEntityInstanceAction[] {this};
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityInstanceActionContributor#contributeInstanceActions(java.lang.Object, name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public IEntityInstanceAction[] contributeInstanceActions(Object entityInstance, EntityDescriptor entityDescriptor) {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityInstanceAction#getName()
	 */
	@Override
	public String getName() {
		return "print name";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityInstanceAction#isActiveFor(java.lang.Object, name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public boolean isActiveFor(Object entityInstance, EntityDescriptor entityDescriptor) {
		return true;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityInstanceAction#execute(java.lang.Object, name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public void execute(Object entityInstance, EntityDescriptor entityDescriptor) {
		EntityInstance instance = (EntityInstance)entityInstance;
		System.out.println("* " + instance.getFieldValue("name"));
	}

}
