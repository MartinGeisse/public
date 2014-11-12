/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common;

import java.io.ByteArrayOutputStream;
import name.martingeisse.stackd.common.geometry.ClusterSize;

/**
 * Common constants for the Stack'd engine.
 */
public class StackdConstants {

	/**
	 * Whenever "detailed" coordinates are needed, fixed-point numbers
	 * are used that are represented using integers. Each cube, whose normal
	 * size is 1, is {@link #GEOMETRY_DETAIL_FACTOR} units wide in detail
	 * coordinates.
	 * 
	 * Detail coordinates are only used in exceptional cases, so whenever
	 * a method comment doesn't mention them, you can assume that the method
	 * uses normal coordinates (1 unit per cube). An example where detail
	 * coordinates are used is building the polygon meshes for "detailed"
	 * cube types. Using integer computations brings a surprising performance
	 * boost for them.
	 */
	public static final int GEOMETRY_DETAIL_FACTOR = 8;
	
	/**
	 * This value is the log2 of {@link #GEOMETRY_DETAIL_FACTOR} and can
	 * be used for bit shifting.
	 */
	public static final int GEOMETRY_DETAIL_SHIFT = 3;

	/**
	 * A {@link ClusterSize} for {@link #GEOMETRY_DETAIL_FACTOR}.
	 */
	public static final ClusterSize GEOMETRY_DETAIL_CLUSTER_SIZE = new ClusterSize(GEOMETRY_DETAIL_SHIFT);
	
	/**
	 * The DEFLATE dictionary used for compressing interactive section data.
	 */
	public static final byte[] INTERACTIVE_SECTION_DATA_COMPRESSION_DICTIONARY;
	static {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		// TODO reverse order (0xff first) might shorten the pointers and improve compression!
		for (int i=0; i<30; i++) {
			s.write(0x00);
		}
		for (int i=0; i<10; i++) {
			s.write(0x09);
		}
		for (int i=0; i<1000; i++) {
			s.write(0xff);
		}
		INTERACTIVE_SECTION_DATA_COMPRESSION_DICTIONARY = s.toByteArray();
	}

	/**
	 * Prevent instantiation.
	 */
	private StackdConstants() {
	}
	
}
