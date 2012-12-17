/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.webide.workbench.components.contextmenu.ContextMenu;
import name.martingeisse.webide.workbench.components.contextmenu.IContextMenuCallbackBuilder;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.json.simple.JSONValue;

/**
 * This behavior can be attached to a component to make sub-elements
 * of the component selectable. Selected elements are marked using
 * specific CSS classes and inline styles. Various types of
 * interactions, as well as provided Javascript functions, allow to
 * trigger server-side actions on the selected elements.
 * 
 * The behavior is configured by two main options:
 * - the elementSelector is a jQuery selector that finds the selectable
 *   elements within the context of the component to which this behavior
 *   is attached
 * - the valueExpression is evaluated to find the "value" of a selectable
 *   element (available in a variable called "element"). This value is
 *   then sent to the server. Such values must be JSON-encodable (they
 *   will be passed to JSON.stringify()).
 * 
 * This class supports a context menu, with the list of selected elements
 * as the anchor.
 * 
 * @param <T> the internal representation type of selectable elements
 */
public abstract class SelectableElementsBehavior<T> extends AbstractDefaultAjaxBehavior implements IContextMenuCallbackBuilder {

	/**
	 * the CONTEXT_MENU_INTERACTION_PREFIX
	 */
	private static final String CONTEXT_MENU_INTERACTION_PREFIX = "contextMenu.";
	
	/**
	 * the elementSelector
	 */
	private String elementSelector;

	/**
	 * the valueExpression
	 */
	private String valueExpression;
	
	/**
	 * the contextMenu
	 */
	private ContextMenu<List<T>> contextMenu;

	/**
	 * Constructor.
	 * @param elementSelector the jQuery selector for selectable elements
	 * @param valueExpression an expression for the value of a selectable element
	 */
	public SelectableElementsBehavior(final String elementSelector, final String valueExpression) {
		this.elementSelector = elementSelector;
		this.valueExpression = valueExpression;
	}
	
	/**
	 * Constructor.
	 * @param elementSelector the jQuery selector for selectable elements
	 * @param valueExpression an expression for the value of a selectable element
	 * @param contextMenu the context menu
	 */
	public SelectableElementsBehavior(final String elementSelector, final String valueExpression, ContextMenu<List<T>> contextMenu) {
		this.elementSelector = elementSelector;
		this.valueExpression = valueExpression;
		this.contextMenu = contextMenu;
	}

	/**
	 * Getter method for the elementSelector.
	 * @return the elementSelector
	 */
	public String getElementSelector() {
		return elementSelector;
	}

	/**
	 * Setter method for the elementSelector.
	 * @param elementSelector the elementSelector to set
	 */
	public void setElementSelector(final String elementSelector) {
		this.elementSelector = elementSelector;
	}

	/**
	 * Getter method for the valueExpression.
	 * @return the valueExpression
	 */
	public String getValueExpression() {
		return valueExpression;
	}

	/**
	 * Setter method for the valueExpression.
	 * @param valueExpression the valueExpression to set
	 */
	public void setValueExpression(final String valueExpression) {
		this.valueExpression = valueExpression;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final Component component, final IHeaderResponse response) {
		super.renderHead(component, response);
		WicketHeadUtil.includeClassJavascript(response, SelectableElementsBehavior.class);

		final CallbackParameter[] parameters = new CallbackParameter[] {
			CallbackParameter.explicit("interaction"),
			CallbackParameter.explicit("selectedValues"),
			CallbackParameter.explicit("data"),
		};

		final StringBuilder builder = new StringBuilder();
		builder.append("\t$('#").append(component.getMarkupId()).append("').selectableElements({\n");
		builder.append("\t\telementSelector: '" + elementSelector + "',\n");
		builder.append("\t\tvalueExtractor: function(element) {return " + valueExpression + ";},\n");
		builder.append("\t\tajaxCallback: ").append(getCallbackFunction(parameters).toString().replace("\n", " ")).append(",");
		builder.append("\t\tnotSelectedStyle: {'background-color': ''},\n");
		builder.append("\t\tselectedStyle: {'background-color': '#f00'},\n");
		builder.append("\t\thasContextMenu: ").append(contextMenu != null).append(",\n");
		builder.append("\t});\n");
		if (contextMenu != null) {
			ContextMenu.renderHead(response);
			contextMenu.buildCreateInstruction(builder, "#" + component.getMarkupId(), this);
		}

		final AjaxRequestTarget target = component.getRequestCycle().find(AjaxRequestTarget.class);
		if (target == null) {
			response.render(OnDomReadyHeaderItem.forScript(builder.toString()));
		} else {
			target.appendJavaScript(builder);
		}

	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void respond(final AjaxRequestTarget target) {
		final RequestCycle requestCycle = RequestCycle.get();
		final IRequestParameters parameters = requestCycle.getRequest().getRequestParameters();
		final String interaction = parameters.getParameterValue("interaction").toString();
		if (interaction != null) {
			final Object selectedValues = getJsonParameter(parameters, "selectedValues", "[]");
			final Object data = getJsonParameter(parameters, "data", "null");
			if (selectedValues instanceof List) {
				List<T> convertedValues = convertSelectedValues((List<?>)selectedValues);
				if (interaction.startsWith(CONTEXT_MENU_INTERACTION_PREFIX)) {
					if (contextMenu != null) {
						String menuItemKey = interaction.substring(CONTEXT_MENU_INTERACTION_PREFIX.length());
						contextMenu.notifySelected(menuItemKey, convertedValues, data);
					}
				} else {
					onInteraction(target, interaction, convertedValues);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private static Object getJsonParameter(IRequestParameters parameters, String name, String encodedDefaultValue) {
		String encodedValue = parameters.getParameterValue(name).toString();
		if (encodedValue == null) {
			encodedValue = encodedDefaultValue;
		}
		return JSONValue.parse(encodedValue);
	}

	/**
	 * Converts a list of values from JSON to the internal value type T.
	 * @param jsonValues the JSON values
	 * @return the internal values
	 */
	protected List<T> convertSelectedValues(List<?> jsonValues) {
		List<T> result = new ArrayList<T>();
		for (Object jsonValue : jsonValues) {
			result.add(convertSelectedValue(jsonValue));
		}
		return result;
	}
	
	/**
	 * Converts a single value from JSON to the internal value type T.
	 * @param jsonValue the JSON value
	 * @return the internal value
	 */
	protected abstract T convertSelectedValue(Object jsonValue);
	
	/**
	 * This method is invoked when the client-side scripts notify the server about
	 * a user interaction.
	 * @param target the ART
	 * @param interaction a string describing the interaction, e.g. "dblclick"
	 * for a double-click
	 * @param selectedValues the selected values
	 */
	protected abstract void onInteraction(AjaxRequestTarget target, String interaction, List<T> selectedValues);

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.IContextMenuCallbackBuilder#buildContextMenuCallback(java.lang.StringBuilder)
	 */
	@Override
	public void buildContextMenuCallback(StringBuilder builder) {
		builder.append("var interaction = '" + CONTEXT_MENU_INTERACTION_PREFIX + "' + key;");
		builder.append("\t$('#").append(getComponent().getMarkupId()).append("').selectableElements_ajax(interaction, data);\n");
	}

}
