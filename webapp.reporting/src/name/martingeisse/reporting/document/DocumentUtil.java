/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.reporting.datasource.DataSources;

/**
 * Utility methods to deal with documents.
 */
public class DocumentUtil {

	/**
	 * Binds a list of document elements to the data sources.
	 * 
	 * If tryPlain is true and all items are "plain" (i.e. return
	 * themselves when binding), then this method returns the input list
	 * instead of a new list. This can be used by the caller to react
	 * differently if it contains only plain elements, e.g. behave in a
	 * "plain" way itself.
	 * 
	 * @param <T> the type of objects to bind
	 * @param dataSources the data sources
	 * @param expectedReturnType the class object used to cast returned objects
	 * @param bindables the items to bind
	 * @param tryPlain whether to return the input list if all items are plain
	 * @return the bound items
	 */
	public static <T extends IDataBindable> List<T> bindToData(DataSources dataSources, Class<T> expectedReturnType, final List<T> bindables, final boolean tryPlain) {
		final List<T> result = new ArrayList<T>();
		boolean plain = tryPlain;
		for (final IDataBindable bindable : bindables) {
			final T bound = expectedReturnType.cast(bindable.bindToData(dataSources));
			result.add(bound);
			if (bound != bindable) {
				plain = false;
			}
		}
		return (plain ? bindables : result);
	}

}
