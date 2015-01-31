/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.common.terms;

/**
 * This interface combines {@link IGetId} and {@link ISetId}.
 *
 * @param <T> the id type
 */
public interface IGetAndSetId<T> extends IGetId<T>, ISetId<T> {
}
