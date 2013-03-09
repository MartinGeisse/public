/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.experiment;

import java.util.Timer;
import java.util.TimerTask;

import name.martingeisse.wicket.icons.flags.Dummy;
import name.martingeisse.wicket.websockets.WebSocketConnectionIdentifier;
import name.martingeisse.wicket.websockets.WebSocketPushBehavior;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.request.resource.PackageResourceReference;

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
		add(new Image("image", new PackageResourceReference(Dummy.class, "de.png")).setOutputMarkupId(true));
		
		// button
		add(new AjaxLink<Void>("button") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				status = "working...";
				target.add(PushTestPage.this.get("status"));
				final WebSocketConnectionIdentifier connectionId = new WebSocketConnectionIdentifier(this);
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						status = "done!";
						((Image)PushTestPage.this.get("image")).setImageResourceReference(new PackageResourceReference(Dummy.class, "england.png"));
						connectionId.push(new MyMessage());
					}
				}, 5000);
			}
			
		});
		add(new WebSocketPushBehavior() {

			/* (non-Javadoc)
			 * @see name.martingeisse.wicket.websockets.WebSocketPushBehavior#onPush(org.apache.wicket.protocol.ws.api.WebSocketRequestHandler, org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage)
			 */
			@Override
			protected void onPush(WebSocketRequestHandler webSocketRequestHandler, IWebSocketPushMessage message) {
				if (message instanceof MyMessage) {
					webSocketRequestHandler.add(PushTestPage.this.get("status"));
					webSocketRequestHandler.add(PushTestPage.this.get("image"));
				}
			}
			
		});
		
	}

	static class MyMessage implements IWebSocketPushMessage {
	}
	
}
