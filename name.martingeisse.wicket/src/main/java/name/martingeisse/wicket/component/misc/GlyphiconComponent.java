/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;

/**
 * Base class for components that render a Bootstrap glyph
 * icon. The concrete subclass must obtain the glyph icon
 * identifier, e.g. "bell" for the CSS classes
 * "glyphicon glyphicon-bell".
 */
public abstract class GlyphiconComponent extends WebComponent {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public GlyphiconComponent(final String id, final IModel<?> model) {
		super(id, model);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public GlyphiconComponent(final String id) {
		super(id);
	}

	/**
	 * @return the glyph icon identifier
	 */
	protected abstract String getGlyphiconIdentifier();

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);
		applyGlyphiconToTag(getGlyphiconIdentifier(), tag);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTagBody(org.apache.wicket.markup.MarkupStream, org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag, "");
	}
	
	/**
	 * Adds the CSS classes "glyphicon" and "glyphicon-?" to the specified
	 * tag to render the glyph icon in it.
	 * 
	 * Note that the tag should not contain text content -- the word spacing
	 * from the glyphicon font looks really ugly on normal text.
	 * 
	 * @param glyphiconIdentifier the glyph icon identifier
	 * @param tag the tag to render the glyph icon to
	 */
	public static void applyGlyphiconToTag(String glyphiconIdentifier, ComponentTag tag) {
		if (tag.isOpenClose()) {
			tag.setType(TagType.OPEN);
		}
		tag.append("class", "glyphicon glyphicon-" + glyphiconIdentifier, " ");

	}
	
}
