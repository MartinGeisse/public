/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.javascript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * A button group for {@link JavascriptImageRadioButtonPanel} objects.
 * 
 * Note that this class is a bit cumbersome in how it gets initialized. If a radio
 * button panel group is put into a page when rendered in a non-AJAX context,
 * the FullJavascriptCodeBehavior nested class can be used. If, however, there
 * is no group originally present on a page and the first group is put into the
 * page in an AJAX context, then StaticJavascriptCodeBehavior must be used
 * when the page is originally rendered in a non-AJAX way and renderInstanceJavascript()
 * be used directly on the header response from the AjaxRequestTarget.
 */
public class JavascriptImageRadioButtonPanelGroup implements Serializable {

	/**
	 * the members
	 */
	private final List<JavascriptImageRadioButtonPanel> members;

	/**
	 * the deselectedIconResourceReference
	 */
	private ResourceReference deselectedIconResourceReference;

	/**
	 * the selectedIconResourceReference
	 */
	private ResourceReference selectedIconResourceReference;

	/**
	 * the callbackFunctionName
	 */
	private String callbackFunctionName;

	/**
	 * the noSelectionUserDataExpression
	 */
	private String noSelectionUserDataExpression;

	/**
	 * Constructor.
	 */
	public JavascriptImageRadioButtonPanelGroup() {
		this.members = new ArrayList<JavascriptImageRadioButtonPanel>();
	}

	/**
	 * Getter method for the members.
	 * @return the members
	 */
	List<JavascriptImageRadioButtonPanel> getMembers() {
		return members;
	}

	/**
	 * Getter method for the callbackFunctionName.
	 * @return the callbackFunctionName
	 */
	public String getCallbackFunctionName() {
		return callbackFunctionName;
	}

	/**
	 * Setter method for the callbackFunctionName.
	 * @param callbackFunctionName the callbackFunctionName to set
	 */
	public void setCallbackFunctionName(final String callbackFunctionName) {
		this.callbackFunctionName = callbackFunctionName;
	}

	/**
	 * Getter method for the noSelectionUserDataExpression.
	 * @return the noSelectionUserDataExpression
	 */
	public String getNoSelectionUserDataExpression() {
		return noSelectionUserDataExpression;
	}

	/**
	 * Setter method for the noSelectionUserDataExpression.
	 * @param noSelectionUserDataExpression the noSelectionUserDataExpression to set
	 */
	public void setNoSelectionUserDataExpression(final String noSelectionUserDataExpression) {
		this.noSelectionUserDataExpression = noSelectionUserDataExpression;
	}

	/**
	 * Getter method for the deselectedIconResourceReference.
	 * @return the deselectedIconResourceReference
	 */
	public ResourceReference getDeselectedIconResourceReference() {
		return deselectedIconResourceReference;
	}

	/**
	 * Setter method for the deselectedIconResourceReference.
	 * @param deselectedIconResourceReference the deselectedIconResourceReference to set
	 */
	public void setDeselectedIconResourceReference(final ResourceReference deselectedIconResourceReference) {
		this.deselectedIconResourceReference = deselectedIconResourceReference;
	}

	/**
	 * Getter method for the selectedIconResourceReference.
	 * @return the selectedIconResourceReference
	 */
	public ResourceReference getSelectedIconResourceReference() {
		return selectedIconResourceReference;
	}

	/**
	 * Setter method for the selectedIconResourceReference.
	 * @param selectedIconResourceReference the selectedIconResourceReference to set
	 */
	public void setSelectedIconResourceReference(final ResourceReference selectedIconResourceReference) {
		this.selectedIconResourceReference = selectedIconResourceReference;
	}

	/**
	 * Renders a reference to the static (non-instance) JS code for the radio button functionality.
	 * 
	 * This method cannot be used outside header rendering for an AjaxRequestTarget. Users normally
	 * don't call this method directly but use StaticJavascriptCodeBehavior or
	 * FullJavascriptCodeBehavior instead. 
	 * 
	 * @param response the header response
	 */
	public static void renderStaticJavascript(final IHeaderResponse response) {
		final Class<?> c = JavascriptImageRadioButtonPanelGroup.class;
		response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(c, c.getSimpleName() + ".js"), c.getCanonicalName()));
	}

	/**
	 * Renders a reference to the instance JS code for the radio button functionality. For groups that are
	 * not added via AJAX but already present in the non-AJAX part of a page, this method is invoked automatically
	 * by FullJavascriptCodeBehavior.
	 * 
	 * Invoke this method manually for groups that are added via AJAX.
	 * 
	 * This method may be called outside header rendering for the specified response even for AjaxRequestTarget.
	 * 
	 * @param response the header response
	 */
	public void renderInstanceJavascript(final IHeaderResponse response) {
		final StringBuilder builder = new StringBuilder();
		builder.append("initializeJavascriptImageRadioButtonPanelGroup('");
		builder.append(RequestCycle.get().mapUrlFor(deselectedIconResourceReference, null));
		builder.append("', '");
		builder.append(RequestCycle.get().mapUrlFor(selectedIconResourceReference, null));
		builder.append("', ");
		builder.append(callbackFunctionName);
		builder.append(", ");
		builder.append(noSelectionUserDataExpression);
		builder.append(", [");
		boolean first = true;
		for (final JavascriptImageRadioButtonPanel member : members) {
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append("{selector: '#");
			builder.append(member.getMarkupId());
			builder.append("', userData: ");
			builder.append(member.getUserDataExpression());
			builder.append("}");
		}
		builder.append("]);");
		response.render(OnDomReadyHeaderItem.forScript(builder.toString()));
	}

	/**
	 * This behavior provides only the static (non-instance) JS code in its header contribution.
	 * Use this for pages that do not initially contain any groups but load groups via AJAX,
	 * so the code is there when the groups arrive.
	 */
	public static class StaticJavascriptCodeBehavior extends Behavior {
		@Override
		public void renderHead(final Component component, final IHeaderResponse response) {
			super.renderHead(component, response);
			renderStaticJavascript(response);
		}
	}

	/**
	 * This behavior provides all (static and instance) JS code in its header contribution.
	 * Use this -- for example on some surrounding DIV -- to initialize groups that are present
	 * in the non-AJAX part of a page.
	 */
	public class FullJavascriptCodeBehavior extends Behavior {
		@Override
		public void renderHead(final Component component, final IHeaderResponse response) {
			super.renderHead(component, response);
			renderStaticJavascript(response);
			renderInstanceJavascript(response);
		}
	}

	/**
	 * Convenience method since the syntax for creating inner class instances is so ugly.
	 * @return the inner class instance
	 */
	public FullJavascriptCodeBehavior createFullJavascriptCodeBehavior() {
		return new FullJavascriptCodeBehavior();
	}

}
