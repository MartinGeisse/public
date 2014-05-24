/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import name.martingeisse.common.util.ParameterUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

/**
 * Utility methods for implementing stateless components and
 * stateless pages.
 */
public class StatelessUtil {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(StatelessUtil.class);
	
	/**
	 * Dumps the stateful components in the component hierarchy, starting at
	 * the specified component. The information is logged at INFO level.
	 * 
	 * @param root the component to start at
	 */
	public static void dumpStatefulComponents(final Component root) {
		ParameterUtil.ensureNotNull(root, "root");
		if (!root.isStateless()) {
			logger.info("stateful component: " + root.getPath());
		}
		if (root instanceof MarkupContainer) {
			final MarkupContainer container = (MarkupContainer)root;
			for (final Component child : container) {
				dumpStatefulComponents(child);
			}
		}
	}

}
