/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.util;

import java.util.UUID;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxNewWindowNotifyingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.behavior.IBehaviorListener;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;

/**
 * This behavior serves a similar purpose as {@link AjaxNewWindowNotifyingBehavior}.
 * It also detects when a page is loaded into a new browser window/tab.
 * Unlike AjaxNewWindowNotifyingBehavior, however, it doesn't send a delayed AJAX
 * callback in that case, but intercepts client-side scripts right at the top
 * (using a {@link PriorityHeaderItem}) such that no further scripts are executed,
 * and redirects the browser to the callback URL of this behavior. A typical
 * subclass of this class would then issue a redirect (e.g. to a newly created
 * page).
 * 
 * The purpose of this class is to avoid callbacks happening from the second tab
 * that could interfere with the rendered page in the first tab, both "normal"
 * AJAX callbacks that happen right on-ready as well as establishing an
 * Atmosphere connection.
 * 
 * For example, a stateful page with an Atmosphere resource would not be very
 * user-friendly when rendered in multiple tabs (since the page's UI in the
 * different tabs would affect each other), and per-tab state (such as AJAX
 * delta-rendered components), would not work at all without some really dirty
 * tricks; performance would not be great either since we cannot know whether
 * "old" Atmosphere resources are still used, so all push-renderings must happen
 * for all old resources, even though most of them are not used anymore. Instead,
 * this behavior allows to send all new tabs to new pages, so each page is visible
 * only in a single tab and all old Atmosphere resources can be discarded (the
 * latter is NOT done by this behavior, though).
 */
public abstract class NewWindowInterceptBehavior extends Behavior implements IBehaviorListener {

	/**
	 * A unique name used for the page window's name
	 */
	private final String windowName;

	/**
	 * The name of the HTTP request parameter that brings the current page window's name.
	 */
	private static final String PARAM_WINDOW_NAME = "windowName";

	/**
	 * A flag whether this behavior has been rendered at least once.
	 */
	private boolean hasBeenRendered;

	/**
	 * Constructor.
	 */
	public NewWindowInterceptBehavior() {
		this(UUID.randomUUID().toString());
	}

	/**
	 * Constructor.
	 *
	 * @param windowName the custom name to use for the page's window
	 */
	public NewWindowInterceptBehavior(final String windowName) {
		this.windowName = windowName;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#bind(org.apache.wicket.Component)
	 */
	@Override
	public void bind(Component component) {
		super.bind(component);
		if (component instanceof WebPage == false) {
			throw new WicketRuntimeException(AjaxNewWindowNotifyingBehavior.class.getName() + " can be assigned only to WebPage instances.");
		}
	}

	/**
	 * Returns the callback URL for the specified component.
	 * @param component the component
	 * @return the URL
	 */
	public CharSequence getUrl(Component component) {
		return component.urlFor(this, IBehaviorListener.INTERFACE, null);
	}
	
	@Override
	public void renderHead(final Component component, final IHeaderResponse response) {
		super.renderHead(component, response);
		StringBuilder builder = new StringBuilder();
		
		// set the window name on first rendering; this affects only the first tab
		if (!hasBeenRendered) {
			hasBeenRendered = true;
			builder.append("window.name='").append(windowName).append("';\n");
		}
		
		// check the name each time; in the first tab this won't do anything
		{
			builder.append("if (window.name != '").append(windowName).append("') {\n");
			builder.append("\tlocation.href = '").append(getUrl(component)).append("';\n");
			builder.append("}\n");
		}
		
		response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forScript(builder.toString(), null)));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.IBehaviorListener#onRequest()
	 */
	@Override
	public void onRequest() {
		final RequestCycle requestCycle = RequestCycle.get();
		final StringValue uuidParam = requestCycle.getRequest().getRequestParameters().getParameterValue(PARAM_WINDOW_NAME);
		if (!windowName.equals(uuidParam.toString())) {
			IRequestHandler scheduledHandler1 = requestCycle.getRequestHandlerScheduledAfterCurrent();
			onNewWindow();
			IRequestHandler scheduledHandler2 = requestCycle.getRequestHandlerScheduledAfterCurrent();
			if (scheduledHandler1 == scheduledHandler2) {
				throw new RuntimeException("NewWindowInterceptBehavior.onNewWindow() did not schedule a new request handler -- " +
					"this would cause an infinite loop, so we break here");
			}
		}
	}
	
	/**
	 * A callback method when a new window/tab is opened for a page instance
	 * which is already opened in another window/tab. This method should issue
	 * a redirect, otherwise the current page will be reloaded in an
	 * infinite loop.
	 */
	protected abstract void onNewWindow();

}
