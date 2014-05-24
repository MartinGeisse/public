/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import name.martingeisse.stackd.client.system.SystemResourceNode;
import name.martingeisse.stackd.common.collision.CompositeCollider;
import name.martingeisse.stackd.common.collision.IAxisAlignedCollider;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.SectionId;
import org.apache.log4j.Logger;

/**
 * Represents the "working set" (i.e. currently visible sections) of a potentially huge
 * cube matrix world. This class supports loading parts of the world dynamically. It
 * does NOT support "parallel dimensions", i.e. separate cube matrices -- use a
 * separate instance for that.
 * 
 * TODO: dispose of system resources; allow passing a parent resource node to the constructor
 */
public final class WorldWorkingSet {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(WorldWorkingSet.class);
	
	/**
	 * the engineParameters
	 */
	private final EngineParameters engineParameters;

	/**
	 * the systemResourceNode
	 */
	private final SystemResourceNode systemResourceNode;

	/**
	 * the clusterSize
	 */
	private final ClusterSize clusterSize;

	/**
	 * the renderableSections
	 */
	private final Map<SectionId, RenderableSection> renderableSections;

	/**
	 * the renderableSectionsLoadedQueue
	 */
	private final ConcurrentLinkedQueue<RenderableSection> renderableSectionsLoadedQueue;

	/**
	 * the collidingSections
	 */
	private final Map<SectionId, CollidingSection> collidingSections;

	/**
	 * the collidingSectionsLoadedQueue
	 */
	private final ConcurrentLinkedQueue<CollidingSection> collidingSectionsLoadedQueue;

	/**
	 * the combined collider
	 */
	private final IAxisAlignedCollider compositeCollider;
	
	/**
	 * the renderUnits
	 */
	private RenderUnit[] renderUnits;

	/**
	 * Constructor.
	 * @param engineParameters static engine parameters, such as strategies
	 * @param clusterSize the cluster size
	 */
	public WorldWorkingSet(final EngineParameters engineParameters, final ClusterSize clusterSize) {
		this.engineParameters = engineParameters;
		this.systemResourceNode = new SystemResourceNode();
		this.clusterSize = clusterSize;
		this.renderableSections = new HashMap<SectionId, RenderableSection>();
		this.renderableSectionsLoadedQueue = new ConcurrentLinkedQueue<RenderableSection>();
		this.collidingSections = new HashMap<SectionId, CollidingSection>();
		this.collidingSectionsLoadedQueue = new ConcurrentLinkedQueue<CollidingSection>();
		this.compositeCollider = new CompositeCollider(collidingSections.values());
		this.renderUnits = null;
	}

	/**
	 * Getter method for the engineParameters.
	 * @return the engineParameters
	 */
	public EngineParameters getEngineParameters() {
		return engineParameters;
	}

	/**
	 * Getter method for the systemResourceNode.
	 * @return the systemResourceNode
	 */
	public SystemResourceNode getSystemResourceNode() {
		return systemResourceNode;
	}

	/**
	 * Getter method for the clusterSize.
	 * @return the clusterSize
	 */
	public ClusterSize getClusterSize() {
		return clusterSize;
	}

	/**
	 * Getter method for the renderableSections.
	 * @return the renderableSections
	 */
	public Map<SectionId, RenderableSection> getRenderableSections() {
		return renderableSections;
	}

	/**
	 * Getter method for the renderableSectionsLoadedQueue.
	 * @return the renderableSectionsLoadedQueue
	 */
	public ConcurrentLinkedQueue<RenderableSection> getRenderableSectionsLoadedQueue() {
		return renderableSectionsLoadedQueue;
	}

	/**
	 * Getter method for the collidingSections.
	 * @return the collidingSections
	 */
	public Map<SectionId, CollidingSection> getCollidingSections() {
		return collidingSections;
	}

	/**
	 * Getter method for the collidingSectionsLoadedQueue.
	 * @return the collidingSectionsLoadedQueue
	 */
	public ConcurrentLinkedQueue<CollidingSection> getCollidingSectionsLoadedQueue() {
		return collidingSectionsLoadedQueue;
	}

	/**
	 * Getter method for the composite collider that represents the world.
	 * @return the composite collider
	 */
	public IAxisAlignedCollider getCompositeCollider() {
		return compositeCollider;
	}

	/**
	 * Draws the working set, using the currently installed transformation. The specified
	 * viewer position is used only for culling; it doesn't affect the transformation.
	 * 
	 * @param frameRenderParameters per-frame rendering parameters
	 */
	public void draw(final FrameRenderParameters frameRenderParameters) {
		
		// prepare
		if (renderUnits == null) {
			List<RenderUnit> renderUnitList = new ArrayList<>();
			for (final RenderableSection renderableSection : renderableSections.values()) {
				renderableSection.prepare(engineParameters.getSectionRenderer());
				for (RenderUnit renderUnit : renderableSection.getRenderUnits()) {
					renderUnitList.add(renderUnit);
				}
			}
			renderUnits = renderUnitList.toArray(new RenderUnit[renderUnitList.size()]);
			Arrays.sort(renderUnits, new Comparator<RenderUnit>() {
				@Override
				public int compare(RenderUnit unit1, RenderUnit unit2) {
					if (unit1.getTextureIndex() != unit2.getTextureIndex()) {
						return unit1.getTextureIndex() - unit2.getTextureIndex();
					}
					if (unit1.getTextureCoordinateGenerationDirection() != unit2.getTextureCoordinateGenerationDirection()) {
						return unit1.getTextureCoordinateGenerationDirection().ordinal() - unit2.getTextureCoordinateGenerationDirection().ordinal();
					}
					if (unit1.getBackfaceCullingDirection() == unit2.getBackfaceCullingDirection()) {
						return 0;
					}
					if (unit1.getBackfaceCullingDirection() == null) {
						return -1;
					}
					if (unit2.getBackfaceCullingDirection() == null) {
						return +1;
					}
					return unit1.getBackfaceCullingDirection().ordinal() - unit2.getBackfaceCullingDirection().ordinal();
				}
			});
			logger.info("working set now has " + renderUnits.length + " render units");
		}
		
		// render
		engineParameters.getSectionRenderer().onBeforeRenderWorkingSet(this, frameRenderParameters);
		for (RenderUnit renderUnit : renderUnits) {
			renderUnit.draw(engineParameters, frameRenderParameters, engineParameters.getSectionRenderer().getGlWorkerLoop());
		}
		engineParameters.getSectionRenderer().onAfterRenderWorkingSet(this, frameRenderParameters);
		
	}

	/**
	 * Actually adds loaded sections from the "loaded" queues to the working set.
	 */
	public void acceptLoadedSections() {
		{
			boolean modified = false;
			RenderableSection renderableSection;
			while ((renderableSection = renderableSectionsLoadedQueue.poll()) != null) {
				renderableSections.put(renderableSection.getSectionId(), renderableSection);
				modified = true;
			}
			if (modified) {
				markRenderModelsModified();
			}
		}
		{
			CollidingSection collidingSection;
			while ((collidingSection = collidingSectionsLoadedQueue.poll()) != null) {
				collidingSections.put(collidingSection.getSectionId(), collidingSection);
			}
		}
	}

	/**
	 * Checks whether all render models around the specified center are present in the working
	 * set. Currently using "city block distance" (checking a rectangular region), not
	 * euclidian distance (which would check a sphere).
	 * 
	 * @param center the center section
	 * @param radius the "radius"
	 * @return true if all render models are present, false if some are missing
	 */
	public boolean hasAllRenderModels(SectionId center, final int radius) {
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				for (int dz = -radius; dz <= radius; dz++) {
					final int cx = center.getX() + dx, cy = center.getY() + dy, cz = center.getZ() + dz;
					final SectionId id = new SectionId(cx, cy, cz);
					if (!renderableSections.containsKey(id)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks whether all colliders around the specified center are present in the working
	 * set. Currently using "city block distance" (checking a rectangular region), not
	 * euclidian distance (which would check a sphere).
	 * 
	 * @param center the center section
	 * @param radius the "radius"
	 * @return true if all colliders are present, false if some are missing
	 */
	public boolean hasAllColliders(SectionId center, final int radius) {
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				for (int dz = -radius; dz <= radius; dz++) {
					final int cx = center.getX() + dx, cy = center.getY() + dy, cz = center.getZ() + dz;
					final SectionId id = new SectionId(cx, cy, cz);
					if (!collidingSections.containsKey(id)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method should be called when render models have been added or removed.
	 * 
	 * TODO: there should be high-level modification methods, and they should do this themselves.
	 */
	public void markRenderModelsModified() {
		
		// TODO dispose of the VBOs!!!
		renderUnits = null;
		
	}
	
}
