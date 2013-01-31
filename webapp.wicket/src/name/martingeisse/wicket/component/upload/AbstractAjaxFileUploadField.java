/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.upload;

import java.util.List;

import name.martingeisse.wicket.util.ISimpleCallbackListener;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequestImpl;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileItem;

/**
 * Base class for AJAX file upload files based on jQuery File Upload.
 */
public abstract class AbstractAjaxFileUploadField extends WebComponent implements ISimpleCallbackListener {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AbstractAjaxFileUploadField.class);
	
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
		String options = renderOptions();
		String script = ("$('#" + getMarkupId() + "').createAjaxFileUploadField('" + url + '\'' + (options == null ? "" : (", " + options)) + ");");
		response.render(OnDomReadyHeaderItem.forScript(script));
	}
	
	/**
	 * Renders a Javascript expression for the options for createAjaxFileUploadField(),
	 * which are ultimately passed to $().fileupload()
	 * @return the options, or null to omit options (the default)
	 */
	protected String renderOptions() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		checkComponentTag(tag, "input");
		checkComponentTagAttribute(tag, "type", "file");
		tag.put("name", getInputName());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.util.ISimpleCallbackListener#onSimpleCallback()
	 */
	@Override
	public void onSimpleCallback() {
		Request originalRequest = RequestCycle.get().getRequest();
		if (originalRequest instanceof ServletWebRequest) {
			try {
				String uploadId = RandomStringUtils.randomAscii(32);
				ServletWebRequest servletRequest = (ServletWebRequest)originalRequest;
				MultipartServletWebRequestImpl multipartRequest = (MultipartServletWebRequestImpl)servletRequest.newMultipartWebRequest(Bytes.megabytes(100), uploadId);
				List<FileItem> fileItems = multipartRequest.getFile(getInputName());
				for (FileItem fileItem : fileItems) {
					onFileUploaded(multipartRequest, fileItem);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
	}
	
	/**
	 * This method is invoked whenever a file is uploaded.
	 * @param fileItem the uploaded file
	 */
	protected abstract void onFileUploaded(MultipartServletWebRequestImpl multipartRequest, FileItem fileItem);
	
	/**
	 * Returns the "name" attribute of the input element.
	 * @return the name attribute
	 */
	public String getInputName() {
		return Form.getRootFormRelativeId(this);
	}
	
}
