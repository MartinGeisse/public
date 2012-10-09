/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.instance;

import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceEndpoint;
import name.martingeisse.admin.util.LinkUtil;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;
import name.martingeisse.wicket.util.WicketConverterUtil;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This presentation panel can be used with arbitrary entity instances
 * and just lists the fields in a read-only way.
 */
public class RawEntityPresentationPanel extends Panel {

	/**
	 * the entityInstance
	 */
	private transient IEntityInstance entityInstance;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the entity instance model
	 */
	public RawEntityPresentationPanel(final String id, final IModel<IEntityInstance> model) {
		super(id, ParameterUtil.ensureNotNull(model, "model"));
				
		add(new Loop("fields", new PropertyModel<Integer>(this, "width")) {
			@Override
			protected void populateItem(final LoopItem item) {

				// create the name label
				final String fieldName = entityInstance.getDataRowMeta().getNames()[item.getIndex()];
				item.add(new Label("name", fieldName));

				// create the value label / link
				final Object fieldValue = entityInstance.getDataRowFields()[item.getIndex()];
				final EntityReferenceEndpoint referenceEndpoint = model.getObject().getEntity().findReference(fieldName);
				WebMarkupContainer link;
				if (referenceEndpoint == null || fieldValue == null) {
					link = new WebMarkupContainer("link");
				} else {
					// TODO doesn't work if the far property is not the ID. Should also check the multiplicity
					link = LinkUtil.createSingleEntityLink("link", referenceEndpoint.getOther().getEntity(), fieldValue);
				}
				link.add(new Label("value", WicketConverterUtil.createLabelModel(fieldValue, this)));
				item.add(link);

			}
		});
		
		EntityDescriptor entity = model.getObject().getEntity();
		add(LinkUtil.createSingleEntityLink("editLink", entity, null, "edit"));
		
	}

	/**
	 * @return the row width, i.e. the number of fields
	 */
	public int getWidth() {
		return entityInstance.getDataRowMeta().getNames().length;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		entityInstance = ReturnValueUtil.nullMeansMissing((IEntityInstance)getDefaultModelObject(), "entity instance");
		super.onBeforeRender();
	}

}
