/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;
import name.martingeisse.admin.application.capabilities.IEntityPresentationContributor;
import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.single.EntityInstance;
import name.martingeisse.admin.single.ISingleEntityOverviewPresenter;

/**
 * TODO: document me
 *
 */
public class OverviewPresenter implements ISingleEntityOverviewPresenter, IEntityPresentationContributor, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute(ApplicationCapabilities applicationCapabilities) {
		applicationCapabilities.getEntityPresentationContributors().add(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityPresentationContributor#contributeEntityPresenters(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public void contributeEntityPresenters(EntityDescriptor entity) {
		entity.contibuteOverviewPresenter(this, 1);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.single.ISingleEntityOverviewPresenter#createPanel(java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public Panel createPanel(String id, IModel<EntityInstance> model) {
		return new OverviewPanel(id, model);
	}

}
