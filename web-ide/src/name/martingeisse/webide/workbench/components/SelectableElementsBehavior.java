/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components;

import java.util.List;

import name.martingeisse.webide.workbench.components.contextmenu.ContextMenu;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
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
public abstract class SelectableElementsBehavior<T> extends AbstractDefaultAjaxBehavior {

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

	TODO: pass Class<T> in the constructor, cast & pass typed to onInteraction and to the context menu
	
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
	public SelectableElementsBehavior(final String elementSelector, final String valueExpression, ContextMenu contextMenu) {
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
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(SelectableElementsBehavior.class, SelectableElementsBehavior.class.getSimpleName().toString() + ".js")));

		final CallbackParameter[] parameters = new CallbackParameter[] {
			CallbackParameter.explicit("interaction"), CallbackParameter.explicit("selectedValues"),
		};

		final StringBuilder builder = new StringBuilder();
		builder.append("\t$('#").append(component.getMarkupId()).append("').selectableElements({\n");
		builder.append("\t\telementSelector: '" + elementSelector + "',\n");
		builder.append("\t\tvalueExtractor: function(element) {return " + valueExpression + ";},\n");
		builder.append("\t\tajaxCallback: ").append(getCallbackFunction(parameters).toString().replace("\n", " ")).append(",");
		builder.append("\t\tnotSelectedStyle: {'background-color': ''},\n");
		builder.append("\t\tselectedStyle: {'background-color': '#f00'}\n");
		builder.append("\t})");

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
			String encodedSelectedValues = parameters.getParameterValue("selectedValues").toString();
			if (encodedSelectedValues == null) {
				encodedSelectedValues = "[]";
			}
			final Object selectedValues = JSONValue.parse(encodedSelectedValues);
			if (selectedValues instanceof List) {
				onInteraction(target, interaction, (List<?>)selectedValues);
			}
		}
	}

	/**
	 * This method is invoked when the client-side scripts notify the server about
	 * a user interaction.
	 * @param target the ART
	 * @param interaction a string describing the interaction, e.g. "dblclick"
	 * for a double-click
	 */
	protected abstract void onInteraction(AjaxRequestTarget target, String interaction, List<?> selectedValues);

}