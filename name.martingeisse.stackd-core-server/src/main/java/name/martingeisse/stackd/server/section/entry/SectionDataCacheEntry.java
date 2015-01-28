/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.entry;

import name.martingeisse.stackd.common.network.SectionDataId;
import name.martingeisse.stackd.common.task.Task;
import name.martingeisse.stackd.server.section.SectionWorkingSet;

/**
 * Base class for cached section data objects. This class stores
 * the data (in the actual subclass) as well as the {@link SectionDataId}
 * used to identify the data.
 * 
 * Each instance has a "modified" flag that keeps track of unsaved
 * changes, as well as a {@link #save()} method to save such changes.
 * 
 * Changing this object (specifically, {@link #markModified()}) also
 * adds a task item to save the changes automatically in the near future --
 * but not right now, to avoid overloading the calling thread with work,
 * and also in anticipation of further changes which can be batched. 
 */
public abstract class SectionDataCacheEntry {

	/**
	 * the sectionWorkingSet
	 */
	private final SectionWorkingSet sectionWorkingSet;
	
	/**
	 * the sectionDataId
	 */
	private final SectionDataId sectionDataId;

	/**
	 * the modified
	 */
	private boolean modified;
	
	/**
	 * Constructor.
	 * @param sectionWorkingSet the working set from which this cached object comes from
	 * @param sectionDataId the section data id
	 */
	protected SectionDataCacheEntry(final SectionWorkingSet sectionWorkingSet, final SectionDataId sectionDataId) {
		this.sectionWorkingSet = sectionWorkingSet;
		this.sectionDataId = sectionDataId;
		this.modified = false;
	}

	/**
	 * Getter method for the sectionWorkingSet.
	 * @return the sectionWorkingSet
	 */
	public SectionWorkingSet getSectionWorkingSet() {
		return sectionWorkingSet;
	}
	
	/**
	 * Getter method for the sectionDataId.
	 * @return the sectionDataId
	 */
	public SectionDataId getSectionDataId() {
		return sectionDataId;
	}
	
	/**
	 * Getter method for the modified.
	 * @return the modified
	 */
	public synchronized boolean isModified() {
		return modified;
	}

	/**
	 * Marks this object as modified. This will trigger a {@link #save()} call when
	 * the object is evicted from its cache, and will also add a task item
	 * to auto-save in the near future.
	 */
	public synchronized final void markModified() {
		if (!modified) {
			this.modified = true;
			new Task() {
				@Override
				public void run() {
					save();
				}
			}.scheduleRelative(2000);
		}
		onModification();
	}
	
	/**
	 * Saves the modifications (if any) to the section storage.
	 */
	public synchronized final void save() {
		if (modified) {
			sectionWorkingSet.getStorage().saveSectionRelatedObject(sectionDataId, serializeForSave());
			modified = false;
		}
	}
	
	/**
	 * Creates a serialized representation of this object for saving.
	 * @return this, serialized for saving
	 */
	protected abstract byte[] serializeForSave();

	/**
	 * This method gets called by {@link #markModified()} to allow subclasses
	 * to react to modifications.
	 */
	protected void onModification() {
	}
	
	/**
	 * Returns the data that can be sent to the client for this object.
	 * @return the data for the client
	 */
	public abstract byte[] getDataForClient();
	
}
