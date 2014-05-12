/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.network;

import java.io.DataOutput;
import java.nio.charset.StandardCharsets;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * Represents packets that are sent between client and server.
 * The packet consists of header data (including the packet
 * type) and a body that is treated as a buffer by this class.
 * 
 * Conceptually, header and body are separate. Implementation-wise
 * this class uses a single buffer that contains the body and
 * leaves room for header fields. For received packets, the
 * header data is stored redundantly in the buffer and the fields
 * of this class. For sent packets, this class encodes the header
 * fields into the buffer when sending.
 *
 * The packet type tells which kind of packet this is. Packet
 * types are 16-bit unsigned integers (0x0000 through 0xFFFF),
 * with packet types in the range 0xFF00 through 0xFFFF being
 * reserved for special protocol packets.
 * 
 * The total packet size (and thus buffer size) must not
 * exceed 255 bytes.
 * 
 * Wire format for a packet:
 * - body size (unsigned byte)
 * - type (unsigned short)
 * - body (bytes)
 * 
 * When sending a packet, the readable bytes determine the
 * packet's contents (header and body), with the header being
 * assembled in the buffer on-the-fly.
 * 
 * When receiving a packet, the header is consumed by the
 * receiver logic and the readable bytes are set up to contain
 * the packet body.
 */
public final class StackdPacket {

	/**
	 * The number of bytes for the header.
	 */
	public static final int HEADER_SIZE = 4;
	
	/**
	 * The maximum size for the body of a single packet.
	 */
	public static final int MAX_BODY_SIZE = 65535;
	
	/**
	 * The maximum size for a whole packet.
	 */
	public static final int MAX_PACKET_SIZE = HEADER_SIZE + MAX_BODY_SIZE;
	
	/**
	 * The type constant for "hello" packets.
	 */
	public static final int TYPE_HELLO = 0xff00;

	/**
	 * The type constant for "JSON API" packets.
	 */
	public static final int TYPE_JSON_API = 0xff01;

	/**
	 * The type constant for flash message packets (server to client only).
	 */
	public static final int TYPE_FLASH_MESSAGE = 0xff02;
	
	/**
	 * The type constant for single-section modification events.
	 * Sent by the server when a section gets modified. Clients that
	 * are close enough to be interested in the update would typically
	 * request the new section render model and collider in turn. Clients
	 * that are too far away would ignore these events. TODO: store the
	 * client's rough position on the server, filter mod events
	 * server-side, then just send the updated objects. This slightly
	 * increases network traffic (sending unnecessary data) but reduces
	 * latency and simplifies the code.
	 */
	public static final int TYPE_SINGLE_SECTION_MODIFICATION_EVENT = 0xff05;

	/**
	 * When sent from the client to the server, this packet contains a console
	 * command to be executed by the server, encoded as a sequence of strings
	 * (see {@link DataOutput#writeUTF(String)}). When sent from the server
	 * to the client, this packet contains output lines to print on the console.
	 * Only complete lines can be output this way.
	 */
	public static final int TYPE_CONSOLE = 0xff06;
	
	/**
	 * Base request/response code for all section data objects.
	 */
	public static final int TYPE_SINGLE_SECTION_DATA_BASE = 0xff10;
	
	/**
	 * Request/response code to fetch section data objects of type
	 * {@link SectionDataType#DEFINITIVE}.
	 */
	public static final int TYPE_SINGLE_SECTION_DATA_DEFINITIVE = 0xff10;
	
	/**
	 * Request/response code to fetch section data objects of type
	 * {@link SectionDataType#INTERACTIVE}.
	 */
	public static final int TYPE_SINGLE_SECTION_DATA_INTERACTIVE = 0xff11;
	
	/**
	 * Request/response code to fetch section data objects of type
	 * {@link SectionDataType#VIEW_LOD_0}.
	 */
	public static final int TYPE_SINGLE_SECTION_DATA_VIEW_LOD_0 = 0xff12;

	/**
	 * The type constant for cube modification packets (client to server only).
	 * TODO this is not a protocol packet, move this to application code
	 */
	public static final int TYPE_CUBE_MODIFICATION = 0xfe00;

	/**
	 * the type
	 */
	private int type;
	
	/**
	 * the buffer
	 */
	private final ChannelBuffer buffer;
	
	/**
	 * Constructor for a new packet with an empty buffer.
	 * @param type the packet type
	 * @param bodySize the size of the packet body
	 */
	public StackdPacket(int type, int bodySize) {
		this(type, ChannelBuffers.buffer(HEADER_SIZE + bodySize), true);
	}
	
	/**
	 * Constructor that specifies the type and buffer explicitly.
	 * 
	 * This constructor allows to optionally write a header full of
	 * zero-bytes. Callers *must* make sure that the buffer contains
	 * space for the header in the finished buffer; they can either
	 * do that by telling this constructor to do it or do it
	 * themselves.
	 * 
	 * @param type the packet type
	 * @param buffer the buffer for header and body
	 * @param writeZeroHeader whether this class shall write zeroes for the header
	 */
	public StackdPacket(int type, ChannelBuffer buffer, boolean writeZeroHeader) {
		this.type = type;
		this.buffer = buffer;
		if (writeZeroHeader) {
			buffer.writeZero(HEADER_SIZE);
		}
	}

	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Setter method for the type.
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Getter method for the buffer.
	 * @return the buffer
	 */
	public ChannelBuffer getBuffer() {
		return buffer;
	}
	
	/**
	 * Encodes header fields into the buffer. Also sets the reader index to the
	 * beginning and the writer index to the end of the buffer.
	 */
	public void encodeHeader() {
		
		// determine packet size and make sure we can accept it
		int packetSize = buffer.readableBytes();
		if (packetSize < HEADER_SIZE) {
			throw new IllegalArgumentException("buffer is too small for header, readable bytes: " + packetSize);
		}
		if (packetSize > MAX_PACKET_SIZE) {
			throw new IllegalArgumentException("packet is too large, size: " + packetSize);
		}
		
		// assemble header fields
		int previousWriterIndex = buffer.writerIndex();
		buffer.writerIndex(buffer.readerIndex());
		buffer.writeShort(packetSize - HEADER_SIZE);
		buffer.writeShort(type);
		buffer.writerIndex(previousWriterIndex);
		
	}

	/**
	 * Same as encodeJson(TYPE_JSON_API, jsonData).
	 * 
	 * @param jsonData the data to JSON-encode
	 * @return the packet containing the encoded data
	 */
	public static StackdPacket encodeJson(Object jsonData) {
		return encodeJson(TYPE_JSON_API, jsonData);
	}

	/**
	 * Creates a packet that contains JSON-encoded, then UTF-8 encoded
	 * data.
	 * 
	 * @param type the packet type
	 * @param jsonData the data to JSON-encode
	 * @return the packet containing the encoded data
	 */
	public static StackdPacket encodeJson(int type, Object jsonData) {
		// TODO: reserve StackdPacket.HEADER_SIZE bytes for the packet header
		// String json = JSONValue.toJSONString(jsonData);
		// byte[] binary = json.getBytes(StandardCharsets.UTF_8);
		// return new StackdPacket(type, ChannelBuffers.wrappedBuffer(binary));
		throw new RuntimeException();
	}

}
