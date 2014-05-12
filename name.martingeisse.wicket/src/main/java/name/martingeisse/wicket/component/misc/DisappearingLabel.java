/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import java.io.Serializable;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * A {@link Label} that hides when its text becomes empty.
 * 
 * Hiding uses a "display: none" style. The HTML tag is still
 * there to allow AJAX updates to the component.
 * 
 * The label text is considered "empty" if it is null or if its
 * trimmed text content is the empty string.
 */
public class DisappearingLabel extends Label {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public DisappearingLabel(final String id, final IModel<?> model) {
		super(id, model);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param label the label object
	 */
	public DisappearingLabel(final String id, final Serializable label) {
		super(id, label);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param label the label text
	 */
	public DisappearingLabel(final String id, final String label) {
		super(id, label);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public DisappearingLabel(final String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return (super.isVisible() && !isEmpty());
	}
	
	/**
	 * 
	 */
	private boolean isEmpty() {
		String s = getDefaultModelObjectAsString();
		return (s == null || s.trim().isEmpty());
	}
	
}
