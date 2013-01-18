/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.experiment;

import java.util.Timer;
import java.util.TimerTask;

import name.martingeisse.wicket.icons.flags.Dummy;

import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.ws.IWebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.protocol.ws.api.event.WebSocketPushPayload;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
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
				final Application application = getApplication();
				final String sessionId = getSession().getId();
				final int pageId = getPageId();
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						status = "done!";
						((Image)PushTestPage.this.get("image")).setImageResourceReference(new PackageResourceReference(Dummy.class, "england.png"));
						IWebSocketConnectionRegistry connectionRegistry = IWebSocketSettings.Holder.get(application).getConnectionRegistry();
						ConnectedMessage connectedMessage = new ConnectedMessage(application, sessionId, pageId);
						new WebSocketPushBroadcaster(connectionRegistry).broadcast(connectedMessage, new MyMessage());
					}
				}, 5000);
			}
			
			@Override
			public void onEvent(IEvent<?> event) {
				super.onEvent(event);
				if (event.getPayload() instanceof WebSocketPushPayload) {
					WebSocketPushPayload payload = (WebSocketPushPayload)event.getPayload();
					IWebSocketPushMessage message = payload.getMessage();
					if (message instanceof MyMessage) {
						payload.getHandler().add(PushTestPage.this.get("status"));
						payload.getHandler().add(PushTestPage.this.get("image"));
					}
				}
			}
			
		});
		add(new WebSocketBehavior() {});
		
	}

	static class MyMessage implements IWebSocketPushMessage {
	}
	
}
