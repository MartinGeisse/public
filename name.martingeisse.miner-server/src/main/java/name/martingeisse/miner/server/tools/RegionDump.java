/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.tools;

import java.io.File;

import name.martingeisse.miner.server.util.NbtParser;
import name.martingeisse.miner.server.util.RegionParser;

/**
 * This tool prints the contents of a region file (Minecraft map format) to System.out
 */
public final class RegionDump {

	/**
	 * The main method
	 * @param args command-line arguments
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("usage: RegionDump region.mca");
			return;
		}
		new RegionParser(new NbtParser()).parse(new File(args[0]));
	}
	
}
