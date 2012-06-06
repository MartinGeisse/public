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
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Test presentation for {@link DataView} implementation.
 * 
 * TODO: Use this class as a tempalte for a generic presenter. Using an interface
 * that must be implemented by the panel class to obtain the {@link IPageable}.
 */
public class RoleOrderListPresenter implements IGlobalEntityListPresenter, IEntityPresentationContributor, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getUrlId()
	 */
	@Override
	public String getUrlId() {
		return "roleOrder";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getTitle(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public String getTitle(EntityDescriptor entity) {
		return "Role Order List";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#createPanel(java.lang.String, name.martingeisse.admin.schema.EntityDescriptor, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	@Override
	public Panel createPanel(String id, EntityDescriptor entity, PageParameters parameters) {
		return new RoleOrderListPanel(id, entity, parameters);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getPageableForPanel(org.apache.wicket.markup.html.panel.Panel)
	 */
	@Override
	public IPageable getPageableForPanel(Panel panel) {
		return ((RoleOrderListPanel)panel).getPageable();
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
