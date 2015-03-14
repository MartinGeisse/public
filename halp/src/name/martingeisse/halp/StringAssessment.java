/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp;

/**
 * Assessment that consists of a single string of text.
 */
public class StringAssessment implements Assessment {

	/**
	 * the text
	 */
	private final String text;

	/**
	 * Constructor.
	 * @param text the text
	 */
	public StringAssessment(String text) {
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.halp.Assessment#getText()
	 */
	@Override
	public String getText() {
		return text;
	}

}
