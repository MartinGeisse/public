/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.tools;

import java.io.File;
import name.martingeisse.miner.server.util.NbtParser;

/**
 * This tool prints the contents of an NBT file (Minecraft map format) to System.out
 */
public final class NbtDump {

	/**
	 * The main method
	 * @param args command-line arguments
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("usage: NbtDump file.nbt");
			return;
		}
		new NbtParser().parse(new File(args[0]));
	}
}
