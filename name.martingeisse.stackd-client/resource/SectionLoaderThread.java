/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import name.martingeisse.stackd.client.engine.Section;
import name.martingeisse.stackd.common.geometry.SectionId;

/**
 * This thread loads sections from the server in parallel to the main game code.
 */
class SectionLoaderThread extends Thread {

	/**
	 * the requestedSectionIds
	 */
	private static final ConcurrentHashMap<SectionId, SectionId> requestedSectionIds = new ConcurrentHashMap<SectionId, SectionId>();
	
	/**
	 * the loadedSections
	 */
	private static final ConcurrentLinkedQueue<Section> loadedSections = new ConcurrentLinkedQueue<Section>();
	
	/**
	 * Requests a section to be loaded by the loader thread.
	 * @param sectionId the ID of the section to load
	 */
	public static void requestSection(SectionId sectionId) {
		requestedSectionIds.put(sectionId, sectionId);
	}

	/**
	 * Fetches a loaded section from the loader thread.
	 * @return the section, or null if no more loaded sections are ready
	 */
	public static Section fetchLoadedSection() {
		return loadedSections.poll();
	}

	/**
	 * Constructor.
	 */
	SectionLoaderThread() {
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				while (requestedSectionIds.isEmpty()) {
					Thread.sleep(100);
				}
				Set<SectionId> sectionIds = new HashSet<SectionId>();
				for (SectionId sectionId : requestedSectionIds.values()) {
					if (requestedSectionIds.remove(sectionId) != null) {
						sectionIds.add(sectionId);
					}
				}
				for (Section section : loadSections(sectionIds)) {
					loadedSections.add(section);
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	private List<Section> loadSections(Set<SectionId> sectionIds) {
		return null;
	}
	
}
