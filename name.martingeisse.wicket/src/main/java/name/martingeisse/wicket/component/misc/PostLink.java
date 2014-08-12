/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.component.misc;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 * A link that sends its request via POST. The "link" is actually a form
 * that contains only a submit button.
 */
public abstract class PostLink extends Border {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public PostLink(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public PostLink(String id, IModel<?> model) {
		super(id, model);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				PostLink.this.onSubmit();
			}
		};
		addToBorder(form);
	}

	/**
	 * Callback for when the user clicks on the link.
	 */
	protected abstract void onSubmit();
	
}
