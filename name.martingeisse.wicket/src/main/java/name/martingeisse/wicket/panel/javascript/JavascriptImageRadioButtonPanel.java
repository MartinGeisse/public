/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.javascript;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * This panel shows a Javascript-controlled radiobutton
 * whose selected/deselected state is visualized by two different
 * images. The button belongs to a corresponding group of which
 * at most one button can be enabled at any time. Currently,
 * clicking on the selected button de-selects it such that
 * subsequently no button is selected.
 * 
 * Selecting a button from the group occurs entirely on
 * the client side, without server interaction, and is
 * implemented in Javascript. The button group defines a
 * callback that is invoked when the user (de-)selects a
 * button.
 */
public class JavascriptImageRadioButtonPanel extends Panel {

	/**
	 * the group
	 */
	private final JavascriptImageRadioButtonPanelGroup group;

	/**
	 * the userDataExpression
	 */
	private final String userDataExpression;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param group the radio button group this button panel belongs to
	 * @param userDataExpression a Javascript expression source text to use for the userData field (passed to the group callback)
	 */
	public JavascriptImageRadioButtonPanel(final String id, final JavascriptImageRadioButtonPanelGroup group, final String userDataExpression) {
		this(id, group, userDataExpression, null);
	}
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param group the radio button group this button panel belongs to
	 * @param userDataExpression a Javascript expression source text to use for the userData field (passed to the group callback)
	 * @param tooltip optional tooltip text
	 */
	public JavascriptImageRadioButtonPanel(final String id, final JavascriptImageRadioButtonPanelGroup group, final String userDataExpression, String tooltip) {
		super(id);
		this.group = group;
		this.userDataExpression = userDataExpression;
		group.getMembers().add(this);
		setOutputMarkupId(true);
		
		WebMarkupContainer link = new WebMarkupContainer("link");
		add(link);
		
		Image image = new Image("image", new PropertyModel<ResourceReference>(group, "deselectedIconResourceReference"));
		link.add(image);
		
		if (tooltip != null) {
			image.add(new AttributeModifier("title", Model.of(tooltip)));
		}
	}

	/**
	 * Getter method for the group.
	 * @return the group
	 */
	public JavascriptImageRadioButtonPanelGroup getGroup() {
		return group;
	}

	/**
	 * Getter method for the userDataExpression.
	 * @return the userDataExpression
	 */
	public String getUserDataExpression() {
		return userDataExpression;
	}

}
