/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine;

import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.system.OpenGlVertexBuffer;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * The {@link RenderableSection}s of the working set get subdivided into units
 * of this type, which then get sorted by required render state and which do
 * the actual rendering.
 * 
 * A render unit consists of a render-state part and a data part. Render units
 * should be sorted by render state to improve performance. The render state
 * consists of a texture index and an {@link AxisAlignedDirection} for texture
 * coordinate generation. The data part is a VBO containing the vertices. The
 * VBO is not specified directly at construction but can be set later on, such
 * that the render unit can be build in the application thread and the creation
 * of the VBO be passed on to the OpenGL worker thread. Trying to draw a render
 * unit without a VBO will simply skip that unit.
 * 
 * A render unit also stores a bounding box for quick frustum culling (TODO) and
 * quick backface culling.
 */
public final class RenderUnit extends GlWorkUnit {

	/**
	 * the boundingBox
	 */
	private final RectangularRegion boundingBox;

	/**
	 * the textureIndex
	 */
	private final int textureIndex;

	/**
	 * the textureCoordinateGenerationDirection
	 */
	private final AxisAlignedDirection textureCoordinateGenerationDirection;

	/**
	 * the backfaceCullingDirection
	 */
	private final AxisAlignedDirection backfaceCullingDirection;

	/**
	 * the vertexCount
	 */
	private int vertexCount;

	/**
	 * the vertexBuffer
	 */
	private OpenGlVertexBuffer vertexBuffer;

	/**
	 * Constructor.
	 * @param boundingBox the bounding box (used for quick culling)
	 * @param textureIndex the texture index
	 * @param textureCoordinateGenerationDirection the direction to use for texture coordinate generation
	 * @param backfaceCullingDirection the direction to use for quick backface culling (or null to disable QBFC)
	 */
	public RenderUnit(final RectangularRegion boundingBox, final int textureIndex, final AxisAlignedDirection textureCoordinateGenerationDirection, final AxisAlignedDirection backfaceCullingDirection) {
		this.boundingBox = boundingBox;
		this.textureIndex = textureIndex;
		this.textureCoordinateGenerationDirection = textureCoordinateGenerationDirection;
		this.backfaceCullingDirection = backfaceCullingDirection;
	}

	/**
	 * Getter method for the boundingBox.
	 * @return the boundingBox
	 */
	public RectangularRegion getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Getter method for the vertexCount.
	 * @return the vertexCount
	 */
	public int getVertexCount() {
		return vertexCount;
	}

	/**
	 * Setter method for the vertexCount.
	 * @param vertexCount the vertexCount to set
	 */
	public void setVertexCount(final int vertexCount) {
		this.vertexCount = vertexCount;
	}

	/**
	 * Getter method for the vertexBuffer.
	 * @return the vertexBuffer
	 */
	public OpenGlVertexBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	/**
	 * Setter method for the vertexBuffer.
	 * @param vertexBuffer the vertexBuffer to set
	 */
	public void setVertexBuffer(final OpenGlVertexBuffer vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
	}

	/**
	 * Getter method for the textureIndex.
	 * @return the textureIndex
	 */
	public int getTextureIndex() {
		return textureIndex;
	}

	/**
	 * Getter method for the textureCoordinateGenerationDirection.
	 * @return the textureCoordinateGenerationDirection
	 */
	public AxisAlignedDirection getTextureCoordinateGenerationDirection() {
		return textureCoordinateGenerationDirection;
	}

	/**
	 * Getter method for the backfaceCullingDirection.
	 * @return the backfaceCullingDirection
	 */
	public AxisAlignedDirection getBackfaceCullingDirection() {
		return backfaceCullingDirection;
	}

	/**
	 * Draws this render unit. This method should be called from the high-level rendering thread.
	 * It will decide whether to place this object into the GL worker loop as a work unit.
	 * 
	 * @param engineParameters the engine parameters
	 * @param frameRenderParameters the per-frame rendering parameters
	 * @param glWorkerLoop the GL worker loop to use to schedule GL work units
	 */
	public void draw(final EngineParameters engineParameters, final FrameRenderParameters frameRenderParameters, final GlWorkerLoop glWorkerLoop) {

		// backface culling
		if (backfaceCullingDirection != null) {
			
			// Possible optimization:
			// ... depending on the viewer's plane, we either draw all planes in one direction and none
			// ... in the opposite direction, or (if the player is "inside" the section along this
			// ... axis) we draw some planes in one direction and some in the opposite direction.
			// ... Note that "direction" is the direction of the faces, so the viewer will see the faces
			// ... if the sign of the direction is equal to the sign of the player's position relative 
			// ... to the faces.
			// This is currently not done because it means drawing *part* of a VBO, which is rather complex.
			// So we just backface-cull whole sections, then let the graphics hardware backface-cull
			// individual polygons
			final int dx = (frameRenderParameters.getX() - boundingBox.getStartX());
			final int dy = (frameRenderParameters.getY() - boundingBox.getStartY());
			final int dz = (frameRenderParameters.getZ() - boundingBox.getStartZ());
			final int viewerPlane = backfaceCullingDirection.selectByAxis(dx, dy, dz);
			
			// quick whole-section backface culling (note that sizeX == sizeY == sizeZ for the region)
			if (viewerPlane < 0 && !backfaceCullingDirection.isNegative()) {
				return;
			}
			if (viewerPlane >= boundingBox.getSizeX() && backfaceCullingDirection.isNegative()) {
				return;
			}
			
		}

		// this render unit must actually be drawn
		// TODO don't set the texture / tex coords twice 
		engineParameters.getSectionRenderer().prepareForTexture(engineParameters.getCubeTexture(textureIndex));
		engineParameters.getSectionRenderer().prepareForDirection(textureCoordinateGenerationDirection);
		glWorkerLoop.schedule(this);

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.glworker.GlWorkUnit#execute()
	 */
	@Override
	public void execute() {
		vertexBuffer.bind();
		glVertexPointer(3, GL_INT, 0, 0);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
	}

}
