/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnclosureContainer;

/**
 * This class works similar to {@link EnclosureContainer} -- i.e. it is
 * not an "automagic" enclosure but an explicitly created container --
 * but it senses the visibility of its first child component, not that
 * of a child passed explicitly to the constructor. This makes it easier
 * to use this class with children that are created later, and in
 * unrelated code.
 */
public final class FirstChildEnclosureContainer extends WebMarkupContainer {

	/**
	 * the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param id the wicket ID
	 */
	public FirstChildEnclosureContainer(String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onConfigure()
	 */
	@Override
	protected void onConfigure() {
		if (size() == 0) {
			setVisible(false);
		} else {
			Component child = get(0);
			child.configure();
			setVisible(child.determineVisibility());
		}
	}
	
}
