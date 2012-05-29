/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.readonly;

import org.apache.wicket.markup.html.basic.Label;

/**
 * This interface allows to render a property of an entity
 * in a read-only way. 
 */
public interface IPropertyReadOnlyRenderer {

	/**
	 * Creates a Wicket label for the specified value. Returns null if this renderer is not
	 * able to render the specified value, allowing to chain renderers.
	 * 
	 * @param id the Wicket id of the label
	 * @param value the value to render
	 * @return the label
	 */
	public Label createLabel(String id, Object value);
	
}
