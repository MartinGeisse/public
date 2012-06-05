/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi;

import name.martingeisse.admin.schema.EntityDescriptor;

/**
 * Base implementation of {@link IGlobalEntityListPresenter} that
 * stores the URL ID and the title in fields.
 */
public abstract class AbstractGlobalEntityListPresenter implements IGlobalEntityListPresenter {

	/**
	 * the urlId
	 */
	private String urlId;

	/**
	 * the title
	 */
	private String title;

	/**
	 * Constructor.
	 */
	public AbstractGlobalEntityListPresenter() {
	}

	/**
	 * Constructor.
	 * @param urlId the URL id used to select this presenter
	 * @param title the user-visible title of this presenter
	 */
	public AbstractGlobalEntityListPresenter(final String urlId, final String title) {
		this.urlId = urlId;
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getUrlId()
	 */
	@Override
	public String getUrlId() {
		return urlId;
	}

	/**
	 * Setter method for the urlId.
	 * @param urlId the urlId to set
	 */
	public void setUrlId(final String urlId) {
		this.urlId = urlId;
	}

	/**
	 * Getter method for the title.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for the title.
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getTitle(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public String getTitle(EntityDescriptor entity) {
		return title;
	}

}
