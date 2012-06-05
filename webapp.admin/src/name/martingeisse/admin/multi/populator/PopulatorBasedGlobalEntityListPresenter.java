/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi.populator;

import java.util.List;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.application.capabilities.ApplicationCapabilities;
import name.martingeisse.admin.application.capabilities.IEntityPresentationContributor;
import name.martingeisse.admin.multi.AbstractGlobalEntityListPresenter;
import name.martingeisse.admin.schema.EntityDescriptor;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * A global entity list presenter based on {@link ICellPopulator}
 * objects to do the actual presentation.
 */
public class PopulatorBasedGlobalEntityListPresenter extends AbstractGlobalEntityListPresenter implements IEntityPresentationContributor, IPlugin {

	/**
	 * the cellPopulators
	 */
	private List<IEntityCellPopulator> cellPopulators;

	/**
	 * Constructor.
	 */
	public PopulatorBasedGlobalEntityListPresenter() {
		super();
	}

	/**
	 * Constructor.
	 * @param urlId the URL id used to select this presenter
	 * @param title the user-visible title of this presenter
	 * @param cellPopulators the cell populators
	 */
	public PopulatorBasedGlobalEntityListPresenter(final String urlId, final String title, final List<IEntityCellPopulator> cellPopulators) {
		super(urlId, title);
		this.cellPopulators = cellPopulators;
	}

	/**
	 * Getter method for the cellPopulators.
	 * @return the cellPopulators
	 */
	public List<IEntityCellPopulator> getCellPopulators() {
		return cellPopulators;
	}

	/**
	 * Setter method for the cellPopulators.
	 * @param cellPopulators the cellPopulators to set
	 */
	public void setCellPopulators(final List<IEntityCellPopulator> cellPopulators) {
		this.cellPopulators = cellPopulators;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#createPanel(java.lang.String, name.martingeisse.admin.schema.EntityDescriptor, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	@Override
	public Panel createPanel(final String id, final EntityDescriptor entity, final PageParameters parameters) {
		return new PopulatorBasedEntityListPanel(id, entity, cellPopulators);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IGlobalEntityListPresenter#getPageableForPanel(org.apache.wicket.markup.html.panel.Panel)
	 */
	@Override
	public IPageable getPageableForPanel(final Panel panel) {
		return ((PopulatorBasedEntityListPanel)panel).getPageable();
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
		// TODO: define a filter
		entity.getGlobalListPresenters().add(this);
	}

}
