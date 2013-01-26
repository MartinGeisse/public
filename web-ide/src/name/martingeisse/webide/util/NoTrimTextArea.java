/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.util;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

/**
 * A {@link TextArea} that doesn't trim its input.
 * 
 * @param <T> the model type
 */
public class NoTrimTextArea<T> extends TextArea<T> {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public NoTrimTextArea(final String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public NoTrimTextArea(final String id, final IModel<T> model) {
		super(id, model);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.FormComponent#shouldTrimInput()
	 */
	@Override
	protected boolean shouldTrimInput() {
		return false;
	}

}
