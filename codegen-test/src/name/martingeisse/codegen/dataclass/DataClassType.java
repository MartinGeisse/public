/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.codegen.dataclass;

/**
 * Specifies one of the types of a data-holding class.
 */
public enum DataClassType {

	/**
	 * Base class that provides only getter methods.
	 */
	READABLE("Readable"),
	
	/**
	 * Base class for immutable types, still allows
	 * additional fields to be added in subclasses.
	 */
	IMMUTABLE_BASE("Base"),
	
	/**
	 * Final immutable class (no additional fields). This type
	 * can be passed around without defensive copies.
	 */
	IMMUTABLE(""),
	
	/**
	 * Mutable implementation; non-final since mutable objects
	 * carry implicit hidden state anyway by way of the objects
	 * that hold a reference to them.
	 */
	MUTABLE("Mutable");
	
	/**
	 * the classPrefix
	 */
	private final String classPrefix;

	/**
	 * Constructor.
	 * @param classPrefix the prefix for the generated class name
	 */
	private DataClassType(String classPrefix) {
		this.classPrefix = classPrefix;
	}
	
	/**
	 * Getter method for the classPrefix.
	 * @return the classPrefix
	 */
	public String getClassPrefix() {
		return classPrefix;
	}

	/**
	 * @return an empty string, "final " or "abstract ", depending
	 * on the extensibility of the class.
	 */
	public String getClassExtensibility() {
		switch (this) {
		case READABLE:
			return "abstract ";
		case IMMUTABLE_BASE:
			return "abstract ";
		case IMMUTABLE:
			return "final ";
		case MUTABLE:
			return "";
		default:
			return "";
		}
	}
	
	/**
	 * @return true if the class has fields, false if not
	 */
	public boolean hasFields() {
		return (this == IMMUTABLE_BASE || this == MUTABLE);
	}
	
	/**
	 * @return true if the class has final fields, false if not
	 */
	public boolean hasFinalFields() {
		return (this == IMMUTABLE_BASE);
	}
	
	/**
	 * @return true if the class has a field-initializing constructor,
	 * false if not
	 */
	public boolean hasInitializingConstructor() {
		return hasFields();
	}
	
	/**
	 * @return true if the class has a super-calling constructor,
	 * false if not
	 */
	public boolean hasDelegatingConstructor() {
		return (this == IMMUTABLE);
	}
	
	/**
	 * @return true if the class has abstract getter methods, false if not
	 */
	public boolean hasAbstractGetters() {
		return (this == READABLE);
	}
	
	/**
	 * @return true if the class has concrete getter methods, false if not
	 */
	public boolean hasConcreteGetters() {
		return (this == IMMUTABLE_BASE || this == MUTABLE);
	}
	
	/**
	 * @return true if the class has setter methods, false if not
	 */
	public boolean hasSetters() {
		return (this == MUTABLE);
	}
	
	/**
	 * Returns the DataClassType for the superclass, or null
	 * if the superclass is none of the known types.
	 * 
	 * @return the DataClassType for the superclass or null
	 */
	public DataClassType getSuperclassType() {
		switch (this) {
		case READABLE:
			return null;
		case IMMUTABLE_BASE:
			return READABLE;
		case IMMUTABLE:
			return IMMUTABLE_BASE;
		case MUTABLE:
			return READABLE;
		default:
			return null;
		}
	}
	
}
