/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.readonly;

import org.apache.wicket.markup.html.basic.Label;

/**
 * Renders boolean values as checkboxes. The checkboxes are disabled
 * to make the rendering read-only.
 * 
 * A shared instance is provided, though separate instance may be used.
 */
public class BooleanRenderer implements IPropertyReadOnlyRenderer {

	/**
	 * The shared instance of this class.
	 */
	public static final BooleanRenderer INSTANCE = new BooleanRenderer();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer#createLabel(java.lang.String, java.lang.Object)
	 */
	@Override
	public Label createLabel(String id, Object rawValue) {
		
		// determine the boolean value
		Boolean value = getValue(rawValue);
		if (value == null) {
			return null;
		}
		
		// create the HTML for the checkbox
		String code;
		if (value) {
			code = "<input type=\"checkbox\" disabled=\"disabled\" checked=\"checked\" />";
		} else {
			code = "<input type=\"checkbox\" disabled=\"disabled\" />";
		}
		
		// create the label
		Label label = new Label(id, code);
		label.setEscapeModelStrings(false);
		return label;
		
	}
	
	/**
	 * 
	 */
	private Boolean getValue(Object o) {
		if (o instanceof Boolean) {
			return (Boolean)o;
		} else if (o instanceof Integer) {
			return ((Integer)o != 0);
		} else {
			return null;
		}
	}

}