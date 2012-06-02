/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.readonly.FallbackRenderer;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRendererContributor;


/**
 * This class encapsulates all capabilities contributed by plugins.
 */
public class ApplicationCapabilities {

	/**
	 * the rawEntityListPropertyDisplayFilters
	 */
	private final List<IRawEntityListPropertyDisplayFilter> rawEntityListPropertyDisplayFilters = new ArrayList<IRawEntityListPropertyDisplayFilter>();

	/**
	 * the entityReferenceDetectors
	 */
	private final List<IEntityReferenceDetector> entityReferenceDetectors = new ArrayList<IEntityReferenceDetector>();

	/**
	 * the entityPresentationContributors
	 */
	private final List<IEntityPresentationContributor> entityPresentationContributors = new ArrayList<IEntityPresentationContributor>();
	
	/**
	 * the propertyReadOnlyRendererContributors
	 */
	private final List<IPropertyReadOnlyRendererContributor> propertyReadOnlyRendererContributors = new ArrayList<IPropertyReadOnlyRendererContributor>();

	/**
	 * the entityInstanceActionContributors
	 */
	private final List<IEntityInstanceActionContributor> entityInstanceActionContributors = new ArrayList<IEntityInstanceActionContributor>();

	/**
	 * Getter method for the rawEntityListPropertyDisplayFilters.
	 * @return the rawEntityListPropertyDisplayFilters
	 */
	public List<IRawEntityListPropertyDisplayFilter> getRawEntityListPropertyDisplayFilters() {
		return rawEntityListPropertyDisplayFilters;
	}
	
	/**
	 * Getter method for the entityReferenceDetectors.
	 * @return the entityReferenceDetectors
	 */
	public List<IEntityReferenceDetector> getEntityReferenceDetectors() {
		return entityReferenceDetectors;
	}

	/**
	 * Getter method for the entityPresentationContributors.
	 * @return the entityPresentationContributors
	 */
	public List<IEntityPresentationContributor> getEntityPresentationContributors() {
		return entityPresentationContributors;
	}
	
	/**
	 * Getter method for the propertyReadOnlyRendererContributors.
	 * @return the propertyReadOnlyRendererContributors
	 */
	public List<IPropertyReadOnlyRendererContributor> getPropertyReadOnlyRendererContributors() {
		return propertyReadOnlyRendererContributors;
	}
	
	/**
	 * Returns an {@link IPropertyReadOnlyRenderer} instance to handle the specified SQL type.
	 * The first contributed renderer will be wrapped in a {@link FallbackRenderer} and this
	 * renderer is returned.
	 * @param sqlType the SQL type to handle
	 * @return the renderer
	 */
	public IPropertyReadOnlyRenderer createPropertyReadOnlyRenderer(int sqlType) {
		FallbackRenderer fallbackRenderer = new FallbackRenderer();
		for (IPropertyReadOnlyRendererContributor contributor : ApplicationConfiguration.getCapabilities().getPropertyReadOnlyRendererContributors()) {
			IPropertyReadOnlyRenderer renderer = contributor.getRenderer(sqlType);
			if (renderer != null) {
				fallbackRenderer.setPrimaryRenderer(renderer);
				break;
			}
		}
		return fallbackRenderer;
	}
	
	/**
	 * Getter method for the entityInstanceActionContributors.
	 * @return the entityInstanceActionContributors
	 */
	public List<IEntityInstanceActionContributor> getEntityInstanceActionContributors() {
		return entityInstanceActionContributors;
	}
	
}
