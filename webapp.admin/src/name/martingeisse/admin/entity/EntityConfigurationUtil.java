/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.entity.property.IRawEntityListPropertyDisplayFilter;
import name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector;

/**
 * Utilities to access entity configuration in the {@link ApplicationConfiguration}.
 */
public final class EntityConfigurationUtil {

	/**
	 * The parameter key for the general entity configuration.
	 */
	public static final Class<GeneralEntityConfiguration> GENERAL_ENTITY_CONFIGURATION_PARAMETER_KEY = GeneralEntityConfiguration.class;

	/**
	 * The capability key for entity presentation contributors.
	 */
	public static final Class<IEntityPresentationContributor> ENTITY_PRESENTATION_CONTRIBUTOR_CAPABILITY_KEY = IEntityPresentationContributor.class;

	/**
	 * The capability key for raw entity list property display filters.
	 */
	public static final Class<IRawEntityListPropertyDisplayFilter> RAW_ENTITY_LIST_PROPERTY_DISPLAY_FILTER_CAPABILITY_KEY = IRawEntityListPropertyDisplayFilter.class;

	/**
	 * The capability key for entity reference detectors.
	 */
	public static final Class<IEntityReferenceDetector> ENTITY_REFERENCE_DETECTOR_CAPABILITY_KEY = IEntityReferenceDetector.class;

	/**
	 * The capability key for entity instance navigation contributors.
	 */
	public static final Class<IEntityNavigationContributor> ENTITY_NAVIGATION_CONTRIBUTOR_CAPABILITY_KEY = IEntityNavigationContributor.class;

	/**
	 * Prevent instantiation.
	 */
	private EntityConfigurationUtil() {
	}

	/**
	 * Getter method for the general entity configuration.
	 * @return the general entity configuration
	 */
	public static GeneralEntityConfiguration getGeneralEntityConfiguration() {
		return ApplicationConfiguration.get().getParameters().get(GENERAL_ENTITY_CONFIGURATION_PARAMETER_KEY);
	}

	/**
	 * Setter method for the general entity configuration.
	 * @param generalEntityConfiguration the general entity configuration to set
	 */
	public static void setGeneralEntityConfiguration(final GeneralEntityConfiguration generalEntityConfiguration) {
		ApplicationConfiguration.get().getParameters().set(GENERAL_ENTITY_CONFIGURATION_PARAMETER_KEY, generalEntityConfiguration);
	}

	/**
	 * Adds the specified entity presentation contributor.
	 * @param contributor the contributor to add
	 */
	public static void addEntityPresentationContributor(final IEntityPresentationContributor contributor) {
		ApplicationConfiguration.get().getCapabilities().add(ENTITY_PRESENTATION_CONTRIBUTOR_CAPABILITY_KEY, contributor);
	}

	/**
	 * @return an {@link Iterable} for all entity presentation contributors.
	 */
	public static Iterable<IEntityPresentationContributor> getEntityPresentationContributors() {
		return ApplicationConfiguration.get().getCapabilities().getIterable(ENTITY_PRESENTATION_CONTRIBUTOR_CAPABILITY_KEY);
	}

	/**
	 * Adds the specified raw entity list property display filter.
	 * @param rawEntityListPropertyDisplayFilter the raw entity list property display filter to add
	 */
	public static void addRawEntityListPropertyDisplayFilter(final IRawEntityListPropertyDisplayFilter rawEntityListPropertyDisplayFilter) {
		ApplicationConfiguration.get().getCapabilities().add(RAW_ENTITY_LIST_PROPERTY_DISPLAY_FILTER_CAPABILITY_KEY, rawEntityListPropertyDisplayFilter);
	}

	/**
	 * @return an {@link Iterable} for all raw entity list property display filters.
	 */
	public static Iterable<IRawEntityListPropertyDisplayFilter> getRawEntityListPropertyDisplayFilters() {
		return ApplicationConfiguration.get().getCapabilities().getIterable(RAW_ENTITY_LIST_PROPERTY_DISPLAY_FILTER_CAPABILITY_KEY);
	}

	/**
	 * Adds the specified entity reference detector.
	 * @param entityReferenceDetector the entity reference detector to add
	 */
	public static void addEntityReferenceDetector(final IEntityReferenceDetector entityReferenceDetector) {
		ApplicationConfiguration.get().getCapabilities().add(ENTITY_REFERENCE_DETECTOR_CAPABILITY_KEY, entityReferenceDetector);
	}

	/**
	 * @return an {@link Iterable} for all entity reference detectors.
	 */
	public static Iterable<IEntityReferenceDetector> getEntityReferenceDetectors() {
		return ApplicationConfiguration.get().getCapabilities().getIterable(ENTITY_REFERENCE_DETECTOR_CAPABILITY_KEY);
	}

	/**
	 * Adds the specified entity instance navigation contributor.
	 * @param entityNavigationContributor the entity instance navigation contributor to add
	 */
	public static void addEntityNavigationContributor(final IEntityNavigationContributor entityNavigationContributor) {
		ApplicationConfiguration.get().getCapabilities().add(ENTITY_NAVIGATION_CONTRIBUTOR_CAPABILITY_KEY, entityNavigationContributor);
	}

	/**
	 * @return an {@link Iterable} for all entity instance navigation contributors.
	 */
	public static Iterable<IEntityNavigationContributor> getEntityNavigationContributors() {
		return ApplicationConfiguration.get().getCapabilities().getIterable(ENTITY_NAVIGATION_CONTRIBUTOR_CAPABILITY_KEY);
	}

}
