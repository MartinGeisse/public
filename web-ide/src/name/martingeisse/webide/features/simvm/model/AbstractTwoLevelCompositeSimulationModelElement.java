/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a specialized composite simulation model element
 * that gives certain sub-elements the privilege of executing
 * more simulation steps than other elements in a given time
 * frame.
 * 
 * More precisely, for each simulation step in this element,
 * each privileged sub-element executes a step. Every N steps,
 * the non-privileged sub-elements also execute a step, where N
 * is a privilege factor specified at construction time.
 * 
 * The privileged sub-elements should also be in the normal list
 * of sub-elements to participate in event handling, state
 * persistence etc.
 * 
 * @param <S> the sub-element type
 */
public abstract class AbstractTwoLevelCompositeSimulationModelElement<S extends ISimulationModelElement> extends AbstractCompositeSimulationModelElement<S> {

	/**
	 * the factor
	 */
	private final int factor;
	
	/**
	 * the privilegedSubElements
	 */
	private final List<S> privilegedSubElements;
	
	/**
	 * the remainingPrivilegedSteps
	 */
	private int remainingPrivilegedSteps;
	
	/**
	 * Constructor.
	 * @param factor the privilege factor
	 */
	public AbstractTwoLevelCompositeSimulationModelElement(int factor) {
		if (factor < 1) {
			throw new IllegalArgumentException("privilege factor must be at least 1");
		}
		this.factor = factor;
		this.privilegedSubElements = new ArrayList<S>();
		this.remainingPrivilegedSteps = factor;
	}
	
	/**
	 * Getter method for the factor.
	 * @return the factor
	 */
	public final int getFactor() {
		return factor;
	}
	
	/**
	 * Getter method for the privilegedSubElements.
	 * @return the privilegedSubElements
	 */
	public List<S> getPrivilegedSubElements() {
		return privilegedSubElements;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.AbstractCompositeSimulationModelElement#singleStep()
	 */
	@Override
	public void singleStep() {
		remainingPrivilegedSteps--;
		if (remainingPrivilegedSteps == 0) {
			remainingPrivilegedSteps = factor;
			super.singleStep();
		} else {
			for (S subElement : privilegedSubElements) {
				subElement.singleStep();
			}
		}
	}

}
