/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.system;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.GL15;

/**
 * Represents OpenGL-server-side VBOs.
 */
public class OpenGlVertexBuffer extends OpenGlBuffer {

	/**
	 * the size
	 */
	private long size;
	
	/**
	 * Binds this buffer to the GL_ARRAY_BUFFER target.
	 */
	public final void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, getName());
	}

	/**
	 * Creates a new OpenGL-server-side data store for this buffer with uninitialized contents.
	 * This also binds this buffer to GL_ARRAY_BUFFER implicitly.
	 * @param size the size of the data store in bytes
	 * @param usage the usage mode (see glBufferData() documentation for details)
	 */
	public final void createDataStore(long size, int usage) {
		bind();
		glBufferData(GL_ARRAY_BUFFER, size, usage);
		this.size = size;
	}

	/**
	 * Creates a new OpenGL-server-side data store for this buffer and uploads data from
	 * the specified NIO buffer into the new data store.
	 * This also binds this buffer to GL_ARRAY_BUFFER implicitly.
	 * @param sourceBuffer the NIO buffer to read data from
	 * @param usage the usage mode (see glBufferData() documentation for details)
	 */
	public final void createDataStore(ByteBuffer sourceBuffer, int usage) {
		bind();
		this.size = sourceBuffer.remaining();
		glBufferData(GL_ARRAY_BUFFER, sourceBuffer, usage);
	}
	
	/**
	 * Creates a new OpenGL-server-side data store for this buffer and uploads data from
	 * the specified NIO buffer into the new data store.
	 * This also binds this buffer to GL_ARRAY_BUFFER implicitly.
	 * @param sourceBuffer the NIO buffer to read data from
	 * @param usage the usage mode (see glBufferData() documentation for details)
	 */
	public final void createDataStore(ShortBuffer sourceBuffer, int usage) {
		bind();
		this.size = sourceBuffer.remaining() * 2;
		glBufferData(GL_ARRAY_BUFFER, sourceBuffer, usage);
	}
	
	/**
	 * Creates a new OpenGL-server-side data store for this buffer and uploads data from
	 * the specified NIO buffer into the new data store.
	 * This also binds this buffer to GL_ARRAY_BUFFER implicitly.
	 * @param sourceBuffer the NIO buffer to read data from
	 * @param usage the usage mode (see glBufferData() documentation for details)
	 */
	public final void createDataStore(IntBuffer sourceBuffer, int usage) {
		bind();
		this.size = sourceBuffer.remaining() * 4;
		glBufferData(GL_ARRAY_BUFFER, sourceBuffer, usage);
	}
	
	/**
	 * Creates a new OpenGL-server-side data store for this buffer and uploads data from
	 * the specified NIO buffer into the new data store.
	 * This also binds this buffer to GL_ARRAY_BUFFER implicitly.
	 * @param sourceBuffer the NIO buffer to read data from
	 * @param usage the usage mode (see glBufferData() documentation for details)
	 */
	public final void createDataStore(FloatBuffer sourceBuffer, int usage) {
		bind();
		this.size = sourceBuffer.remaining() * 4;
		glBufferData(GL_ARRAY_BUFFER, sourceBuffer, usage);
	}
	
	/**
	 * Creates a new OpenGL-server-side data store for this buffer and uploads data from
	 * the specified NIO buffer into the new data store.
	 * This also binds this buffer to GL_ARRAY_BUFFER implicitly.
	 * @param sourceBuffer the NIO buffer to read data from
	 * @param usage the usage mode (see glBufferData() documentation for details)
	 */
	public final void createDataStore(DoubleBuffer sourceBuffer, int usage) {
		bind();
		this.size = sourceBuffer.remaining() * 8;
		glBufferData(GL_ARRAY_BUFFER, sourceBuffer, usage);
	}
	
	/**
	 * Maps this buffer to main memory.
	 * 
	 * @param readable whether the mapped buffer should be readable
	 * @param writeable whether the mapped buffer should be writable
	 */
	public final void map(boolean readable, boolean writeable) {
		if (!readable && !writeable) {
			throw new IllegalArgumentException("requested buffer mapping that is neither readable nor writable");
		}
		int access = (!readable ? GL15.GL_WRITE_ONLY : !writeable ? GL15.GL_READ_ONLY : GL15.GL_READ_WRITE);
		GL15.glMapBuffer(GL_ARRAY_BUFFER, access, size, null);
	}
	
	/**
	 * Unmaps this buffer from main memory.
	 */
	public final void unmap() {
		GL15.glUnmapBuffer(GL_ARRAY_BUFFER);
	}
	
}
