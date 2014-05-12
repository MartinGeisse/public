/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

/**
 * Helps profiling the rendering of components.
 */
public class ProfilingBehavior extends Behavior {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(ProfilingBehavior.class);
	
	/**
	 * the context
	 */
	private String context;
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#beforeRender(org.apache.wicket.Component)
	 */
	/**
	 * Constructor.
	 * @param context context information used for logging
	 */
	public ProfilingBehavior(String context) {
		this.context = context;
	}

	@Override
	public void beforeRender(Component component) {
		logger.info(context + " - beforeRender()");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#afterRender(org.apache.wicket.Component)
	 */
	@Override
	public void afterRender(Component component) {
		logger.info(context + " - afterRender()");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#onComponentTag(org.apache.wicket.Component, org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		logger.info(context + " - onComponentTag()");
	}
	
}
