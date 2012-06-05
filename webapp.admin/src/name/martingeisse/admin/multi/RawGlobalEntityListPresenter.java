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
 * Raw presentation of entities.
 */
public class RawGlobalEntityListPresenter extends AbstractGlobalEntityListPresenter {

	/**
	 * Constructor.
	 */
	public RawGlobalEntityListPresenter() {
		super("default", "Default");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#createPanel(java.lang.String, name.martingeisse.admin.schema.EntityDescriptor, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	@Override
	public Panel createPanel(String id, EntityDescriptor entity, PageParameters parameters) {
		return new MultiCellPanel(id, entity, parameters);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getPageableForPanel(org.apache.wicket.markup.html.panel.Panel)
	 */
	@Override
	public IPageable getPageableForPanel(Panel panel) {
		return ((MultiCellPanel)panel).getPageable();
	}

}
