/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.upload;

import name.martingeisse.wicket.util.ISimpleCallbackListener;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Base class for AJAX file upload files based on jQuery File Upload.
 */
public abstract class AbstractAjaxFileUploadField extends WebComponent implements ISimpleCallbackListener {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public AbstractAjaxFileUploadField(final String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public AbstractAjaxFileUploadField(final String id, final IModel<?> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AbstractAjaxFileUploadField.class, "jquery.iframe-transport.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AbstractAjaxFileUploadField.class, "jquery.fileupload.js")));
		WicketHeadUtil.includeClassJavascript(response, AbstractAjaxFileUploadField.class);
		String url = urlFor(ISimpleCallbackListener.INTERFACE, null).toString();
		response.render(OnDomReadyHeaderItem.forScript("$('#" + getMarkupId() + "').createAjaxFileUploadField('" + url + "');"));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		checkComponentTag(tag, "input");
		checkComponentTagAttribute(tag, "type", "file");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.util.ISimpleCallbackListener#onSimpleCallback()
	 */
	@Override
	public void onSimpleCallback() {
		System.out.println(RequestCycle.get().getRequest().getClass());
	}

}
