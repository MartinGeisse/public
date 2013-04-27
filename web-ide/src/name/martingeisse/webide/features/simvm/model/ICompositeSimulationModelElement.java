/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import java.util.List;

/**
 * This interface is used by model elements that keep a list of
 * sub-elements.
 * 
 * @param <S> the sub-element type
 */
public interface ICompositeSimulationModelElement<S extends ISimulationModelElement> extends ISimulationModelElement {

	/**
	 * Getter method for the subElements.
	 * @return the subElements
	 */
	public List<S> getSubElements();

}
