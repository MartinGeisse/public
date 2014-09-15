/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator;

/**
 * This enum type defines the various conditions that can be used in
 * JUMP, CALL and RETURN instructions. The main purpose of this enum
 * is to reduce the number of different methods in
 * {@link PicoblazeState} (because without this enum, there'd be
 * 15 jump methods!)
 */
public enum PicoblazeJumpCondition {

	/**
	 * jumps unconditionally
	 */
	NONE {
		@Override
		public boolean test(final boolean zero, final boolean carry) {
			return true;
		}
	},

	/**
	 * jumps if the zero flag is set
	 */
	ZERO {
		@Override
		public boolean test(final boolean zero, final boolean carry) {
			return zero;
		}
	},

	/**
	 * jumps if the zero flag is not set
	 */
	NON_ZERO {
		@Override
		public boolean test(final boolean zero, final boolean carry) {
			return !zero;
		}
	},

	/**
	 * jumps if the carry flag is set
	 */
	CARRY {
		@Override
		public boolean test(final boolean zero, final boolean carry) {
			return carry;
		}
	},

	/**
	 * jumps if the carry flag is not set
	 */
	NO_CARRY {
		@Override
		public boolean test(final boolean zero, final boolean carry) {
			return !carry;
		}
	};

	/**
	 * Tests this condition in the context of the specified zero and carry flags. Returns true
	 * if the condition is met, i.e. if the jump should occur.
	 * @param zero the zero flag value
	 * @param carry the carry flag value
	 * @return whether to jump
	 */
	public abstract boolean test(boolean zero, boolean carry);

	/**
	 * Obtains the jump condition from the specified encoded instruction.
	 * This method assumes that the instruction is actually a jump-type
	 * instruction and will run but return an unspecified value if that
	 * is not the case.
	 * @return the jump condition
	 */
	static PicoblazeJumpCondition fromEncodedInstruction(final int encodedInstruction) {
		final int conditionCode = ((encodedInstruction >> 10) & 7);
		switch (conditionCode) {
		case 4:
			return ZERO;
		case 5:
			return NON_ZERO;
		case 6:
			return CARRY;
		case 7:
			return NO_CARRY;
		default:
			return NONE;
		}

	}

}
