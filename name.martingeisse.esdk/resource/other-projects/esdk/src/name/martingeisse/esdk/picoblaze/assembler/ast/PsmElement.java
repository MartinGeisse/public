package name.martingeisse.esdk.picoblaze.assembler.ast;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * This class represents a single element from a PSM file. It is
 * used for directives, labels, and instructions.
 * 
 * @author Martin Geisse
 */
public abstract class PsmElement {

	/**
	 * The full syntactic range of this element, for error output.
	 */
	private final Range fullRange;

	/**
	 * Creates a new PSM element at the specified location.
	 * @param fullRange the full syntactic range of this element, for error output.
	 */
	public PsmElement(final Range fullRange) {
		this.fullRange = fullRange;
	}

	/**
	 * Getter method for the fullRange.
	 * @return the fullRange
	 */
	public Range getFullRange() {
		return fullRange;
	}

	/**
	 * This method can be used to check values at construction.
	 * It throws an <code>IllegalArgumentException</code> if
	 * the <code>value</code> is not in the range (0..255).
	 */
	protected final void checkByte(final int value, final String meaning) {
		checkValue(value, 0, 255, meaning);
	}

	/**
	 * This method can be used to check values at construction.
	 * It throws an <code>IllegalArgumentException</code> if
	 * the <code>value</code> is not in the range (min..max).
	 */
	protected final void checkValue(final int value, final int min, final int max, final String meaning) {
		if (value < min || value > max) {
			throw new IllegalArgumentException("invalid " + meaning + ": " + value + " is not in the range (" + min + "..." + max + ")");
		}
	}

	/**
	 * Throws an error about an undefined register name.
	 * @param name the unknown name
	 * @param errorHandler the error handler
	 */
	protected void noSuchRegister(final Range range, final String name, final IPicoblazeAssemblerErrorHandler errorHandler) {
		errorHandler.handleError(range, "No such register: " + name);
	}

	/**
	 * Throws an error about an undefined register name.
	 * @param name the unknown name
	 * @param errorHandler the error handler
	 */
	protected void noSuchRegisterOrConstant(final Range range, final String name, final IPicoblazeAssemblerErrorHandler errorHandler) {
		errorHandler.handleError(range, "No such register or constant: " + name);
	}

	/**
	 * Throws an error about an undefined label name.
	 * @param name the unknown name
	 * @param errorHandler the error handler
	 */
	protected void noSuchLabel(final Range range, final String name, final IPicoblazeAssemblerErrorHandler errorHandler) {
		errorHandler.handleError(range, "No such label: " + name);
	}

}
