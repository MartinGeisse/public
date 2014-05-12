/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude.corefunctions;

import java.io.IOException;

import name.martingeisse.texturedude.IFunction;
import name.martingeisse.texturedude.IFunctionHost;

/**
 * Implements the core set of functions.
 */
public final class CoreFunctions {

	/**
	 * the MINUS_ONE
	 */
	private static final int MINUS_ONE = -1;

	/**
	 * See {@link MoveToSlotFunction}.
	 */
	public static final int MOVE_TO_SLOT = MINUS_ONE + 1;

	/**
	 * See {@link MoveFromSlotFunction}.
	 */
	public static final int MOVE_FROM_SLOT = MOVE_TO_SLOT + 1;
	
	/**
	 * See {@link CopyToSlotFunction}.
	 */
	public static final int COPY_TO_SLOT = MOVE_FROM_SLOT + 1;
	
	/**
	 * See {@link CopyFromSlotFunction}.
	 */
	public static final int COPY_FROM_SLOT = COPY_TO_SLOT + 1;
	
	/**
	 * See {@link CreateLayerFunction}.
	 */
	public static final int CREATE_LAYER = COPY_FROM_SLOT + 1;
	
	/**
	 * See {@link FlatColorFunction}.
	 */
	public static final int FLAT_COLOR = CREATE_LAYER + 1;
	
	/**
	 * See {@link FlatRgbaFunction}.
	 */
	public static final int FLAT_RGBA = FLAT_COLOR + 1;
	
	/**
	 * See {@link PerlinNoiseFunction}.
	 */
	public static final int PERLIN_NOISE = FLAT_RGBA + 1;

	/**
	 * the functionHost
	 */
	private final IFunctionHost functionHost;
	
	/**
	 * the functions
	 */
	private final IFunction[] functions;
	
	/**
	 * Constructor.
	 * @param functionHost the function host that uses this function set
	 */
	public CoreFunctions(final IFunctionHost functionHost) {
		this.functionHost = functionHost;
		this.functions = new IFunction[] {
			MoveToSlotFunction.INSTANCE,
			MoveFromSlotFunction.INSTANCE,
			CopyToSlotFunction.INSTANCE,
			CopyFromSlotFunction.INSTANCE,
			CreateLayerFunction.INSTANCE,
			FlatColorFunction.INSTANCE,
			FlatRgbaFunction.INSTANCE,
			PerlinNoiseFunction.INSTANCE,
		};
	}
	
	/**
	 * Calls the specified function. The function pops its arguments
	 * off the operand stack and pushes results on the stack.
	 * 
	 * @param functionIndex the function index of the function to call
	 * @throws IOException on I/O errors
	 */
	public void call(int functionIndex) throws IOException {
		if (functionIndex < 0 || functionIndex >= functions.length) {
			throw new IndexOutOfBoundsException("invalid function index: " + functionIndex);
		}
		functions[functionIndex].call(functionHost);
	}
	
}
