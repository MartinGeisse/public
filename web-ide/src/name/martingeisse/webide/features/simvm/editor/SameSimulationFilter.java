/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import javax.annotation.Nullable;

import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.wicket.atmosphere.AtmosphereEvent;
import org.apache.wicket.atmosphere.Subscribe;

import com.google.common.base.Predicate;

/**
 * This filter should be used when the components of simulation model
 * elements subscribe to UI events via {@link Subscribe}. It ensures
 * that only events from the same simulation trigger the subscription.
 */
public final class SameSimulationFilter implements Predicate<AtmosphereEvent> {

	/* (non-Javadoc)
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(@Nullable final AtmosphereEvent input) {
		final String eventTargetUuid = input.getResource().uuid();
		// String originalUuid = (String)input.getResource().getRequest().getAttribute(ApplicationConfig.SUSPENDED_ATMOSPHERE_RESOURCE_UUID);
		final ResourceHandle eventTargetResourceHandle = EditorPanel.editorPageSimulationAnchors.get(eventTargetUuid);
		final SimulationEventMessage message = (SimulationEventMessage)input.getPayload();
		final ResourceHandle affectedResourceHandle = message.getVirtualMachine().getDocument().getResourceHandle();
		return ObjectUtils.equals(eventTargetResourceHandle, affectedResourceHandle);
	}

}
