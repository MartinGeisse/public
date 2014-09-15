package name.martingeisse.esdk.picoblaze.assembler.ast;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * A PSM element that represents a constant definition. The value should
 * be in the range 0..255.
 * 
 * @author Martin Geisse
 */
public class PsmConstant extends PsmElement {

	/**
	 * the nameRange
	 */
	private final Range nameRange;

	/**
	 * the valueRange
	 */
	private final Range valueRange;

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the value
	 */
	private final int value;

	/**
	 * Creates a new constant definition.
	 * @param fullRange the full syntactic range of the renaming, or null if not known
	 * @param nameRange the syntactic range of the constant name
	 * @param valueRange the syntactic range of the constant value
	 * @param name the name of the constant
	 * @param value the value of the constant
	 */
	public PsmConstant(final Range fullRange, final Range nameRange, final Range valueRange, final String name, final int value) {
		super(fullRange);
		this.nameRange = nameRange;
		this.valueRange = valueRange;
		this.name = name;
		this.value = value;
		checkByte(value, "constant value");
	}

	/**
	 * Getter method for the nameRange.
	 * @return the nameRange
	 */
	public Range getNameRange() {
		return nameRange;
	}

	/**
	 * Getter method for the valueRange.
	 * @return the valueRange
	 */
	public Range getValueRange() {
		return valueRange;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

}
