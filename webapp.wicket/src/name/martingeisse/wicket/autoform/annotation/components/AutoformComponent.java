/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.components;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.martingeisse.wicket.autoform.annotation.structure.AutoformReadOnly;
import name.martingeisse.wicket.panel.simple.LabelPanel;
import name.martingeisse.wicket.panel.simple.TextFieldPanel;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

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
 * If no {@link AutoformComponentConstructorArgumentName} is present for the property,
 * then the component constructor is expected to have two arguments of type {@link String}
 * and {@link IModel} that take the wicket id and the model for the property, respectively.
 * If {@link AutoformComponentConstructorArgumentName} is present, then the corresponding
 * annotation instance is passed as an additional constructor argument and the constructor
 * is expected to accept either the corresponding annotation interface or {@link Annotation}
 * as its third parameter type.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformComponent {

	/**
	 * @return the component class to use
	 */
	public Class<? extends Component> value();
	
}
