/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.entity.multi.IEntityListFieldOrder;
import name.martingeisse.admin.entity.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.entity.property.IRawEntityListPropertyDisplayFilter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * Utilities to access entity configuration in the {@link ApplicationConfiguration}.
 */
public final class EntityConfigurationUtil {

	/**
	 * The parameter key for the default global entity list presenter.
	 */
	public static final Class<IGlobalEntityListPresenter> GLOBAL_ENTITY_LIST_PRESENTER_PARAMETER_KEY = IGlobalEntityListPresenter.class;

	/**
	 * The parameter key for the entity list field order
	 */
	public static final Class<IEntityListFieldOrder> ENTITY_LIST_FIELD_ORDER_PARAMETER_KEY = IEntityListFieldOrder.class;

	/**
	 * The parameter key for the entity name mapping strategy.
	 */
	public static final Class<IEntityNameMappingStrategy> ENTITY_NAME_MAPPING_STRATEGY_PARAMETER_KEY = IEntityNameMappingStrategy.class;

	/**
	 * The capability key for entity presentation contributors.
	 */
	public static final Class<IEntityPresentationContributor> ENTITY_PRESENTATION_CONTRIBUTOR_CAPABILITY_KEY = IEntityPresentationContributor.class;

	/**
	 * The capability key for entity instance action contributors.
	 */
	public static final Class<IEntityInstanceActionContributor> ENTITY_INSTANCE_ACTION_CONTRIBUTOR_CAPABILITY_KEY = IEntityInstanceActionContributor.class;

	/**
	 * The capability key for raw entity list property display filters.
	 */
	public static final Class<IRawEntityListPropertyDisplayFilter> RAW_ENTITY_LIST_PROPERTY_DISPLAY_FILTER_CAPABILITY_KEY = IRawEntityListPropertyDisplayFilter.class;

	/**
	 * The capability key for entity reference detectors.
	 */
	public static final Class<IEntityReferenceDetector> ENTITY_REFERENCE_DETECTOR_CAPABILITY_KEY = IEntityReferenceDetector.class;

	/**
	 * Prevent instantiation.
	 */
	private EntityConfigurationUtil() {
	}

	/**
	 * Getter method for the defaultEntityListPresenter.
	 * @return the defaultEntityListPresenter
	 */
	public static IGlobalEntityListPresenter getDefaultEntityListPresenter() {
		return ApplicationConfiguration.get().getParameters().get(GLOBAL_ENTITY_LIST_PRESENTER_PARAMETER_KEY);
	}

	/**
	 * Setter method for the defaultEntityListPresenter.
	 * @param defaultEntityListPresenter the defaultEntityListPresenter to set
	 */
	public static void setDefaultEntityListPresenter(final IGlobalEntityListPresenter defaultEntityListPresenter) {
		ApplicationConfiguration.get().getParameters().set(GLOBAL_ENTITY_LIST_PRESENTER_PARAMETER_KEY, defaultEntityListPresenter);
	}

	/**
	 * Getter method for the entityListFieldOrder.
	 * @return the entityListFieldOrder
	 */
	public static IEntityListFieldOrder getEntityListFieldOrder() {
		return ApplicationConfiguration.get().getParameters().get(ENTITY_LIST_FIELD_ORDER_PARAMETER_KEY);
	}

	/**
	 * Setter method for the entityListFieldOrder.
	 * @param entityListFieldOrder the entityListFieldOrder to set
	 */
	public static void setEntityListFieldOrder(final IEntityListFieldOrder entityListFieldOrder) {
		ApplicationConfiguration.get().getParameters().set(ENTITY_LIST_FIELD_ORDER_PARAMETER_KEY, entityListFieldOrder);
	}

	/**
	 * Getter method for the entity name mapping strategy.
	 * @return the entity name mapping strategy
	 */
	public static IEntityNameMappingStrategy getEntityNameMappingStrategy() {
		return ApplicationConfiguration.get().getParameters().get(ENTITY_NAME_MAPPING_STRATEGY_PARAMETER_KEY);
	}

	/**
	 * Setter method for the entity name mapping strategy.
	 * @param entityNameMappingStrategy the entity name mapping strategy to set
	 */
	public static void setEntityNameMappingStrategy(final IEntityNameMappingStrategy entityNameMappingStrategy) {
		ApplicationConfiguration.get().getParameters().set(ENTITY_NAME_MAPPING_STRATEGY_PARAMETER_KEY, entityNameMappingStrategy);
	}

	/**
	 * Returns the entity name for the specified entity.
	 * @param entity the entity
	 * @return the name to display
	 */
	public static String getEntityName(final EntityDescriptor entity) {
		final IEntityNameMappingStrategy entityNameMappingStrategy = getEntityNameMappingStrategy();
		return (entityNameMappingStrategy == null ? entity.getTableName() : entityNameMappingStrategy.getEntityName(entity));
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
	 * Adds the specified entity instance action contributor.
	 * @param contributor the contributor to add
	 */
	public static void addEntityInstanceActionContributor(final IEntityInstanceActionContributor contributor) {
		ApplicationConfiguration.get().getCapabilities().add(ENTITY_INSTANCE_ACTION_CONTRIBUTOR_CAPABILITY_KEY, contributor);
	}

	/**
	 * @return an {@link Iterable} for all entity instance action contributors.
	 */
	public static Iterable<IEntityInstanceActionContributor> getEntityInstanceActionContributors() {
		return ApplicationConfiguration.get().getCapabilities().getIterable(ENTITY_INSTANCE_ACTION_CONTRIBUTOR_CAPABILITY_KEY);
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
	
}
