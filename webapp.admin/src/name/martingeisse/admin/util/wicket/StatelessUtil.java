/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

/**
 * Utility methods for implementing stateless components and
 * stateless pages.
 */
public class StatelessUtil {

	/**
	 * Dumps the component hierarchy, starting at the specified component.
	 * @param root the component to start at
	 */
	public static void dumpStatefulComponents(Component root) {
		if (!root.isStateless()) {
			System.out.println("stateful component: " + root.getPath());
		}
		if (root instanceof MarkupContainer) {
			MarkupContainer container = (MarkupContainer)root;
			for (Component child : container) {
				dumpStatefulComponents(child);
			}
		}
	}
	
}
