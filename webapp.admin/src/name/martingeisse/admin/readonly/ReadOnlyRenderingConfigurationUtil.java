/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.readonly;

import java.util.Collections;
import java.util.List;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.entity.property.type.ISqlType;
import name.martingeisse.admin.util.ScoreComparator;

/**
 * Utilities to access read-only rendering configuration in the {@link ApplicationConfiguration}.
 */
public final class ReadOnlyRenderingConfigurationUtil {

	/**
	 * Prevent instantiation.
	 */
	private ReadOnlyRenderingConfigurationUtil() {
	}

	/**
	 * The capability key for property read-only renderer contributors.
	 */
	public static final Class<IPropertyReadOnlyRendererContributor> PROPERTY_READ_ONLY_RENDERER_CONTRIBUTOR_CAPABILITY_KEY = IPropertyReadOnlyRendererContributor.class;

	/**
	 * Adds the specified property read-only renderer contributor.
	 * @param propertyReadOnlyRendererContributor the property read-only renderer contributor to add
	 */
	public static void addPropertyReadOnlyRendererContributor(final IPropertyReadOnlyRendererContributor propertyReadOnlyRendererContributor) {
		ApplicationConfiguration.get().getCapabilities().add(PROPERTY_READ_ONLY_RENDERER_CONTRIBUTOR_CAPABILITY_KEY, propertyReadOnlyRendererContributor);
	}

	/**
	 * @return an {@link Iterable} for all property read-only renderer contributors.
	 */
	public static Iterable<IPropertyReadOnlyRendererContributor> getPropertyReadOnlyRendererContributors() {
		return ApplicationConfiguration.get().getCapabilities().getIterable(PROPERTY_READ_ONLY_RENDERER_CONTRIBUTOR_CAPABILITY_KEY);
	}

	/**
	 * Returns an {@link IPropertyReadOnlyRenderer} instance to handle the specified data type.
	 * The first contributed renderer will be wrapped in a {@link FallbackRenderer} and this
	 * renderer is returned.
	 * @param type the data type to handle
	 * @return the renderer
	 */
	public static IPropertyReadOnlyRenderer createPropertyReadOnlyRenderer(ISqlType type) {
		final FallbackRenderer fallbackRenderer = new FallbackRenderer();
		for (final IPropertyReadOnlyRendererContributor contributor : getPropertyReadOnlyRendererContributors()) {
			final IPropertyReadOnlyRenderer renderer = contributor.getRenderer(type);
			if (renderer != null) {
				fallbackRenderer.setPrimaryRenderer(renderer);
				break;
			}
		}
		return fallbackRenderer;
	}

	/**
	 * Prepares the application configuration with respect to read-only rendering contributors.
	 */
	public static void prepareConfiguration() {
		List<IPropertyReadOnlyRendererContributor> contributors = ApplicationConfiguration.get().getCapabilities().getCachedList(PROPERTY_READ_ONLY_RENDERER_CONTRIBUTOR_CAPABILITY_KEY);
		if (contributors != null) {
			Collections.sort(contributors, ScoreComparator.DESCENDING);
		}
	}
	
}
