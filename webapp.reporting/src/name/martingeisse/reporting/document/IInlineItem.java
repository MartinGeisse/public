/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

/**
 * This interface is the supertype for all inline-formatted items.
 * The bound items generated by this item must also be inline-formatted items
 * to retain proper document structure.
 */
public interface IInlineItem extends IDataBindable {
}