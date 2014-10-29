/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.components.ascii_gpu;

import java.io.IOException;
import java.io.InputStream;

import name.martingeisse.esdk.picoblaze.simulator.core.PicoblazeSimulatorException;
import name.martingeisse.esdk.picoblaze.simulator.cycle.InstructionCyclePicoblazeSimulator;
import name.martingeisse.esdk.picoblaze.simulator.port.IPicoblazePortHandler;
import name.martingeisse.esdk.simulation.lwjgl.CharacterMatrixSimulatorGui;
import name.martingeisse.esdk.util.BidirectionalStreams;

/**
 * Emulates the ASCII GPU. The commands are exchanged via a
 * {@link BidirectionalStreams}. The resulting image is displayed
 * using LWJGL. Interpretation of the commands is done using
 * a Picoblaze simulation, so the actual code gets used.
 */
public final class AsciiGpuEmulator extends CharacterMatrixSimulatorGui {

	/**
	 * the clientStreams
	 */
	private final BidirectionalStreams clientStreams;
	
	/**
	 * the picoblazeSimulator
	 */
	private final InstructionCyclePicoblazeSimulator picoblazeSimulator;
	
	/**
	 * the cursorX
	 */
	private int cursorX;
	
	/**
	 * the cursorY
	 */
	private int cursorY;
	
	/**
	 * Constructor.
	 * @param clientStreams the streams used to communicate with the client
	 */
	public AsciiGpuEmulator(BidirectionalStreams clientStreams) {
		super(128, 37);
		this.clientStreams = clientStreams;
		this.picoblazeSimulator = new InstructionCyclePicoblazeSimulator();
		picoblazeSimulator.setInstructionMemory(new AsciiGpuControllerProgramMemory());
		picoblazeSimulator.setPortHandler(new IPicoblazePortHandler() {

			/* (non-Javadoc)
			 * @see name.martingeisse.esdk.picoblaze.simulator.port.IPicoblazePortHandler#handleInput(int)
			 */
			@Override
			public int handleInput(int address) {
				try {
					switch (address) {
					
					case 0x00:
						return (AsciiGpuEmulator.this.clientStreams.getInputStream().available() > 0 ? 1 : 0);
						
					case 0x01: {
						InputStream s = AsciiGpuEmulator.this.clientStreams.getInputStream();
						if (s.available() > 0) {
							return (s.read() & 0xff);
						} else {
							throw new RuntimeException("controller code is trying to read from empty stream");
						}
					}
						
					default:
						throw new RuntimeException("controller code is trying to read from address " + address);
						
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			/* (non-Javadoc)
			 * @see name.martingeisse.esdk.picoblaze.simulator.port.IPicoblazePortHandler#handleOutput(int, int)
			 */
			@Override
			public void handleOutput(int address, int value) {
				switch (address) {

				case 0x02:
					cursorX = value;
					break;
					
				case 0x04:
					cursorY = value;
					break;
					
				case 0x08: {
					int cellValue = getCellValue(cursorX, cursorY);
					cellValue = (cellValue & 0xff) + (value << 8);
					setCellValue(cursorX, cursorY, cellValue);
					break;
				}
					
				case 0x10: {
					int cellValue = getCellValue(cursorX, cursorY);
					cellValue = (cellValue & 0xff00) + value;
					setCellValue(cursorX, cursorY, cellValue);
					break;
				}
					
				default:
					throw new RuntimeException("controller code is trying to write to address " + address);
				
				}
			}
			
		});
	}

	/**
	 * Performs some instruction cycles, then returns. This function should be
	 * called regularly, responding to system events in between to keep the
	 * emulation responsive.
	 */
	public final void performSomeCycles() {
		try {
			picoblazeSimulator.performMultipleInstructionCycles(10000);
		} catch (PicoblazeSimulatorException e) {
			throw new RuntimeException(e);
		}
	}
	
}
