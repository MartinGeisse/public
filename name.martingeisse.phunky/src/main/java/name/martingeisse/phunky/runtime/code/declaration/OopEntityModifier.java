/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.declaration;

/**
 * The various modifiers that can be used for classes, interfaces, fields, and methods.
 * 
 * Note that "var" is not treated as a modifier, but rather as a syntactic keyword
 * that implies default modifiers.
 */
public enum OopEntityModifier {

	/**
	 * The "public" keyword.
	 */
	PUBLIC,

	/**
	 * The "protected" keyword.
	 */
	PROTECTED,

	/**
	 * The "private" keyword.
	 */
	PRIVATE,

	/**
	 * The "static" keyword.
	 */
	STATIC,
	
	/**
	 * The "abstract" keyword.
	 */
	ABSTRACT,

	/**
	 * The "final" keyword.
	 */
	FINAL;

}
