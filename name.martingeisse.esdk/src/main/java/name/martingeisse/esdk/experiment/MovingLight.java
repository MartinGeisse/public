/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.experiment;

import name.martingeisse.esdk.rtl.structure.StructuralModule;
import name.martingeisse.esdk.rtl.structure.basic_parts.BitVectorAdder;
import name.martingeisse.esdk.rtl.structure.basic_parts.Register;
import name.martingeisse.esdk.util.BitVector;
import name.martingeisse.esdk.util.ConstantValueSource;

/**
 *
 */
public class MovingLight extends StructuralModule {

	/**
	 * Constructor.
	 */
	public MovingLight() {
		
		// Delay so the light moves by about 4 steps per second. The design runs at
		// 50 MHz, that's roughly 64 MiHz = 2^26 Hz. So we need to put a delay of
		// 2^24 clock cycles per step in place.
		Register<BitVector> delayCounter = new Register<BitVector>(null, new BitVector(32, 0)).setSynthesisWidth(32);
		delayCounter.setSource(new BitVectorAdder(delayCounter, new ConstantValueSource<>(new BitVector(32, 1))));
		
		
	}
	
	
}
