/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.experiment;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.atmosphere.EventBus;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

/**
 *
 */
public class PushTestPage extends WebPage {

	/**
	 * the status
	 */
	private String status = "new";
	
	/**
	 * Constructor.
	 */
	public PushTestPage() {
		setStatelessHint(false);
		final Application application = getApplication();
		add(new AjaxLink<Void>("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						status = "idle Ж";
						EventBus.get(application).post(new MyMessage());
					}
				}, 5000);
				status = "running Ж...";
				target.add(PushTestPage.this.get("status"));
			}
		});
		add(new Label("status", new PropertyModel<String>(this, "status")).setOutputMarkupId(true));
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
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Subscribe
	public void receiveMessage(AjaxRequestTarget target, MyMessage message) {
		target.add(PushTestPage.this.get("status"));
	}
	
	class MyMessage {
		
	}
	
}
