/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi;

import name.martingeisse.admin.schema.EntityDescriptor;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Raw presentation of entities. TODO: This presenter will supersede {@link RawGlobalEntityListPanel}.
 */
public class RawGlobalEntityListPresenter implements IGlobalEntityListPresenter {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getUrlId()
	 */
	@Override
	public String getUrlId() {
		return "default";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getTitle(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public String getTitle(EntityDescriptor entity) {
		return "Default";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#createPanel(java.lang.String, name.martingeisse.admin.schema.EntityDescriptor, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	@Override
	public Panel createPanel(String id, EntityDescriptor entity, PageParameters parameters) {
		return new RawGlobalEntityListPanel(id, entity, parameters);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getPageableForPanel(org.apache.wicket.markup.html.panel.Panel)
	 */
	@Override
	public IPageable getPageableForPanel(Panel panel) {
		return ((RawGlobalEntityListPanel)panel).getPageable();
	}

}
