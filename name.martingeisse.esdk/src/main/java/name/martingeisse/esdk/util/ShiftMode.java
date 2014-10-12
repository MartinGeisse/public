/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.util;

/**
 * Bit-shifting modes.
 */
public enum ShiftMode {

	/**
	 * Fills the vacant bit position with the value "false".
	 */
	FALSE {
		@Override
		public boolean getShiftedInBit(boolean previousBit, boolean oppositeShiftedOutBit) {
			return false;
		}
	},
	
	/**
	 * Fills the vacant bit position with the value "true".
	 */
	TRUE {
		@Override
		public boolean getShiftedInBit(boolean previousBit, boolean oppositeShiftedOutBit) {
			return true;
		}
	},
	
	/**
	 * Fills the vacant bit position with its previous value. This is used to
	 * implement arithmetic shift-right operations.
	 */
	CLONE {
		@Override
		public boolean getShiftedInBit(boolean previousBit, boolean oppositeShiftedOutBit) {
			return previousBit;
		}
	},
	
	/**
	 * Fills the vacant bit position with the value being shifted out at the opposite
	 * end. This is used to implement rotate operations.
	 */
	ROTATE {
		@Override
		public boolean getShiftedInBit(boolean previousBit, boolean oppositeShiftedOutBit) {
			return oppositeShiftedOutBit;
		}
	};
	
	/**
	 * Returns the resulting shifted-in bit value.
	 * 
	 * @param previousBit the previous value of the bit cell to determine the value for
	 * @param oppositeShiftedOutBit the previous value of the cell at the opposite end --
	 * this value is currently being shifted out
	 * @return the shifted-in bit value
	 */
	public abstract boolean getShiftedInBit(boolean previousBit, boolean oppositeShiftedOutBit);
	
}
