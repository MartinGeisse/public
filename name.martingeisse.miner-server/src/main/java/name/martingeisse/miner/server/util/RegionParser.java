/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.InflaterInputStream;

import name.martingeisse.miner.server.util.NbtParser.SyntaxException;

/**
 * This tool reads the contents of an region file (Minecraft map format) and
 * invokes subclass method for parse events, similar to a SAX parser. It is also
 * parameterized with an {@link NbtParser} that receives events for the actual
 * map chunks.
 * 
 * Note that since the handling of NBT parse events is up to the supplied NBT
 * parser, this class can handle both the Anvil and pre-Anvil formats.
 */
public class RegionParser {

	/**
	 * the nbtParser
	 */
	private final NbtParser nbtParser;
	
	/**
	 * Constructor.
	 * @param nbtParser the NBT parser that handles individual chunks
	 */
	public RegionParser(final NbtParser nbtParser) {
		this.nbtParser = nbtParser;
	}
	
	/**
	 * Parses the specified region file.
	 * 
	 * @param file the file to parse
	 * @throws IOException on I/O errors
	 * @throws SyntaxException on NBT format errors
	 */
	public final void parse(File file) throws IOException, SyntaxException {
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		try {
			int[] chunkLocations = new int[32 * 32];
			int[] chunkLengths = new int[32 * 32];
			for (int i=0; i<32*32; i++) {
				int encodedLocation = randomAccessFile.readInt();
				chunkLocations[i] = ((encodedLocation >> 8) << 12);
				chunkLengths[i] = ((encodedLocation & 255) << 12);
			}
			for (int x=0; x<32; x++) {
				for (int z=0; z<32; z++) {
					int index = (x + z*32);
					if (chunkLengths[index] == 0) {
						onEmptyChunk(x, z);
					} else {
						onBeforeChunk(x, z);
						randomAccessFile.seek(chunkLocations[index]);
						int exactChunkLength = randomAccessFile.readInt();
						int compressionSchemeSelector = randomAccessFile.readByte();
						byte[] compressedData = new byte[exactChunkLength - 1];
						randomAccessFile.read(compressedData);
						if (compressionSchemeSelector == 2) {
							handleZlibCompressedChunk(x, z, compressedData);
						} else {
							throw new RuntimeException("unknown compression scheme selector: " + compressionSchemeSelector);
						}
						onAfterChunk(x, z);
					}
				}
			}
		} finally {
			randomAccessFile.close();
		}
	}

	/**
	 * Called before parsing the NBT data of a chunk.
	 * @param x the x coordinate of the chunk, measured in chunk units relative to the region's start
	 * @param z the z coordinate of the chunk, measured in chunk units relative to the region's start
	 */
	protected void onBeforeChunk(int x, int z) {
	}

	/**
	 * Called after parsing the NBT data of a chunk.
	 * @param x the x coordinate of the chunk, measured in chunk units relative to the region's start
	 * @param z the z coordinate of the chunk, measured in chunk units relative to the region's start
	 */
	protected void onAfterChunk(int x, int z) {
	}

	/**
	 * Called for chunks without associated NBT data. Note that {@link #onBeforeChunk(int, int)} and
	 * {@link #onAfterChunk(int, int)} are NOT called for such chunks automatically.
	 * @param x the x coordinate of the chunk, measured in chunk units relative to the region's start
	 * @param z the z coordinate of the chunk, measured in chunk units relative to the region's start
	 */
	protected void onEmptyChunk(int x, int z) {
	}

	/**
	 * Decompresses and reads a Zlib-compressed chunk at the specified position.
	 */
	private void handleZlibCompressedChunk(int x, int z, byte[] data) throws IOException, SyntaxException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream);
		nbtParser.parse(inflaterInputStream);
	}
	
}
