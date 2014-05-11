/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.terms;

/**
 * This interface combines {@link IGetId} and {@link ISetId}.
 *
 * @param <T> the id type
 */
public interface IGetAndSetId<T> extends IGetId<T>, ISetId<T> {
}
