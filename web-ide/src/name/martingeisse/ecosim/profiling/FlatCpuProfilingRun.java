/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.profiling;


/**
 * This class collects data about a "flat" CPU profiling run. "Flat" in this
 * sense means that the code is seen as a flat array of instructions, i.e.
 * the profiler has no notion of subroutine calls and returns.
 * 
 * The profiler counts the number of times an instruction is executed from
 * a specific virtual address.
 * 
 * The profiler has a granularity defined at creation. The granularity is
 * measured as a number of instruction addresses. The profiler will collect
 * data about blocks of instructions with that size, but will not break down
 * data into finer detail levels.
 * 
 * The profiler also takes a start and end address to collect data about
 * a subrange of the address space. This keeps data size small when the
 * granularity is small. The number of data points is, ignoring rounding
 * errors, (endAddress - startAddress) / granularity.
 */
public class FlatCpuProfilingRun {

	/**
	 * the startAddress
	 */
	private final int startAddress;

	/**
	 * the endAddress
	 */
	private final int endAddress;

	/**
	 * the granularity
	 */
	private final int granularity;

	/**
	 * the result
	 */
	private final int[] result;

	/**
	 * Constructor.
	 * @param startAddress the first address where instructions shall be recorded
	 * @param endAddress the last address where instructions shall be recorded
	 * @param granularity the address space granularity, measured in bytes
	 */
	public FlatCpuProfilingRun(final int startAddress, final int endAddress, final int granularity) {
		if (startAddress > endAddress) {
			throw new IllegalArgumentException("startAddress > endAddress");
		}
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.granularity = granularity;
		this.result = new int[(endAddress - startAddress) / granularity + 1];
	}

	/**
	 * Getter method for the startAddress.
	 * @return the startAddress
	 */
	public int getStartAddress() {
		return startAddress;
	}

	/**
	 * Getter method for the endAddress.
	 * @return the endAddress
	 */
	public int getEndAddress() {
		return endAddress;
	}

	/**
	 * Getter method for the granularity.
	 * @return the granularity
	 */
	public int getGranularity() {
		return granularity;
	}

	/**
	 * Getter method for the result.
	 * @return the result
	 */
	public int[] getResult() {
		return result;
	}

	/**
	 * Samples an instruction address.
	 * @param instructionAddress the instruction address
	 */
	public void sample(int instructionAddress) {
		int index = (instructionAddress - startAddress) / granularity;
		if (index >= 0 && index < result.length) {
			result[index]++;
		}
	}

	/**
	 * Generates a report about this current run.
	 * @param includeZeroBlocks whether to include address blocks in the report from which
	 * no instruction has been executed at all
	 * @return the report
	 */
	public String createReport(boolean includeZeroBlocks) {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<result.length; i++) {
			int value = result[i];
			if (value == 0 && !includeZeroBlocks) {
				continue;
			}
			long address = (startAddress + i * granularity) & 0xffffffffL;
			builder.append(String.format("%1$8x: %2$d\n", address, value));
		}
		return builder.toString();
	}

}
