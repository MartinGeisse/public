/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.multi.populator;

import java.util.List;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.entity.IEntityPresentationContributor;
import name.martingeisse.admin.entity.multi.AbstractGlobalEntityListPresenter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
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

}
