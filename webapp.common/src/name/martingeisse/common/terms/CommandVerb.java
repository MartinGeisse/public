/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.terms;

import java.io.Serializable;

import name.martingeisse.common.util.ParameterUtil;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Instances of this class each represent a verb from a command phrase,
 * like "run" or the word "open" in "open readme.txt", with the following
 * properties:
 * 
 * - the verb itself does not contain arguments (like "readme.txt" above)
 * - the verb itself does not specify its meaning, e.g. does not specify
 *   what "run" actually means. This is interpreted by the code that
 *   receives a verb
 * - verbs are compared with equals(), not by object identity.
 * 
 * 
 * Command verbs are specified by a class object (to avoid collision) and
 * a name (string; to allow multiple verb definitions per class).
 */
public final class CommandVerb implements Serializable {

	/**
	 * the definingClass
	 */
	private final Class<?> definingClass;

	/**
	 * the name
	 */
	private final String name;

	/**
	 * Constructor.
	 * @param definingClass the class that defines this command verb (to avoid collision)
	 * @param name the name of this verb (to support multiple verbs per defining class)
	 */
	public CommandVerb(Class<?> definingClass, String name) {
		this.definingClass = ParameterUtil.ensureNotNull(definingClass, "definingClass") ;
		this.name = ParameterUtil.ensureNotNull(name, "name");
	}

	/**
	 * Getter method for the definingClass.
	 * @return the definingClass
	 */
	public Class<?> getDefiningClass() {
		return definingClass;
	}
	
	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a canonical identifier, built as the canonical class name and the
	 * verb name, separated by a colon character. Two command verbs are equal if
	 * and only if their canonical identifiers are (a crucial point being that
	 * the class name cannot contain a colon character, so the first colon
	 * always the separates defining class and the verb name).
	 * 
	 * @return the canonical identifier
	 */
	public String getCanonicalIdentifier() {
		return definingClass.getCanonicalName() + ':' + name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object untypedOther) {
		if (untypedOther instanceof CommandVerb) {
			CommandVerb other = (CommandVerb)untypedOther;
			return (definingClass.equals(other.definingClass) && name.equals(other.name));
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(definingClass).append(name).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{CommandVerb " + definingClass.getCanonicalName() + ": " + name + "}";
	}
	
	/**
	 * Builds a command verb from its canonical identifier. This method throws an
	 * {@link IllegalArgumentException} if the identifier is malformed.
	 * 
	 * @param canonicalIdentifier the canonical identifier of the command verb
	 * @return the command verb
	 */
	public static CommandVerb fromCanonicalIdentifier(String canonicalIdentifier) {
		int index = canonicalIdentifier.indexOf(':');
		if (index == -1) {
			throw new IllegalArgumentException("malformed canonical command verb identifier: " + canonicalIdentifier);
		}
		String definingClassName = canonicalIdentifier.substring(0, index);
		String verbName = canonicalIdentifier.substring(index + 1);
		Class<?> definingClass;
		try {
			definingClass = Class.forName(definingClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("unknown defining class in command verb identifier: " + canonicalIdentifier);
		}
		return new CommandVerb(definingClass, verbName);
	}

	/**
	 * Builds a command verb from its canonical identifier. This method returns null
	 * if the identifier is malformed.
	 * 
	 * @param canonicalIdentifier the canonical identifier of the command verb
	 * @return the command verb or null
	 */
	public static CommandVerb fromCanonicalIdentifierSafe(String canonicalIdentifier) {
		try {
			return fromCanonicalIdentifier(canonicalIdentifier);
		} catch (Exception e) {
			return null;
		}
	}
	
}
