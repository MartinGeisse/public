/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import name.martingeisse.common.terms.IGetDisplayNameAware;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * This class is a choice renderer for enums that can provide a display name.
 * It uses the display name for visualization and the enum name for identification.
 * @param <T> the value type being rendered
 */
public class DisplayNameEnumChoiceRenderer<T extends Enum<T> & IGetDisplayNameAware> implements IChoiceRenderer<T> {

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	@Override
	public Object getDisplayValue(T object) {
		return object.getDisplayName();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
	 */
	@Override
	public String getIdValue(T object, int index) {
		return object.name();
	}

}
