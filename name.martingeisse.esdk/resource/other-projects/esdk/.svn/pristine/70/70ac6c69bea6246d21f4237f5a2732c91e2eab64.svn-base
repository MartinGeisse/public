package name.martingeisse.esdk.picoblaze.assembler.ast;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * A PSM element that represents a register renaming directive.
 * 
 * @author Martin Geisse
 */
public class PsmNamereg extends PsmElement {

	/**
	 * the oldNameRange
	 */
	private final Range oldNameRange;
	
	/**
	 * the newNameRange
	 */
	private final Range newNameRange;
	
	/**
	 * the oldName
	 */
	private final String oldName;

	/**
	 * the newName
	 */
	private final String newName;

	/**
	 * 
	 * Creates a new register renaming.
	 * @param fullRange the full syntactic range of the renaming, or null if not known
	 * @param oldNameRange the syntactic range of the old register name
	 * @param newNameRange the syntactic range of the new register name
	 * @param oldName the old register name
	 * @param newName the new register name
	 */
	public PsmNamereg(final Range fullRange, final Range oldNameRange, final Range newNameRange, final String oldName, final String newName) {
		super(fullRange);
		this.oldNameRange = oldNameRange;
		this.newNameRange = newNameRange;
		this.oldName = oldName;
		this.newName = newName;
	}

	/**
	 * Getter method for the oldNameRange.
	 * @return the oldNameRange
	 */
	public Range getOldNameRange() {
		return oldNameRange;
	}

	/**
	 * Getter method for the newNameRange.
	 * @return the newNameRange
	 */
	public Range getNewNameRange() {
		return newNameRange;
	}

	/**
	 * Getter method for the oldName.
	 * @return the oldName
	 */
	public String getOldName() {
		return oldName;
	}

	/**
	 * Getter method for the newName.
	 * @return the newName
	 */
	public String getNewName() {
		return newName;
	}

}
