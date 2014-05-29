/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import name.martingeisse.stackd.common.geometry.SectionId;
import name.martingeisse.stackd.common.network.SectionDataId;
import name.martingeisse.stackd.common.network.SectionDataType;
import name.martingeisse.stackd.common.network.StackdPacket;
import name.martingeisse.stackd.common.task.Task;
import name.martingeisse.stackd.server.network.StackdServer;
import name.martingeisse.stackd.server.network.StackdSession;
import name.martingeisse.stackd.server.section.entry.SectionDataCacheEntry;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import com.google.common.collect.ImmutableMap;

/**
 * This is a helper class used by the {@link StackdServer} to fetch
 * sections from the {@link SectionWorkingSet} in parallel and ship
 * them to the client. The point of this class is to fetch as many
 * section datas as possible from Cassandra in a single query.
 */
public final class SectionToClientShipper {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(SectionToClientShipper.class);
	
	/**
	 * the workingSet
	 */
	private final SectionWorkingSet workingSet;
	
	/**
	 * the jobQueue
	 */
	private final BlockingQueue<ShippingJob> jobQueue;
	
	/**
	 * the handleAllJobsTask
	 */
	private final HandleAllJobsTask handleAllJobsTask;

	/**
	 * Constructor.
	 * @param workingSet the working set
	 */
	public SectionToClientShipper(final SectionWorkingSet workingSet) {
		this.workingSet = workingSet;
		this.jobQueue = new LinkedBlockingQueue<>();
		this.handleAllJobsTask = new HandleAllJobsTask();
	}
	
	/**
	 * Adds a shipping job.
	 * 
	 * @param sectionDataId the ID of the section data to ship
	 * @param session the session to ship to
	 */
	public void addJob(SectionDataId sectionDataId, StackdSession session) {
		ShippingJob job = new ShippingJob();
		job.sectionDataId = sectionDataId;
		job.session = session;
		jobQueue.add(job);
		handleAllJobsTask.schedule();
	}
	
	/**
	 * Fetches all jobs from the job queue and handles them.
	 */
	public void handleJobs() {
		
		// Fetch pending jobs, returning if there is nothing to do. This happens quite often because
		// the job handling task gets scheduled for every added job, but the task handles *all*
		// pending jobs, so the remaining task executions only find an empty job queue.
		if (jobQueue.isEmpty()) {
			return;
		}
		ArrayList<ShippingJob> jobs = new ArrayList<SectionToClientShipper.ShippingJob>();
		jobQueue.drainTo(jobs);
		if (jobs.isEmpty()) {
			return;
		}
		
		// collect section data IDs
		Set<SectionDataId> sectionDataIds = new HashSet<>();
		for (ShippingJob job : jobs) {
			sectionDataIds.add(job.sectionDataId);
		}
		
		// fetch the objects from the cache
		ImmutableMap<SectionDataId, SectionDataCacheEntry> cacheEntries = workingSet.getAll(sectionDataIds);
		
		// complete the jobs by sending data to the clients
		for (ShippingJob job : jobs) {
			final SectionDataId sectionDataId = job.sectionDataId;
			final SectionId sectionId = sectionDataId.getSectionId();
			final SectionDataType type = sectionDataId.getType();
			final byte[] data = cacheEntries.get(sectionDataId).getDataForClient();
			final StackdPacket response = new StackdPacket(StackdPacket.TYPE_SINGLE_SECTION_DATA_BASE + type.ordinal(), data.length + 12);
			ChannelBuffer buffer = response.getBuffer();
			buffer.writeInt(sectionId.getX());
			buffer.writeInt(sectionId.getY());
			buffer.writeInt(sectionId.getZ());
			buffer.writeBytes(data);
			logger.debug("SERVER sending section data: " + sectionDataId + " (" + data.length + " bytes)");
			job.session.sendPacketDestructive(response);
			logger.debug("SERVER sent section data: " + sectionDataId + " (" + data.length + " bytes)");
		}
		
	}
	
	/**
	 * Contains the data for a single shipping job.
	 */
	static final class ShippingJob {
		SectionDataId sectionDataId;
		StackdSession session;
	}
	
	/**
	 * A task that gets scheduled repeatedly to finish the jobs.
	 */
	class HandleAllJobsTask extends Task {
		@Override
		public void run() {
			try {
				SectionToClientShipper.this.handleJobs();
			} catch (Throwable t) {
				logger.error("unexpected exception", t);
			}
		}
	}
	
}
