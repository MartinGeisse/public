/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.common;

import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.cubetype.EmptyCubeType;
import name.martingeisse.stackd.common.cubetype.SlabCubeType;
import name.martingeisse.stackd.common.cubetype.SolidOpaqueCubeType;
import name.martingeisse.stackd.common.cubetype.StairsCubeType;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;

/**
 * Defines the cube types.
 */
public final class MinerCubeTypes {

	/**
	 * names of the texture files for cubes
	 */
	public static final String[] CUBE_TEXTURE_FILENAMES = {
		"invalid.png", // 0
		"stone.png", 
		"dirt.png",
		"grass.png",
		"grass-side.png", // 4
		"pattern.png",
		"leaves.png",
		"wood.png",
		"wood-circular.png", // 8
		"wood-bars.png",
		"water.png",
		"sand.png",
		"lava.png", // 12
		"gravel.png",
		"stone-and-gold.png",
		"stone-and-coal.png",
		"snow.png", // 16
		"sandstone-block.png",
		"sandstone-bricks.png",
		"dead-shrub.png",
		"tile.png", // 20
		"tile-half-height.png",
		"four-tiles.png",
		
//		"bricks1.png",
//		"bricks1-mossy.png", // 4
//		"ice.png",
//		"sandstone-block.png",
//		"sandstone-bricks.png",
//		"sponge.png",
//		"stone-and-diamond.png",
//		"stone-and-emerald.png",
//		"stone-and-ruby.png", // 16
//		"stone-and-sapphire.png",
//		"tile.png",
//		"wood.png",
	};

	/**
	 * the cube types
	 */
	public static final CubeType[] CUBE_TYPES = {
		new EmptyCubeType(), // 0: empty space
		new SolidOpaqueCubeType(1), // 1: stone
		new SolidOpaqueCubeType(new int[] {4, 4, 2, 3, 4, 4}), // 2: grass
		new SolidOpaqueCubeType(2), // 3: dirt
		new SolidOpaqueCubeType(5), // 4: TODO cobblestone
		new SolidOpaqueCubeType(9), // 5: wood planks
		new SolidOpaqueCubeType(5), // 6: invalid (MC: saplings)
		new SolidOpaqueCubeType(1), // 7: stone (MC: bedrock)
		new SolidOpaqueCubeType(10), // 8: flowing water
		new SolidOpaqueCubeType(10), // 9: still water
		new SolidOpaqueCubeType(12), // 10: flowing lava
		new SolidOpaqueCubeType(12), // 11: still lava
		new SolidOpaqueCubeType(11), // 12: sand
		new SolidOpaqueCubeType(13), // 13: gravel
		new SolidOpaqueCubeType(14), // 14: gold ore
		new SolidOpaqueCubeType(5), // 15: TODO: iron ore
		new SolidOpaqueCubeType(15), // 16: coal ore
		new SolidOpaqueCubeType(new int[] {7, 7, 8, 8, 7, 7}), // 17: wood
		new SolidOpaqueCubeType(6), // 18: leaves
		new SolidOpaqueCubeType(5), // 19: invalid TODO sponge
		new SolidOpaqueCubeType(5), // 20: invalid TODO glass
		new SolidOpaqueCubeType(5), // 21: invalid TODO lapis lazuli ore
		new SolidOpaqueCubeType(5), // 22: invalid TODO lapis lazuli block
		new SolidOpaqueCubeType(5), // 23: invalid (MC: dispenser)
		new SolidOpaqueCubeType(new int[] {18, 18, 17, 17, 18, 18}), // 24: sandstone
		new SolidOpaqueCubeType(5), // 25: invalid (MC: note block)
		new SolidOpaqueCubeType(5), // 26: invalid
		new SolidOpaqueCubeType(5), // 27: invalid
		new SolidOpaqueCubeType(5), // 28: invalid
		new SolidOpaqueCubeType(5), // 29: invalid
		new SolidOpaqueCubeType(5), // 30: invalid
		// new DiagonalCrossCubeType(19), // 31: dead shrub
		new SlabCubeType(21, 20), // 31: dead shrub TODO
		new SolidOpaqueCubeType(5), // 32: invalid
		new SolidOpaqueCubeType(5), // 33: invalid
		new SolidOpaqueCubeType(5), // 34: invalid
		new SolidOpaqueCubeType(16), // 35: invalid TODO wool
		new SolidOpaqueCubeType(5), // 36: invalid
		new SolidOpaqueCubeType(5), // 37: invalid TODO dandelion
		new SolidOpaqueCubeType(5), // 38: invalid
		new SolidOpaqueCubeType(5), // 39: invalid
		new SolidOpaqueCubeType(5), // 40: invalid TODO red mushroom
		new SolidOpaqueCubeType(5), // 41: invalid
		new SolidOpaqueCubeType(5), // 42: invalid
		new SolidOpaqueCubeType(5), // 43: invalid
		new SolidOpaqueCubeType(5), // 44: invalid
		new SolidOpaqueCubeType(5), // 45: invalid
		new SolidOpaqueCubeType(5), // 46: invalid
		new SolidOpaqueCubeType(5), // 47: invalid
		new SolidOpaqueCubeType(5), // 48: invalid
		new SolidOpaqueCubeType(5), // 49: invalid
		new StairsCubeType(AxisAlignedDirection.NEGATIVE_X, 21, 20, 22, 20, 22), // 50: invalid TODO used for test
		new StairsCubeType(AxisAlignedDirection.POSITIVE_X, 21, 20, 22, 20, 22), // 51: invalid TODO used for test
		new StairsCubeType(AxisAlignedDirection.NEGATIVE_Z, 21, 20, 22, 20, 22), // 52: invalid TODO used for test
		new StairsCubeType(AxisAlignedDirection.POSITIVE_Z, 21, 20, 22, 20, 22), // 53: invalid TODO used for test
		new SolidOpaqueCubeType(5), // 54: invalid
		new SolidOpaqueCubeType(5), // 55: invalid
		new SolidOpaqueCubeType(5), // 56: invalid
		new SolidOpaqueCubeType(5), // 57: invalid
		new SolidOpaqueCubeType(5), // 58: invalid
		new SolidOpaqueCubeType(5), // 59: invalid
		new SolidOpaqueCubeType(5), // 60: invalid
		new SolidOpaqueCubeType(5), // 61: invalid
		new SolidOpaqueCubeType(5), // 62: invalid
		new SolidOpaqueCubeType(5), // 63: invalid
		new SolidOpaqueCubeType(5), // 64: invalid
		new SolidOpaqueCubeType(5), // 65: invalid
		new SolidOpaqueCubeType(5), // 66: invalid
		new SolidOpaqueCubeType(5), // 67: invalid
		new SolidOpaqueCubeType(5), // 68: invalid
		new SolidOpaqueCubeType(5), // 69: invalid
		new SolidOpaqueCubeType(5), // 70: invalid
		new SolidOpaqueCubeType(5), // 71: invalid
		new SolidOpaqueCubeType(5), // 72: invalid
		new SolidOpaqueCubeType(5), // 73: invalid
		new SolidOpaqueCubeType(5), // 74: invalid
		new SolidOpaqueCubeType(5), // 75: invalid
		new SolidOpaqueCubeType(5), // 76: invalid
		new SolidOpaqueCubeType(5), // 77: invalid
		new SolidOpaqueCubeType(16), // 78: snow
		new SolidOpaqueCubeType(5), // 79: invalid
		new SolidOpaqueCubeType(16), // 80: snow block
		new SolidOpaqueCubeType(5), // 81: invalid
		new SolidOpaqueCubeType(5), // 82: invalid
		new SolidOpaqueCubeType(5), // 83: invalid
		new SolidOpaqueCubeType(5), // 84: invalid
		new SolidOpaqueCubeType(5), // 85: invalid
		new SolidOpaqueCubeType(5), // 86: invalid
		new SolidOpaqueCubeType(5), // 87: invalid
		new SolidOpaqueCubeType(5), // 88: invalid
		new SolidOpaqueCubeType(5), // 89: invalid
		new SolidOpaqueCubeType(5), // 90: invalid
		new SolidOpaqueCubeType(5), // 91: invalid
		new SolidOpaqueCubeType(5), // 92: invalid
		new SolidOpaqueCubeType(5), // 93: invalid
		new SolidOpaqueCubeType(5), // 94: invalid
		new SolidOpaqueCubeType(5), // 95: invalid
		new SolidOpaqueCubeType(5), // 96: invalid
		new SolidOpaqueCubeType(5), // 97: invalid
		new SolidOpaqueCubeType(5), // 98: invalid
		new SolidOpaqueCubeType(5), // 99: invalid
		new SolidOpaqueCubeType(5), // 100: invalid
		new SolidOpaqueCubeType(5), // 101: invalid
		new SolidOpaqueCubeType(5), // 102: invalid
		new SolidOpaqueCubeType(5), // 103: invalid
		new SolidOpaqueCubeType(5), // 104: invalid
		new SolidOpaqueCubeType(5), // 105: invalid
		new SolidOpaqueCubeType(5), // 106: invalid
		new SolidOpaqueCubeType(5), // 107: invalid
		new SolidOpaqueCubeType(5), // 108: invalid
		new SolidOpaqueCubeType(5), // 109: invalid
		new SolidOpaqueCubeType(5), // 110: invalid
		new SolidOpaqueCubeType(5), // 111: invalid
		new SolidOpaqueCubeType(5), // 112: invalid
		new SolidOpaqueCubeType(5), // 113: invalid
		new SolidOpaqueCubeType(5), // 114: invalid
		new SolidOpaqueCubeType(5), // 115: invalid
		new SolidOpaqueCubeType(5), // 116: invalid
		new SolidOpaqueCubeType(5), // 117: invalid
		new SolidOpaqueCubeType(5), // 118: invalid
		new SolidOpaqueCubeType(5), // 119: invalid
		new SolidOpaqueCubeType(5), // 120: invalid
		new SolidOpaqueCubeType(5), // 121: invalid
		new SolidOpaqueCubeType(5), // 122: invalid
		new SolidOpaqueCubeType(5), // 123: invalid
		new SolidOpaqueCubeType(5), // 124: invalid
		new SolidOpaqueCubeType(5), // 125: invalid
		new SolidOpaqueCubeType(5), // 126: invalid
		new SolidOpaqueCubeType(5), // 127: invalid
		new SolidOpaqueCubeType(new int[] {18, 18, 17, 17, 18, 18}), // 128: TODO sandstone stairs
		new SolidOpaqueCubeType(5), // 129: invalid
		new SolidOpaqueCubeType(5), // 130: invalid
		new SolidOpaqueCubeType(5), // 131: invalid
		new SolidOpaqueCubeType(5), // 132: invalid
		new SolidOpaqueCubeType(5), // 133: invalid
		new SolidOpaqueCubeType(5), // 134: invalid
		new SolidOpaqueCubeType(5), // 135: invalid
		new SolidOpaqueCubeType(5), // 136: invalid
		new SolidOpaqueCubeType(5), // 137: invalid
		new SolidOpaqueCubeType(5), // 138: invalid
		new SolidOpaqueCubeType(5), // 139: invalid
		new SolidOpaqueCubeType(5), // 140: invalid
		new SolidOpaqueCubeType(5), // 141: invalid
		new SolidOpaqueCubeType(5), // 142: invalid
		new SolidOpaqueCubeType(5), // 143: invalid
		new SolidOpaqueCubeType(5), // 144: invalid
		new SolidOpaqueCubeType(5), // 145: invalid
		new SolidOpaqueCubeType(5), // 146: invalid
		new SolidOpaqueCubeType(5), // 147: invalid
		new SolidOpaqueCubeType(5), // 148: invalid
		new SolidOpaqueCubeType(5), // 149: invalid
		new SolidOpaqueCubeType(5), // 150: invalid
		new SolidOpaqueCubeType(5), // 151: invalid
		new SolidOpaqueCubeType(5), // 152: invalid
		new SolidOpaqueCubeType(5), // 153: invalid
		new SolidOpaqueCubeType(5), // 154: invalid
		new SolidOpaqueCubeType(5), // 155: invalid
		new SolidOpaqueCubeType(5), // 156: invalid
		new SolidOpaqueCubeType(5), // 157: invalid
		new SolidOpaqueCubeType(5), // 158: invalid
		new SolidOpaqueCubeType(5), // 159: invalid
		new SolidOpaqueCubeType(5), // 160: invalid
		new SolidOpaqueCubeType(5), // 161: invalid
		new SolidOpaqueCubeType(5), // 162: invalid
		new SolidOpaqueCubeType(5), // 163: invalid
		new SolidOpaqueCubeType(5), // 164: invalid
		new SolidOpaqueCubeType(5), // 165: invalid
		new SolidOpaqueCubeType(5), // 166: invalid
		new SolidOpaqueCubeType(5), // 167: invalid
		new SolidOpaqueCubeType(5), // 168: invalid
		new SolidOpaqueCubeType(5), // 169: invalid
		new SolidOpaqueCubeType(5), // 170: invalid
		new SolidOpaqueCubeType(5), // 171: invalid
		new SolidOpaqueCubeType(5), // 172: invalid
		new SolidOpaqueCubeType(5), // 173: invalid
		new SolidOpaqueCubeType(5), // 174: invalid
		new SolidOpaqueCubeType(5), // 175: invalid
		new SolidOpaqueCubeType(5), // 176: invalid
		new SolidOpaqueCubeType(5), // 177: invalid
		new SolidOpaqueCubeType(5), // 178: invalid
		new SolidOpaqueCubeType(5), // 179: invalid
		new SolidOpaqueCubeType(5), // 180: invalid
		new SolidOpaqueCubeType(5), // 181: invalid
		new SolidOpaqueCubeType(5), // 182: invalid
		new SolidOpaqueCubeType(5), // 183: invalid
		new SolidOpaqueCubeType(5), // 184: invalid
		new SolidOpaqueCubeType(5), // 185: invalid
		new SolidOpaqueCubeType(5), // 186: invalid
		new SolidOpaqueCubeType(5), // 187: invalid
		new SolidOpaqueCubeType(5), // 188: invalid
		new SolidOpaqueCubeType(5), // 189: invalid
		new SolidOpaqueCubeType(5), // 190: invalid
		new SolidOpaqueCubeType(5), // 191: invalid
		new SolidOpaqueCubeType(5), // 192: invalid
		new SolidOpaqueCubeType(5), // 193: invalid
		new SolidOpaqueCubeType(5), // 194: invalid
		new SolidOpaqueCubeType(5), // 195: invalid
		new SolidOpaqueCubeType(5), // 196: invalid
		new SolidOpaqueCubeType(5), // 197: invalid
		new SolidOpaqueCubeType(5), // 198: invalid
		new SolidOpaqueCubeType(5), // 199: invalid
		new SolidOpaqueCubeType(5), // 200: invalid
		new SolidOpaqueCubeType(5), // 201: invalid
		new SolidOpaqueCubeType(5), // 202: invalid
		new SolidOpaqueCubeType(5), // 203: invalid
		new SolidOpaqueCubeType(5), // 204: invalid
		new SolidOpaqueCubeType(5), // 205: invalid
		new SolidOpaqueCubeType(5), // 206: invalid
		new SolidOpaqueCubeType(5), // 207: invalid
		new SolidOpaqueCubeType(5), // 208: invalid
		new SolidOpaqueCubeType(5), // 209: invalid
		new SolidOpaqueCubeType(5), // 210: invalid
		new SolidOpaqueCubeType(5), // 211: invalid
		new SolidOpaqueCubeType(5), // 212: invalid
		new SolidOpaqueCubeType(5), // 213: invalid
		new SolidOpaqueCubeType(5), // 214: invalid
		new SolidOpaqueCubeType(5), // 215: invalid
		new SolidOpaqueCubeType(5), // 216: invalid
		new SolidOpaqueCubeType(5), // 217: invalid
		new SolidOpaqueCubeType(5), // 218: invalid
		new SolidOpaqueCubeType(5), // 219: invalid
		new SolidOpaqueCubeType(5), // 220: invalid
		new SolidOpaqueCubeType(5), // 221: invalid
		new SolidOpaqueCubeType(5), // 222: invalid
		new SolidOpaqueCubeType(5), // 223: invalid
		new SolidOpaqueCubeType(5), // 224: invalid
		new SolidOpaqueCubeType(5), // 225: invalid
		new SolidOpaqueCubeType(5), // 226: invalid
		new SolidOpaqueCubeType(5), // 227: invalid
		new SolidOpaqueCubeType(5), // 228: invalid
		new SolidOpaqueCubeType(5), // 229: invalid
		new SolidOpaqueCubeType(5), // 230: invalid
		new SolidOpaqueCubeType(5), // 231: invalid
		new SolidOpaqueCubeType(5), // 232: invalid
		new SolidOpaqueCubeType(5), // 233: invalid
		new SolidOpaqueCubeType(5), // 234: invalid
		new SolidOpaqueCubeType(5), // 235: invalid
		new SolidOpaqueCubeType(5), // 236: invalid
		new SolidOpaqueCubeType(5), // 237: invalid
		new SolidOpaqueCubeType(5), // 238: invalid
		new SolidOpaqueCubeType(5), // 239: invalid
		new SolidOpaqueCubeType(5), // 240: invalid
		new SolidOpaqueCubeType(5), // 241: invalid
		new SolidOpaqueCubeType(5), // 242: invalid
		new SolidOpaqueCubeType(5), // 243: invalid
		new SolidOpaqueCubeType(5), // 244: invalid
		new SolidOpaqueCubeType(5), // 245: invalid
		new SolidOpaqueCubeType(5), // 246: invalid
		new SolidOpaqueCubeType(5), // 247: invalid
		new SolidOpaqueCubeType(5), // 248: invalid
		new SolidOpaqueCubeType(5), // 249: invalid
		new SolidOpaqueCubeType(5), // 250: invalid
		new SolidOpaqueCubeType(5), // 251: invalid
		new SolidOpaqueCubeType(5), // 252: invalid
		new SolidOpaqueCubeType(5), // 253: invalid
		new SolidOpaqueCubeType(5), // 254: invalid
		new SolidOpaqueCubeType(5), // 255: invalid
	};

	/**
	 * Prevent instantiation.
	 */
	private MinerCubeTypes() {
	}

}
