/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;

/**
 * A border component whose purpose is to render either its
 * contents, or a replacement. The replacement is, by default,
 * the string "*** replacement ***" -- subclass this border
 * and add child markup to use that as the replacement. Also,
 * the subclass must implement the {@link #useReplacement()}
 * method that decides whether the original content or the
 * replacement is visible. 
 */
public abstract class ReplacementBorder extends Border {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public ReplacementBorder(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public ReplacementBorder(String id, IModel<?> model) {
		super(id, model);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		addToBorder(new WebMarkupContainer("replacement"));
	}
	
	/**
	 * Returns the replacement container.
	 * @return the replacement container.
	 */
	public WebMarkupContainer getReplacement() {
		return (WebMarkupContainer)get("replacement");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onConfigure()
	 */
	@Override
	protected void onConfigure() {
		super.onConfigure();
		boolean useReplacement = useReplacement();
		getReplacement().setVisible(useReplacement);
		getBodyContainer().setVisible(!useReplacement);
	}
	
	/**
	 * @return
	 */
	protected abstract boolean useReplacement();
	
}
