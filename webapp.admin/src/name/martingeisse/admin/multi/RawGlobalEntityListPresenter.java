/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi;


/**
 * Raw presentation of entities.
 */
public class RawGlobalEntityListPresenter extends GlobalEntityListPresenter {

	/**
	 * Constructor.
	 */
	public RawGlobalEntityListPresenter() {
		super("default", "Default", RawEntityListPanel.class);
	}

}
