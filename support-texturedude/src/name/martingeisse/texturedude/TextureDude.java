/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import name.martingeisse.texturedude.corefunctions.CoreFunctions;

/**
 * The TD virtual machine.
 * 
 * TODO: the name "texture dude" is already used by somebody else.
 * -> "image dude"?
 * 
 * TODO: implement "prefix state" that can be used for prefix instructions
 * which modify following instructions: seed (default 0), background color
 * (default black), ... default prefix state has nothing; prefix instructions
 * add state (decorate); after each normal instruction reset to default state.
 * -> simplifies programs; less confusion about parameter order; smaller programs
 */
public final class TextureDude {
	
	/**
	 * the functionHost
	 */
	private final IFunctionHost functionHost;
	
	/**
	 * the layerStack
	 */
	private final LayerStack layerStack;
	
	/**
	 * the layerSlots
	 */
	private final LayerSlots layerSlots;
	
	/**
	 * the coreFunctions
	 */
	private final CoreFunctions coreFunctions;

	/**
	 * the program
	 */
	private InputStream program;
	
	/**
	 * Constructor.
	 */
	public TextureDude() {
		this.functionHost = new FunctionHost();
		this.layerStack = new LayerStack();
		this.layerSlots = new LayerSlots();
		this.coreFunctions = new CoreFunctions(functionHost);
		this.program = null;
	}
	
	/**
	 * Getter method for the functionHost.
	 * @return the functionHost
	 */
	public IFunctionHost getFunctionHost() {
		return functionHost;
	}

	/**
	 * Getter method for the layerStack.
	 * @return the layerStack
	 */
	public LayerStack getLayerStack() {
		return layerStack;
	}
	
	/**
	 * Getter method for the layerSlots.
	 * @return the layerSlots
	 */
	public LayerSlots getLayerSlots() {
		return layerSlots;
	}
	
	/**
	 * Getter method for the coreFunctions.
	 * @return the coreFunctions
	 */
	public CoreFunctions getCoreFunctions() {
		return coreFunctions;
	}

	/**
	 * Getter method for the program.
	 * @return the program
	 */
	public InputStream getProgram() {
		return program;
	}
	
	/**
	 * Setter method for the program.
	 * @param program the program to set
	 */
	public void setProgram(InputStream program) {
		this.program = program;
	}
	
	/**
	 * Setter method for the program.
	 * @param program the program to set
	 */
	public void setProgram(byte[] program) {
		setProgram(new ByteArrayInputStream(program));
	}
	
	/**
	 * Executes the next instruction.
	 * @return true if there was an instruction to execute, false if the end of
	 * the program was reached
	 * @throws IOException on I/O errors
	 */
	public boolean executeNextInstruction() throws IOException {
		int opcode = program.read();
		if (opcode == -1) {
			return false;
		}
		coreFunctions.call(opcode);
		return true;
	}

	/**
	 * Executes the program. By convention, the result of the program is
	 * the active layer after execution has finished.
	 * 
	 * @throws IOException on I/O errors
	 */
	public void execute() throws IOException {
		while (executeNextInstruction());
	}

	/**
	 * {@link IFunctionHost} implementation.
	 */
	private class FunctionHost implements IFunctionHost {

		/* (non-Javadoc)
		 * @see name.martingeisse.texturedude.IFunctionHost#fetchArgumentByte()
		 */
		@Override
		public int fetchArgumentByte() throws IOException {
			int byteValue = program.read();
			if (byteValue == -1) {
				throw new EOFException();
			} else {
				return (byteValue & 0xff);
			}
		}
	
		/* (non-Javadoc)
		 * @see name.martingeisse.texturedude.IFunctionHost#fetchArgumentShort()
		 */
		@Override
		public int fetchArgumentShort() throws EOFException, IOException {
			return (fetchArgumentByte() << 8) + fetchArgumentByte();
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.texturedude.IFunctionHost#getDude()
		 */
		@Override
		public TextureDude getDude() {
			return TextureDude.this;
		}
		
	}
	
}
