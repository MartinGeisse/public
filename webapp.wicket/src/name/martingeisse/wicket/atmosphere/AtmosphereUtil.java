/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.atmosphere;

import org.apache.wicket.Component;
import org.apache.wicket.atmosphere.AtmosphereBehavior;
import org.apache.wicket.atmosphere.AtmosphereEvent;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.atmosphere.cpr.AtmosphereResource;

/**
 * Utility methods to deal with Atmosphere.
 */
public class AtmosphereUtil {

	/**
	 * Returns true if this is a request that uses the most recent
	 * Atmosphere resource for the specified component's page.
	 * 
	 * @param component the component that is used to determine the page
	 * @return true for the most recent resource, false if the
	 * request uses an older resource or isn't associated with
	 * an atmosphere resource at all, or if the page doesn't
	 * have an atmosphere resource.
	 */
	public static boolean isRequestForMostRecentResource(Component component) {
		String requestUuid = getResourceUuidForCurrentRequest();
		String pageUuid = AtmosphereBehavior.getUUID(component.getPage());
		return (requestUuid != null && pageUuid != null && requestUuid.equals(pageUuid));
	}
	
	/**
	 * Returns the {@link AtmosphereResource} UUID for the current
	 * request, or null if the current request is not an Atmosphere
	 * push request.
	 * 
	 * @return the UUID or null
	 */
	public static String getResourceUuidForCurrentRequest() {
		return getResourceUuidForCurrentPushRequest();
	}

	/**
	 * Returns the {@link AtmosphereResource} UUID for the current
	 * request, or null if the current request is not an Atmosphere
	 * push request.
	 * 
	 * @return the UUID or null
	 */
	private static String getResourceUuidForCurrentPushRequest() {
		Request request = RequestCycle.get().getRequest();
		if (request.getClass().getSimpleName().equals("AtmosphereWebRequest")) {
			AtmosphereEvent atmosphereEvent = new PropertyModel<AtmosphereEvent>(request, "event").getObject();
			return atmosphereEvent.getResource().uuid();
		} else {
			return null;
		}
	}

	/**
	 * Prevent instantiation.
	 */
	private AtmosphereUtil() {
	}
	
}
