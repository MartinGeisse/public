/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * 
 */
public enum BusAccessSize {

	/**
	 * byte-sized access
	 */
	BYTE {

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.BusAccessSize#isAligned(int)
		 */
		@Override
		public boolean isAligned(int address) {
			return true;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.bus.BusAccessSize#getByteCount()
		 */
		@Override
		public int getByteCount() {
			return 1;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.bus.BusAccessSize#getValidBitMask()
		 */
		@Override
		public int getValidBitMask() {
			return 0x000000ff;
		}

	},

	/**
	 * halfword-sized access
	 */
	HALFWORD {

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.BusAccessSize#isAligned(int)
		 */
		@Override
		public boolean isAligned(int address) {
			return (address & 1) == 0;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.bus.BusAccessSize#getByteCount()
		 */
		@Override
		public int getByteCount() {
			return 2;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.bus.BusAccessSize#getValidBitMask()
		 */
		@Override
		public int getValidBitMask() {
			return 0x0000ffff;
		}

	},

	/**
	 * word-sized access
	 */
	WORD {

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.BusAccessSize#isAligned(int)
		 */
		@Override
		public boolean isAligned(int address) {
			return (address & 3) == 0;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.bus.BusAccessSize#getByteCount()
		 */
		@Override
		public int getByteCount() {
			return 4;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.bus.BusAccessSize#getValidBitMask()
		 */
		@Override
		public int getValidBitMask() {
			return 0xffffffff;
		}

	};

	/**
	 * Checks if the specified address is aligned with respect to this access size.
	 * @param address the address to check.
	 * @return Returns true if the address is aligned, false if not.
	 */
	public abstract boolean isAligned(int address);

	/**
	 * @return Returns the number of bytes accessed with this size.
	 */
	public abstract int getByteCount();
	
	/**
	 * @return Returns a bit mask that contains a 1 bit at those positions that are
	 * relevant in a data value transferred with this bus access size. For example,
	 * for access size BYTE this mask is 0x000000ff.
	 */
	public abstract int getValidBitMask();

}
