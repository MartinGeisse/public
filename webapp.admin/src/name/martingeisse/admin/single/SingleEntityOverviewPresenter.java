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
 * Basic implementation of {@link ISingleEntityOverviewPresenter}.
 * This class stores the class of a panel to use for presentation.
 * The panel class is instantiated using the two-argument constructor which
 * takes the Wicket id and the model for the {@link EntityInstance}.
 * 
 * This class keeps a score that is only used when used directly as
 * an overview presentation contributor.
 */
public class SingleEntityOverviewPresenter implements ISingleEntityOverviewPresenter, IEntityPresentationContributor, IPlugin {

	/**
	 * the score
	 */
	private int score;

	/**
	 * the panelClass
	 */
	private Class<? extends Panel> panelClass;

	/**
	 * Constructor.
	 */
	public SingleEntityOverviewPresenter() {
	}

	/**
	 * Constructor.
	 * @param panelClass the panel class
	 */
	public SingleEntityOverviewPresenter(final Class<? extends Panel> panelClass) {
		this.panelClass = panelClass;
	}

	/**
	 * Constructor.
	 * @param panelClass the panel class
	 * @param score the contribution score
	 */
	public SingleEntityOverviewPresenter(final Class<? extends Panel> panelClass, final int score) {
		this.panelClass = panelClass;
		this.score = score;
	}

	/**
	 * Getter method for the score.
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Setter method for the score.
	 * @param score the score to set
	 */
	public void setScore(final int score) {
		this.score = score;
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
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute(final ApplicationCapabilities applicationCapabilities) {
		applicationCapabilities.getEntityPresentationContributors().add(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityPresentationContributor#contributeEntityPresenters(name.martingeisse.admin.schema.EntityDescriptor)
	 */
	@Override
	public void contributeEntityPresenters(final EntityDescriptor entity) {
		entity.contibuteOverviewPresenter(this, score);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.single.ISingleEntityOverviewPresenter#createPanel(java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public Panel createPanel(final String id, final IModel<EntityInstance> model) {
		try {
			final Constructor<? extends Panel> constructor = panelClass.getConstructor(String.class, IModel.class);
			return constructor.newInstance(id, model);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
