/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page;

import name.martingeisse.common.util.ObjectStateUtil;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * This page shows an error message. It is typically used when an error does
 * not allow the user to continue whatever he/she was doing. (Otherwise a
 * more sophisticated error page, with additional information, links etc.
 * would be used).
 */
public class ErrorPage extends AbstractAdminPage {

	/**
	 * Constructor.
	 * @param message the error message
	 */
	public ErrorPage(final String message) {
		this(Model.of(ParameterUtil.ensureNotNull(message, "message")));
	}

	/**
	 * Constructor.
	 * @param messageModel the model for the error message
	 */
	public ErrorPage(final IModel<String> messageModel) {
		super(ParameterUtil.ensureNotNull(messageModel, "messageModel"));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.page.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		getMainContainer().add(new Label("message", ObjectStateUtil.nullNotAllowed(getDefaultModel(), "error message model value")));
	}
	
}
