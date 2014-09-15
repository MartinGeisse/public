/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler;
import name.martingeisse.esdk.picoblaze.simulator.PicoblazeSimulatorException;
import name.martingeisse.esdk.picoblaze.simulator.PicoblazeState;

import org.junit.Test;

/**
 * Test case class for {@link PicoblazeState}.
 */
public class PicoblazeStateTest {

	/**
	 * the falseTrue
	 */
	private static final boolean[] falseTrue = {false, true};
	
	/**
	 * the unconditionalJumpCodes
	 */
	private static final int[] unconditionalJumpCodes = {0, 1, 2, 3};
	
	/**
	 * the initialRegisterContents
	 */
	private static final byte[] initialRegisterContents = {
		5, 101, 99, 64, 78, -128, 25, 0, 12, 122, 112, 56, -99, 1, 2, 3
	};

	/**
	 * the initialRegisterContents
	 */
	private static final byte[] initialRamContents;

	/**
	 * the returnStackTemplate
	 */
	private static final int[] returnStackTemplate = {
		99, 30, 100, 1000, 199, 130, 1100, 600,  
		93, 33, 103, 1003, 193, 133, 1103, 603,  
		95, 35, 105, 1005, 195, 135, 1105, 605,  
		97, 37, 107, 1007, 197, 137, 1107, 607,  
	};
	
	/**
	 * static initializer
	 */
	static {
		initialRamContents = new byte[64];
		for (int i = 0; i < 16; i++) {
			initialRamContents[i] = (byte)(initialRegisterContents[i] + 9);
			initialRamContents[i + 16] = (byte)(initialRegisterContents[i] + 100);
			initialRamContents[i + 32] = (byte)(initialRegisterContents[i] - 100);
			initialRamContents[i + 48] = (byte)(initialRegisterContents[i] + 79);
		}
	}

	/**
	 * the state
	 */
	private PicoblazeState state;

	/**
	 * the carryInitializer
	 */
	private Boolean carryInitializer;

	/**
	 * Constructor.
	 */
	public PicoblazeStateTest() {
		this.state = new PicoblazeState();
		System.arraycopy(initialRegisterContents, 0, state.getRegisters(), 0, 16);
		System.arraycopy(initialRamContents, 0, state.getRam(), 0, 64);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testMeta() throws Exception {
		assertEquals(16, initialRegisterContents.length);
		assertEquals(64, initialRamContents.length);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testGetterSetter() throws Exception {

		// prepare a fresh state for this test
		this.state = new PicoblazeState();

		// PC
		assertEquals(0, state.getPc());
		state.setPc(33);
		assertEquals(33, state.getPc());
		state.setPc(0x4a756);
		assertEquals(0x356, state.getPc());

		// zero
		assertFalse(state.isZero());
		state.setZero(true);
		assertTrue(state.isZero());

		// carry
		assertFalse(state.isCarry());
		state.setCarry(true);
		assertTrue(state.isCarry());

		// return stack pointer
		assertEquals(0, state.getReturnStackPointer());
		state.setReturnStackPointer(5);
		assertEquals(5, state.getReturnStackPointer());
		state.setReturnStackPointer(39);
		assertEquals(7, state.getReturnStackPointer());

		// registers, RAM, return stack
		assertEquals(16, state.getRegisters().length);
		assertEquals(64, state.getRam().length);
		assertEquals(32, state.getReturnStack().length);

		// interrup enable
		assertFalse(state.isInterruptEnable());
		state.setInterruptEnable(true);
		assertTrue(state.isInterruptEnable());

		// preserved zero
		assertFalse(state.isPreservedZero());
		state.setPreservedZero(true);
		assertTrue(state.isPreservedZero());

		// preserved carry
		assertFalse(state.isPreservedCarry());
		state.setPreservedCarry(true);
		assertTrue(state.isPreservedCarry());

		// port handler
		final ExceptionPortHandler portHandler = new ExceptionPortHandler();
		assertNull(state.getPortHandler());
		state.setPortHandler(portHandler);
		assertSame(portHandler, state.getPortHandler());

	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testReset() throws Exception {
		
		ExceptionPortHandler portHandler = new ExceptionPortHandler();
		
		System.arraycopy(returnStackTemplate, 0, state.getReturnStack(), 0, 32);
		assertEquals(99, state.getPc());
		
		state.setZero(true);
		state.setCarry(true);
		state.setInterruptEnable(true);
		state.setPreservedZero(true);
		state.setPreservedCarry(true);
		state.setReturnStackPointer(5);
		state.setPc(77);
		state.setPortHandler(portHandler);
		
		assertEquals(77, state.getReturnStack()[5]);
		state.reset();
		
		assertFalse(state.isZero());
		assertFalse(state.isCarry());
		assertFalse(state.isInterruptEnable());
		assertTrue(state.isPreservedZero());
		assertTrue(state.isPreservedCarry());
		assertEquals(5, state.getReturnStackPointer());
		assertEquals(0, state.getPc());
		assertSame(portHandler, state.getPortHandler());
		
		assertArrayEquals(initialRegisterContents, state.getRegisters());
		assertArrayEquals(initialRamContents, state.getRam());
		assertEquals(0, state.getReturnStack()[5]);
		state.getReturnStack()[5] = returnStackTemplate[5];
		assertArrayEquals(returnStackTemplate, state.getReturnStack());
		
	}
	
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testInstructionHelpers() throws Exception {

		// registers
		assertEquals(-99 & 0xff, state.getRegisterValue(12));
		assertEquals((byte)-99, state.getRegisters()[12]);
		state.setRegisterValue(12, 77);
		assertEquals(77 & 0xff, state.getRegisterValue(12));
		assertEquals((byte)77, state.getRegisters()[12]);
		state.setRegisterValue(12, 177);
		assertEquals(177 & 0xff, state.getRegisterValue(12));
		assertEquals((byte)177, state.getRegisters()[12]);
		state.getRegisters()[12] = -3;
		assertEquals(-3 & 0xff, state.getRegisterValue(12));
		assertEquals((byte)-3, state.getRegisters()[12]);

		// RAM
		assertEquals(80 & 0xff, state.getRamValue(61));
		assertEquals((byte)80, state.getRam()[61]);
		state.setRamValue(61, 77);
		assertEquals(77 & 0xff, state.getRamValue(61));
		assertEquals((byte)77, state.getRam()[61]);
		state.setRamValue(61, 177);
		assertEquals(177 & 0xff, state.getRamValue(61));
		assertEquals((byte)177, state.getRam()[61]);
		state.getRam()[61] = -3;
		assertEquals(-3 & 0xff, state.getRamValue(61));
		assertEquals((byte)-3, state.getRam()[61]);

		// ports
		final CountingPortHandler portHandler = new CountingPortHandler();
		state.setPortHandler(portHandler);
		assertEquals(0, portHandler.inputCount);
		assertEquals(0, portHandler.outputCount);
		assertEquals(0, portHandler.lastAddress);
		assertEquals(0, portHandler.lastOutputValue);
		assertEquals(33, state.input(56));
		assertEquals(1, portHandler.inputCount);
		assertEquals(0, portHandler.outputCount);
		assertEquals(56, portHandler.lastAddress);
		assertEquals(0, portHandler.lastOutputValue);
		state.output(91, 95);
		assertEquals(1, portHandler.inputCount);
		assertEquals(1, portHandler.outputCount);
		assertEquals(91, portHandler.lastAddress);
		assertEquals(95, portHandler.lastOutputValue);

		// reg/immediate
		assertEquals(99, state.getRegisterOrImmediate(2, false));
		assertEquals(2, state.getRegisterOrImmediate(2, true));

		// pc
		state.setPc(1022);
		state.incrementPc();
		assertEquals(1023, state.getPc());
		state.incrementPc();
		assertEquals(0, state.getPc());
		state.incrementPc();
		assertEquals(1, state.getPc());

	}

	// ...
	private void computationInstructionHelper(final int primaryOpcode, final int x, final int y, final int expectedResult, final boolean expectedZero, final boolean expectedCarry) throws PicoblazeSimulatorException {

		// prepare PC check
		state.setPc(55);

		// immediate right operand
		state.setRegisterValue(7, x);
		if (carryInitializer != null) {
			state.setCarry(carryInitializer);
		}
		state.performInstruction((primaryOpcode << 13) | (7 << 8) | (y & 0xff));
		assertEquals(expectedResult, state.getRegisterValue(7));
		assertEquals(expectedZero, state.isZero());
		assertEquals(expectedCarry, state.isCarry());

		// register operand
		state.setRegisterValue(7, x);
		state.setRegisterValue(13, y);
		if (carryInitializer != null) {
			state.setCarry(carryInitializer);
		}
		state.performInstruction((primaryOpcode << 13) | (1 << 12) | (7 << 8) | (13 << 4));
		assertEquals(expectedResult, state.getRegisterValue(7));
		assertEquals(expectedZero, state.isZero());
		assertEquals(expectedCarry, state.isCarry());

		// perform PC check
		assertEquals(57, state.getPc());

	}

	private void addHelper(final int primaryOpcode) throws Exception {
		computationInstructionHelper(primaryOpcode, 0, 0, 0, true, false);
		computationInstructionHelper(primaryOpcode, 0, 1, 1, false, false);
		computationInstructionHelper(primaryOpcode, 1, 0, 1, false, false);
		computationInstructionHelper(primaryOpcode, 100, 90, 190, false, false);
		computationInstructionHelper(primaryOpcode, 50, 150, 200, false, false);
		computationInstructionHelper(primaryOpcode, 200, 56, 0, true, true);
		computationInstructionHelper(primaryOpcode, 150, 150, 44, false, true);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testAdd() throws Exception {
		carryInitializer = true;
		addHelper(12);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testAddWithCarry() throws Exception {

		carryInitializer = false;
		addHelper(13);

		carryInitializer = true;
		computationInstructionHelper(13, 100, 90, 191, false, false);
		computationInstructionHelper(13, 50, 150, 201, false, false);
		computationInstructionHelper(13, 200, 55, 0, true, true);
		computationInstructionHelper(13, 200, 56, 1, false, true);
		computationInstructionHelper(13, 150, 150, 45, false, true);

	}

	private void subtractHelper(final int primaryOpcode) throws Exception {
		computationInstructionHelper(primaryOpcode, 0, 0, 0, true, false);
		computationInstructionHelper(primaryOpcode, 0, 1, 255, false, true);
		computationInstructionHelper(primaryOpcode, 1, 0, 1, false, false);
		computationInstructionHelper(primaryOpcode, 100, 90, 10, false, false);
		computationInstructionHelper(primaryOpcode, 50, 150, 156, false, true);
		computationInstructionHelper(primaryOpcode, 200, 200, 0, true, false);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSubtract() throws Exception {
		carryInitializer = true;
		subtractHelper(14);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSubtractWithCarry() throws Exception {

		carryInitializer = false;
		subtractHelper(15);

		carryInitializer = true;
		computationInstructionHelper(15, 100, 90, 9, false, false);
		computationInstructionHelper(15, 50, 150, 155, false, true);
		computationInstructionHelper(15, 200, 0, 199, false, false);
		computationInstructionHelper(15, 200, 199, 0, true, false);
		computationInstructionHelper(15, 200, 200, 255, false, true);
		computationInstructionHelper(15, 0, 255, 0, true, true);

	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testAnd() throws Exception {
		carryInitializer = true;
		computationInstructionHelper(5, 0x00, 0x00, 0x00, true, false);
		computationInstructionHelper(5, 0xff, 0xff, 0xff, false, false);
		computationInstructionHelper(5, 0x00, 0xff, 0x00, true, false);
		computationInstructionHelper(5, 0xff, 0x00, 0x00, true, false);
		computationInstructionHelper(5, 0x78, 0xac, 0x28, false, false);
		computationInstructionHelper(5, 0xa3, 0x54, 0x00, true, false);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testOr() throws Exception {
		carryInitializer = true;
		computationInstructionHelper(6, 0x00, 0x00, 0x00, true, false);
		computationInstructionHelper(6, 0xff, 0xff, 0xff, false, false);
		computationInstructionHelper(6, 0x00, 0xff, 0xff, false, false);
		computationInstructionHelper(6, 0xff, 0x00, 0xff, false, false);
		computationInstructionHelper(6, 0x78, 0xac, 0xfc, false, false);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testXor() throws Exception {
		carryInitializer = true;
		computationInstructionHelper(7, 0x00, 0x00, 0x00, true, false);
		computationInstructionHelper(7, 0xff, 0xff, 0x00, true, false);
		computationInstructionHelper(7, 0x00, 0xff, 0xff, false, false);
		computationInstructionHelper(7, 0xff, 0x00, 0xff, false, false);
		computationInstructionHelper(7, 0xa3, 0x54, 0xf7, false, false);
		computationInstructionHelper(7, 0xa3, 0xa3, 0x00, true, false);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testTest() throws Exception {
		carryInitializer = true;

		// bit test
		computationInstructionHelper(9, 0x00, 0x00, 0x00, true, false);
		computationInstructionHelper(9, 0x5a, 0x00, 0x5a, true, false);
		computationInstructionHelper(9, 0x00, 0x5a, 0x00, true, false);
		computationInstructionHelper(9, 0x5a, 0x5a, 0x5a, false, false);
		computationInstructionHelper(9, 0x5b, 0xff, 0x5b, false, true);
		computationInstructionHelper(9, 0xff, 0x5b, 0xff, false, true);
		computationInstructionHelper(9, 0xa3, 0x54, 0xa3, true, false);

		// parity
		computationInstructionHelper(9, 0x00, 0xff, 0x00, true, false);
		computationInstructionHelper(9, 0x01, 0xff, 0x01, false, true);
		computationInstructionHelper(9, 0x02, 0xff, 0x02, false, true);
		computationInstructionHelper(9, 0x03, 0xff, 0x03, false, false);
		computationInstructionHelper(9, 0x04, 0xff, 0x04, false, true);
		computationInstructionHelper(9, 0x05, 0xff, 0x05, false, false);
		computationInstructionHelper(9, 0x06, 0xff, 0x06, false, false);
		computationInstructionHelper(9, 0x07, 0xff, 0x07, false, true);
		computationInstructionHelper(9, 0x08, 0xff, 0x08, false, true);

	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testCompare() throws Exception {
		computationInstructionHelper(10, 0, 0, 0, true, false);
		computationInstructionHelper(10, 0, 1, 0, false, true);
		computationInstructionHelper(10, 1, 0, 1, false, false);
		computationInstructionHelper(10, 100, 90, 100, false, false);
		computationInstructionHelper(10, 50, 150, 50, false, true);
		computationInstructionHelper(10, 200, 200, 200, true, false);
	}

	private void shiftInstructionHelper(final int shiftCode, final int x, final int expectedResult, final boolean expectedZero, final boolean expectedCarry) throws PicoblazeSimulatorException {

		// prepare PC check
		state.setPc(55);

		// immediate right operand
		state.setRegisterValue(7, x);
		if (carryInitializer != null) {
			state.setCarry(carryInitializer);
		}
		state.performInstruction((1 << 17) | (7 << 8) | (shiftCode & 0x0f));
		assertEquals(expectedResult, state.getRegisterValue(7));
		assertEquals(expectedZero, state.isZero());
		assertEquals(expectedCarry, state.isCarry());

		// perform PC check
		assertEquals(56, state.getPc());

	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testShiftLeft0() throws Exception {
		carryInitializer = false;
		do {
			shiftInstructionHelper(6, 0, 0, true, false);
			shiftInstructionHelper(6, 1, 2, false, false);
			shiftInstructionHelper(6, 2, 4, false, false);
			shiftInstructionHelper(6, 3, 6, false, false);
			shiftInstructionHelper(6, 80, 160, false, false);
			shiftInstructionHelper(6, 128, 0, true, true);
			shiftInstructionHelper(6, 212, 424 & 0xff, false, true);
			carryInitializer = !carryInitializer;
		} while (carryInitializer);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testShiftLeft1() throws Exception {
		carryInitializer = false;
		do {
			shiftInstructionHelper(7, 0, 1, false, false);
			shiftInstructionHelper(7, 1, 3, false, false);
			shiftInstructionHelper(7, 2, 5, false, false);
			shiftInstructionHelper(7, 3, 7, false, false);
			shiftInstructionHelper(7, 80, 161, false, false);
			shiftInstructionHelper(7, 128, 1, false, true);
			shiftInstructionHelper(7, 212, 425 & 0xff, false, true);
			carryInitializer = !carryInitializer;
		} while (carryInitializer);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testShiftLeftReplicate() throws Exception {
		carryInitializer = false;
		do {
			shiftInstructionHelper(4, 0, 0, true, false);
			shiftInstructionHelper(4, 1, 3, false, false);
			shiftInstructionHelper(4, 2, 4, false, false);
			shiftInstructionHelper(4, 3, 7, false, false);
			shiftInstructionHelper(4, 80, 160, false, false);
			shiftInstructionHelper(4, 128, 0, true, true);
			shiftInstructionHelper(4, 129, 3, false, true);
			shiftInstructionHelper(4, 212, 424 & 0xff, false, true);
			carryInitializer = !carryInitializer;
		} while (carryInitializer);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testShiftLeftAll() throws Exception {

		carryInitializer = false;
		shiftInstructionHelper(0, 0, 0, true, false);
		shiftInstructionHelper(0, 1, 2, false, false);
		shiftInstructionHelper(0, 2, 4, false, false);
		shiftInstructionHelper(0, 3, 6, false, false);
		shiftInstructionHelper(0, 80, 160, false, false);
		shiftInstructionHelper(0, 128, 0, true, true);
		shiftInstructionHelper(0, 212, 424 & 0xff, false, true);

		carryInitializer = true;
		shiftInstructionHelper(0, 0, 1, false, false);
		shiftInstructionHelper(0, 1, 3, false, false);
		shiftInstructionHelper(0, 2, 5, false, false);
		shiftInstructionHelper(0, 3, 7, false, false);
		shiftInstructionHelper(0, 80, 161, false, false);
		shiftInstructionHelper(0, 128, 1, false, true);
		shiftInstructionHelper(0, 212, 425 & 0xff, false, true);

	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testRotateLeft() throws Exception {
		carryInitializer = false;
		do {
			shiftInstructionHelper(2, 0, 0, true, false);
			shiftInstructionHelper(2, 1, 2, false, false);
			shiftInstructionHelper(2, 2, 4, false, false);
			shiftInstructionHelper(2, 3, 6, false, false);
			shiftInstructionHelper(2, 80, 160, false, false);
			shiftInstructionHelper(2, 128, 1, false, true);
			shiftInstructionHelper(2, 212, 425 & 0xff, false, true);
			carryInitializer = !carryInitializer;
		} while (carryInitializer);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testShiftRight0() throws Exception {
		carryInitializer = false;
		do {
			shiftInstructionHelper(14, 0, 0, true, false);
			shiftInstructionHelper(14, 128, 64, false, false);
			shiftInstructionHelper(14, 64, 32, false, false);
			shiftInstructionHelper(14, 192, 96, false, false);
			shiftInstructionHelper(14, 82, 41, false, false);
			shiftInstructionHelper(14, 1, 0, true, true);
			shiftInstructionHelper(14, 101, 50, false, true);
			carryInitializer = !carryInitializer;
		} while (carryInitializer);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testShiftRight1() throws Exception {
		carryInitializer = false;
		do {
			shiftInstructionHelper(15, 0, 128, false, false);
			shiftInstructionHelper(15, 128, 192, false, false);
			shiftInstructionHelper(15, 64, 160, false, false);
			shiftInstructionHelper(15, 192, 224, false, false);
			shiftInstructionHelper(15, 82, 169, false, false);
			shiftInstructionHelper(15, 1, 128, false, true);
			shiftInstructionHelper(15, 101, 178, false, true);
			carryInitializer = !carryInitializer;
		} while (carryInitializer);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testShiftRightReplicate() throws Exception {
		carryInitializer = false;
		do {
			shiftInstructionHelper(10, 0, 0, true, false);
			shiftInstructionHelper(10, 128, 192, false, false);
			shiftInstructionHelper(10, 64, 32, false, false);
			shiftInstructionHelper(10, 192, 224, false, false);
			shiftInstructionHelper(10, 82, 41, false, false);
			shiftInstructionHelper(10, 1, 0, true, true);
			shiftInstructionHelper(10, 101, 50, false, true);
			shiftInstructionHelper(10, 129, 192, false, true);
			carryInitializer = !carryInitializer;
		} while (carryInitializer);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testShiftRightAll() throws Exception {

		carryInitializer = false;
		shiftInstructionHelper(8, 0, 0, true, false);
		shiftInstructionHelper(8, 128, 64, false, false);
		shiftInstructionHelper(8, 64, 32, false, false);
		shiftInstructionHelper(8, 192, 96, false, false);
		shiftInstructionHelper(8, 82, 41, false, false);
		shiftInstructionHelper(8, 1, 0, true, true);
		shiftInstructionHelper(8, 101, 50, false, true);

		carryInitializer = true;
		shiftInstructionHelper(8, 0, 128, false, false);
		shiftInstructionHelper(8, 128, 192, false, false);
		shiftInstructionHelper(8, 64, 160, false, false);
		shiftInstructionHelper(8, 192, 224, false, false);
		shiftInstructionHelper(8, 82, 169, false, false);
		shiftInstructionHelper(8, 1, 128, false, true);
		shiftInstructionHelper(8, 101, 178, false, true);

	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testRotateRight() throws Exception {
		carryInitializer = false;
		do {
			shiftInstructionHelper(12, 0, 0, true, false);
			shiftInstructionHelper(12, 128, 64, false, false);
			shiftInstructionHelper(12, 64, 32, false, false);
			shiftInstructionHelper(12, 192, 96, false, false);
			shiftInstructionHelper(12, 82, 41, false, false);
			shiftInstructionHelper(12, 1, 128, false, true);
			shiftInstructionHelper(12, 101, 178, false, true);
			carryInitializer = !carryInitializer;
		} while (carryInitializer);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testLoad() throws Exception {
		
		state.setCarry(false);
		state.setZero(false);
		computationInstructionHelper(0, 0x12, 0x00, 0x00, false, false);
		computationInstructionHelper(0, 0x12, 0x34, 0x34, false, false);
		computationInstructionHelper(0, 0x20, 0xff, 0xff, false, false);

		state.setCarry(true);
		state.setZero(true);
		computationInstructionHelper(0, 0x12, 0x00, 0x00, true, true);
		computationInstructionHelper(0, 0x12, 0x34, 0x34, true, true);
		computationInstructionHelper(0, 0x20, 0xff, 0xff, true, true);
		
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testFetch() throws Exception {
		
		state.setCarry(false);
		state.setZero(false);
		computationInstructionHelper(3, 134, 0, 14, false, false);
		computationInstructionHelper(3, 134, 1, 110, false, false);
		computationInstructionHelper(3, 134, 2, 108, false, false);
		
		state.setCarry(true);
		state.setZero(true);
		computationInstructionHelper(3, 134, 0, 14, true, true);
		computationInstructionHelper(3, 134, 1, 110, true, true);
		computationInstructionHelper(3, 134, 2, 108, true, true);
		
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testStore() throws Exception {
		
		assertEquals(110, state.getRamValue(1));
		state.setCarry(false);
		state.setZero(false);
		computationInstructionHelper(23, 55, 1, 55, false, false);
		assertEquals(55, state.getRamValue(1));
		
		state.setRamValue(1, 110);

		assertEquals(110, state.getRamValue(1));
		state.setCarry(true);
		state.setZero(true);
		computationInstructionHelper(23, 55, 1, 55, true, true);
		assertEquals(55, state.getRamValue(1));
		
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testInput() throws Exception {
		
		CountingPortHandler portHandler = new CountingPortHandler();
		portHandler.lastOutputValue = 99;
		state.setPortHandler(portHandler);
		
		state.setCarry(false);
		state.setZero(false);
		
		// note: computationInstructionHelper does *two* input operations (register / immediate address)
		computationInstructionHelper(2, 134, 2, 33, false, false);
		assertEquals(2, portHandler.inputCount);
		assertEquals(0, portHandler.outputCount);
		assertEquals(2, portHandler.lastAddress);
		assertEquals(99, portHandler.lastOutputValue);
		
		computationInstructionHelper(2, 134, 1, 33, false, false);
		assertEquals(4, portHandler.inputCount);
		assertEquals(0, portHandler.outputCount);
		assertEquals(1, portHandler.lastAddress);
		assertEquals(99, portHandler.lastOutputValue);
		
		computationInstructionHelper(2, 134, 0, 33, false, false);
		assertEquals(6, portHandler.inputCount);
		assertEquals(0, portHandler.outputCount);
		assertEquals(0, portHandler.lastAddress);
		assertEquals(99, portHandler.lastOutputValue);
		
		state.setCarry(true);
		state.setZero(true);
		
		computationInstructionHelper(2, 134, 2, 33, true, true);
		assertEquals(8, portHandler.inputCount);
		assertEquals(0, portHandler.outputCount);
		assertEquals(2, portHandler.lastAddress);
		assertEquals(99, portHandler.lastOutputValue);
		
		computationInstructionHelper(2, 134, 1, 33, true, true);
		assertEquals(10, portHandler.inputCount);
		assertEquals(0, portHandler.outputCount);
		assertEquals(1, portHandler.lastAddress);
		assertEquals(99, portHandler.lastOutputValue);
		
		computationInstructionHelper(2, 134, 0, 33, true, true);
		assertEquals(12, portHandler.inputCount);
		assertEquals(0, portHandler.outputCount);
		assertEquals(0, portHandler.lastAddress);
		assertEquals(99, portHandler.lastOutputValue);
		
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testOutput() throws Exception {

		CountingPortHandler portHandler = new CountingPortHandler();
		state.setPortHandler(portHandler);
		
		state.setCarry(false);
		state.setZero(false);
		
		// note: computationInstructionHelper does *two* output operations (register / immediate address)
		portHandler.lastOutputValue = 99;
		computationInstructionHelper(22, 134, 2, 134, false, false);
		assertEquals(0, portHandler.inputCount);
		assertEquals(2, portHandler.outputCount);
		assertEquals(2, portHandler.lastAddress);
		assertEquals(134, portHandler.lastOutputValue);
		
		portHandler.lastOutputValue = 99;
		computationInstructionHelper(22, 134, 1, 134, false, false);
		assertEquals(0, portHandler.inputCount);
		assertEquals(4, portHandler.outputCount);
		assertEquals(1, portHandler.lastAddress);
		assertEquals(134, portHandler.lastOutputValue);
		
		portHandler.lastOutputValue = 99;
		computationInstructionHelper(22, 134, 0, 134, false, false);
		assertEquals(0, portHandler.inputCount);
		assertEquals(6, portHandler.outputCount);
		assertEquals(0, portHandler.lastAddress);
		assertEquals(134, portHandler.lastOutputValue);
		
		state.setCarry(true);
		state.setZero(true);
		
		portHandler.lastOutputValue = 99;
		computationInstructionHelper(22, 134, 2, 134, true, true);
		assertEquals(0, portHandler.inputCount);
		assertEquals(8, portHandler.outputCount);
		assertEquals(2, portHandler.lastAddress);
		assertEquals(134, portHandler.lastOutputValue);
		
		portHandler.lastOutputValue = 99;
		computationInstructionHelper(22, 134, 1, 134, true, true);
		assertEquals(0, portHandler.inputCount);
		assertEquals(10, portHandler.outputCount);
		assertEquals(1, portHandler.lastAddress);
		assertEquals(134, portHandler.lastOutputValue);
		
		portHandler.lastOutputValue = 99;
		computationInstructionHelper(22, 134, 0, 134, true, true);
		assertEquals(0, portHandler.inputCount);
		assertEquals(12, portHandler.outputCount);
		assertEquals(0, portHandler.lastAddress);
		assertEquals(134, portHandler.lastOutputValue);
		
	}
	
	private void jumpTypeInstructionHelper(int primaryOpcode, int conditionCode, boolean zero, boolean carry, int expectedNewPc) throws Exception {
		state.setReturnStackPointer(6);
		state.setPc(60);
		state.setReturnStackPointer(4);
		state.setPc(40);
		state.setReturnStackPointer(5);
		state.setPc(50);
		state.setZero(zero);
		state.setCarry(carry);
		state.performInstruction((primaryOpcode << 13) | (conditionCode << 10) | 0x123);
		assertEquals(expectedNewPc, state.getPc());
		assertEquals(carry, state.isCarry());
		assertEquals(zero, state.isZero());
	}

	private void jumpInstructionHelper(int conditionCode, boolean zero, boolean carry, boolean jump) throws Exception {
		jumpTypeInstructionHelper(26, conditionCode, zero, carry, jump ? 0x123 : 51);
		assertEquals(5, state.getReturnStackPointer());
		assertEquals(40, state.getReturnStack()[4]);
		assertEquals(60, state.getReturnStack()[6]);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testJump() throws Exception {
		
		// unconditional
		for (int jumpCode : unconditionalJumpCodes) {
			for (boolean zero : falseTrue) {
				for (boolean carry : falseTrue) {
					jumpInstructionHelper(jumpCode, zero, carry, true);
				}
			}
		}
		
		// Z, NZ
		for (boolean carry : falseTrue) {
			jumpInstructionHelper(4, false, carry, false);
			jumpInstructionHelper(4, true, carry, true);
			jumpInstructionHelper(5, false, carry, true);
			jumpInstructionHelper(5, true, carry, false);
		}

		// C, NC
		for (boolean zero : falseTrue) {
			jumpInstructionHelper(6, zero, false, false);
			jumpInstructionHelper(6, zero, true, true);
			jumpInstructionHelper(7, zero, false, true);
			jumpInstructionHelper(7, zero, true, false);
		}
		
	}

	private void callInstructionHelper(int conditionCode, boolean zero, boolean carry, boolean jump) throws Exception {
		jumpTypeInstructionHelper(24, conditionCode, zero, carry, jump ? 0x123 : 51);
		if (jump) {
			assertEquals(6, state.getReturnStackPointer());
			assertEquals(50, state.getReturnStack()[5]);
			assertEquals(40, state.getReturnStack()[4]);
		} else {
			assertEquals(5, state.getReturnStackPointer());
			assertEquals(60, state.getReturnStack()[6]);
			assertEquals(40, state.getReturnStack()[4]);
		}
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testCall() throws Exception {
		
		// unconditional
		for (int jumpCode : unconditionalJumpCodes) {
			for (boolean zero : falseTrue) {
				for (boolean carry : falseTrue) {
					callInstructionHelper(jumpCode, zero, carry, true);
				}
			}
		}
		
		// Z, NZ
		for (boolean carry : falseTrue) {
			callInstructionHelper(4, false, carry, false);
			callInstructionHelper(4, true, carry, true);
			callInstructionHelper(5, false, carry, true);
			callInstructionHelper(5, true, carry, false);
		}

		// C, NC
		for (boolean zero : falseTrue) {
			callInstructionHelper(6, zero, false, false);
			callInstructionHelper(6, zero, true, true);
			callInstructionHelper(7, zero, false, true);
			callInstructionHelper(7, zero, true, false);
		}
		
	}

	private void returnInstructionHelper(int conditionCode, boolean zero, boolean carry, boolean jump) throws Exception {
		jumpTypeInstructionHelper(21, conditionCode, zero, carry, jump ? 41 : 51);
		if (jump) {
			assertEquals(4, state.getReturnStackPointer());
			assertEquals(50, state.getReturnStack()[5]);
			assertEquals(60, state.getReturnStack()[6]);
		} else {
			assertEquals(5, state.getReturnStackPointer());
			assertEquals(40, state.getReturnStack()[4]);
			assertEquals(60, state.getReturnStack()[6]);
		}
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testReturn() throws Exception {
		
		// unconditional
		for (int jumpCode : unconditionalJumpCodes) {
			for (boolean zero : falseTrue) {
				for (boolean carry : falseTrue) {
					returnInstructionHelper(jumpCode, zero, carry, true);
				}
			}
		}
		
		// Z, NZ
		for (boolean carry : falseTrue) {
			returnInstructionHelper(4, false, carry, false);
			returnInstructionHelper(4, true, carry, true);
			returnInstructionHelper(5, false, carry, true);
			returnInstructionHelper(5, true, carry, false);
		}

		// C, NC
		for (boolean zero : falseTrue) {
			returnInstructionHelper(6, zero, false, false);
			returnInstructionHelper(6, zero, true, true);
			returnInstructionHelper(7, zero, false, true);
			returnInstructionHelper(7, zero, true, false);
		}
		
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testSetInterrupt() throws Exception {
		state.setPc(5);
		assertFalse(state.isInterruptEnable());
		state.performInstruction(0x3c001);
		assertTrue(state.isInterruptEnable());
		state.performInstruction(0x3c000);
		assertFalse(state.isInterruptEnable());
		assertEquals(7, state.getPc());
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testInterrupt() throws Exception {
		for (boolean interruptEnable : falseTrue) {
			for (boolean zero : falseTrue) {
				for (boolean carry : falseTrue) {
					for (boolean preservedZero : falseTrue) {
						for (boolean preservedCarry : falseTrue) {
							state.setInterruptEnable(interruptEnable);
							state.setZero(zero);
							state.setCarry(carry);
							state.setPreservedZero(preservedZero);
							state.setPreservedCarry(preservedCarry);
							state.setReturnStackPointer(5);
							state.getReturnStack()[5] = 50;
							state.getReturnStack()[6] = 60;
							state.performInterrupt();
							assertFalse(state.isInterruptEnable());
							assertEquals(zero, state.isZero());
							assertEquals(carry, state.isCarry());
							assertEquals(zero, state.isPreservedZero());
							assertEquals(carry, state.isPreservedCarry());
							assertEquals(6, state.getReturnStackPointer());
							assertEquals(50, state.getReturnStack()[5]);
							assertEquals(0x3ff, state.getReturnStack()[6]);
						}
					}
				}
			}
		}
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testReturnInterrupt() throws Exception {
		for (boolean interruptEnable : falseTrue) {
			for (boolean zero : falseTrue) {
				for (boolean carry : falseTrue) {
					for (boolean preservedZero : falseTrue) {
						for (boolean preservedCarry : falseTrue) {
							for (boolean enableAfterReturn : falseTrue) {
								state.setInterruptEnable(interruptEnable);
								state.setZero(zero);
								state.setCarry(carry);
								state.setPreservedZero(preservedZero);
								state.setPreservedCarry(preservedCarry);
								state.setReturnStackPointer(6);
								state.getReturnStack()[5] = 50;
								state.getReturnStack()[6] = 60;
								state.performInstruction(enableAfterReturn ? 0x38001 : 0x38000);
								assertEquals(enableAfterReturn, state.isInterruptEnable());
								assertEquals(preservedZero, state.isZero());
								assertEquals(preservedCarry, state.isCarry());
								assertEquals(preservedZero, state.isPreservedZero());
								assertEquals(preservedCarry, state.isPreservedCarry());
								assertEquals(5, state.getReturnStackPointer());
								assertEquals(50, state.getReturnStack()[5]);
								assertEquals(60, state.getReturnStack()[6]);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * This port handler throws an exception for all input and output operations.
	 */
	private static class ExceptionPortHandler implements IPicoblazePortHandler {

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.IPicoblazePortHandler#handleInput(int)
		 */
		@Override
		public int handleInput(final int address) {
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.IPicoblazePortHandler#handleOutput(int, int)
		 */
		@Override
		public void handleOutput(final int address, final int value) {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * This port handler remembers how often an operation was performed, as well as
	 * the last address and last output value. Its input value is always 33.
	 */
	private static class CountingPortHandler implements IPicoblazePortHandler {

		/**
		 * the inputCount
		 */
		private int inputCount = 0;

		/**
		 * the outputCount
		 */
		private int outputCount = 0;

		/**
		 * the lastAddress
		 */
		private int lastAddress = 0;

		/**
		 * the lastOutputValue
		 */
		private int lastOutputValue = 0;

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.IPicoblazePortHandler#handleInput(int)
		 */
		@Override
		public int handleInput(final int address) {
			inputCount++;
			lastAddress = address;
			return 33;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.IPicoblazePortHandler#handleOutput(int, int)
		 */
		@Override
		public void handleOutput(final int address, final int value) {
			outputCount++;
			lastAddress = address;
			lastOutputValue = value;
		}

	}

}
