/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.martingeisse.guiserver.xml.value.ValueParser;

/**
 * This annotation can be used to bind an attribute to a method that takes
 * the parsed type of the attribute as its parameter type, such as a
 * setter method.
 * 
 * The value parser that parses the attribute value can be specified
 * explicitly. If not specified (or if specified to the default,
 * {@link ValueParser}.class), the {@link #type()} property of this
 * annotation (if present) or the type of the constructor parameter is
 * used to infer the parser. 
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindPropertyAttribute {

	/**
	 * @return the attribute name
	 */
	public String name();
	
	/**
	 * @return whether the attribute is mandatory or optional and whether it has
	 * a default value
	 */
	public AttributeValueBindingOptionality optionality() default AttributeValueBindingOptionality.MANDATORY;
	
	/**
	 * @return the default value (only used if the optionality is
	 * OPTIONAL_WITH_DEFAULT).
	 */
	public String defaultValue() default "";

	/**
	 * Allows to specify the parsed type directly, overriding the parameter type of
	 * the method. The parser associated with the specified type must still be the
	 * parameter type or a subtype, otherwise invoking the method will fail.
	 * 
	 * The default void.class causes the type to be inferred from the method
	 * signature.
	 */
	public Class<?> type() default void.class;

	/**
	 * Allows to specify the value parser directly. The specified parser
	 * class must have a no-arg constructor to allow creating an instance of it,
	 * and that instance will be used as the parser for the value of the attribute.
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends ValueParser> valueParserClass() default ValueParser.class;
	
}
