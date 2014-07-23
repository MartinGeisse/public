/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;

/**
 * This behavior expects the model object to be a JSON string.
 * It applies JSON formatting (line breaks, indentation, whitespace)
 * before rendering and after submitting. This is useful when
 * editing JSON data directly in a text field or CodeMirror.
 */
public class JsonFormattingBehavior extends Behavior {

	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#beforeRender(org.apache.wicket.Component)
	 */
	@Override
	public void beforeRender(Component component) {
		super.beforeRender(component);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#onEvent(org.apache.wicket.Component, org.apache.wicket.event.IEvent)
	 */
	@Override
	public void onEvent(Component component, IEvent<?> event) {
		super.onEvent(component, event);
	}
	
}
