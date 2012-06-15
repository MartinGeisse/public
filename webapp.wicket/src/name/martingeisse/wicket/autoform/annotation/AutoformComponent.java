/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.martingeisse.wicket.panel.simple.LabelPanel;
import name.martingeisse.wicket.panel.simple.TextFieldPanel;

import org.apache.wicket.Component;

/**
 * This annotation specifies the Wicket component to use for an autoform property.
 * The component is attached to an empty DIV element and is usually a panel or
 * fragment, though any component that can be attached to such a tag is in principle
 * possible.
 * 
 * For example, this annotation can be used to specify a {@link LabelPanel} (or a
 * {@link TextFieldPanel} with {@link AutoformReadOnly}) to visualize arbitrary
 * bean properties as text, using toString(). Another example is to enforce a
 * text area instead of a text field.
 * 
 * The component must have either a three-argument constructor that takes the Wicket
 * id, the IModel for the property and the IModel for the bean being edited, or a
 * two-argument constructor that takes the Wicket id and the IModel for the property.
 * If {@link ConstructorArgumentName} is present, then the corresponding annotation
 * instance is passed as an additional argument and a corresponding four-argument
 * or three-argument constructor is expected instead that takes either the type
 * of the annotation or {@link Annotation} as an additional parameter type.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformComponent {

	/**
	 * @return the component class to use
	 */
	public Class<? extends Component> value();
	
}
