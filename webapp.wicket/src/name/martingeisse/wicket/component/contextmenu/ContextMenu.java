/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.terms.CommandVerb;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.markup.head.IHeaderResponse;

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
 * In addition to the anchor, individual menu items may generate 
 * additional data on the client-side and pass it to the server-side
 * handling methods. This data is passed in a format that is specific
 * to where the context menu is attached (though parsed JSON is a
 * typical format). The structure of this data is totally dependent
 * on the menu item that generates it. An example for such additional
 * data is a menu item that asks the user for an input string in a
 * pop-up dialog before sending the context menu request to the server.
 * 
 * @param <A> the anchor type
 */
public class ContextMenu<A> implements Serializable {

	/**
	 * Renders head items needed by context menus. This method should be used
	 * by components and behaviors when they want to use a context menu.
	 * @param response the header response
	 */
	public static void renderHead(IHeaderResponse response) {
		WicketHeadUtil.includeClassJavascript(response, ContextMenu.class);
	}
	
	/**
	 * the declaredItems
	 */
	private final List<ContextMenuItem<? super A>> declaredItems = new ArrayList<ContextMenuItem<? super A>>();

	/**
	 * the effectiveItems
	 */
	private final List<ContextMenuItem<? super A>> effectiveItems = new ArrayList<ContextMenuItem<? super A>>();
	
	/**
	 * the dirty
	 */
	private boolean dirty;
	
	/**
	 * Constructor.
	 */
	public ContextMenu() {
		dirty = false;
	}

	/**
	 * Adds the specified item to this context menu.
	 * @param item the item to add
	 */
	public final void add(ContextMenuItem<? super A> item) {
		declaredItems.add(item);
		dirty = true;
	}

	/**
	 * Adds the specified command verb as an item to this context menu.
	 * This is a convenience method to add a {@link CommandVerbMenuItem}.
	 * @param name the name of the menu item
	 * @param commandVerb the command verb
	 */
	public final void add(String name, CommandVerb commandVerb) {
		add(new CommandVerbMenuItem<A>(name, commandVerb));
	}

	/**
	 * Getter method for the effectiveItems.
	 * @return the effectiveItems
	 */
	private List<ContextMenuItem<? super A>> getEffectiveItems() {
		if (dirty) {
			effectiveItems.clear();
			for (ContextMenuItem<? super A> declaredItem : declaredItems) {
				addItemOrReplacement(declaredItem);
			}
			dirty = false;
		}
		return effectiveItems;
	}

	/**
	 * Adds either the specified item (if it has no replacement items), or its
	 * replacement items. Replacement happens recursively, so the replacement
	 * items can be replaced again.
	 */
	private void addItemOrReplacement(ContextMenuItem<? super A> item) {
		ContextMenuItem<? super A>[] replacementItems = item.getReplacementItems();
		if (replacementItems == null) {
			effectiveItems.add(item);
		} else {
			for (ContextMenuItem<? super A> replacementItem : replacementItems) {
				addItemOrReplacement(replacementItem);
			}
		}
	}
	
	
	/**
	 * Triggers the effect of the menu item with the specified key.
	 * @param key the key
	 * @param anchor the anchor
	 * @param data additional data from the client-side code for this menu item
	 */
	public final void notifySelected(String key, A anchor, Object data) {
		List<ContextMenuItem<? super A>> items = getEffectiveItems();
		int index = extractIndexFromKey(key);
		if (index < 0 || index >= items.size()) {
			throw new RuntimeException("invalid context menu item key: " + key);
		}
		items.get(index).notifySelected(anchor, data);
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
	public void buildCreateExpression(StringBuilder builder, String selector, IContextMenuCallbackBuilder callbackBuilder) {
		List<ContextMenuItem<? super A>> items = getEffectiveItems();
		builder.append("createContextMenu('").append(selector).append("', function(key, options) {var data = null; \n");
		callbackBuilder.buildContextMenuCallback(builder);
		builder.append("}, {");
		int i=0;
		for (ContextMenuItem<? super A> item : items) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append("					item").append(i).append(": ");
			item.buildItem(builder, callbackBuilder);
			builder.append("\n");
			i++;
		}
		builder.append("})");
	}
	
}
