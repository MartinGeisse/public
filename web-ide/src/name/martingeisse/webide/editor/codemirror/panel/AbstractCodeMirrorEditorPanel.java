/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror.panel;

import name.martingeisse.webide.application.Configuration;
import name.martingeisse.webide.editor.codemirror.ot.Dummy;
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.util.NoTrimTextArea;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Base class for CodeMirror based editor panels. Subclasses must
 * provide Javascript code to actually create the CodeMirror instance
 * for the text area.
 */
public class AbstractCodeMirrorEditorPanel extends Panel {

	/**
	 * the resourceHandle
	 */
	private final ResourceHandle resourceHandle;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param contentsModel the model
	 * @param resourceHandle the handle of the workspace resource being edited
	 */
	public AbstractCodeMirrorEditorPanel(String id, IModel<String> contentsModel, ResourceHandle resourceHandle) {
		super(id, contentsModel);
		this.resourceHandle = resourceHandle;
		
		final Form<Void> editorForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				
				// save the resource
				final ResourceHandle resourceHandle = AbstractCodeMirrorEditorPanel.this.resourceHandle;
				final String newContents = (String)AbstractCodeMirrorEditorPanel.this.getDefaultModelObject();
				resourceHandle.writeFile(newContents, true, true);

				// TODO store the "workspace building" indicator globally, update by the
				// builder thread, then push via Atmosphere. Same for resource markers.
				
			}
		};
		
		// TODO: don't load the contents for CodeMirror, we don't need them here (alt: pass directly
		// to the OT server)
		editorForm.add(new NoTrimTextArea<String>("textarea").setOutputMarkupId(true));
		editorForm.add(new AjaxButton("submit", editorForm) {});
		add(editorForm);
		
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(AbstractCodeMirrorEditorPanel.class, "codemirror.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AbstractCodeMirrorEditorPanel.class, "codemirror.js")));
		WicketHeadUtil.includeClassJavascript(response, AbstractCodeMirrorEditorPanel.class);
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dummy.class, "ot.js")));
		response.render(JavaScriptHeaderItem.forUrl("http://" + Configuration.getSelfDomain() + ":8081/socket.io/socket.io.js"));
		response.render(JavaScriptHeaderItem.forScript("window.selfDomain = '" + Configuration.getSelfDomain() + "';", null));
	}
	
	/**
	 * Returns the text area for which subclasses must create a CodeMirror instance.
	 * @return the text area
	 */
	@SuppressWarnings("unchecked")
	public final TextArea<String> getTextArea() {
		return (TextArea<String>)get("form").get("textarea");
	}

	/**
	 * Getter method for the resourceHandle.
	 * @return the resourceHandle
	 */
	public ResourceHandle getResourceHandle() {
		return resourceHandle;
	}
	
}
