/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.readonly;

import org.apache.wicket.markup.html.basic.Label;

/**
 * This implementation first uses another renderer to render values. Only
 * if that other renderer returns null, this renderer uses a fallback
 * rendering (null-safe toString()).
 */
public class FallbackRenderer implements IPropertyReadOnlyRenderer {

	/**
	 * the MAX_LENGTH
	 */
	private static final int MAX_LENGTH = 30;
	
	/**
	 * the primaryRenderer
	 */
	private IPropertyReadOnlyRenderer primaryRenderer;

	/**
	 * Constructor.
	 */
	public FallbackRenderer() {
	}

	/**
	 * Constructor.
	 * @param primaryRenderer the primary renderer to use
	 */
	public FallbackRenderer(final IPropertyReadOnlyRenderer primaryRenderer) {
		this.primaryRenderer = primaryRenderer;
	}

	/**
	 * Getter method for the primaryRenderer.
	 * @return the primaryRenderer
	 */
	public IPropertyReadOnlyRenderer getPrimaryRenderer() {
		return primaryRenderer;
	}

	/**
	 * Setter method for the primaryRenderer.
	 * @param primaryRenderer the primaryRenderer to set
	 */
	public void setPrimaryRenderer(final IPropertyReadOnlyRenderer primaryRenderer) {
		this.primaryRenderer = primaryRenderer;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer#createLabel(java.lang.String, java.lang.Object)
	 */
	@Override
	public Label createLabel(final String id, final Object value) {
		Label primaryRendererResult = (primaryRenderer == null ? null : primaryRenderer.createLabel(id, value));
		if (primaryRendererResult == null) {
			String s = "" + value;
			if (s.length() > MAX_LENGTH) {
				s = s.substring(0, MAX_LENGTH - 3) + "...";
			}
			return new Label(id, s);
		} else {
			return primaryRendererResult;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer#valueToString(java.lang.Object)
	 */
	@Override
	public String valueToString(Object value) {
		String primaryRendererResult = (primaryRenderer == null ? null : primaryRenderer.valueToString(value));
		if (primaryRendererResult == null) {
			String s = "" + value;
			if (s.length() > MAX_LENGTH) {
				s = s.substring(0, MAX_LENGTH - 3) + "...";
			}
			return s;
		} else {
			return primaryRendererResult;
		}
	}

	
}
