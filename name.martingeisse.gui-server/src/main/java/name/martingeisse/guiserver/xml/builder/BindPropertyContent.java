/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * This annotation can be used to bind element content to a method that
 * takes the parsed type of the element content as its parameter type, such as
 * a setter method.
 * 
 * The element parser that parses the element can be specified explicitly.
 * If not specified (or if specified to the default, {@link ContentParser}.class),
 * the {@link #type()} property of this annotation is used to infer the parser. 
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindPropertyContent {

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
	 * Allows to specify the element parser directly. The specified parser
	 * class must have a no-arg constructor to allow creating an instance of it,
	 * and that instance will be used as the parser for the element.
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends ContentParser> contentParserClass() default ContentParser.class;

}
