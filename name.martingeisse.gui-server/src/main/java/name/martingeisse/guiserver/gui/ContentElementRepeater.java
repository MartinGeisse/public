/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.gui;

import java.util.Iterator;
import java.util.List;

import name.martingeisse.guiserver.configuration.content.ContentElementConfiguration;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.AbstractRepeater;

/**
 * This repeater should be attached to an empty HTML element. It uses that element to
 * render each of a list of {@link ContentElementConfiguration} objects.
 */
public class ContentElementRepeater extends AbstractRepeater {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param elements the content elements
	 */
	public ContentElementRepeater(String id, List<ContentElementConfiguration> elements) {
		this(id, elements, DefaultContentElementComponentBuilder.INSTANCE);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param elements the content elements
	 * @param builder this builder turns the content elements into components
	 */
	public ContentElementRepeater(String id, List<ContentElementConfiguration> elements, ContentElementComponentBuilder builder) {
		super(id);
		int i = 0;
		for (ContentElementConfiguration element : elements) {
			add(builder.buildComponent(Integer.toString(i), element));
			i++;
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#onPopulate()
	 */
	@Override
	protected void onPopulate() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#renderIterator()
	 */
	@Override
	protected Iterator<? extends Component> renderIterator() {
		return iterator();
	}
	
}
