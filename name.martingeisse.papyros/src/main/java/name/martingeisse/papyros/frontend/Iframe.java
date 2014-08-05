/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;

/**
 * This component should be used with an iframe HTML element.
 * It specifies the iframe's content directly via its model.
 */
public class Iframe extends WebComponent implements ISimpleCallbackListener {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public Iframe(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public Iframe(String id, IModel<?> model) {
		super(id, model);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		if (tag.isOpenClose()) {
			tag.setType(TagType.OPEN);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript(""));
	}
	
}
