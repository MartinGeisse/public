/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.system;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glIsBuffer;
import org.lwjgl.opengl.GL15;

/**
 * Manages a OpenGL-server-side buffer.
 */
public class OpenGlBuffer extends AbstractSystemResource {

	/**
	 * the name
	 */
	private final int name;
	
	/**
	 * Constructor.
	 */
	public OpenGlBuffer() {
		this.name = glGenBuffers();
	}
	
	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public int getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.lowlevel.AbstractSystemResource#internalDispose()
	 */
	@Override
	protected void internalDispose() {
		glDeleteBuffers(name);
	}

	/**
	 * Checks whether this buffer still exists OpenGL-server-side. That should be the case
	 * if this buffer has not been disposed of, unless it got deleted by calling
	 * {@link GL15#glDeleteBuffers} directly.
	 * @return true if this buffer still exists OpenGL-server-side, false if not
	 */
	public final boolean existsOpenGlServerSide() {
		return glIsBuffer(name);
	}
	
}
