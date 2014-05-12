/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.tools;

import java.io.File;
import name.martingeisse.miner.server.util.NbtParser;
import name.martingeisse.miner.server.util.RegionParser;
import name.martingeisse.stackd.common.geometry.SectionId;
import org.apache.commons.lang3.StringUtils;

/**
 * This tool converts the contents of a region file (Minecraft map format) to
 * Stack'd format. Currently assumes that the Stack'd storage format also uses
 * 16^3-sized sections.
 */
public final class RegionConverter {

	/**
	 * the storage
	 */
	// private WorldStorage storage = new WorldStorage(new ClusterSize(4), new File("data/persisted-procedural-world"));
	
	/**
	 * the nbtParser
	 */
	private MyNbtParser nbtParser = new MyNbtParser();

	/**
	 * the regionParser
	 */
	private MyRegionParser regionParser = new MyRegionParser();
	
	/**
	 * the baseChunkX
	 */
	private int baseChunkX;
	
	/**
	 * the baseChunkZ
	 */
	private int baseChunkZ;
	
	/**
	 * the currentChunkX
	 */
	private int currentChunkX;
	
	/**
	 * the currentChunkZ
	 */
	private int currentChunkZ;
	
	/**
	 * the inSections
	 */
	private boolean inSections;
	
	/**
	 * the currentSectionY
	 */
	private int currentSectionY;
	
	/**
	 * The main method
	 * @param args command-line arguments
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("usage: RegionConverter <region-folder>");
			return;
		}
		File regionFolder = new File(args[0]);
		RegionConverter converter = new RegionConverter();
		for (File regionFile : regionFolder.listFiles()) {
			converter.handleRegionFile(regionFile);
		}
	}

	private void handleRegionFile(File regionFile) throws Exception {
		System.out.println("region file: " + regionFile.getName());
		String[] segments = StringUtils.split(regionFile.getName(), '.');
		baseChunkX = Integer.parseInt(segments[1]) * 32;
		baseChunkZ = Integer.parseInt(segments[2]) * 32;
		regionParser.parse(regionFile);
	}
	
	/**
	 * 
	 */
	private class MyRegionParser extends RegionParser {
		
		MyRegionParser() {
			super(nbtParser);
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.RegionParser#onBeforeChunk(int, int)
		 */
		@Override
		protected void onBeforeChunk(int x, int z) {
			currentChunkX = baseChunkX + x;
			currentChunkZ = baseChunkZ + z;
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.RegionParser#onAfterChunk(int, int)
		 */
		@Override
		protected void onAfterChunk(int x, int z) {
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.RegionParser#onEmptyChunk(int, int)
		 */
		@Override
		protected void onEmptyChunk(int x, int z) {
		}
		
	}

	/**
	 * 
	 */
	private class MyNbtParser extends NbtParser {

		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleListStart(java.lang.String, int)
		 */
		@Override
		protected void handleListStart(String tagName, int elementCount) {
			if (tagName.equals("Sections")) {
				inSections = true;
			}
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleListEnd(java.lang.String, int)
		 */
		@Override
		protected void handleListEnd(String tagName, int elementCount) {
			if (tagName.equals("Sections")) {
				inSections = false;
			}
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleByte(java.lang.String, byte)
		 */
		@Override
		protected void handleByte(String tagName, byte value) {
			if (inSections && tagName.equals("Y")) {
				currentSectionY = value;
			}
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleByteArray(java.lang.String, byte[])
		 */
		@Override
		protected void handleByteArray(String tagName, byte[] value) {
			if (inSections && tagName.equals("Blocks")) {
				if (value.length != 16 * 16 * 16) {
					throw new RuntimeException("Blocks tag with invalid size: " + value.length);
				}
				SectionId sectionId = new SectionId(currentChunkX, currentSectionY, currentChunkZ);
				System.out.println("" + sectionId);
				byte[] cubes = new byte[16 * 16 * 16];
				for (int y = 0; y < 16; y++) {
					int sourceBaseY = y * 16 * 16;
					for (int z = 0; z < 16; z++) {
						int sourceBaseYZ = sourceBaseY + z * 16;
						for (int x = 0; x < 16; x++) {
							int sourceIndex = sourceBaseYZ + x;
							int targetIndex = x * 16 * 16 + y * 16 + z;
							cubes[targetIndex] = (byte)((value[sourceIndex] > 0 && value[sourceIndex] != 7) ? 1 : 0);
						}
					}
				}
				// byte[] compressedCubes = SectionDataImageOld.compressCubes(cubes, new ClusterSize(4));
				// storage.saveSection(new ByteArrayInputStream(compressedCubes), sectionId);
			}
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleInt(java.lang.String, int)
		 */
		@Override
		protected void handleInt(String tagName, int value) {
			if (tagName.equals("xPos")) {
				if (value != currentChunkX) {
					throw new RuntimeException("xPos in chunk is incorrect");
				}
			} else if (tagName.equals("zPos")) {
				if (value != currentChunkZ) {
					throw new RuntimeException("zPos in chunk is incorrect");
				}
			}
		}
		
	}
	
}
