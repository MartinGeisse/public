/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.network;

import name.martingeisse.stackd.common.network.StackdPacket;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * A builder for the packet type that is sent to tell the
 * server about cube modifications. One packet can contain
 * an arbitrary number of modifications. Data density is
 * best for single-cube or sparse modifications though,
 * not to replace whole sections.
 */
public final class CubeModificationPacketBuilder {

	/**
	 * the packet
	 */
	private final StackdPacket packet;
	
	/**
	 * the buffer
	 */
	private final ChannelBuffer buffer;


	/**
	 * Constructor.
	 */
	public CubeModificationPacketBuilder() {
		this(ChannelBuffers.dynamicBuffer());
	}
	
	/**
	 * Constructor.
	 * @param sizeEstimate an estimate for the buffer size
	 */
	public CubeModificationPacketBuilder(int sizeEstimate) {
		this(ChannelBuffers.dynamicBuffer(sizeEstimate));
	}

	/**
	 * Constructor.
	 * @param buffer the buffer to use
	 */
	public CubeModificationPacketBuilder(final ChannelBuffer buffer) {
		this.buffer = buffer;
		this.packet = new StackdPacket(StackdPacket.TYPE_CUBE_MODIFICATION, buffer, true);
	}
	
	/**
	 * Adds a modification entry to this packet.
	 * 
	 * @param x the x position in the world
	 * @param y the y position in the world
	 * @param z the z position in the world
	 * @param newCubeTypeIndex the new cube type index that was placed at that position 
	 */
	public void addModification(int x, int y, int z, byte newCubeTypeIndex) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeByte(newCubeTypeIndex);
	}
	
	/**
	 * Getter method for the packet.
	 * @return the packet
	 */
	public StackdPacket getPacket() {
		return packet;
	}

	/**
	 * Builds a cube modification packet for a single-cube modification.
	 * @param x the x position in the world
	 * @param y the y position in the world
	 * @param z the z position in the world
	 * @param newCubeTypeIndex the new cube type index that was placed at that position 
	 * @return the packet
	 */
	public static StackdPacket buildForSingleCube(int x, int y, int z, byte newCubeTypeIndex) {
		CubeModificationPacketBuilder builder = new CubeModificationPacketBuilder(ChannelBuffers.buffer(13 + StackdPacket.HEADER_SIZE));
		builder.addModification(x, y, z, newCubeTypeIndex);
		return builder.getPacket();
	}

}
