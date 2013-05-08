/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.ui;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * A label whose contents can be extended via push-rendering.
 * 
 * Usage: Create like a normal label, using an {@link IModel}
 * that links to a mutable variable. Whenever it is known that
 * the model value has changed but only in that characters have
 * been appended, call {@link #onModelAppend(AjaxRequestTarget)}
 * instead of adding this label to the {@link AjaxRequestTarget}.
 * (Note that the output will be incorrect if other changes
 * to the model value have occurred).
 * 
 * This label also allows actions to be triggered on appending
 * by subclassing this class:
 * - scroll an outer element to the bottom, both after appending
 *   and initially
 *
 * Note that this class requires that only a single rendering of
 * the label exists. If the label is rendered multiple times
 * (e.g. to different browser tabs), and they all get incremental
 * updates, then they will reflect the wrong value since each
 * incremental update is generated only once.
 * 
 * Currently only works for string-typed models.
 */
public class PushAppendLabel extends Label {

	/**
	 * the lastDeltaUpdatePosition
	 */
	private long lastDeltaUpdatePosition;
	
	/**
	 * the scrollContainerQueryExpression
	 */
	private String scrollContainerQueryExpression;

	/**
	 * Constructor.
	 * 
	 * If an enclosing container element shall be scrolled to the
	 * bottom on appending, pass a Javascript expression for a
	 * jQuery query-object for that container based on the
	 * query-object for this label itself, which is stored in
	 * a variable whose name is marked by %s. For example, if the
	 * scroll container is a DIV that encloses the label, pass
	 * "%s.parent()".
	 * 
	 * @param id the wicket id
	 * @param model the model
	 * @param scrollContainerQueryExpression the expression for
	 * the scroll container query, or null to skip auto-scrolling
	 */
	public PushAppendLabel(String id, IModel<?> model, String scrollContainerQueryExpression) {
		super(id, model);
		setOutputMarkupId(true);
		this.lastDeltaUpdatePosition = 0;
		this.scrollContainerQueryExpression = scrollContainerQueryExpression;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		StringBuilder builder = new StringBuilder();
		builder.append("var $label = $('#").append(getMarkupId()).append("');\n");
		String scrollCommand = getScrollCommand("$label");
		builder.append(scrollCommand);
		builder.append("window.appendToPushAppendLabel = function(labelMarkupId, delta) {\n");
		builder.append("	var $label = $('#' + labelMarkupId);\n");
		builder.append("	$label.append($('<div />').text(delta).html());\n");
		builder.append("	").append(scrollCommand);
		builder.append("};\n");
		response.render(JavaScriptHeaderItem.forScript(builder.toString(), null));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onAfterRender()
	 */
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		lastDeltaUpdatePosition = ((String)getDefaultModelObject()).length();
	}
	
	/**
	 * Use this method to render an incremental change
	 * for appended contents to the specified target.
	 * 
	 * @param target the target to render to
	 */
	public void onModelAppend(AjaxRequestTarget target) {
		String modelValue = (String)getDefaultModelObject();
		if (lastDeltaUpdatePosition < modelValue.length()) {
			String delta = modelValue.substring((int)lastDeltaUpdatePosition);
			String escapedDelta = JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(delta);
			target.appendJavaScript("appendToPushAppendLabel('" + getMarkupId() + "', '" + escapedDelta + "');");
		}
		lastDeltaUpdatePosition = modelValue.length();
	}

	/**
	 * 
	 */
	private String getScrollCommand(String labelQueryVariable) {
		if (scrollContainerQueryExpression == null) {
			return "\n";
		} else {
			String containerQueryExpression = String.format(scrollContainerQueryExpression, labelQueryVariable);
			return "(" + containerQueryExpression + ").scrollTop(" + labelQueryVariable + ".height());\n";
		}
	}
	
}
