/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.glworker;


/**
 * Specialization of {@link FixedSubjectsWorkUnits} for enum constants.
 * 
 * @param <T> the enum type
 */
public abstract class EnumWorkUnits<T extends Enum<T>> extends FixedSubjectsWorkUnits<T> {

	/**
	 * Constructor.
	 * @param enumType the enum type
	 */
	public EnumWorkUnits(Class<T> enumType) {
		super(enumType.getEnumConstants());
	}
	
}
