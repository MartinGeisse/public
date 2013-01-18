/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.wicket.component.contextmenu.ContextMenu;
import name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.json.simple.JSONValue;

/**
 * Handles AJAX requests for a JsTree.
 * 
 * @param <T> the tree node type
 */
class TreeAjaxBehavior<T> extends AbstractDefaultAjaxBehavior implements IContextMenuCallbackBuilder {

	/**
	 * the CONTEXT_MENU_INTERACTION_PREFIX
	 */
	private static final String CONTEXT_MENU_INTERACTION_PREFIX = "contextMenu.";

	/**
	 * the tree
	 */
	private final JsTree<T> tree;

	/**
	 * Constructor.
	 * @param tree the tree
	 */
	TreeAjaxBehavior(final JsTree<T> tree) {
		this.tree = tree;
	}

	/**
	 * Returns a Javascript expression that specifies the AJAX callback function.
	 */
	String getCallbackSpecifier() {
		final CallbackParameter[] parameters = new CallbackParameter[] {
			CallbackParameter.explicit("interaction"), CallbackParameter.explicit("selectedNodes"), CallbackParameter.explicit("data"),
		};
		return getCallbackFunction(parameters).toString().replace("\n", " ");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void respond(final AjaxRequestTarget target) {
		final RequestCycle requestCycle = RequestCycle.get();
		final IRequestParameters parameters = requestCycle.getRequest().getRequestParameters();
		final String interaction = parameters.getParameterValue("interaction").toString();
		final String selectedNodeIndices = parameters.getParameterValue("selectedNodes").toString();
		
		System.out.println("* [" + interaction + "][" + selectedNodeIndices + "]");
		
		if (interaction != null && selectedNodeIndices != null) {
			final List<T> selectedNodes = lookupSelectedNodes(selectedNodeIndices);
			final Object data = getJsonParameter(parameters, "data", "null");
			if (interaction.startsWith(CONTEXT_MENU_INTERACTION_PREFIX)) {
				final ContextMenu<List<T>> contextMenu = tree.getContextMenu();
				if (contextMenu != null) {
					final String menuItemKey = interaction.substring(CONTEXT_MENU_INTERACTION_PREFIX.length());
					contextMenu.notifySelected(menuItemKey, selectedNodes, data);
				}
			} else {
				tree.onInteraction(target, interaction, selectedNodes);
			}
		}
	}

	/**
	 * 
	 */
	private static Object getJsonParameter(final IRequestParameters parameters, final String name, final String encodedDefaultValue) {
		String encodedValue = parameters.getParameterValue(name).toString();
		if (encodedValue == null) {
			encodedValue = encodedDefaultValue;
		}
		return JSONValue.parse(encodedValue);
	}

	/**
	 * Converts a list of node indices from JSON to the internal node type T.
	 * @param jsonValues the JSON values
	 * @return the internal nodes
	 */
	protected List<T> lookupSelectedNodes(String specifier) {
		final List<T> result = new ArrayList<T>();
		if (specifier.isEmpty()) {
			return result;
		}
		for (String nodeSpec : StringUtils.split(specifier, ':')) {
			try {
				int index = Integer.parseInt(nodeSpec);
				final Item<T> item = GenericTypeUtil.unsafeCast((Item<?>)tree.get(Integer.toString(index)));
				result.add(item.getModelObject());
			} catch (Exception e) {
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.IContextMenuCallbackBuilder#buildContextMenuCallback(java.lang.StringBuilder)
	 */
	@Override
	public void buildContextMenuCallback(final StringBuilder builder) {
		builder.append("var interaction = '" + CONTEXT_MENU_INTERACTION_PREFIX + "' + key;");
		builder.append("\t$('#").append(tree.getMarkupId()).append("').jstree_ajax(interaction, data);\n");
	}

}
