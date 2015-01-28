/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.stackd.client.glworker;

/**
 * Base implementation for {@link SingleWorkUnitVisualTemplate}.
 */
public abstract class AbstractSingleWorkUnitVisualTemplate<T> implements SingleWorkUnitVisualTemplate<T> {

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.glworker.VisualTemplate#render(java.lang.Object, name.martingeisse.stackd.client.glworker.GlWorkerLoop)
	 */
	@Override
	public final void render(T subject, GlWorkerLoop worker) {
		worker.schedule(createWorkUnit(subject));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.glworker.SingleWorkUnitVisualTemplate#createWorkUnit(java.lang.Object)
	 */
	@Override
	public final GlWorkUnit createWorkUnit(final T subject) {
		return new GlWorkUnit() {
			@Override
			public void execute() {
				renderEmbedded(subject);
			}
		};
	}
	
}
