/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.stackd.client.engine.prepare;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import name.martingeisse.stackd.client.engine.RenderUnit;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.system.OpenGlVertexBuffer;
import name.martingeisse.stackd.client.system.SystemResourceNode;
import name.martingeisse.stackd.common.cubes.MeshBuilderBase;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.RectangularRegion;
import org.apache.log4j.Logger;

/**
 * This class accepts polygons to render and builds an array of
 * {@link RenderUnit}s from them.
 */
public final class MeshBuilder extends MeshBuilderBase {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(MeshBuilder.class);
	
	/**
	 * Constructor.
	 */
	public MeshBuilder() {
	}

	/**
	 * Builds {@link RenderUnit}s from the triangles.
	 * 
	 * @param systemResourceNode the resource node that manages the actual VBOs
	 * @param glWorkerLoop the OpenGL worker loop that builds the actual VBOs
	 * @return the render units
	 */
	public RenderUnit[] build(final SystemResourceNode systemResourceNode, final GlWorkerLoop glWorkerLoop) {
		RectangularRegion boundingBox = getBoundingBox();
		List<Triangle> triangles = getTriangles();
			

		// check
		if (boundingBox == null) {
			throw new IllegalStateException("no bounding box set");
		}
		
		// sort the triangles by render state
		Collections.sort(triangles, new Comparator<Triangle>() {
			@Override
			public int compare(final Triangle t1, final Triangle t2) {
				if (t1.textureIndex != t2.textureIndex) {
					return t1.textureIndex - t2.textureIndex;
				}
				if (t1.textureCoordinateGenerationDirection != t2.textureCoordinateGenerationDirection) {
					return t1.textureCoordinateGenerationDirection.ordinal() - t2.textureCoordinateGenerationDirection.ordinal();
				}
				if (t1.backfaceCullingDirection != t2.backfaceCullingDirection) {
					return t1.backfaceCullingDirection.ordinal() - t2.backfaceCullingDirection.ordinal();
				}
				return 0;
			}
		});

		// build render units
		final ArrayList<RenderUnit> renderUnits = new ArrayList<>();
		int i = 0;
		while (i < triangles.size()) {
			final int textureIndex = triangles.get(i).textureIndex;
			final AxisAlignedDirection textureCoordinateGenerationDirection = triangles.get(i).textureCoordinateGenerationDirection;
			final AxisAlignedDirection backfaceCullingDirection = triangles.get(i).backfaceCullingDirection;
			final int start = i;
			i++;
			while (i < triangles.size() && triangles.get(i).textureIndex == textureIndex && triangles.get(i).textureCoordinateGenerationDirection == textureCoordinateGenerationDirection && triangles.get(i).backfaceCullingDirection == backfaceCullingDirection) {
				i++;
			}
			final int renderUnitTriangleCount = (i - start);
			final RenderUnit renderUnit = new RenderUnit(boundingBox, textureIndex, textureCoordinateGenerationDirection, backfaceCullingDirection);
			renderUnit.setVertexCount(renderUnitTriangleCount * 3);
			final IntBuffer vertexData = ByteBuffer.allocateDirect(renderUnitTriangleCount * 3 * 3 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
			for (int k = start; k < i; k++) {
				final Triangle triangle = triangles.get(k);
				vertexData.put(triangle.x1);
				vertexData.put(triangle.y1);
				vertexData.put(triangle.z1);
				vertexData.put(triangle.x2);
				vertexData.put(triangle.y2);
				vertexData.put(triangle.z2);
				vertexData.put(triangle.x3);
				vertexData.put(triangle.y3);
				vertexData.put(triangle.z3);
			}
			vertexData.rewind();
			renderUnits.add(renderUnit);
			glWorkerLoop.schedule(new GlWorkUnit() {
				@Override
				public void execute() {
					logger.debug("building vertex buffer for mesh...");
					final OpenGlVertexBuffer vertexBuffer = new OpenGlVertexBuffer();
					vertexBuffer.createDataStore(vertexData, GL_STATIC_DRAW);
					systemResourceNode.addResource(vertexBuffer);
					renderUnit.setVertexBuffer(vertexBuffer);
					logger.debug("vertex buffer built.");
				}
			});
		}

		return renderUnits.toArray(new RenderUnit[renderUnits.size()]);
	}

}
