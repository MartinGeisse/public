/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.experiment;

import java.io.ByteArrayInputStream;

import name.martingeisse.esdk.components.ascii_gpu.AsciiGpuEmulator;
import name.martingeisse.esdk.util.BidirectionalStreams;

import org.apache.commons.io.output.NullOutputStream;
import org.lwjgl.input.Keyboard;

/**
 * Turns the host computer into an emulator for
 * the ASCII GPU. The host should be connected to
 * an Arduino with a USB-serial-port-cable, and
 * the code on the Arduino should send commands
 * over the cable just as if the real ASCII GPU
 * were on the other side.
 */
public class ArduinoAsciiGpuEmulator {

	/**
	 * the TEST_INPUT
	 */
	// private static final byte[] TEST_INPUT = null;
	// private static final byte[] TEST_INPUT = {00, 0x09, 'A'};
	// private static final byte[] TEST_INPUT = {02, 2, 10, 0x0f, 13, 'H', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
	private static final byte[] TEST_INPUT = {03, 2, 10, 50, 2, 0x0f, 'a'};

	/**
	 * The main method.
	 * @param args command-line arguments (ignored)
	 */
	public static void main(String[] args) {
		AsciiGpuEmulator emulator = null;
		try {

			// connect to the serial port (or stdio for testing)
			BidirectionalStreams clientStreams;
			if (TEST_INPUT != null) {
				clientStreams = new BidirectionalStreams(new ByteArrayInputStream(TEST_INPUT), new NullOutputStream());
			} else {
				throw new RuntimeException("real USB not yet supported");
			}

			// set up the emulator including the GUI
			emulator = new AsciiGpuEmulator(clientStreams);

			// main loop
			emulator.open();
			while (!emulator.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				emulator.respond();
				emulator.performSomeCycles();
			}

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (emulator != null) {
				emulator.close();
			}
		}
		System.exit(0);
	}

}
