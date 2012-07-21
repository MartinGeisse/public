/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.instance;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceInfo;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This presentation panel can be used with arbitrary entity instances
 * and just lists the fields in a read-only way.
 */
public class RawEntityPresentationPanel extends Panel {

	/**
	 * the entityInstance
	 */
	private transient EntityInstance entityInstance;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the entity instance model
	 */
	public RawEntityPresentationPanel(final String id, final IModel<EntityInstance> model) {
		super(id, model);
		add(new Loop("fields", new PropertyModel<Integer>(this, "width")) {
			@Override
			protected void populateItem(LoopItem item) {
				
				// create the name label
				String fieldName = entityInstance.getFieldNames()[item.getIndex()];
				item.add(new Label("name", fieldName));
				
				// create the value label / link
				Object fieldValue = entityInstance.getFieldValues()[item.getIndex()];
				EntityReferenceInfo reference = model.getObject().getEntity().findOutgoingReference(fieldName);
				WebMarkupContainer link;
				if (reference == null || fieldValue == null) {
					link = new WebMarkupContainer("link");
				} else {
					PageParameters parameters = new PageParameters();
					parameters.add("entity", reference.getDestination().getTableName());
					parameters.add("id", fieldValue);
					link = null; // TODO new BookmarkablePageLink<Void>("link", EntityPresentationPage.class, parameters);
					throw new RuntimeException("NOT YET IMPLEMENTED");
				}
				link.add(new Label("value", "" + fieldValue));
				item.add(link);
				
			}
		});
	}

	/**
	 * @return the row width, i.e. the number of fields
	 */
	public int getWidth() {
		return entityInstance.getFieldNames().length;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		entityInstance = (EntityInstance)getDefaultModelObject();
		super.onBeforeRender();
	}

}
