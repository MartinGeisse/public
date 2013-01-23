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
import name.martingeisse.webide.plugin.state.IPluginBundleStateSerializer;
import name.martingeisse.webide.plugin.state.JsonSerializer;
import name.martingeisse.webide.plugin.state.PluginBundleStateAccessToken;
import name.martingeisse.webide.plugin.state.PluginBundleStateKey;

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
			String className = elementAnalyzer.analyzeMapElement("className").expectString();
			entries.add(new Entry(pluginBundleId, className));
		}
		return entries;
	}
	
	/**
	 * 
	 */
	private static Object getJson() {
		IPluginBundleStateSerializer<Object> serializer = new JsonSerializer();
		PluginBundleStateAccessToken token = new PluginBundleStateAccessToken();
		PluginBundleStateKey key = new PluginBundleStateKey(token, 1, 0);
		Object existing = key.load(serializer);
		if (existing != null) {
			return existing;
		}
		List<Object> jsonList = new ArrayList<Object>();
		for (Result extension : ExtensionQuery.fetch(1, "webide.context_menu.resource")) {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("pluginBundleId", extension.getPluginBundleId());
			jsonMap.put("className", extension.getDescriptor().toString());
			jsonList.add(jsonMap);
		}
		key.save(jsonList, serializer);
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
		 * Constructor.
		 * @param pluginBundleId the plugin bundle id
		 * @param className the class name
		 */
		public Entry(final long pluginBundleId, final String className) {
			this.pluginBundleId = pluginBundleId;
			this.className = className;
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
		
	}

}
