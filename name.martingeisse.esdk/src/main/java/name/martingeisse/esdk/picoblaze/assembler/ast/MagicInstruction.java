package name.martingeisse.esdk.picoblaze.assembler.ast;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * Used for "magic" simulation instructions. This class just
 * stores the encoded instruction.
 * 
 * @author Martin Geisse
 */
public class MagicInstruction extends PsmInstruction {

	/**
	 * the encodedInstruction
	 */
	private final int encodedInstruction;

	/**
	 * Creates a new magic instruction instance for the specified
	 * encoded instruction.
	 *  
	 * @param fullRange the full syntactic range of the renaming, or null if not known
	 * @param encodedInstruction the encoded instruction
	 */
	public MagicInstruction(final Range fullRange, int encodedInstruction) {
		super(fullRange);
		if ((encodedInstruction & ~0x3ffff) != 0) {
			throw new IllegalArgumentException("invalid instruction code at " + fullRange + ": " + encodedInstruction);
		}
		this.encodedInstruction = encodedInstruction;
	}
	
	/**
	 * Getter method for the encodedInstruction.
	 * @return the encodedInstruction
	 */
	public int getEncodedInstruction() {
		return encodedInstruction;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.ast.PsmInstruction#encode(name.martingeisse.esdk.picoblaze.assembler.ast.Context, name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler)
	 */
	@Override
	public int encode(final Context context, final IPicoblazeAssemblerErrorHandler errorHandler) {
		return encodedInstruction;
	}
}
