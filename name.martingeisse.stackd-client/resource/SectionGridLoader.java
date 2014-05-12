/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.storage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import name.martingeisse.stackd.client.engine.Section;
import name.martingeisse.stackd.client.engine.WorldWorkingSet;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.SectionId;
import name.martingeisse.stackd.common.util.ProfilingHelper;

/**
 * This class updates the set of sections in the {@link WorldWorkingSet} based
 * on the viewer's position. It loads new section from an
 * {@link ISectionGridStorage} when the viewer moves towards them
 * and evicts them from the working set when the viewer walks away.
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
	 * the storage
	 */
	private final ISectionGridStorage storage;

	/**
	 * the radius
	 */
	private final int radius;

	/**
	 * the loaderThread
	 */
	private final SectionLoaderThread loaderThread;

	/**
	 * Constructor.
	 * @param workingSet the working set to load sections for
	 * @param storage the storage to load sections from
	 * @param radius the "radius" of the active set
	 * @param useLoaderThread whether section loading should happen in a separate thread
	 */
	public SectionGridLoader(final WorldWorkingSet workingSet, final ISectionGridStorage storage, final int radius, final boolean useLoaderThread) {
		this.workingSet = workingSet;
		this.storage = storage;
		this.radius = radius;
		if (useLoaderThread) {
			this.loaderThread = new SectionLoaderThread();
			loaderThread.start();
		} else {
			this.loaderThread = null;
		}
	}

	/**
	 * Updates the set of visible sections, based on the viewer's position which
	 * is specified in cube units.
	 * 
	 * @param x the viewer's x position, measured in cube units
	 * @param y the viewer's y position, measured in cube units
	 * @param z the viewer's z position, measured in cube units
	 */
	public void updateForViewerPosition(final int x, final int y, final int z) {
		final int shift = workingSet.getClusterSize().getShiftBits();
		updateForViewerSection(x >> shift, y >> shift, z >> shift);
	}

	/**
	 * Updates the set of visible sections, based on the viewer's position which
	 * is specified in cluster-size units.
	 * 
	 * @param x the viewer's x position, measured in cluster-size units
	 * @param y the viewer's y position, measured in cluster-size units
	 * @param z the viewer's z position, measured in cluster-size units
	 */
	public void updateForViewerSection(final int x, final int y, final int z) {

		ProfilingHelper.start();

		if (loaderThread != null) {
			fetchSectionsFromLoaderThread();
		}

		// remove all sections that are too far away
		final Set<SectionId> idsToRemove = new HashSet<SectionId>();
		for (final Map.Entry<SectionId, Section> entry : workingSet.getSections().entrySet()) {
			final SectionId id = entry.getKey();
			final int dx = id.getX() - x, dy = id.getY() - y, dz = id.getZ() - z;
			if (dx < -radius || dx > radius || dy < -radius || dy > radius || dz < -radius || dz > radius) {
				idsToRemove.add(id);
			}
		}
		workingSet.getSections().keySet().removeAll(idsToRemove);

		// detect missing sections in the viewer's proximity
		final Set<SectionId> newSectionIds = new HashSet<SectionId>();
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				for (int dz = -radius; dz <= radius; dz++) {
					final int cx = x + dx, cy = y + dy, cz = z + dz;
					final SectionId id = new SectionId(cx, cy, cz);
					if (workingSet.getSections().get(id) == null) {
						newSectionIds.add(id);
					}
				}
			}
		}

		// shortcut if no sections are missing
		if (newSectionIds.isEmpty()) {
			return;
		}

		// load the sections, or request loading
		if (loaderThread == null) {
			addSectionsImmediately(newSectionIds);
		} else {
			addSectionsUsingLoaderThread(newSectionIds);
		}
	}

	/**
	 * Non-threaded loading.
	 */
	private void addSectionsImmediately(final Set<SectionId> newSectionIds) {

		// build sections for the IDs
		final Set<Section> newSections = new HashSet<Section>();
		for (final SectionId id : newSectionIds) {
			newSections.add(new Section(workingSet, id, workingSet.getClusterSize()));
		}

		// add the new sections to the working set so we can find neighbors while building the render models
		for (final Section section : newSections) {
			workingSet.addSection(section);
		}

		// load data for the new sections
		final long startTime = System.currentTimeMillis();
		storage.loadSections(newSections.toArray(new Section[newSections.size()]));
		final long endTime = System.currentTimeMillis();

		ProfilingHelper.check("SectionGridLoader");

	}

	/**
	 * Threaded loading.
	 */
	private void fetchSectionsFromLoaderThread() {
		/*
		while (true) {
			Section section = loaderThread.fetchLoadedSection();
			
		}
		
		
		// build sections for the IDs
		Set<Section> newSections = new HashSet<Section>();
		for (SectionId id : newSectionIds) {
			newSections.add(new Section(workingSet, id, clusterSize));
		}
		
		// add the new sections to the working set so we can find neighbors while building the render models
		for (Section section : newSections) {
			workingSet.addSection(section);
		}
		
		// load data for the new sections
		long startTime = System.currentTimeMillis();
		storage.loadSections(newSections.toArray(new Section[newSections.size()]));
		long endTime = System.currentTimeMillis();
		System.out.println("loaded " + newSections.size() + " sections in " + (endTime - startTime) + " milliseconds");
		
		// build the render models
		for (Section section : newSections) {
			section.setModified(false);
			section.buildRenderModel();
		}
		long endTime2 = System.currentTimeMillis();
		System.out.println("... and optimized them in " + (endTime2 - endTime) + " milliseconds");
		
		ProfilingHelper.check("SectionGridLoader");
		*/
	}

	/**
	 * Threaded loading.
	 */
	private void addSectionsUsingLoaderThread(final Set<SectionId> newSectionIds) {

	}

}
