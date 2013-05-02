/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import javax.annotation.Nullable;

import org.apache.wicket.atmosphere.AtmosphereEvent;
import org.atmosphere.cpr.AtmosphereResource;

import com.google.common.base.Predicate;

/**
 * This filter checks whether the event payload implements {@link ISetter}
 * and if so, sets the {@link AtmosphereResource} using that interface.
 */
public class AtmosphereResourceCaptureFilter implements Predicate<AtmosphereEvent> {

	/* (non-Javadoc)
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(@Nullable final AtmosphereEvent input) {
		if (input.getPayload() instanceof ISetter) {
			ISetter setter = (ISetter)input.getPayload();
			setter.set(input.getResource());
		}
		return true;
	}

	/**
	 * The interface to be implemented by the event payload.
	 */
	public static interface ISetter {
		
		/**
		 * Sets the resource
		 * @param resource the resource
		 */
		public void set(AtmosphereResource resource);
		
	}
	
}
