/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend;

import name.martingeisse.wicket.util.ISimpleCallbackListener;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;

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
		tag.put("src", urlFor(ISimpleCallbackListener.INTERFACE, null));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.util.ISimpleCallbackListener#onSimpleCallback()
	 */
	@Override
	public void onSimpleCallback() {
		RequestCycle.get().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {
			
			@Override
			public void respond(IRequestCycle requestCycle) {
				((WebResponse)requestCycle.getResponse()).setContentType("text/html; charset=utf-8");
				requestCycle.getResponse().write("Hello World!");
			}
			
			@Override
			public void detach(IRequestCycle requestCycle) {
			}
			
		});
	}

}
