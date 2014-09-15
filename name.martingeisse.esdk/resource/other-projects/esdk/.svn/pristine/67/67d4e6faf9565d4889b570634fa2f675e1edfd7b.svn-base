package name.martingeisse.esdk.picoblaze.assembler.ast;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * This class collects information during compilation.
 * 
 * @author Martin Geisse
 */
public class Context {

	/**
	 * The mapping of all known constants to their values
	 */
	private final Map<String, Integer> constants;

	/**
	 * The mapping of register names to indices.
	 */
	private final Map<String, Integer> registers;

	/**
	 * The mapping of labels to addresses.
	 */
	private final Map<String, Integer> labels;

	/**
	 * the errorHandler
	 */
	private final IPicoblazeAssemblerErrorHandler errorHandler;

	/**
	 * Creates a new default context. No constants or labels are defined,
	 * and all registers use their default names.
	 * @param errorHandler the error handler
	 */
	public Context(final IPicoblazeAssemblerErrorHandler errorHandler) {
		this.constants = new HashMap<String, Integer>();
		this.registers = new HashMap<String, Integer>();
		this.labels = new HashMap<String, Integer>();
		this.errorHandler = errorHandler;

		for (int i = 0; i < 10; i++) {
			final char c = (char)(i + '0');
			final String name = "s" + c;
			registers.put(name, i);
		}
		for (int i = 0; i < 6; i++) {
			final char c = (char)(i + 'A');
			final String name = "s" + c;
			registers.put(name, 10 + i);
		}
	}

	/**
	 * Resolves a constant name to its index. Returns -1 if no such
	 * constant is known.
	 * @param name the name to look for
	 * @return the constant value
	 */
	public int getConstant(final String name) {
		final Integer result = constants.get(name);
		if (result == null) {
			return -1;
		} else {
			return result;
		}
	}

	/**
	 * Resolves a register name to its index. Returns -1 if no such
	 * register is known.
	 * @param name the name to look for
	 * @return the register number
	 */
	public int getRegister(final String name) {
		final Integer result = registers.get(name);
		if (result == null) {
			return -1;
		} else {
			return result;
		}
	}

	/**
	 * Resolves a label name to its address. Returns -1 if no such
	 * label is known.
	 * @param name the name to look for
	 * @return the label position
	 */
	public int getLabel(final String name) {
		final Integer result = labels.get(name);
		if (result == null) {
			return -1;
		} else {
			return result;
		}
	}

	/**
	 * Defines a label in this context.
	 * @param label the label to add
	 * @param address the address of the label
	 */
	public void addLabel(final PsmLabel label, final int address) {
		final Integer old = labels.put(label.getName(), address);
		if (old != null) {
			errorHandler.handleError(label.getNameRange(), "Duplicate label: " + label.getName());
		}
	}

	/**
	 * Defines a constant in this context.
	 * @param constant the constant to add
	 */
	public void addConstant(final PsmConstant constant) {
		final Integer previous = constants.put(constant.getName(), constant.getValue());
		if (previous != null) {
			errorHandler.handleError(constant.getNameRange(), constant.getName() + " has already been defined as a constant");
		}
		if (registers.get(constant.getName()) != null) {
			errorHandler.handleError(constant.getNameRange(), constant.getName() + " has already been defined as a register");
		}
	}

	/**
	 * Renames a register.
	 * @param renaming the register renaming
	 */
	public void renameRegister(final PsmNamereg renaming) {

		String oldName = renaming.getOldName();
		Range oldNameRange = renaming.getOldNameRange();
		String newName = renaming.getNewName();
		Range newNameRange = renaming.getNewNameRange();
		
		final Integer index = registers.remove(oldName);
		if (index == null) {
			errorHandler.handleError(oldNameRange, "No such register: " + oldName);
		}

		final Integer previous = registers.put(newName, index);
		if (previous != null) {
			errorHandler.handleError(newNameRange, newName + " has already been defined as a register");
		}
		if (constants.get(newName) != null) {
			errorHandler.handleError(newNameRange, newName + " has already been defined as a constant");
		}
		
	}
	

}
