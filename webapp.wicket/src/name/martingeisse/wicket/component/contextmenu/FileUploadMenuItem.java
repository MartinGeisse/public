/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.wicket.component.upload.AbstractAjaxFileUploadField;
import name.martingeisse.wicket.util.ISimpleCallbackListener;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * This menu item creates an "input type=file" element.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public class FileUploadMenuItem<A> extends AbstractNamedContextMenuItem<A> {

	/**
	 * the behavior
	 */
	private MyBehavior behavior;
	
	/**
	 * the behaviorAddedToPage
	 */
	private boolean behaviorAddedToPage;
	
	/**
	 * Constructor.
	 */
	public FileUploadMenuItem() {
		this.behavior = new MyBehavior();
		this.behaviorAddedToPage = false;
	}

	/**
	 * Constructor.
	 * @param name the displayed name of this menu item
	 */
	public FileUploadMenuItem(final String name) {
		super(name);
		this.behavior = new MyBehavior();
		this.behaviorAddedToPage = false;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#onBeforeRender(org.apache.wicket.Component)
	 */
	@Override
	protected void onConfigure(Component component) {
		super.onConfigure(component);
		if (!behaviorAddedToPage) {
			Page page = component.getPage();
			page.add(behavior);
			behaviorAddedToPage = true;
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#notifySelectedInternal(java.lang.Object, java.lang.Object)
	 */
	@Override
	void notifySelectedInternal(final A anchor, final Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#buildItem(java.lang.StringBuilder, name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder)
	 */
	@Override
	void buildItem(final StringBuilder builder, final IContextMenuCallbackBuilder callbackBuilder) {
		Component component = callbackBuilder.getPage();
		final String url = component.urlFor(behavior, ISimpleCallbackListener.INTERFACE, null).toString();
		
		// TODO escape the name in all menu item classes
		String escapedName = JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(getName());
		String escapedUrl = JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(url);
		builder.append("createFileUploadMenuItem('").append(escapedName).append("', '").append(escapedUrl).append("')");
	}

	/**
	 * This behavior is added to the page to obtain callback URLs and contribute header items.
	 */
	class MyBehavior extends Behavior implements ISimpleCallbackListener {

		/* (non-Javadoc)
		 * @see org.apache.wicket.behavior.Behavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)
		 */
		@Override
		public void renderHead(final Component component, final IHeaderResponse response) {
			super.renderHead(component, response);
			response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AbstractAjaxFileUploadField.class, "jquery.iframe-transport.js")));
			response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AbstractAjaxFileUploadField.class, "jquery.fileupload.js")));
			WicketHeadUtil.includeClassJavascript(response, AbstractAjaxFileUploadField.class);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.util.ISimpleCallbackListener#onSimpleCallback()
		 */
		@Override
		public void onSimpleCallback() {
		}

	}

}
