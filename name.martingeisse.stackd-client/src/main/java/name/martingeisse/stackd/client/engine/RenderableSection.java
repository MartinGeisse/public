/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine;

import java.util.Arrays;
import name.martingeisse.stackd.client.engine.prepare.EmptyWrapPlane;
import name.martingeisse.stackd.client.engine.prepare.IWrapPlane;
import name.martingeisse.stackd.client.engine.prepare.MeshBuilder;
import name.martingeisse.stackd.client.engine.renderer.ISectionRenderer;
import name.martingeisse.stackd.client.system.SystemResourceNode;
import name.martingeisse.stackd.common.cubes.Cubes;
import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.RectangularRegion;
import name.martingeisse.stackd.common.geometry.SectionId;

/**
 * This class wraps a {@link Cubes} object and augments it with
 * additional data for rendering. Instances of this class are stored
 * for sections in the {@link WorldWorkingSet}.
 */
public final class RenderableSection {

	/**
	 * the workingSet
	 */
	private final WorldWorkingSet workingSet;

	/**
	 * the clusterSize
	 */
	private final ClusterSize clusterSize;

	/**
	 * the sectionId
	 */
	private final SectionId sectionId;

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * the cubes
	 */
	private Cubes cubes;

	/**
	 * the systemResourceNode
	 */
	private SystemResourceNode systemResourceNode;
	
	/**
	 * the renderUnits
	 */
	private RenderUnit[] renderUnits;
	
	/**
	 * Constructor.
	 * @param workingSet the working set that contains this object
	 * @param sectionId the section id
	 * @param cubes the cubes for this section
	 */
	public RenderableSection(final WorldWorkingSet workingSet, final SectionId sectionId, final Cubes cubes) {
		this.workingSet = workingSet;
		this.clusterSize = workingSet.getClusterSize();
		this.sectionId = sectionId;
		this.region = new RectangularRegion(sectionId.getX(), sectionId.getY(), sectionId.getZ()).multiply(clusterSize);
		this.cubes = cubes;
		this.systemResourceNode = null;
		this.renderUnits = null;
	}

	/**
	 * Getter method for the workingSet.
	 * @return the workingSet
	 */
	public WorldWorkingSet getWorkingSet() {
		return workingSet;
	}

	/**
	 * Getter method for the clusterSize.
	 * @return the clusterSize
	 */
	public ClusterSize getClusterSize() {
		return clusterSize;
	}

	/**
	 * Getter method for the sectionId.
	 * @return the sectionId
	 */
	public SectionId getSectionId() {
		return sectionId;
	}

	/**
	 * Getter method for the region.
	 * @return the region
	 */
	public RectangularRegion getRegion() {
		return region;
	}
	
	/**
	 * Creates a system resource node if none is present yet.
	 */
	private void needResourceNode() {
		if (systemResourceNode == null) {
			systemResourceNode = new SystemResourceNode(workingSet.getSystemResourceNode());
		}
	}

	/**
	 * Disposes of the system resource node for this object.
	 */
	private void disposeSystemResourceNode() {
		if (systemResourceNode != null) {
			systemResourceNode.dispose();
			systemResourceNode = null;
		}
	}

	/**
	 * Prepares this object for rendering. Calling this method is not necessary, since drawing
	 * will automatically prepare this object. Nor is it harmful to call this method multiple
	 * times; it will only prepare the first time. However, this method can be used to prepare
	 * this object at a time when a delay is not harmful, or even in another thread. Note though
	 * that this whole object is not synchronized, so you'll have to do that yourself.
	 * 
	 * @param renderer the section renderer
	 */
	public void prepare(final ISectionRenderer renderer) {
		if (renderUnits != null) {
			return;
		}
		needResourceNode();
		MeshBuilder meshBuilder = new MeshBuilder();
		meshBuilder.setBoundingBox(region);
		buildMesh(meshBuilder);
		renderUnits = meshBuilder.build(systemResourceNode, renderer.getGlWorkerLoop());
	}
	
	/**
	 * Builds the triangle mesh for this section.
	 * @param meshBuilder the mesh builder
	 */
	private void buildMesh(MeshBuilder meshBuilder) {
		int size = clusterSize.getSize();
		CubeType[] cubeTypes = getWorkingSet().getEngineParameters().getCubeTypes();
		
		// build cube faces
		{
			IWrapPlane uniformWrapPlane = new EmptyWrapPlane();
			IWrapPlane[] wrapPlanes = new IWrapPlane[] {uniformWrapPlane, uniformWrapPlane, uniformWrapPlane, uniformWrapPlane, uniformWrapPlane, uniformWrapPlane};
			byte[] cubeFaceTextureIndexPlane = new byte[size * size];
			for (AxisAlignedDirection direction : AxisAlignedDirection.values()) {
				for (int plane = 0; plane < size; plane++) {
					
					// determine the wrapping plane
					IWrapPlane wrapPlane;
					if (plane == (direction.isNegative() ? 0 : size - 1)) {
						wrapPlane = wrapPlanes[direction.ordinal()];
					} else {
						wrapPlane = null;
					}
					
					// determine the texture indices for the current plane
					Arrays.fill(cubeFaceTextureIndexPlane, (byte)0);
					for (int u=0; u<size; u++) {
						for (int v=0; v<size; v++) {
							final int x = direction.selectByAxis(plane, u, v);
							final int y = direction.selectByAxis(v, plane, u);
							final int z = direction.selectByAxis(u, v, plane);
							final int cubeTypeCode = cubes.getCubeRelative(clusterSize, x, y, z) & 0xff;
							final CubeType cubeType = cubeTypes[cubeTypeCode];
							if (cubeTypeCode != 255) {
								final int textureIndex = cubeType.getCubeFaceTextureIndex(direction);
								if (textureIndex != 0) {
									final CubeType neighborCubeType;
									if (wrapPlane == null) {
										final int nx = x + direction.getSignX();
										final int ny = y + direction.getSignY();
										final int nz = z + direction.getSignZ();
										final int neighborCubeTypeCode = cubes.getCubeRelative(clusterSize, nx, ny, nz) & 0xff;
										if (neighborCubeTypeCode == 255) {
											continue;
										}
										neighborCubeType = cubeTypes[neighborCubeTypeCode];
									} else {
										neighborCubeType = wrapPlane.getCubeType(clusterSize, direction, u, v, cubeTypes);
									}
									if (!neighborCubeType.obscuresNeighbor(direction.getOpposite())) {
										cubeFaceTextureIndexPlane[v * size + u] = (byte)textureIndex;
									}
								}
							}
						}
					}
					
					// some geometry juggling need to build the polygons: compute d(x,y,z)/d(u,v)
					final int dx_du = direction.getAbsY();
					final int dy_du = direction.getAbsZ();
					final int dz_du = direction.getAbsX();
					final int dx_dv = direction.getAbsZ();
					final int dy_dv = direction.getAbsX();
					final int dz_dv = direction.getAbsY();
					
					// detect large rectangles and build the actual polygons using the MeshBuilder
					for (int u = 0; u < size; u++) {
						for (int v = 0; v < size; v++) {
							
							// skip already cleared faces
							int textureIndex = cubeFaceTextureIndexPlane[v * size + u];
							if (textureIndex == 0) {
								continue;
							}
							
							// find a large rectangle
							int w = 1;
							while (u + w < size && cubeFaceTextureIndexPlane[v * size + (u + w)] == textureIndex) {
								w++;
							}
							int h = 1;
							extendHeight: while (v + h < size) {
								for (int i=0; i<w; i++) {
									if (cubeFaceTextureIndexPlane[(v + h) * size + (u + i)] != textureIndex) {
										break extendHeight;
									}
								}
								h++;
							}
							
							// clear the faces covered by that rectangle
							for (int i=0; i<w; i++) {
								for (int j=0; j<h; j++) {
									cubeFaceTextureIndexPlane[(v + j) * size + (u + i)] = 0;
								}
							}
							
							// find the base coordinates and transform everything to scaled space
							final int scaledX = (region.getStartX() + direction.getStepX() + direction.selectByAxis(plane, u, v)) << 3;
							final int scaledY = (region.getStartY() + direction.getStepY() + direction.selectByAxis(v, plane, u)) << 3;
							final int scaledZ = (region.getStartZ() + direction.getStepZ() + direction.selectByAxis(u, v, plane)) << 3;
							final int scaledW = w << 3;
							final int scaledH = h << 3;
							
							// scale d-vectors by w, h
							final int scaledW_dx_du = scaledW * dx_du;
							final int scaledW_dy_du = scaledW * dy_du;
							final int scaledW_dz_du = scaledW * dz_du;
							final int scaledH_dx_dv = scaledH * dx_dv;
							final int scaledH_dy_dv = scaledH * dy_dv;
							final int scaledH_dz_dv = scaledH * dz_dv;
							
							// determine vertex coordinates (possibly swapped for correct winding)
							int x2, y2, z2, x3, y3, z3, x4, y4, z4;
							if (direction.isNegative()) {
								x2 = scaledX + scaledW_dx_du;
								y2 = scaledY + scaledW_dy_du;
								z2 = scaledZ + scaledW_dz_du;
								x3 = scaledX + scaledH_dx_dv;
								y3 = scaledY + scaledH_dy_dv;
								z3 = scaledZ + scaledH_dz_dv;
							} else {
								x2 = scaledX + scaledH_dx_dv;
								y2 = scaledY + scaledH_dy_dv;
								z2 = scaledZ + scaledH_dz_dv;
								x3 = scaledX + scaledW_dx_du;
								y3 = scaledY + scaledW_dy_du;
								z3 = scaledZ + scaledW_dz_du;
							}
							x4 = scaledX + scaledW_dx_du + scaledH_dx_dv;
							y4 = scaledY + scaledW_dy_du + scaledH_dy_dv;
							z4 = scaledZ + scaledW_dz_du + scaledH_dz_dv;
							
							// build the triangles
							meshBuilder.addTriangle(textureIndex, direction, direction, scaledX, scaledY, scaledZ, x2, y2, z2, x3, y3, z3);
							meshBuilder.addTriangle(textureIndex, direction, direction, x3, y3, z3, x2, y2, z2, x4, y4, z4);
							
						}
					}
				}
			}
		}

		// build other polygons
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				for (int z=0; z<size; z++) {
					final int cubeTypeCode = cubes.getCubeRelative(clusterSize, x, y, z) & 0xff;
					if (cubeTypeCode != 255) {
						final CubeType cubeType = cubeTypes[cubeTypeCode];
						final int scaledX = (region.getStartX() + x) << 3;
						final int scaledY = (region.getStartY() + y) << 3;
						final int scaledZ = (region.getStartZ() + z) << 3;
						cubeType.buildInnerPolygons(meshBuilder, scaledX, scaledY, scaledZ);
					}
				}
			}
		}
		
	}

	/**
	 * Getter method for the renderUnits.
	 * @return the renderUnits
	 */
	public RenderUnit[] getRenderUnits() {
		return renderUnits;
	}
	
	/**
	 * Disposes of this object. This method must be called before dropping the last reference
	 * to this object.
	 */
	public void dispose() {
		disposeSystemResourceNode();
		cubes = null;
		renderUnits = null;
	}

}
