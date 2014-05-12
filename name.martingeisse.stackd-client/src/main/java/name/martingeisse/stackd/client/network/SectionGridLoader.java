/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import name.martingeisse.stackd.client.engine.CollidingSection;
import name.martingeisse.stackd.client.engine.RenderableSection;
import name.martingeisse.stackd.client.engine.WorldWorkingSet;
import name.martingeisse.stackd.common.collision.CubeArrayClusterCollider;
import name.martingeisse.stackd.common.collision.IAxisAlignedCollider;
import name.martingeisse.stackd.common.cubes.Cubes;
import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.SectionId;
import name.martingeisse.stackd.common.network.StackdPacket;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * This class updates the set of sections in the {@link WorldWorkingSet} based
 * on the viewer's position. It requests new sections by sending section request
 * packets via a {@link StackdProtocolClient} and should be registered with
 * that client to handle section update packets.
 * 
 * Sending a request for sections is not initiated by this class; this class
 * only provides a method to do so. Clients should call that method in regular
 * intervals. TODO provide a frame handler for that.
 * 
 * The current implementation is very simple: It keeps a cube-shaped region
 * of sections in the working set. The region contains an odd number of
 * sections along each axis, with the viewer in the middle section.
 * The section set changes whenever the viewer moves to another section.
 * In other words, the number of active sections is (2r+1)^3, with
 * r being the "radius" of the active set. The radius is a parameter for
 * the constructor of this class.
 */
public final class SectionGridLoader {

	/**
	 * the workingSet
	 */
	private final WorldWorkingSet workingSet;

	/**
	 * the protocolClient
	 */
	private final StackdProtocolClient protocolClient;

	/**
	 * the renderModelRadius
	 */
	private final int renderModelRadius;

	/**
	 * the colliderRadius
	 */
	private final int colliderRadius;
	
	/**
	 * the viewerPosition
	 */
	private SectionId viewerPosition;

	/**
	 * Constructor.
	 * @param workingSet the working set to load sections for
	 * @param protocolClient the protocol client used to send requests
	 * @param renderModelRadius the "radius" of the active set of render models
	 * @param colliderRadius the "radius" of the active set of colliders
	 */
	public SectionGridLoader(final WorldWorkingSet workingSet, final StackdProtocolClient protocolClient, final int renderModelRadius, final int colliderRadius) {
		this.workingSet = workingSet;
		this.protocolClient = protocolClient;
		this.renderModelRadius = renderModelRadius;
		this.colliderRadius = colliderRadius;
		this.viewerPosition = null;
	}

	/**
	 * Getter method for the viewerPosition.
	 * @return the viewerPosition
	 */
	public SectionId getViewerPosition() {
		return viewerPosition;
	}
	
	/**
	 * Setter method for the viewerPosition.
	 * @param viewerPosition the viewerPosition to set
	 */
	public void setViewerPosition(SectionId viewerPosition) {
		this.viewerPosition = viewerPosition;
	}

	/**
	 * Updates the set of visible sections, based on the viewer's position which
	 * was previously set via {@link #setViewerPosition(SectionId)} (mandatory).
	 * 
	 * @return true if anything was update-requested, false if everything stays the same
	 */
	public boolean update() {
		boolean anythingUpdated = false;
		
		// ProfilingHelper.start();
		
		// check that a position was set.
		if (viewerPosition == null) {
			throw new IllegalStateException("viewer position not set");
		}

		// remove all sections that are too far away
		if (restrictMapToRadius(workingSet.getRenderableSections(), renderModelRadius)) {
			workingSet.markRenderModelsModified();
			anythingUpdated = true;
		}
		anythingUpdated |= restrictMapToRadius(workingSet.getCollidingSections(), colliderRadius);
		
		// ProfilingHelper.checkRelevant("update sections 1");
		
		// if the protocol client isn't ready yet, we cannot load anything
		if (!protocolClient.isReady()) {
			System.out.println("* " + (System.currentTimeMillis() % 100000) + ": cannot load sections, protocol client not ready yet");
			return anythingUpdated;
		}

		// detect missing section render models in the viewer's proximity, then request them all at once
		// TODO implement a batch request packet
		// TODO fetch non-interactive data for "far" sections
		{
			final List<SectionId> missingSectionIds = findMissingSectionIds(workingSet.getRenderableSections().keySet(), renderModelRadius);
			if (missingSectionIds != null && !missingSectionIds.isEmpty()) {
				final SectionId[] sectionIds = missingSectionIds.toArray(new SectionId[missingSectionIds.size()]);
				for (SectionId sectionId : sectionIds) {
					StackdPacket packet = new StackdPacket(StackdPacket.TYPE_SINGLE_SECTION_DATA_INTERACTIVE, 12);
					ChannelBuffer buffer = packet.getBuffer();
					buffer.writeInt(sectionId.getX());
					buffer.writeInt(sectionId.getY());
					buffer.writeInt(sectionId.getZ());
					System.out.println("* " + (System.currentTimeMillis() % 100000) + ": requested update for section " + sectionId);
					protocolClient.send(packet);
					anythingUpdated = true;
				}
			}
		}

		// ProfilingHelper.checkRelevant("update sections 2");
		
		// detect missing section colliders in the viewer's proximity, then request them all at once
		// TODO implement a batch request packet
		{
			final List<SectionId> missingSectionIds = findMissingSectionIds(workingSet.getCollidingSections().keySet(), colliderRadius);
			if (missingSectionIds != null && !missingSectionIds.isEmpty()) {
				final SectionId[] sectionIds = missingSectionIds.toArray(new SectionId[missingSectionIds.size()]);
				for (SectionId sectionId : sectionIds) {
					StackdPacket packet = new StackdPacket(StackdPacket.TYPE_SINGLE_SECTION_DATA_INTERACTIVE, 12);
					ChannelBuffer buffer = packet.getBuffer();
					buffer.writeInt(sectionId.getX());
					buffer.writeInt(sectionId.getY());
					buffer.writeInt(sectionId.getZ());
					protocolClient.send(packet);
					anythingUpdated = true;
				}
			}
		}

		// ProfilingHelper.checkRelevant("update sections 3");
		
		return anythingUpdated;
	}
	
	/**
	 * Reloads a single section by requesting its render model and/or collider from the server.
	 * @param sectionId the ID of the section to reload
	 */
	public void reloadSection(SectionId sectionId) {
		{
			StackdPacket packet = new StackdPacket(StackdPacket.TYPE_SINGLE_SECTION_DATA_INTERACTIVE, 12);
			ChannelBuffer buffer = packet.getBuffer();
			buffer.writeInt(sectionId.getX());
			buffer.writeInt(sectionId.getY());
			buffer.writeInt(sectionId.getZ());
			protocolClient.send(packet);
		}
		{
			StackdPacket packet = new StackdPacket(StackdPacket.TYPE_SINGLE_SECTION_DATA_INTERACTIVE, 12);
			ChannelBuffer buffer = packet.getBuffer();
			buffer.writeInt(sectionId.getX());
			buffer.writeInt(sectionId.getY());
			buffer.writeInt(sectionId.getZ());
			protocolClient.send(packet);
		}
	}
	
	/**
	 * Handles a "single interactive section image" packet that was received from the server.
	 * @param packet the packet
	 */
	public void handleInteractiveSectionImagePacket(StackdPacket packet) {
		
		// read the section data from the packet
		ChannelBuffer buffer = packet.getBuffer();
		final SectionId sectionId = new SectionId(buffer.readInt(), buffer.readInt(), buffer.readInt());
		byte[] data = new byte[buffer.readableBytes()];
		buffer.readBytes(data);
		Cubes cubes = Cubes.createFromCompressedData(workingSet.getClusterSize(), data);
		
		// add a renderable section
		workingSet.getRenderableSectionsLoadedQueue().add(new RenderableSection(workingSet, sectionId, cubes));
		
		// add a colliding section
		ClusterSize clusterSize = workingSet.getClusterSize();
		CubeType[] cubeTypes = workingSet.getEngineParameters().getCubeTypes();
		int size = clusterSize.getSize();
		byte[] colliderCubes = new byte[size * size * size];
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				for (int z=0; z<size; z++) {
					colliderCubes[x * size * size + y * size + z] = cubes.getCubeRelative(clusterSize, x, y, z);
				}
			}
		}
		final IAxisAlignedCollider collider = new CubeArrayClusterCollider(clusterSize, sectionId, colliderCubes, cubeTypes);
		final CollidingSection collidingSection = new CollidingSection(workingSet, sectionId, collider);
		workingSet.getCollidingSectionsLoadedQueue().add(collidingSection);
		
	}
	
	/**
	 * Handles a "single section modification event" packet that was received from the server.
	 * Note that such packets are ignored here until the player's position has been set.
	 * @param packet the packet
	 */
	public void handleModificationEventPacket(StackdPacket packet) {
		ChannelBuffer buffer = packet.getBuffer();
		final SectionId sectionId = new SectionId(buffer.readInt(), buffer.readInt(), buffer.readInt());
		reloadSection(sectionId);
	}

	/**
	 * Removes all entries from the map whose keys are too far away from the center,
	 * currently using "city block distance" (leaving a rectangular region), not
	 * euclidian distance (which would leave a sphere).
	 * 
	 * Returns true if any entries have been removed.
	 */
	private boolean restrictMapToRadius(final Map<SectionId, ?> map, final int radius) {
		List<SectionId> idsToRemoveOld = null;
		for (final Map.Entry<SectionId, ?> entry : map.entrySet()) {
			final SectionId id = entry.getKey();
			final int dx = id.getX() - viewerPosition.getX(), dy = id.getY() - viewerPosition.getY(), dz = id.getZ() - viewerPosition.getZ();
			if (dx < -radius || dx > radius || dy < -radius || dy > radius || dz < -radius || dz > radius) {
				if (idsToRemoveOld == null) {
					idsToRemoveOld = new ArrayList<SectionId>();
				}
				idsToRemoveOld.add(id);
			}
		}
		if (idsToRemoveOld != null) {
			map.keySet().removeAll(idsToRemoveOld);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Builds a list of section IDs that are "close enough" to the center and are not yet
	 * present in the specified set of section IDs. May return null instead of an
	 * empty list.
	 */
	private List<SectionId> findMissingSectionIds(final Set<SectionId> presentSectionIds, final int radius) {
		List<SectionId> missingSectionIds = null;
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				for (int dz = -radius; dz <= radius; dz++) {
					final int cx = viewerPosition.getX() + dx, cy = viewerPosition.getY() + dy, cz = viewerPosition.getZ() + dz;
					final SectionId id = new SectionId(cx, cy, cz);
					if (!presentSectionIds.contains(id)) {
						if (missingSectionIds == null) {
							missingSectionIds = new ArrayList<SectionId>();
						}
						missingSectionIds.add(id);
					}
				}
			}
		}
		return missingSectionIds;
	}

}
