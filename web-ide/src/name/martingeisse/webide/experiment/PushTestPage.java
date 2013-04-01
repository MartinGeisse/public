/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.experiment;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;

/**
 * TODO: document me
 *
 */
public class PushTestPage extends WebPage {

	/**
	 * the status
	 */
	private String status;

	/**
	 * Constructor.
	 */
	public PushTestPage() {
		status = "initial";
		add(new Label("status", new PropertyModel<String>(this, "status")).setOutputMarkupId(true));
		add(new AjaxLink<Void>("button") {

			@Override
			public void onClick(final AjaxRequestTarget target) {
				status = "working...";
				target.add(PushTestPage.this.get("status"));
			}

		});
		add(new WebSocketBehavior() {
		});
	}

	/**
	 * Getter method for the status.
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter method for the status.
	 * @param status the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

}
