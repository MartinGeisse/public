/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide_plugin;

import name.martingeisse.wicket.util.AjaxRequestUtil;

/**
 * Test runnable that gets invoked by the workbench.
 */
public class MyRunnable1 implements Runnable {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		String javascript = "alert('Hello plugin JAR!');";
		AjaxRequestUtil.getAjaxRequestTarget().appendJavaScript(javascript);
	}
	
}
