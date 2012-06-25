/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser.adapter;

import name.martingeisse.reporting.definition.ITabularQuery;
import name.martingeisse.reporting.definition.keycount.IKeyCountQuery;
import name.martingeisse.reporting.definition.keycount.SimpleTabularKeyCountQueryAdapter;

/**
 * This adapter makes an {@link ITabularQuery} behave as an {@link IKeyCountQuery},
 * using a {@link SimpleTabularKeyCountQueryAdapter}.
 */
public class TabularKeyCountAdapterFactory extends AbstractAdapterFactory<ITabularQuery, IKeyCountQuery> {

	/**
	 * Constructor.
	 */
	public TabularKeyCountAdapterFactory() {
		super(ITabularQuery.class, IKeyCountQuery.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.parser.adapter.AbstractAdapterFactory#createAdapter(java.lang.Object)
	 */
	@Override
	protected IKeyCountQuery createAdapter(ITabularQuery originalReturnData) {
		return new SimpleTabularKeyCountQueryAdapter(originalReturnData);
	}

}
