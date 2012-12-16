/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components.contextmenu;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes the structure of a context menu. It is not
 * itself a component or behavior; rather, it is used by such.
 * 
 * The context menu is used in the context of an "anchor" of type A.
 * In a typical example, the context menu is invoked by right-clicking
 * on the visual representation of the anchor, and selecting a menu
 * item has an effect on the anchor. The anchor is passed to the
 * handler methods of the menu item.
 * 
 * Note, however, that the menu is *used* in the context of an anchor,
 * but typically *created* once for a whole list of anchors. This means
 * that the anchor isn't yet available at construction time.
 * 
 * @param <A> the anchor type
 */
public class ContextMenu<A> {

	/**
	 * the items
	 */
	private final List<ContextMenuItem<? super A>> items = new ArrayList<ContextMenuItem<? super A>>();
	
	/**
	 * Constructor.
	 */
	public ContextMenu() {
	}

	/**
	 * Getter method for the items.
	 * @return the items
	 */
	public final List<ContextMenuItem<? super A>> getItems() {
		return items;
	}
	
	/**
	 * Triggers the effect of the menu item with the specified key.
	 * @param key the key
	 * @param anchor the anchor
	 */
	public final void notifySelected(String key, A anchor) {
		int index = extractIndexFromKey(key);
		if (index < 0 || index >= items.size()) {
			throw new RuntimeException("invalid context menu item key: " + key);
		}
		items.get(index).notifySelected(anchor);
	}
	
	/**
	 * 
	 */
	private int extractIndexFromKey(String key) {
		if (!key.startsWith("item")) {
			return -1;
		}
		try {
			return Integer.parseInt(key.substring(4));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	 * Builds a Javascript instruction that creates this menu using the jQuery context menu plugin.
	 * @param builder the string builder to append to
	 * @param selector the jQuery selector for the menu trigger element
	 * @param callbackBuilder an object that knows how to generate callback requests
	 * to the server and route them to the appropriate menu item
	 */
	public void buildCreateInstruction(StringBuilder builder, String selector, IContextMenuCallbackBuilder callbackBuilder) {
		builder.append("$(document).ready(function() {\n");
		builder.append("	$.contextMenu({\n");
		builder.append("		selector: '").append(selector).append("',\n");
		builder.append("		build: function() {\n");
		builder.append("			return {\n");
		builder.append("				callback: function(key /*, options*/) {\n");
		callbackBuilder.buildContextMenuCallback(builder);
		builder.append("				},\n");
		builder.append("				items: {\n");
		int i=0;
		for (ContextMenuItem<? super A> item : items) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append("					item").append(i).append(": ");
			item.buildItem(builder);
			builder.append("\n");
			i++;
		}
		builder.append("				}\n");
		builder.append("			};\n");
		builder.append("		}\n");
		builder.append("	});\n");
		builder.append("});\n");
	}
	
}
