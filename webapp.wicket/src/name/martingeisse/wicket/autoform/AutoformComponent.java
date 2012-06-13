/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.martingeisse.wicket.panel.simple.LabelPanel;
import name.martingeisse.wicket.panel.simple.TextFieldPanel;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * This annotation specifies the Wicket component to use for an
 * autoform property. The component must be a Wicket Panel (or subclass)
 * instance since property components must bring their own markup. The
 * component must have either a three-argument constructor that takes
 * the Wicket id, the IModel for the property and the IModel for the bean
 * being edited, or a two-argument constructor that takes the
 * Wicket id and the IModel for the property. An additional String-typed
 * argument for the constructor can be specified with {@link AutoformComponentAdditionalConstructorArgument}.
 * 
 * For example, this annotation can be used to specify a {@link LabelPanel}.
 * Together with {@link AutoformReadOnly}, a {@link TextFieldPanel} can be used too.
 * Either one uses toString() to turn the object to a string (using the empty string
 * for null values) and shows it in a label or read-only text field.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformComponent {

	/**
	 * @return the component class to use
	 */
	public Class<? extends Panel> value();
	
}
