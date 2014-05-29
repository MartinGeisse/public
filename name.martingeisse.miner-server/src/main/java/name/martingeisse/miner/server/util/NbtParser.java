/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This tool reads the contents of an NBT file (Minecraft map format) and
 * invokes subclass method for parse events, similar to a SAX parser.
 */
public class NbtParser {

	/**
	 * the dataInput
	 */
	private DataInput dataInput;
	
	/**
	 * Constructor.
	 */
	public NbtParser() {
	}
	
	/**
	 * Parses the specified NBT file.
	 * 
	 * The file should contain one main tag; if it doesn't then this method throws
	 * a {@link SyntaxException}.
	 * 
	 * @param file the file to parse
	 * @throws IOException on I/O errors
	 * @throws SyntaxException on format errors
	 */
	public final void parse(File file) throws IOException, SyntaxException {
		try (FileInputStream inputStream = new FileInputStream(file)) {
			parse(inputStream);
			if (inputStream.read() != -1) {
				throw new SyntaxException("NBT file contains extra bytes after the main tag");
			}
		}
	}

	/**
	 * Parses the specified input stream. The stream should contain one main tag;
	 * if it doesn't, only one main tag is parsed and the rest of the stream is
	 * not read.
	 * 
	 * While parsing is in process, this parser object must not be used
	 * to parse other files recursively.
	 * 
	 * The input stream is not closed by this method.
	 * 
	 * @param inputStream the stream to parse
	 * @throws IOException on I/O errors
	 * @throws SyntaxException on format errors
	 */
	public final void parse(InputStream inputStream) throws IOException, SyntaxException {
		DataInput dataInput = new DataInputStream(inputStream);
		parse(dataInput);
	}

	/**
	 * Parses the specified data input. The data input should contain one main tag;
	 * if it doesn't, only one main tag is parsed and the rest of the data input is
	 * not read.
	 * 
	 * While parsing is in process, this parser object must not be used
	 * to parse other files recursively.
	 * 
	 * The data input is not closed by this method.
	 * 
	 * @param dataInput the input to parse
	 * @throws IOException on I/O errors
	 * @throws SyntaxException on format errors
	 */
	public final void parse(DataInput dataInput) throws IOException, SyntaxException {
		this.dataInput = dataInput;
		try {
			parseTag();
		} finally {
			this.dataInput = null;
		}
	}
	
	/**
	 * Parses a single tag. Returns true for a normal tag, false for a closing tag.
	 */
	private final boolean parseTag() throws IOException, SyntaxException {
		int tagType = dataInput.readByte();
		if (tagType < 0) {
			throw new SyntaxException("expected opening tag, found EOF");
		}
		if (tagType == 0) {
			return false;
		}
		String tagName = dataInput.readUTF();
		handleTag(tagType, tagName);
		return true;
	}

	/**
	 * Handles the data for a tag. The tag byte itself and the tag name are not parsed
	 * but given as arguments to this method. Cannot handle END tags.
	 * 
	 * This method is used when parsing a normal tag, but also for the repeated tags
	 * inside a LIST tag.
	 */
	private final void handleTag(int tagType, String tagName) throws IOException, SyntaxException {
		switch (tagType) {
		
		case 1:
			handleByte(tagName, dataInput.readByte());
			break;
			
		case 2:
			handleShort(tagName, dataInput.readShort());
			break;
			
		case 3:
			handleInt(tagName, dataInput.readInt());
			break;
			
		case 4:
			handleLong(tagName, dataInput.readLong());
			break;
			
		case 5:
			handleFloat(tagName, dataInput.readFloat());
			break;
			
		case 6:
			handleDouble(tagName, dataInput.readDouble());
			break;
			
		case 7: {
			int length = dataInput.readInt();
			byte[] data = new byte[length];
			dataInput.readFully(data);
			handleByteArray(tagName, data);
			break;
		}
			
		case 8:
			handleString(tagName, dataInput.readUTF());
			break;
			
		case 9: {
			int elementTagType = dataInput.readByte();
			int elementCount = dataInput.readInt();
			handleListStart(tagName, elementCount);
			for (int i=0; i<elementCount; i++) {
				handleTag(elementTagType, tagName);
			}
			handleListEnd(tagName, elementCount);
			break;
		}

		case 10:
			handleCompoundStart(tagName);
			while (parseTag());
			handleCompoundEnd(tagName);
			break;
			
		case 11: {
			int length = dataInput.readInt();
			int[] data = new int[length];
			for (int i=0; i<length; i++) {
				data[i] = dataInput.readInt();
			}
			handleIntArray(tagName, data);
			break;
		}
			
		default:
			throw new SyntaxException("found unknown tag type: " + tagType);
			
		}
	}
	
	/**
	 * Invoked for byte tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleByte(String tagName, byte value) {
	}
	
	/**
	 * Invoked for short tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleShort(String tagName, short value) {
	}
	
	/**
	 * Invoked for int tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleInt(String tagName, int value) {
	}
	
	/**
	 * Invoked for long tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleLong(String tagName, long value) {
	}
	
	/**
	 * Invoked for float tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleFloat(String tagName, float value) {
	}
	
	/**
	 * Invoked for double tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleDouble(String tagName, double value) {
	}

	/**
	 * Invoked for byte-array tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleByteArray(String tagName, byte[] value) {
	}
	
	/**
	 * Invoked for int-array tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleIntArray(String tagName, int[] value) {
	}

	/**
	 * Invoked for string tags.
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	protected void handleString(String tagName, String value) {
	}
	
	/**
	 * Invoked before parsing the elements of a list tag.
	 * @param tagName the tag name
	 * @param elementCount the number of list elements
	 */
	protected void handleListStart(String tagName, int elementCount) {
	}
	
	/**
	 * Invoked after parsing the elements of a list tag.
	 * @param tagName the tag name
	 * @param elementCount the number of list elements
	 */
	protected void handleListEnd(String tagName, int elementCount) {
	}

	/**
	 * Invoked before parsing the elements of a compound tag.
	 * @param tagName the tag name
	 */
	protected void handleCompoundStart(String tagName) {
	}
	
	/**
	 * Invoked after parsing the elements of a compound tag.
	 * @param tagName the tag name
	 */
	protected void handleCompoundEnd(String tagName) {
	}

	/**
	 * This exception type is thrown when an NBT file doesn't have a valid format.
	 */
	public static class SyntaxException extends Exception {

		/**
		 * Constructor.
		 */
		public SyntaxException() {
			super();
		}

		/**
		 * Constructor.
		 * @param message the exception message
		 */
		public SyntaxException(String message) {
			super(message);
		}

		/**
		 * Constructor.
		 * @param cause the exception that caused this exception
		 */
		public SyntaxException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor.
		 * @param message the exception message
		 * @param cause the exception that caused this exception
		 */
		public SyntaxException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}
