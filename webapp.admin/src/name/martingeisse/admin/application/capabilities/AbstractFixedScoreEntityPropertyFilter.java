/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.schema.EntityPropertyDescriptor;

/**
 * Base implementation for {@link IRawEntityListPropertyDisplayFilter}. This
 * implementation acts as a plugin to add itself to the application
 * capabilities. In addition, it keeps a fixed score value that it returns
 * for all entities and properties.
 */
public abstract class AbstractFixedScoreEntityPropertyFilter extends AbstractEntityPropertyFilter {

	/**
	 * the score
	 */
	private int score;

	/**
	 * Constructor.
	 */
	public AbstractFixedScoreEntityPropertyFilter() {
	}

	/**
	 * Constructor.
	 * @param score the score
	 */
	public AbstractFixedScoreEntityPropertyFilter(final int score) {
		this.score = score;
	}

	/**
	 * Getter method for the score.
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Setter method for the score.
	 * @param score the score to set
	 */
	public void setScore(final int score) {
		this.score = score;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityPropertyDisplayFilter#getScore(name.martingeisse.admin.schema.EntityDescriptor, name.martingeisse.admin.schema.EntityPropertyDescriptor)
	 */
	@Override
	public int getScore(EntityDescriptor entityDescriptor, EntityPropertyDescriptor propertyDescriptor) {
		return score;
	}

}
