/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.experiment;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

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
			public void onClick(AjaxRequestTarget target) {
				status = "working...";
				target.add(PushTestPage.this.get("status"));
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO
					}
				}, 5000);
			}
		});
	}
	
}
