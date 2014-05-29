/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.time.Duration;

/**
 * Attaching this behavior to a component results in AJAX calls being made every 10 minutes
 * (or a user-defined interval) to keep the server-side session alive.
 */
public class KeepSessionAliveBehavior extends AbstractAjaxTimerBehavior {

	/**
	 * Constructor.
	 */
	public KeepSessionAliveBehavior() {
		super(Duration.minutes(10));
	}

	/**
	 * Constructor.
	 * @param minutes the duration in minutes
	 */
	public KeepSessionAliveBehavior(int minutes) {
		super(Duration.minutes(minutes));
	}
	
	/**
	 * Constructor.
	 * @param duration the duration
	 */
	public KeepSessionAliveBehavior(Duration duration) {
		super(duration);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractAjaxTimerBehavior#onTimer(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onTimer(AjaxRequestTarget target) {
	}

}
