/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.image;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Implementations of this interface represent images being drawn. Instances are
 * usually created by {@link IImageBackendType} objects.
 * 
 * A newly created instance is initially in the "not finished" state. In this state, 
 * an AWT graphics context can be requested for drawing. However, the image cannot
 * be consumed by any of the consumption methods; trying to do so results in an exception.
 * 
 * When drawing is completed, clients must finish the image backend. The backend is then
 * in the "finished" state. In this state, trying to retrieve the graphics context
 * results in an exception. If the graphics context was previously stored by clients,
 * it cannot be considered valid anymore. However, the image can be consumed in this state.
 * 
 * Implementations may make stronger guarantees and allow consumption before being finished
 * or allow drawing after being finished. However, the standard methods must still throw
 * an exception on such attempts; such implementations must provide additional methods for
 * the extended behavior.
 */
public interface IImageBackend {

	/**
	 * @return the AWT graphics context
	 * @throws IllegalStateException if this object has been finished
	 */
	public Graphics2D getGraphics() throws IllegalStateException;
	
	/**
	 * Finishes this image backend.
	 * @return this for chaining
	 */
	public IImageBackend finish();
	
	/**
	 * Consumption method: Writes the binary image data to the specified stream. This method
	 * does not close the stream afterwards.
	 * @param s the stream to write to
	 * @throws IOException on I/O errors
	 */
	public void writeTo(OutputStream s) throws IOException;
	
	/**
	 * Consumption method: Writes the binary image data to a new byte array and returns it.
	 * @return the byte array
	 * @throws IOException on I/O errors
	 */
	public byte[] writeToByteArray() throws IOException;

}
