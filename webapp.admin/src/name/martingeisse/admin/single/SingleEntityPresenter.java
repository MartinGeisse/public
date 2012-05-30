/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.single;

import java.lang.reflect.Constructor;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;
import name.martingeisse.admin.application.capabilities.IEntityPresentationContributor;
import name.martingeisse.admin.schema.EntityDescriptor;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Basic implementation of {@link ISingleEntityPresenter}. This class stores the URL ID
 * and title in its fields, as well as the class of a panel to use for presentation.
 * The panel class is instantiated using the two-argument constructor which
 * takes the Wicket id and the model for the {@link EntityInstance}.
 */
public class SingleEntityPresenter implements ISingleEntityPresenter, IEntityPresentationContributor, IPlugin {

	/**
	 * the urlId
	 */
	private String urlId;
	
	/**
	 * the title
	 */
	private String title;
	
	/**
	 * the panelClass
	 */
	private Class<? extends Panel> panelClass;
	
	/**
	 * Constructor.
	 */
	public SingleEntityPresenter() {
	}

	/**
	 * Constructor.
	 * @param urlId the URL ID
	 * @param title the title
	 * @param panelClass the panel class
	 */
	public SingleEntityPresenter(String urlId, String title, Class<? extends Panel> panelClass) {
		this.urlId = urlId;
		this.title = title;
		this.panelClass = panelClass;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.presenter.IEntityPresenter#getUrlId()
	 */
	@Override
	public String getUrlId() {
		return urlId;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.presenter.IEntityPresenter#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
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
		entity.getSinglePresenters().add(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.presenter.IEntityPresenter#createPanel(java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public Panel createPanel(String id, IModel<EntityInstance> model) {
		try {
			Constructor<? extends Panel> constructor = panelClass.getConstructor(String.class, IModel.class);
			return constructor.newInstance(id, model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
