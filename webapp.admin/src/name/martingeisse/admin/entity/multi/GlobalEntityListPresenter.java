/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.multi;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.util.IGetPageable;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Default implementation of {@link IGlobalEntityListPresenter}. This implementation
 * stores the URL ID, title, and a class object for the panel to use.
 * 
 * The panel may implement {@link IPageable} or {@link IGetPageable} to obtain the
 * pageable. If the panel does not implement either interface or returns null in
 * getPageable() then framework paging support is disabled.
 * 
 * REFACTOR: no significant code here
 */
public class GlobalEntityListPresenter extends AbstractGlobalEntityListPresenter {

	/**
	 * the panelClass
	 */
	private Class<? extends Panel> panelClass;

	/**
	 * Constructor.
	 */
	public GlobalEntityListPresenter() {
		super();
	}

	/**
	 * Constructor.
	 * @param urlId the URL id used to select this presenter
	 * @param title the user-visible title of this presenter
	 * @param panelClass the panel class
	 */
	public GlobalEntityListPresenter(final String urlId, final String title, final Class<? extends Panel> panelClass) {
		super(urlId, title);
		this.panelClass = panelClass;
	}

	/**
	 * Getter method for the panelClass.
	 * @return the panelClass
	 */
	public Class<? extends Panel> getPanelClass() {
		return panelClass;
	}

	/**
	 * Setter method for the panelClass.
	 * @param panelClass the panelClass to set
	 */
	public void setPanelClass(final Class<? extends Panel> panelClass) {
		this.panelClass = panelClass;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#createPanel(java.lang.String, name.martingeisse.admin.schema.EntityDescriptor, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	@Override
	public Panel createPanel(String id, EntityDescriptor entity, PageParameters parameters) {
		try {
			return panelClass.getConstructor(String.class, EntityDescriptor.class, PageParameters.class).newInstance(id, entity, parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
