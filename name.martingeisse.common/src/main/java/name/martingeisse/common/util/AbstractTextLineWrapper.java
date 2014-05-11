/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * This class wraps lines from a single long line of text into multiple output lines.
 */
public abstract class AbstractTextLineWrapper {

	/**
	 * the words
	 */
	private String[] words;

	/**
	 * the wordsDone
	 */
	private int wordsDone;
	
	/**
	 * the currentLine
	 */
	private int currentLine;
	
	/**
	 * the currentColumn
	 */
	private int currentColumn;
	
	/**
	 * Constructor.
	 * @param text the input text to wrap
	 */
	public AbstractTextLineWrapper(String text) {
		words = text.trim().split("\\s+");
	}
	
	/**
	 * This method determines the width of the specified word.
	 * 
	 * @param word the word
	 * @return the width of the word
	 */
	protected abstract int getWordWidth(String word);

	/**
	 * This method determines the width of the specified line, using the same unit as getWordWidth().
	 * Returning 0 is allowed and naturally enforces a blank line. Returning a negative value indicates
	 * that no more lines are allowed and that the remainder of the input text shall be discarded.
	 * 
	 * @param lineNumber the line number
	 * @return the line length
	 */
	protected abstract int getLineWidth(int lineNumber);
	
	/**
	 * This method handles a word at the specified line and column position.
	 * 
	 * @param lineNumber the line number
	 * @param columnNumber the column number
	 * @param word the word to handle
	 */
	protected abstract void handleWord(int lineNumber, int columnNumber, String word);
	
	/**
	 * This method must return the width of the space between two words. This wrapper will
	 * reserve that amount of space between two words, but not let space at the end of a
	 * line cause wrapping.
	 * 
	 * @return the space width
	 */
	protected abstract int getSpaceWidth();
	
	/**
	 * Runs this line wrapper.
	 */
	public void run() {
		wordsDone = 0;
		currentLine = 0;
		currentColumn = 0;
		while (wordsDone < words.length) {
			if (!handleInputWord(words[wordsDone])) {
				break;
			}
			wordsDone++;
		}
	}
	
	/**
	 * Internal handling method for input words.
	 * @param word the word to handle
	 * @return true if the word was handled, false to stop the wrapping algorithm
	 */
	private boolean handleInputWord(String word) {
		int spaceWidth = getSpaceWidth();
		int wordWidth = getWordWidth(word);
		
		while (true) {
			int lineWidth = getLineWidth(currentLine);
			if (lineWidth < 0) {
				return false;
			}
			if (currentColumn + wordWidth >= lineWidth) {
				currentColumn = 0;
				currentLine++;
			} else {
				break;
			}
		}
		
		handleWord(currentLine, currentColumn, word);
		currentColumn = currentColumn + wordWidth + spaceWidth;
		return true;
	}
	
}
