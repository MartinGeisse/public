/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.attribute;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.martingeisse.guiserver.xmlbind.element.BindElement;
import name.martingeisse.guiserver.xmlbind.value.TextValueBinding;

/**
 * This annotation can be used to bind an attribute to a constructor parameter.
 * It should be used inside the 'attributes' section of a {@link BindElement}.
 * 
 * The text-to-value binding that parses the attribute value can be specified
 * explicitly. If not specified (or if specified to the default,
 * {@link TextValueBinding}.class), the type of the constructor parameter is
 * used to infer the binding. 
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindAttribute {

	/**
	 * @return the attribute name
	 */
	public String name();
	
	/**
	 * @return whether the attribute is mandatory or optional and whether it has
	 * a default value
	 */
	public AttributeValueBindingOptionality optionality();
	
	/**
	 * @return the default value (only used if the optionality is
	 * OPTIONAL_WITH_DEFAULT).
	 */
	public String defaultValue() default "";

	/**
	 * Allows to specify the text-to-value binding directly. The specified binding
	 * class must have a no-arg constructor to allow creating an instance of it,
	 * and that instance will be used as the binding for the value of this attribute.
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends TextValueBinding> textValueBindingClass() default TextValueBinding.class;
	
}
