package name.martingeisse.esdk.picoblaze.assembler.ast;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;

/**
 * This class represents a parsed PSM file. It contains mainly a set
 * of elements (including labels, directives, and instructions).
 */
public class PsmFile {

	// the list of elements
	private final List<PsmElement> elements;

	/**
	 * 
	 */
	public PsmFile() {
		this.elements = new ArrayList<PsmElement>();
	}

	/**
	 * @return the list of elements.
	 */
	public List<PsmElement> getElements() {
		return elements;
	}

	/**
	 * Adds an element.
	 * @param e the element to add
	 */
	public void add(final PsmElement e) {
		elements.add(e);
	}

	/**
	 * Adds all constants and labels to the specified context.
	 * @param context the context to add to
	 */
	public void collectConstantsAndLabels(final Context context) {
		int address = 0;
		for (final PsmElement e : elements) {
			if (e instanceof PsmLabel) {

				final PsmLabel label = (PsmLabel)e;
				context.addLabel(label, address);

			} else if (e instanceof PsmConstant) {

				final PsmConstant constant = (PsmConstant)e;
				context.addConstant(constant);

			} else if (e instanceof PsmAddress) {

				final PsmAddress addressDirective = (PsmAddress)e;
				address = addressDirective.getAddress();

			} else if (e instanceof PsmNamereg) {

				// no effect at this point

			} else if (e instanceof PsmInstruction) {

				address++;

			} else {
				throw new IllegalArgumentException("unknown PSM element: " + e);
			}
		}
	}

	/**
	 * Encodes the program relative to the specified context. The result is
	 * an array of 1024 encoded instructions, each using a code word in the
	 * range 0..2^18-1.
	 * @param context the context to take constant definitions and labels from
	 * @param errorHandler the error handler
	 * @return the encoded instructions. This array has always a length of 1024.
	 * The remainder of the array not used for instructions contains 0-values.
	 */
	public int[] encode(final Context context, final IPicoblazeAssemblerErrorHandler errorHandler) {
		final int[] result = new int[1024];
		int address = 0;
		boolean programMemoryAddressRangeExceeded = false;

		for (final PsmElement e : elements) {
			
			if (e instanceof PsmLabel) {

				// already stored in the context

			} else if (e instanceof PsmConstant) {

				// already stored in the context

			} else if (e instanceof PsmAddress) {

				final PsmAddress addressDirective = (PsmAddress)e;
				address = addressDirective.getAddress();

			} else if (e instanceof PsmNamereg) {

				final PsmNamereg nameregDirective = (PsmNamereg)e;
				context.renameRegister(nameregDirective);

			} else if (e instanceof PsmInstruction) {

				if (address >= 1024) {
					if (!programMemoryAddressRangeExceeded) {
						programMemoryAddressRangeExceeded = true;
						errorHandler.handleError(e.getFullRange(), "program is exceeding program memory address range 0..1023");
					}
				} else {
					programMemoryAddressRangeExceeded = false;
				}
				
				final PsmInstruction instruction = (PsmInstruction)e;
				int encodedInstruction = instruction.encode(context, errorHandler);
				if (!programMemoryAddressRangeExceeded) {
					result[address] = encodedInstruction;					
				}
				address++;
				
			} else {
				throw new IllegalArgumentException("unknown PSM element: " + e);
			}
		}

		return result;
	}
}
