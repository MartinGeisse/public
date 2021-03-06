/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.common.frontend.components;

import name.martingeisse.wicket.util.AjaxRequestUtil;
import name.martingeisse.wicket.util.ISimpleCallbackListener;

import org.apache.wicket.ajax.AjaxRequestTarget;
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
		setOutputMarkupId(true);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public Iframe(String id, IModel<?> model) {
		super(id, model);
		setOutputMarkupId(true);
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
				requestCycle.getResponse().write(Iframe.this.getDefaultModelObjectAsString());
			}
			
			@Override
			public void detach(IRequestCycle requestCycle) {
			}
			
		});
	}

	/**
	 * Renders a javascript snipped to the current {@link AjaxRequestTarget} that
	 * reloads the iframe.
	 */
	public final void renderReloadScript() {
		renderReloadScript(AjaxRequestUtil.getAjaxRequestTarget());
	}
	
	/**
	 * Renders a javascript snipped to the specified {@link AjaxRequestTarget} that
	 * reloads the iframe.
	 * @param target the target to render to
	 */
	public void renderReloadScript(AjaxRequestTarget target) {
		target.appendJavaScript("document.getElementById('" + getMarkupId() + "').contentWindow.location.reload(true);");
	}
	
}
