/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.common.terms.CommandVerb;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.wicket.component.contextmenu.ContextMenu;
import name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder;
import name.martingeisse.wicket.component.tree.JsTree.Item;
import name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
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
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TreeAjaxBehavior.class);
	
	/**
	 * the CONTEXT_MENU_INTERACTION_PREFIX
	 */
	private static final String CONTEXT_MENU_INTERACTION_PREFIX = "contextMenu.";

	/**
	 * the COMMAND_VERB_INTERACTION_PREFIX
	 */
	private static final String COMMAND_VERB_INTERACTION_PREFIX = "commandVerb.";
	
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

	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#onConfigure(org.apache.wicket.Component)
	 */
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		tree.getContextMenu().onConfigure(component);
	};
	
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
		if (interaction != null && selectedNodeIndices != null) {
			final List<T> selectedNodes = lookupSelectedNodes(selectedNodeIndices);
			final Object data = getJsonParameter(parameters, "data", "null");
			if (interaction.startsWith(CONTEXT_MENU_INTERACTION_PREFIX)) {
				final ContextMenu<List<T>> contextMenu = tree.getContextMenu();
				if (contextMenu != null) {
					final String menuItemKey = interaction.substring(CONTEXT_MENU_INTERACTION_PREFIX.length());
					contextMenu.notifySelected(menuItemKey, selectedNodes, data);
				}
			} else if (interaction.startsWith(COMMAND_VERB_INTERACTION_PREFIX)) {
				String commandVerbCanonicalId = interaction.substring(COMMAND_VERB_INTERACTION_PREFIX.length());
				CommandVerb commandVerb = CommandVerb.fromCanonicalIdentifierSafe(commandVerbCanonicalId);
				if (commandVerb != null) {
					tree.onCommandVerb(commandVerb, selectedNodes, data);
				}
			} else {
				tree.onInteraction(interaction, selectedNodes, data);
				if (interaction.equals("dblclick")) {
					tree.onDoubleClick(selectedNodes, data);
				}
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
	 * @param specifier specifier the node index list
	 * @return the internal nodes
	 */
	protected List<T> lookupSelectedNodes(final String specifier) {
		final List<T> result = new ArrayList<T>();
		if (specifier.isEmpty()) {
			return result;
		}
		for (final String nodeSpec : StringUtils.split(specifier, ':')) {
			try {
				final Item<T> item = GenericTypeUtil.unsafeCast((Item<?>)tree.get(nodeSpec));
				result.add(item.getModelObject());
			} catch (final Exception e) {
				logger.debug("could not parse JsTree item specifier: " + specifier, e);
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
		builder.append("\t$('#").append(tree.getMarkupId()).append("').jstreeInteract(interaction, data);\n");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder#buildContextMenuCallback(java.lang.StringBuilder, name.martingeisse.common.terms.CommandVerb)
	 */
	@Override
	public void buildContextMenuCallback(final StringBuilder builder, final CommandVerb commandVerb) {
		buildCommandVerbInteraction(builder, commandVerb);
	}

	/**
	 * Builds a Javascript fragment that sends a command verb to the tree component, respecting the
	 * interceptor for that verb.
	 */
	void buildCommandVerbInteraction(final StringBuilder builder, final CommandVerb commandVerb) {
		String interactionId = (COMMAND_VERB_INTERACTION_PREFIX + commandVerb.getCanonicalIdentifier());
		IJavascriptInteractionInterceptor<?> interceptor = tree.getInterceptor(commandVerb);
		if (interceptor != null) {
			builder.append("function onInterceptorPassed(data) {\n");
		} else {
			builder.append("var data = null;\n");
		}
		builder.append("var interaction = '" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(interactionId) + "';\n");
		builder.append("\t$('#").append(tree.getMarkupId()).append("').jstreeInteract(interaction, data);\n");
		if (interceptor != null) {
			builder.append("}\n");
			builder.append("var interceptor = ");
			interceptor.printInterceptorFunction(builder);
			builder.append(";");
			builder.append("interceptor(onInterceptorPassed);\n");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder#getPage()
	 */
	@Override
	public Page getPage() {
		return getComponent().getPage();
	}
	
}
