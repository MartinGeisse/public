/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

/**
 * Represents a long-running computation, remote request or similar that will
 * deliver a result in the future, and this result is of interest to the browser.
 * The client will ask this server regularly whether the result is available
 * using AJAX requests. Each request will check the available futures, remove
 * finished futures, and potentially add new ones.
 * 
 * A finished future delivery its value by adding Javascript code to the
 * {@link AjaxRequestTarget}.
 */
public interface IClientFuture {

	/**
	 * Checks for any necessary action for this future. This may add further
	 * futures to the behavior.
	 * 
	 * @param behavior the behavior (used to add further futures)
	 * @return true if this future shall be removed from the behavior, false to stay
	 */
	public boolean check(Behavior behavior);
	
	/**
	 * This behavior must be added to a component to receive client-side
	 * futures. It is typically added to the page to avoid using multiple
	 * concurrent AJAX calls for different components.
	 */
	public static class Behavior extends AbstractDefaultAjaxBehavior {

		/**
		 * the logger
		 */
		@SuppressWarnings("unused")
		private static Logger logger = Logger.getLogger(IClientFuture.class);
		
		/**
		 * the futures
		 */
		private final Set<IClientFuture> futures = new HashSet<IClientFuture>();

		/**
		 * Adds a future to this behavior.
		 * @param future the future to add
		 */
		public void addFuture(IClientFuture future) {
			futures.add(future);
			AjaxRequestTarget target = AjaxRequestUtil.getAjaxRequestTarget();
			if (target != null) {
				target.appendJavaScript(getDelayedCallback());
			}
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		@Override
		protected void respond(AjaxRequestTarget target) {
			checkFutures();
			if (!futures.isEmpty()) {
				target.appendJavaScript(getDelayedCallback());
			}
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.behavior.Behavior#beforeRender(org.apache.wicket.Component)
		 */
		@Override
		public void beforeRender(Component component) {
			super.beforeRender(component);
			if (AjaxRequestUtil.getAjaxRequestTarget() == null) {
				checkFutures();
			}
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)
		 */
		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			super.renderHead(component, response);
			if (AjaxRequestUtil.getAjaxRequestTarget() == null && !futures.isEmpty()) {
				response.render(JavaScriptHeaderItem.forScript(getDelayedCallback(), null));
			}
		}
		
		/**
		 * 
		 */
		private String getDelayedCallback() {
			StringBuilder builder = new StringBuilder();
			builder.append("setTimeout(function() {");
			builder.append(getCallbackScript());
			builder.append("}, 1000);");
			return builder.toString();
		}
		
		/**
		 * 
		 */
		private void checkFutures() {
			Set<IClientFuture> currrentFutures = new HashSet<IClientFuture>(futures);
			for (IClientFuture future : currrentFutures) {
				if (future.check(this)) {
					futures.remove(future);
				}
			}
		}

		/**
		 * Returns the instance of this class for the specified component.
		 * @param component the component
		 * @return the behavior
		 */
		public static Behavior get(Component component) {
			List<Behavior> behaviors = component.getBehaviors(Behavior.class);
			if (behaviors.isEmpty()) {
				return null;
			} else if (behaviors.size() == 1) {
				return behaviors.get(0);
			} else {
				throw new IllegalStateException("multiple client-side future behaviors at component " + component);
			}
		}
		
	}
	
}
