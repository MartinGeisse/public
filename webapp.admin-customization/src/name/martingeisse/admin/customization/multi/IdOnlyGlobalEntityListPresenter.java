/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.multi;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;
import name.martingeisse.admin.application.capabilities.IEntityPresentationContributor;
import name.martingeisse.admin.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.schema.EntityDescriptor;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * ID-only presentation of entities.
 */
public class IdOnlyGlobalEntityListPresenter implements IGlobalEntityListPresenter, IEntityPresentationContributor, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getUrlId()
	 */
	@Override
	public String getUrlId() {
		return "ids";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getTitle(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public String getTitle(EntityDescriptor entity) {
		return "ID List";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#createPanel(java.lang.String, name.martingeisse.admin.schema.EntityDescriptor, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	@Override
	public Panel createPanel(String id, EntityDescriptor entity, PageParameters parameters) {
		return new IdOnlyGlobalEntityListPanel(id, entity, parameters);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getPageableForPanel(org.apache.wicket.markup.html.panel.Panel)
	 */
	@Override
	public IPageable getPageableForPanel(Panel panel) {
		return (IdOnlyGlobalEntityListPanel)panel;
	}

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
		entity.getGlobalListPresenters().add(this);
	}

}
