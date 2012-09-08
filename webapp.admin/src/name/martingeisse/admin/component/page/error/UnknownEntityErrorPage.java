/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page.error;

import name.martingeisse.admin.entity.AllEntityDescriptorsModel;
import name.martingeisse.admin.entity.UnknownEntityException;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

/**
 * Specialized error page for {@link UnknownEntityException}.
 */
public class UnknownEntityErrorPage extends AbstractSpecializedExceptionPage {

	/**
	 * Constructor.
	 * @param caughtException the exception that was actually caught by the request cycle
	 * @param originalCause the original cause -- an unknown entity
	 * @param page the page where the exception occurred
	 */
	public UnknownEntityErrorPage(Throwable caughtException, UnknownEntityException originalCause, Page page) {
		super(caughtException, page);
		add(new Label("entityName", originalCause.getEntityName()));
		add(new ListView<EntityDescriptor>("knownEntityList", new AllEntityDescriptorsModel()) {
			@Override
			protected void populateItem(ListItem<EntityDescriptor> item) {
				item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
			}
		});
	}
	
}
