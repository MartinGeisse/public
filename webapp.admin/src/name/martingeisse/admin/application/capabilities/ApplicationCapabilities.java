/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.navigation.AbstractNavigationNode;
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
	 * the navigationBackMappers
	 */
	private final List<INavigationBackMapper> navigationBackMappers = new ArrayList<INavigationBackMapper>();

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
	public IPropertyReadOnlyRenderer createPropertyReadOnlyRenderer(final int sqlType) {
		final FallbackRenderer fallbackRenderer = new FallbackRenderer();
		for (final IPropertyReadOnlyRendererContributor contributor : ApplicationConfiguration.getCapabilities().getPropertyReadOnlyRendererContributors()) {
			final IPropertyReadOnlyRenderer renderer = contributor.getRenderer(sqlType);
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
	
	/**
	 * Getter method for the navigationBackMappers.
	 * @return the navigationBackMappers
	 */
	public List<INavigationBackMapper> getNavigationBackMappers() {
		return navigationBackMappers;
	}
	
	/**
	 * Prepares internal data structures for the navigation back-mappers.
	 * This method must be invoked after building the navigation tree but
	 * before using back-mappers.
	 */
	public void initializeNavigationBackMappers() {
		for (INavigationBackMapper navigationBackMapper : navigationBackMappers) {
			navigationBackMapper.initialize(ApplicationConfiguration.getNavigationTree());
		}
	}
	
	/**
	 * Given a page, returns the navigation node to which the page belongs.
	 * Returns null if no appropriate navigation node was found.
	 * @param page the page
	 * @return the navigation node or null
	 */
	public AbstractNavigationNode mapPageToNavigationNode(Page page) {
		// TODO: handle conflicts by returning the most specific node
		for (INavigationBackMapper navigationBackMapper : navigationBackMappers) {
			AbstractNavigationNode node = navigationBackMapper.mapPageToNavigationNode(page);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

}
