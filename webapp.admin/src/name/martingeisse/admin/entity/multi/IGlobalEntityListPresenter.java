/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.multi;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class acts as a factory for entity list presentation. More precisely,
 * it is used for *global* entity lists, i.e. lists that are directly
 * invoked by an URL and whose behavior is determined only by that URL.
 * 
 * A user invokes a presenter by passing an URL that selects an entity
 * class and presenter name. This invokes a presenter and uses it to
 * display the entity list. Most presenters will delegate to the
 * general concepts of an entity filter (or set of filters) and
 * an entity list view factory, as well as the entity descriptor itself
 * to actually fetch data.
 * 
 * Implementations are by default not restricted to specific entity
 * classes, table names, or similar. That is, it is entirely possible
 * to create a presenter that can present lists of any entity.
 * 
 * The framework will automatically provide paging support if the
 * implementation is able for return an {@link IPageable} from
 * the panel it created. Returning null disables framework paging
 * support.
 */
public interface IGlobalEntityListPresenter {

	/**
	 * Returns the presenter's URL ID. This ID is used to mount a page for
	 * the presenter.
	 * @return the URL ID
	 */
	public String getUrlId();
	
	/**
	 * Returns the presenter's title. This is used for displaying a link
	 * to the presenter's page as well as the page title.
	 * @param entity the entity
	 * @return the title
	 */
	public String getTitle(EntityDescriptor entity);

	/**
	 * Creates a Wicket panel for presentation of the entity list.
	 * @param id the wicket id
	 * @param entity the entity
	 * @param parameters the parameters used to modify the list presentation
	 * @return the panel
	 */
	public Panel createPanel(String id, EntityDescriptor entity, PageParameters parameters);

	/**
	 * Returns the {@link IPageable} for a panel created by this presenter to provide framework
	 * paging support.
	 * @param panel the panel. Must be a panel returned by the createPanel() method of this
	 * interface.
	 * @return the pageable, or null to deactivate framework paging support
	 */
	public IPageable getPageableForPanel(Panel panel);
	
}
