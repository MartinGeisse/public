/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.compute;

/**
 * This model returns the sum of the values from a list of
 * other models of type {@link Integer}.
 */
public class IntegerModelListSumModel extends AbstractModelListReductionModel<Integer> {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.model.compute.AbstractModelListReductionModel#getStartValue()
	 */
	@Override
	protected Integer getStartValue() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.model.compute.AbstractModelListReductionModel#reduce(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Integer reduce(Integer x, Integer y) {
		return x + y;
	}

}
