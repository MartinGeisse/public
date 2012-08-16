/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.readonly;

import name.martingeisse.admin.entity.property.type.ISqlType;
import name.martingeisse.admin.util.IGetScore;

/**
 * Given a data type, an instance of this interface may provide
 * a read-only renderer for entity properties of that type.
 * 
 * Implementations are free to pass for a certain type, leaving
 * other contributors a chance.
 * 
 * If multiple contributors can handle a specific type, the
 * contributor with the highest score wins. This is achieved
 * by simple asking contributors in descending-score order.
 * 
 * A common fallback renderer is used in case the returned renderer
 * returns null, i.e. when it cannot actually handle the value.
 * Note however that when a contributor returns a renderer but
 * that renderer returns null, ONLY the common fallback renderer is
 * used (null-safe toString()) -- other contributors with a
 * lower score are NOT asked in this case.
 */
public interface IPropertyReadOnlyRendererContributor extends IGetScore {
	
	/**
	 * Gets the renderer for the specified type, or null if this
	 * contributor does not provide a renderer for that type.
	 * @param type the type
	 * @return the renderer
	 */
	public IPropertyReadOnlyRenderer getRenderer(ISqlType type);
	
}
