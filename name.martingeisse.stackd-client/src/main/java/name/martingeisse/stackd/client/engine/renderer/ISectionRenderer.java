/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine.renderer;

import name.martingeisse.stackd.client.engine.FrameRenderParameters;
import name.martingeisse.stackd.client.engine.WorldWorkingSet;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.system.StackdTexture;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;

/**
 * Implementations of this interface are used to actually render
 * sections. This interface does not know anything about
 * sections or their render models, and instead operates on
 * low-level rendering instructions.
 * 
 * Different implementations might be used to control low-level
 * rendering features, e.g. for performance tuning.
 * 
 * {@link DefaultSectionRenderer} is the default implementation
 * that supports all features, possibly at a performance cost.
 * 
 * Implementations are usually NOT thread-safe and must be called by the thread that
 * does the high-level rendering. They DO keep a {@link GlWorkerLoop} and pass OpenGL
 * commands to it.
 */
public interface ISectionRenderer {
	
	/**
	 * Getter method for the OpenGL worker loop.
	 * @return the GL worker loop
	 */
	public GlWorkerLoop getGlWorkerLoop();
	
	/**
	 * Called just before drawing the working set.
	 * @param workingSet the working set being drawn
	 * @param frameRenderParameters per-frame rendering parameters
	 */
	public void onBeforeRenderWorkingSet(WorldWorkingSet workingSet, FrameRenderParameters frameRenderParameters);
	
	/**
	 * Called just after drawing the working set.
	 * @param workingSet the working set being drawn
	 * @param frameRenderParameters per-frame rendering parameters
	 */
	public void onAfterRenderWorkingSet(WorldWorkingSet workingSet, FrameRenderParameters frameRenderParameters);

	/**
	 * Prepares for drawing with the specified texture.
	 * @param texture the texture to work with
	 */
	public void prepareForTexture(StackdTexture texture);
	
	/**
	 * Prepares for drawing faces that are facing the specified direction.
	 * This usually configures texture coordinate generation.
	 * @param direction the direction
	 */
	public void prepareForDirection(AxisAlignedDirection direction);
	
}
