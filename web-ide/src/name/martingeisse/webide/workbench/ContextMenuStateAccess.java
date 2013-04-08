/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.plugin.ExtensionQuery;
import name.martingeisse.webide.plugin.ExtensionQuery.Result;

/**
 * Accesses plugin state regarding the context menu.
 */
public class ContextMenuStateAccess {

	/**
	 * Gets context menu state.
	 * @return the list of entries
	 */
	public static List<Entry> get() {
		JsonAnalyzer analyzer = new JsonAnalyzer(getJson());
		List<Entry> entries = new ArrayList<Entry>();
		for (JsonAnalyzer elementAnalyzer : analyzer.analyzeList()) {
			long pluginBundleId = elementAnalyzer.analyzeMapElement("pluginBundleId").expectLong();
			String className = elementAnalyzer.analyzeMapElement("class").expectString();
			String menuItemName = elementAnalyzer.analyzeMapElement("name").expectString();
			entries.add(new Entry(pluginBundleId, className, menuItemName));
		}
		return entries;
	}
	
	/**
	 * 
	 */
	private static Object getJson() {
		List<Object> jsonList = new ArrayList<Object>();
		for (Result extension : ExtensionQuery.fetch(1L, 1L, "webide.context_menu.resource")) {
			JsonAnalyzer analyzer = new JsonAnalyzer(extension.getDescriptor());
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("pluginBundleId", extension.getPluginBundleId());
			jsonMap.put("class", analyzer.analyzeMapElement("class").expectString());
			jsonMap.put("name", analyzer.analyzeMapElement("name").expectString());
			jsonList.add(jsonMap);
		}
		return jsonList;
	}
	
	/**
	 * State for a context menu entry.
	 */
	public static class Entry {

		/**
		 * the pluginBundleId
		 */
		private final long pluginBundleId;

		/**
		 * the className
		 */
		private final String className;
		
		/**
		 * the menuItemName
		 */
		private final String menuItemName;

		/**
		 * Constructor.
		 * @param pluginBundleId the plugin bundle id
		 * @param className the class name
		 * @param menuItemName the name to use for the menu item
		 */
		public Entry(final long pluginBundleId, final String className, final String menuItemName) {
			this.pluginBundleId = pluginBundleId;
			this.className = className;
			this.menuItemName = menuItemName;
		}

		/**
		 * Getter method for the pluginBundleId.
		 * @return the pluginBundleId
		 */
		public long getPluginBundleId() {
			return pluginBundleId;
		}
		
		/**
		 * Getter method for the className.
		 * @return the className
		 */
		public String getClassName() {
			return className;
		}
		
		/**
		 * Getter method for the menuItemName.
		 * @return the menuItemName
		 */
		public String getMenuItemName() {
			return menuItemName;
		}
		
	}

}
