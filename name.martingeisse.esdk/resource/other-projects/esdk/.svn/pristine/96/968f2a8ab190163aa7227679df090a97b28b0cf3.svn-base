/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.assembler;

/**
 * A generic syntactic range in a text file.
 */
public class Range {
	
	/**
	 * the startOffset
	 */
	private final int startOffset;

	/**
	 * the startLine
	 */
	private final int startLine;

	/**
	 * the startColumn
	 */
	private final int startColumn;
	
	/**
	 * the endOffset
	 */
	private final int endOffset;

	/**
	 * the endLine
	 */
	private final int endLine;

	/**
	 * the endColumn
	 */
	private final int endColumn;

	/**
	 * Constructor.
	 * @param startOffset the offset of the start of this range
	 * @param startLine the line that contains the start of this range
	 * @param startColumn the column that contains the start of this range
	 * @param endOffset the offset of the end of this range
	 * @param endLine the line that contains the end of this range
	 * @param endColumn the column that contains the end of this range
	 */
	public Range(final int startOffset, final int startLine, final int startColumn, final int endOffset, final int endLine, final int endColumn) {
		this.startOffset = startOffset;
		this.startLine = startLine;
		this.startColumn = startColumn;
		this.endOffset = endOffset;
		this.endLine = endLine;
		this.endColumn = endColumn;
	}
	
	/**
	 * Creates a minimal range that contains both specified ranges. The order of the
	 * arguments is irrelevant, but both ranges must have consistent offset/line/column
	 * values. For example, one range must not have a higher offset but lower line number
	 * than the other one.
	 * @param r1 the first range
	 * @param r2 the second range
	 */
	public Range(Range r1, Range r2) {
		
		// start
		if (r1.getStartOffset() < r2.getStartOffset()) {
			this.startOffset = r1.getStartOffset();
			this.startLine = r1.getStartLine();
			this.startColumn = r1.getStartColumn();
		} else {
			this.startOffset = r2.getStartOffset();
			this.startLine = r2.getStartLine();
			this.startColumn = r2.getStartColumn();
		}
		
		// end
		if (r1.getEndOffset() > r2.getEndOffset()) {
			this.endOffset = r1.getEndOffset();
			this.endLine = r1.getEndLine();
			this.endColumn = r1.getEndColumn();
		} else {
			this.endOffset = r2.getEndOffset();
			this.endLine = r2.getEndLine();
			this.endColumn = r2.getEndColumn();
		}
		
	}

	/**
	 * Getter method for the startOffset.
	 * @return the startOffset
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * Getter method for the startLine.
	 * @return the startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Getter method for the startColumn.
	 * @return the startColumn
	 */
	public int getStartColumn() {
		return startColumn;
	}

	/**
	 * Getter method for the endOffset.
	 * @return the endOffset
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/**
	 * Getter method for the endLine.
	 * @return the endLine
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * Getter method for the endColumn.
	 * @return the endColumn
	 */
	public int getEndColumn() {
		return endColumn;
	}

}
