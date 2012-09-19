/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.javascript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * A button group for {@link JavascriptSpriteRadioButtonPanel} objects.
 * 
 * Note that this class is a bit cumbersome in how it gets initialized. If a radio
 * button panel group is put into a page when rendered in a non-AJAX context,
 * the FullJavascriptCodeBehavior nested class can be used. If, however, there
 * is no group originally present on a page and the first group is put into the
 * page in an AJAX context, then StaticJavascriptCodeBehavior must be used
 * when the page is originally rendered in a non-AJAX way and renderInstanceJavascript()
 * be used directly on the header response from the AjaxRequestTarget.
 */
public class JavascriptSpriteRadioButtonPanelGroup implements Serializable {

	/**
	 * the members
	 */
	private final List<JavascriptSpriteRadioButtonPanel> members;

	/**
	 * the deselectedSpriteData
	 */
	private SpriteData deselectedSpriteData;

	/**
	 * the selectedSpriteData
	 */
	private SpriteData selectedSpriteData;

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
	public JavascriptSpriteRadioButtonPanelGroup() {
		this.members = new ArrayList<JavascriptSpriteRadioButtonPanel>();
	}

	/**
	 * Getter method for the members.
	 * @return the members
	 */
	List<JavascriptSpriteRadioButtonPanel> getMembers() {
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
	 * Getter method for the deselectedSpriteData.
	 * @return the deselectedSpriteData
	 */
	public SpriteData getDeselectedSpriteData() {
		return deselectedSpriteData;
	}

	/**
	 * Setter method for the deselectedSpriteData.
	 * @param deselectedSpriteData the deselectedSpriteData to set
	 */
	public void setDeselectedSpriteData(final SpriteData deselectedSpriteData) {
		this.deselectedSpriteData = deselectedSpriteData;
	}

	/**
	 * Getter method for the selectedSpriteData.
	 * @return the selectedSpriteData
	 */
	public SpriteData getSelectedSpriteData() {
		return selectedSpriteData;
	}

	/**
	 * Setter method for the selectedSpriteData.
	 * @param selectedSpriteData the selectedSpriteData to set
	 */
	public void setSelectedSpriteData(final SpriteData selectedSpriteData) {
		this.selectedSpriteData = selectedSpriteData;
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
		final Class<?> c = JavascriptSpriteRadioButtonPanelGroup.class;
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
		builder.append("initializeJavascriptSpriteRadioButtonPanelGroup(");
		renderSpriteData(builder, deselectedSpriteData);
		builder.append(", ");
		renderSpriteData(builder, selectedSpriteData);
		builder.append(", ");
		builder.append(callbackFunctionName);
		builder.append(", ");
		builder.append(noSelectionUserDataExpression);
		builder.append(", [");
		boolean first = true;
		for (final JavascriptSpriteRadioButtonPanel member : members) {
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
	 * @param builder
	 * @param spriteData
	 */
	private void renderSpriteData(final StringBuilder builder, final SpriteData spriteData) {
		builder.append("{url: ");
		JavascriptAssemblerUtil.appendStringLiteral(builder, spriteData.getUrl());
		builder.append(", x: ").append(spriteData.getX());
		builder.append(", y: ").append(spriteData.getY());
		builder.append(", width: ").append(spriteData.getWidth());
		builder.append(", height: ").append(spriteData.getHeight());
		builder.append("}");
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

	/**
	 * This class describes the properties of a CSS sprite. Two instances are used to
	 * describe the deselected and selected sprites.
	 */
	public static class SpriteData implements Serializable {

		/**
		 * the url
		 */
		private String url;

		/**
		 * the x
		 */
		private int x;

		/**
		 * the y
		 */
		private int y;

		/**
		 * the width
		 */
		private int width;

		/**
		 * the height
		 */
		private int height;

		/**
		 * Constructor.
		 */
		public SpriteData() {
		}

		/**
		 * Constructor.
		 * @param url the URL of the sprite palette
		 * @param x the x position of the sprite in the palette
		 * @param y the y position of the sprite in the palette
		 * @param width the width of the sprite
		 * @param height the height of the sprite
		 */
		public SpriteData(final String url, final int x, final int y, final int width, final int height) {
			this.url = url;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		/**
		 * Getter method for the url.
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * Setter method for the url.
		 * @param url the url to set
		 */
		public void setUrl(final String url) {
			this.url = url;
		}

		/**
		 * Getter method for the x.
		 * @return the x
		 */
		public int getX() {
			return x;
		}

		/**
		 * Setter method for the x.
		 * @param x the x to set
		 */
		public void setX(final int x) {
			this.x = x;
		}

		/**
		 * Getter method for the y.
		 * @return the y
		 */
		public int getY() {
			return y;
		}

		/**
		 * Setter method for the y.
		 * @param y the y to set
		 */
		public void setY(final int y) {
			this.y = y;
		}

		/**
		 * Getter method for the width.
		 * @return the width
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * Setter method for the width.
		 * @param width the width to set
		 */
		public void setWidth(final int width) {
			this.width = width;
		}

		/**
		 * Getter method for the height.
		 * @return the height
		 */
		public int getHeight() {
			return height;
		}

		/**
		 * Setter method for the height.
		 * @param height the height to set
		 */
		public void setHeight(final int height) {
			this.height = height;
		}

	}

}
