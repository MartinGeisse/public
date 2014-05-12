/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.glworker;

import java.util.HashMap;
import java.util.Map;


/**
 * This class maintains a fixed set of {@link GlWorkUnit}s for a fixed
 * set of subjects of type T. The subjects must be specified at construction
 * of this object, and one work unit is created per subject. Later, the
 * work units can be scheduled as a batch, and will invoke the
 * {@link #handleSubject(Object)} method in the GL worker thread for each subject.
 * 
 * @param <T> the subject type
 */
public abstract class FixedSubjectsWorkUnits<T> {

	/**
	 * the workUnits
	 */
	private final GlWorkUnit[] workUnits;
	
	/**
	 * the workUnitBySubject
	 */
	private final Map<T, GlWorkUnit> workUnitBySubject;
	
	/**
	 * Constructor.
	 * @param subjects the subjects
	 */
	public FixedSubjectsWorkUnits(T[] subjects) {
		workUnits = new GlWorkUnit[subjects.length];
		workUnitBySubject = new HashMap<>();
		for (int i=0; i<subjects.length; i++) {
			workUnits[i] = new MyWorkUnit(subjects[i]);
			workUnitBySubject.put(subjects[i], workUnits[i]);
		}
	}

	/**
	 * Schedules the work units.
	 * @param workerLoop the worker loop
	 */
	public final void schedule(GlWorkerLoop workerLoop) {
		for (GlWorkUnit glWorkUnit : workUnits) {
			workerLoop.schedule(glWorkUnit);
		}
	}

	/**
	 * Schedules the work unit for a single subject.
	 * @param workerLoop the worker loop
	 * @param subject the subject to schedule the work unit for
	 */
	@SuppressWarnings("unchecked")
	public final void schedule(GlWorkerLoop workerLoop, T subject) {
		GlWorkUnit workUnit = workUnitBySubject.get(subject);
		if (workUnit == null) {
			throw new IllegalArgumentException("no work unit for subject: " + subject);
		}
		workerLoop.schedule(workUnit);
	}
	
	/**
	 * Handles a subject. This method gets called in the GL worker thread.
	 * @param subject the subject
	 */
	protected abstract void handleSubject(T subject);

	/**
	 * 
	 */
	final class MyWorkUnit extends GlWorkUnit {
		
		final T subject;

		MyWorkUnit(T subject) {
			this.subject = subject;
		}

		@Override
		public void execute() {
			handleSubject(subject);
		}
		
	}
}
